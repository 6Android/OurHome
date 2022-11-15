package com.ssafy.domain.repository.chat

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Query
import com.ssafy.domain.model.chat.DomainChatDTO
import com.ssafy.domain.model.question.DomainQuestionDTO
import com.ssafy.domain.utils.ResultType
import kotlinx.coroutines.flow.Flow

typealias Chats = List<DomainChatDTO>
typealias ChatResponse = ResultType<Chats>

interface ChatRepository {
    fun getChats(familyCode: String) : Flow<ChatResponse>

    fun chatting(familyCode: String, date: String, chat: Map<String, Any>) : Flow<ResultType<Unit>>
}