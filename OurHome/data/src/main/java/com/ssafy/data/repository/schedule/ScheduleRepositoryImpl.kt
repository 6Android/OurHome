package com.ssafy.data.repository.schedule

import com.google.firebase.firestore.FirebaseFirestore
import com.ssafy.data.datasource.schedule.ScheduleDataSource
import com.ssafy.domain.model.schedule.DomainScheduleDTO
import com.ssafy.domain.repository.schedule.ScheduleListResponse
import com.ssafy.domain.repository.schedule.ScheduleRepository
import com.ssafy.domain.utils.ResultType
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val scheduleDataSource: ScheduleDataSource
) : ScheduleRepository {

    // family/schedule Doc (family Collection -> family_code Doc ->  schedule Collection -> schedule Doc)
    // year, month에 해당하는 doc 리스트 받음
    override fun getFamilySchedules(
        familyCode: String,
        year: Int,
        month: Int
    ): Flow<ScheduleListResponse> = callbackFlow {
        scheduleDataSource.getFamilySchedules(familyCode, year, month)
            .addOnSuccessListener {
                val schedules =
                    it.documents.map { document ->
                        document.toObject(DomainScheduleDTO::class.java)?.apply {
                            uid = document.id
                        }!!
                    }
                trySend(ResultType.Success(schedules))
            }
            .addOnFailureListener {
                trySend(ResultType.Error(it))
            }
        awaitClose {}
    }

    override fun deleteFamilySchedule(familyCode: String, uid: String): Flow<ResultType<Unit>> =
        callbackFlow {
            scheduleDataSource.deleteFamilySchedule(familyCode, uid)
                .addOnCompleteListener {
                    val response = if (it.isSuccessful) {
                        ResultType.Success(Unit)
                    } else {
                        ResultType.Fail
                    }
                    trySend(response)
                }
            awaitClose {}
        }

    override fun addFamilySchedule(
        familyCode: String,
        scheduleDTO: DomainScheduleDTO
    ) = callbackFlow {
        scheduleDataSource.addFamilySchedule(familyCode, scheduleDTO)
            .addOnCompleteListener {
                val response = if (it.isSuccessful) {
                    ResultType.Success(Unit)
                } else {
                    ResultType.Fail
                }
                trySend(response)
            }
        awaitClose { }
    }
}