package com.ssafy.domain.usecase.schedule

import com.ssafy.domain.model.schedule.DomainScheduleDTO
import com.ssafy.domain.repository.schedule.ScheduleRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddFamilyScheduleUseCase @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) {
    fun execute(familyCode: String, scheduleDTO: DomainScheduleDTO) =
        scheduleRepository.addFamilySchedule(familyCode, scheduleDTO)
}