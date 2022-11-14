package com.ssafy.data.datasource.question

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query

interface QuestionDataSource {
    fun getTodayQuestion(familyCode: String): Query

    fun getQuestionAnswers(familyCode: String, questionSeq: Int): Query

    fun getLast3Questions(familyCode: String): Query

    fun getLastAllQuestions(familyCode: String): Query

    fun updateTodayQuestion(familyCode: String, newQuestionSeq: Int): Task<Unit>

}