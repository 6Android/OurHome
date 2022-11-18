package com.ssafy.domain.usecase.chat

import com.ssafy.domain.repository.chat.ChatRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChattingUseCase @Inject constructor(
    private val chatRepository: ChatRepository
){
    fun execute(familyCode: String, date: String, chat: Map<String, Any>) = chatRepository.chatting(familyCode, date, chat)
}