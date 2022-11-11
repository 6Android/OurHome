package com.ssafy.data.datasource.question

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.ssafy.data.utils.*
import javax.inject.Inject

class QuestionDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : QuestionDataSource{

    override fun getTodayQuestion(familyCode: String): Query =
        firestore.collection(FAMILY).document(familyCode).collection(QUESTION).orderBy(QUESTION_SEQ).limitToLast(1)

    override fun getQuestionAnswers(familyCode: String, questionSeq: Int): Query =
        firestore.collection(FAMILY).document(familyCode).collection(QUESTION).document(questionSeq.toString()).collection(
            QUESTION_ANSWER)

    override fun getLast3Questions(familyCode: String): Query =
        firestore.collection(FAMILY).document(familyCode).collection(QUESTION).whereGreaterThan(COMPLETED_YEAR, 1).orderBy(COMPLETED_YEAR).limitToLast(3)

    override fun getLastAllQuestions(familyCode: String): Query=
        firestore.collection(FAMILY).document(familyCode).collection(QUESTION).whereGreaterThan(COMPLETED_YEAR, 1)

    override fun updateTodayQuestion(familyCode: String, newQuestionSeq: Int): Task<Unit> =
        firestore.runTransaction{ transaction ->
            val newQuestion = transaction.get(firestore.collection(QUESTION_INFO).document(newQuestionSeq.toString()))

            val questionContent = newQuestion.getString(QUESTION_CONTENT)


            if(!questionContent.isNullOrEmpty()){
                val todayQuestionMap = mapOf<String, Any>(
                    QUESTION_SEQ to newQuestionSeq,
                    QUESTION_CONTENT to questionContent
                )

                transaction.set(firestore.collection(FAMILY).document(familyCode).collection(QUESTION).document(newQuestionSeq.toString()), todayQuestionMap)
            }

        }


}