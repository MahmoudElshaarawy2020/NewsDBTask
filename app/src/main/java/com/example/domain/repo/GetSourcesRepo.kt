package com.example.domain.repo

import com.example.data.response.SourcesResponseList

interface GetSourcesRepo {
    suspend fun getSources(): SourcesResponseList
}