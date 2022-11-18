package com.ssafy.data.datasource.chat

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Query
import com.ssafy.domain.model.chat.DomainChatDTO

interface ChatDataSource {
    fun getChats(familyCode: String) : Query

    fun chatting(familyCode: String, date: String, chat: Map<String, Any>) : Task<Void>
}