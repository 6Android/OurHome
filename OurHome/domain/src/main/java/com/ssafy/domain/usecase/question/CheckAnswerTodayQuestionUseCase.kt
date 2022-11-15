package com.ssafy.domain.usecase.question

import com.ssafy.domain.repository.question.QuestionRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CheckAnswerTodayQuestionUseCase @Inject constructor(
    private val questionRepository: QuestionRepository
) {
    fun execute(familyCode: String, email: String) =
        questionRepository.checkAnswerTodayQuestion(familyCode, email)
}