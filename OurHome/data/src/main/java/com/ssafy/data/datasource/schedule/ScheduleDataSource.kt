package com.ssafy.data.datasource.schedule

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

interface ScheduleDataSource {

    // family/schedule Doc (family Collection -> family_code Doc ->  schedule Collection -> schedule Doc)
    // year, month에 해당하는 doc 리스트 받음
    fun getFamilySchedules(familyCode: String, year: Int, month: Int): Task<QuerySnapshot>

}