package com.ssafy.data.repository.chat

import com.ssafy.data.datasource.chat.ChatDataSource
import com.ssafy.domain.model.chat.DomainChatDTO
import com.ssafy.domain.model.question.DomainQuestionAnswerDTO
import com.ssafy.domain.model.question.DomainQuestionDTO
import com.ssafy.domain.repository.chat.ChatRepository
import com.ssafy.domain.repository.chat.ChatResponse
import com.ssafy.domain.utils.ResultType
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatDataSource: ChatDataSource
) : ChatRepository{
    override fun getChats(familyCode: String):  Flow<ChatResponse> = callbackFlow {
        val snapshotListener =
            chatDataSource.getChats(familyCode).addSnapshotListener { snapshot, e ->
                val response = if (snapshot != null) {
                    val chats = snapshot.toObjects(DomainChatDTO::class.java)
                    ResultType.Success(chats)
                } else {
                    ResultType.Error(e)
                }
                trySend(response)
            }
        awaitClose {
            snapshotListener.remove()
        }
    }

    override fun chatting(
        familyCode: String,
        date: String,
        chat: Map<String, Any>
    ): Flow<ResultType<Unit>> = callbackFlow {
        chatDataSource.chatting(familyCode, date, chat).addOnCompleteListener {
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
}