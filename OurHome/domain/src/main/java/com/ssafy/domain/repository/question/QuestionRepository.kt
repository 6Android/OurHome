package com.ssafy.domain.repository.question

import com.ssafy.domain.model.pet.DomainFamilyPetDTO
import com.ssafy.domain.model.question.DomainQuestionAnswerDTO
import com.ssafy.domain.model.question.DomainQuestionDTO
import com.ssafy.domain.model.user.DomainUserDTO
import com.ssafy.domain.utils.ResultType
import kotlinx.coroutines.flow.Flow

typealias Questions = List<DomainQuestionDTO>
typealias QuestionResponse = ResultType<Questions>
typealias QuestionAnswers = List<DomainQuestionAnswerDTO>
typealias QuestionAnswerResponse= ResultType<QuestionAnswers?>

interface QuestionRepository {
    fun getTodayQuestion(familyCode: String) : Flow<QuestionResponse>

    fun getQuestionAnswers(familyCode: String, questionSeq: Int) : Flow<QuestionAnswerResponse>

    fun getLast3Questions(familyCode: String) : Flow<QuestionResponse>

    fun getLastAllQuestions(familyCode: String) : Flow<QuestionResponse>

    fun updateTodayQuestion(familyCode: String, newQuestionSeq: Int) : Flow<ResultType<Unit>>
}