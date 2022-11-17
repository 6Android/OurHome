package com.ssafy.data.datasource.question

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.ssafy.domain.model.question.DomainQuestionAnswerDTO
import com.ssafy.domain.model.question.DomainQuestionDTO

interface QuestionDataSource {
    fun getTodayQuestion(familyCode: String): Query

    fun getQuestionAnswers(familyCode: String, questionSeq: Int): Task<QuerySnapshot>

    fun getLast3Questions(familyCode: String): Query

    fun getLastAllQuestions(familyCode: String): Query

    fun updateTodayQuestion(familyCode: String, newQuestionSeq: Int): Task<Unit>

    fun answerQuestion(familyCode: String, questionSeq: Int, answer: DomainQuestionAnswerDTO): Task<Void>

    // 완료되었는지 재확인
    fun checkCompleteTodayQuestion(
        familyCode: String,
        questionSeq: Int,
        questionsMap: Map<String, Any>
    ): Task<Unit>

    fun completeTodayQuestion(familyCode: String, questionSeq: Int, questionsMap: Map<String, Any>): Task<Void>

    // question_info doc 반환 (question Collection -> seq Doc)
    fun getQuestionInfoDoc(seq: String): DocumentReference
}