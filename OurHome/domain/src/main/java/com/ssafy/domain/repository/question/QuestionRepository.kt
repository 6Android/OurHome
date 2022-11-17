package com.ssafy.domain.repository.question

import com.google.android.gms.tasks.Task
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

    fun answerQuestion(familyCode: String, questionSeq: Int, answer: DomainQuestionAnswerDTO): Flow<ResultType<Unit>>

    fun completeTodayQuestion(familyCode: String, questionSeq: Int, questionsMap: Map<String, Any>): Flow<ResultType<Unit>>

    // 오늘의 질문 완료되었는지 재 확인
    fun checkCompleteTodayQuestion(
        familyCode: String,
        questionSeq: Int,
        questionsMap: Map<String, Any>
    ): Flow<ResultType<Unit>>

    // 오늘의 질문에 답 했는지 안했는지 체크
    fun checkAnswerTodayQuestion(familyCode: String, email: String): Flow<ResultType<Unit>>
}