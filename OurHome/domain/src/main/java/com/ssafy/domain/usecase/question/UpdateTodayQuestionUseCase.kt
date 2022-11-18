package com.ssafy.domain.usecase.question

import com.ssafy.domain.repository.question.QuestionRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateTodayQuestionUseCase @Inject constructor(
    private val questionRepository: QuestionRepository
){
    fun execute(familyCode: String, newQuestionSeq: Int) = questionRepository.updateTodayQuestion(familyCode, newQuestionSeq)
}