package com.ssafy.data.repository.family

import com.google.firebase.firestore.DocumentReference
import com.ssafy.data.datasource.family.FamilyDataSource
import com.ssafy.domain.model.question.DomainQuestionDTO
import com.ssafy.domain.repository.family.FamilyRepository
import com.ssafy.domain.utils.ResultType
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class FamilyRepositoryImpl @Inject constructor(
    private val familyDataSource: FamilyDataSource
) : FamilyRepository{

    override fun getFamilyManager(familyCode: String): Flow<ResultType<String?>> = callbackFlow {
        val snapshotListener =
            familyDataSource.getFamilyManager(familyCode).addSnapshotListener { snapshot, e ->
                val response = if (snapshot != null) {
                    val managerEmail = snapshot.toObject(String::class.java)
                    ResultType.Success(managerEmail)
                } else {
                    ResultType.Error(e)
                }
                trySend(response)
            }
        awaitClose {
            snapshotListener.remove()
        }
    }
}
