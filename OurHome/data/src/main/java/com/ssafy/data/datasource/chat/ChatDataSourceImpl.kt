package com.ssafy.data.datasource.chat

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Query.Direction
import com.google.firebase.firestore.SetOptions
import com.ssafy.data.utils.*
import com.ssafy.domain.model.chat.DomainChatDTO
import javax.inject.Inject

class ChatDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ChatDataSource{

    override fun getChats(familyCode: String): Query =
        firestore.collection(FAMILY).document(familyCode).collection(CHAT).whereNotIn(DATE, listOf("")).orderBy(
            DATE)

    override fun chatting(familyCode: String, date: String, chat: Map<String, Any>): Task<Void> =
        firestore.collection(FAMILY).document(familyCode).collection(CHAT).document(date).set(chat, SetOptions.merge())

}