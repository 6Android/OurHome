package com.ssafy.domain.usecase.question

import com.ssafy.domain.model.question.DomainQuestionAnswerDTO
import com.ssafy.domain.repository.question.QuestionRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnswerQuestionUsecase @Inject constructor(
    private val questionRepository: QuestionRepository
){
    fun execute(familyCode: String, questionSeq: Int, answer: DomainQuestionAnswerDTO) = questionRepository.answerQuestion(familyCode, questionSeq, answer)
}

