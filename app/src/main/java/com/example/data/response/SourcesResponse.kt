package com.example.data.response

import com.google.gson.annotations.SerializedName

data class SourcesResponseList(
    @field:SerializedName("sources")
    val sources: List<SourcesResponse>
)

data class SourcesResponse(
    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("name")
    val name: String? = null,
)

