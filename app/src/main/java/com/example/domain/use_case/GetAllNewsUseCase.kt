package com.example.domain.use_case

import com.example.domain.repo.GetNewsRepo
import javax.inject.Inject

class GetAllNewsUseCase @Inject constructor(
    private val getNewsRepo: GetNewsRepo
) {
    suspend operator fun invoke(query: String) = getNewsRepo.getAllNews(query)
}