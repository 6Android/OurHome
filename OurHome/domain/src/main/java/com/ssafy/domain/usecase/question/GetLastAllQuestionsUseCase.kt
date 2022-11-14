package com.ssafy.domain.usecase.question

import com.ssafy.domain.repository.question.QuestionRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetLastAllQuestionsUseCase @Inject constructor(
    private val questionRepository: QuestionRepository
){
    fun execute(familyCode: String) = questionRepository.getLastAllQuestions(familyCode)
}