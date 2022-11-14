package com.ssafy.domain.usecase.schedule

import com.ssafy.domain.repository.schedule.ScheduleRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetFamilyScheduleUseCase @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) {
    fun execute(familyCode: String, year: Int, month: Int) =
        scheduleRepository.getFamilySchedules(familyCode, year, month)
}