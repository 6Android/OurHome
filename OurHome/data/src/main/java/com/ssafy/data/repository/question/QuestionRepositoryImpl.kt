package com.ssafy.data.repository.question

import com.ssafy.data.datasource.question.QuestionDataSource
import com.ssafy.domain.model.question.DomainQuestionAnswerDTO
import com.ssafy.domain.model.question.DomainQuestionDTO
import com.ssafy.domain.repository.question.QuestionAnswerResponse
import com.ssafy.domain.repository.question.QuestionRepository
import com.ssafy.domain.repository.question.QuestionResponse
import com.ssafy.domain.utils.ResultType
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class QuestionRepositoryImpl @Inject constructor(
    private val questionDataSource: QuestionDataSource
) : QuestionRepository {

    override fun getTodayQuestion(familyCode: String): Flow<QuestionResponse> = callbackFlow {
        val snapshotListener =
            questionDataSource.getTodayQuestion(familyCode).addSnapshotListener { snapshot, e ->
                val response = if (snapshot != null) {
                    val question = snapshot.toObjects(DomainQuestionDTO::class.java)
                    ResultType.Success(question)
                } else {
                    ResultType.Error(e)
                }
                trySend(response)
            }
        awaitClose {
            snapshotListener.remove()
        }
    }

    override fun getQuestionAnswers(
        familyCode: String,
        questionSeq: Int
    ): Flow<QuestionAnswerResponse> = callbackFlow {
        val snapshotListener =
            questionDataSource.getQuestionAnswers(familyCode, questionSeq).addSnapshotListener { snapshot, e ->
                val response = if (snapshot != null) {
                    val answers = snapshot.toObjects(DomainQuestionAnswerDTO::class.java)
                    ResultType.Success(answers)
                } else {
                    ResultType.Error(e)
                }
                trySend(response)
            }
        awaitClose {
            snapshotListener.remove()
        }
    }

    override fun getLast3Questions(familyCode: String): Flow<QuestionResponse> = callbackFlow {
        val snapshotListener =
            questionDataSource.getLast3Questions(familyCode).addSnapshotListener { snapshot, e ->
                val response = if (snapshot != null) {
                    val questions = snapshot.toObjects(DomainQuestionDTO::class.java)
                    ResultType.Success(questions)
                } else {
                    ResultType.Error(e)
                }
                trySend(response)
            }
        awaitClose {
            snapshotListener.remove()
        }
    }

    override fun getLastAllQuestions(familyCode: String): Flow<QuestionResponse> = callbackFlow {
        val snapshotListener =
            questionDataSource.getLastAllQuestions(familyCode).addSnapshotListener { snapshot, e ->
                val response = if (snapshot != null) {
                    val questions = snapshot.toObjects(DomainQuestionDTO::class.java)
                    ResultType.Success(questions)
                } else {
                    ResultType.Error(e)
                }
                trySend(response)
            }
        awaitClose {
            snapshotListener.remove()
        }
    }

    override fun updateTodayQuestion(familyCode: String, newQuestionSeq: Int): Flow<ResultType<Unit>> = callbackFlow {
        questionDataSource.updateTodayQuestion(familyCode, newQuestionSeq).addOnCompleteListener{
            if(it.isSuccessful){
                trySend(ResultType.Success(Unit))
            }else{
                trySend(ResultType.Error(it.exception))
            }
        }.addOnFailureListener {
            trySend(ResultType.Error(it))
        }
    }

}