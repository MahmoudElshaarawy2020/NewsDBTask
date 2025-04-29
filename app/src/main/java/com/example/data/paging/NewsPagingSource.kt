package com.example.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.constants.Constants.API_KEY
import com.example.data.remote.ApiService
import com.example.data.response.ArticlesItem
import com.example.data.response.NewsResponse
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class NewsPagingSource @Inject constructor(
    private val apiService: ApiService,
    private val query: String
) : PagingSource<Int, ArticlesItem>() {

    override fun getRefreshKey(state: PagingState<Int, ArticlesItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticlesItem> {
        val position = params.key ?: 1
        val pageSize = params.loadSize  // Dynamically use the pageSize passed to the load

        return try {
            // Use the pageSize in the API call
            val response = apiService.getPagingNews(
                query = query,
                page = position,
                pageSize = pageSize,  // Pass the dynamic pageSize here
                apiKey = API_KEY
            )

            // Return the actual data (ArticlesItem list)
            LoadResult.Page(
                data = response.articles?.filterNotNull() ?: emptyList(),
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (response.articles.isNullOrEmpty()) null else position + 1
            )
        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }
}

