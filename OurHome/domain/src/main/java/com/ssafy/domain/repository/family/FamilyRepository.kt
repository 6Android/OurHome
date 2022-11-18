package com.ssafy.domain.repository.family

import com.ssafy.domain.model.question.DomainQuestionDTO
import com.ssafy.domain.utils.ResultType
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

interface FamilyRepository {
    fun getFamilyManager(familyCode: String): Flow<ResultType<Any>>
}