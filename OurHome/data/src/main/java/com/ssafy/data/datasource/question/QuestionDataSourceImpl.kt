package com.ssafy.data.datasource.question

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.ssafy.data.utils.*
import com.ssafy.domain.model.question.DomainQuestionAnswerDTO
import com.ssafy.domain.model.question.DomainQuestionDTO
import javax.inject.Inject

class QuestionDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : QuestionDataSource{

    override fun getTodayQuestion(familyCode: String): Query =
        firestore.collection(FAMILY).document(familyCode).collection(QUESTION).orderBy(QUESTION_SEQ).limitToLast(1)

    override fun getQuestionAnswers(familyCode: String, questionSeq: Int): Task<QuerySnapshot> =
        firestore.collection(FAMILY).document(familyCode).collection(QUESTION).document(questionSeq.toString()).collection(
            QUESTION_ANSWER).get()

    override fun getLast3Questions(familyCode: String, today:String): Query =
        firestore.collection(FAMILY).document(familyCode).collection(QUESTION).whereLessThan(COMPLETED_DATE, today).orderBy(COMPLETED_DATE).limitToLast(3)

    override fun getLastAllQuestions(familyCode: String, today: String): Query=
        firestore.collection(FAMILY).document(familyCode).collection(QUESTION).whereLessThan(COMPLETED_DATE, today)

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

    override fun answerQuestion(familyCode: String, questionSeq: Int, answer: DomainQuestionAnswerDTO): Task<Void> =
        firestore.collection(FAMILY).document(familyCode).collection(QUESTION).document(questionSeq.toString()).collection(
            QUESTION_ANSWER).document(answer.email).set(answer)

    override fun checkCompleteTodayQuestion(
        familyCode: String,
        questionSeq: Int,
        questionsMap: Map<String, Any>
    ): Task<Unit> =
        firestore.runTransaction{ transaction ->
            val todayQuestion = transaction.get(firestore.collection(FAMILY).document(familyCode).collection(
                QUESTION).document(questionSeq.toString()))

            if(todayQuestion.getLong(COMPLETED_YEAR) == null){
                transaction.set(firestore.collection(FAMILY).document(familyCode).collection(QUESTION).document(questionSeq.toString()), questionsMap, SetOptions.merge())
            }

        }

    override fun completeTodayQuestion(
        familyCode: String,
        questionSeq: Int,
        questionsMap: Map<String, Any>
    ): Task<Void> =
        firestore.collection(FAMILY).document(familyCode).collection(QUESTION).document(questionSeq.toString()).set(questionsMap, SetOptions.merge())

    // question_info doc 반환 (question Collection -> seq Doc)
    override fun getQuestionInfoDoc(seq: String) =
        firestore.collection(QUESTION_INFO).document(seq)
}