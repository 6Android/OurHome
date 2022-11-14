package com.ssafy.domain.usecase.schedule

import com.ssafy.domain.repository.schedule.ScheduleRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteFamilyScheduleUseCase @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) {
    fun execute(familyCode: String, uid: String) =
        scheduleRepository.deleteFamilySchedule(familyCode, uid)
}