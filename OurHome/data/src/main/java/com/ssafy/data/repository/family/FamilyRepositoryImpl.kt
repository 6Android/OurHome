package com.ssafy.data.repository.family

import com.google.firebase.firestore.DocumentReference
import com.ssafy.data.datasource.family.FamilyDataSource
import com.ssafy.data.utils.FAMILY_CODE
import com.ssafy.data.utils.MANAGER
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

    override fun getFamilyManager(familyCode: String): Flow<ResultType<Any>> = callbackFlow {
        val snapshotListener =
            familyDataSource.getFamilyManager(familyCode).addSnapshotListener { snapshot, e ->
                val response = if (snapshot != null && snapshot.data != null) {
                    val managerEmail = snapshot.data!!.get(MANAGER)
                    if(managerEmail == null){
                        ResultType.Error(Exception("가족 정보 없음"))
                    }else {
                        ResultType.Success(managerEmail)
                    }
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
