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
            questionDataSource.getQuestionAnswers(familyCode, questionSeq).addOnCompleteListener {
                if(it.isSuccessful){
                    trySend(ResultType.Success(it.result.toObjects(DomainQuestionAnswerDTO::class.java)))
                }else{
                    trySend(ResultType.Error(it.exception))
                }
            }.addOnFailureListener {
                trySend(ResultType.Error(it))
            }
        awaitClose {  }
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
        awaitClose {  }
    }

    override fun answerQuestion(
        familyCode: String,
        questionSeq: Int,
        answer: DomainQuestionAnswerDTO
    ): Flow<ResultType<Unit>> = callbackFlow {
        questionDataSource.answerQuestion(familyCode, questionSeq, answer).addOnCompleteListener {
            if(it.isSuccessful){
                trySend(ResultType.Success(Unit))
            }else{
                trySend(ResultType.Error(it.exception))
            }
        }.addOnFailureListener {
            trySend(ResultType.Error(it))
        }
        awaitClose {  }
    }

    override fun completeTodayQuestion(
        familyCode: String,
        questionSeq: Int,
        questionsMap: Map<String, Any>
    ): Flow<ResultType<Unit>> = callbackFlow {
        questionDataSource.completeTodayQuestion(familyCode, questionSeq, questionsMap).addOnCompleteListener {
            if(it.isSuccessful){
                trySend(ResultType.Success(Unit))
            }else{
                trySend(ResultType.Error(it.exception))
            }
        }.addOnFailureListener {
            trySend(ResultType.Error(it))
        }
        awaitClose {  }
    }

    // 오늘의 질문에 답 했는지 안했는지 체크
    override fun checkAnswerTodayQuestion(
        familyCode: String,
        email: String
    ) =
        callbackFlow {
        questionDataSource.getTodayQuestion(familyCode).get().addOnCompleteListener { it1 ->
            if (it1.isSuccessful){
                val todayQuestionId = it1.result.documents[0].id

                questionDataSource.getQuestionAnswers(familyCode = familyCode, questionSeq = todayQuestionId.toInt()).addOnCompleteListener { it2 ->
                    if (it2.isSuccessful){
                        val list = it2.result.documents.map { it.id }

                        if (list.any { it == email }){
                            trySend(ResultType.Success(Unit))
                        }else{
                            trySend(ResultType.Fail)
                        }

                    }else{
                        trySend(ResultType.Error(it2.exception))
                    }
                }
            }else{
                trySend(ResultType.Error(it1.exception))
            }
        }
        awaitClose { }
    }
}