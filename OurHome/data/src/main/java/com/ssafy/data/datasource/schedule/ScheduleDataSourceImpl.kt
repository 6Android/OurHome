package com.ssafy.data.datasource.schedule

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.ssafy.data.utils.FAMILY
import com.ssafy.data.utils.MONTH
import com.ssafy.data.utils.SCHEDULE
import com.ssafy.data.utils.YEAR
import com.ssafy.domain.model.schedule.DomainScheduleDTO
import javax.inject.Inject

class ScheduleDataSourceImpl @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val fireAuth: FirebaseAuth,
) : ScheduleDataSource {

    // family/schedule Doc (family Collection -> family_code Doc ->  schedule Collection -> schedule Doc)
    // year, month에 해당하는 doc 리스트 받음
    override fun getFamilySchedules(
        familyCode: String,
        year: Int,
        month: Int
    ) = fireStore.collection(FAMILY).document(familyCode).collection(SCHEDULE)
        .whereEqualTo(YEAR, year).whereEqualTo(
            MONTH, month
        ).get()

    // family/schedule Doc에서 해당 id 삭제
    override fun deleteFamilySchedule(familyCode: String, uid: String) =
        fireStore.collection(FAMILY).document(familyCode).collection(SCHEDULE).document(uid)
            .delete()

    // family/schedule 에 데이터 추가
    override fun addFamilySchedule(
        familyCode: String,
        scheduleDTO: DomainScheduleDTO
    ): Task<DocumentReference> =
        fireStore.collection(FAMILY).document(familyCode).collection(SCHEDULE).add(scheduleDTO)

}