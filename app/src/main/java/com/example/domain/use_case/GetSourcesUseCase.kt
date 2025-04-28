package com.example.domain.use_case

import com.example.data.response.NewsResponse
import com.example.data.response.SourcesResponse
import com.example.data.response.SourcesResponseList
import com.example.domain.repo.GetSourcesRepo
import javax.inject.Inject

class GetSourcesUseCase @Inject constructor(
    private val repository: GetSourcesRepo
) {
    suspend operator fun invoke(): SourcesResponseList {
        return repository.getSources()
    }
}