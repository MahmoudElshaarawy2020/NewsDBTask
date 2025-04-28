package com.example.data.repoImpl

import com.example.constants.Constants.API_KEY
import com.example.data.remote.ApiService
import com.example.data.response.SourcesResponse
import com.example.data.response.SourcesResponseList
import com.example.domain.repo.GetSourcesRepo
import javax.inject.Inject

class GetSourceRepoImpl @Inject constructor(
    private val apiService: ApiService
): GetSourcesRepo {
    override suspend fun getSources(): SourcesResponseList {
        return apiService.getSources(API_KEY)
    }
}