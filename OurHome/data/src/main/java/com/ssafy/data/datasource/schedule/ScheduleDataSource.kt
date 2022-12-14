package com.ssafy.data.datasource.schedule

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.QuerySnapshot
import com.ssafy.domain.model.schedule.DomainScheduleDTO

interface ScheduleDataSource {

    // family/schedule Doc (family Collection -> family_code Doc ->  schedule Collection -> schedule Doc)
    // year, month에 해당하는 doc 리스트 받음
    fun getFamilySchedules(familyCode: String, year: Int, month: Int): Task<QuerySnapshot>

    // family/schedule Doc에서 해당 id 삭제
    fun deleteFamilySchedule(familyCode: String, uid: String): Task<Void>

    // family/schedule 에 데이터 추가
    fun addFamilySchedule(familyCode: String, scheduleDTO: DomainScheduleDTO ): Task<DocumentReference>
}