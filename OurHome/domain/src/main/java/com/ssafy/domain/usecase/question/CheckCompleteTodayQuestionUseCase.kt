package com.ssafy.domain.usecase.question

import com.ssafy.domain.repository.question.QuestionRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CheckCompleteTodayQuestionUseCase @Inject constructor(
    private val questionRepository: QuestionRepository
) {
    fun execute(familyCode: String,
                        questionSeq: Int,
                        questionsMap: Map<String, Any>) =
        questionRepository.checkCompleteTodayQuestion(familyCode, questionSeq, questionsMap)
}