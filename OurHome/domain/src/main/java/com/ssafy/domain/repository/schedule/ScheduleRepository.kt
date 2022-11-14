package com.ssafy.domain.repository.schedule

import com.ssafy.domain.model.schedule.DomainScheduleDTO
import com.ssafy.domain.utils.ResultType
import kotlinx.coroutines.flow.Flow

typealias ScheduleListResponse = ResultType<List<DomainScheduleDTO>>

interface ScheduleRepository {

    // family/schedule Doc (family Collection -> family_code Doc ->  schedule Collection -> schedule Doc)
    // year, month에 해당하는 doc 리스트 받음
    fun getFamilySchedules(familyCode: String, year: Int, month: Int): Flow<ScheduleListResponse>

    // family/schedule Doc에서 해당 id 삭제
    fun deleteFamilySchedule(familyCode: String, uid: String): Flow<ResultType<Unit>>

    // family/schedule 에 데이터 추가
    fun addFamilySchedule(
        familyCode: String,
        scheduleDTO: DomainScheduleDTO
    ): Flow<ResultType<Unit>>
}