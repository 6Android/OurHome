package com.ssafy.data.repository.pet

import com.ssafy.data.datasource.pet.PetDataSource
import com.ssafy.domain.model.pet.DomainFamilyPetDTO
import com.ssafy.domain.model.user.DomainUserDTO
import com.ssafy.domain.repository.pet.PetRepository
import com.ssafy.domain.repository.pet.PetResponse
import com.ssafy.domain.utils.ResultType
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class PetRepositoryImpl @Inject constructor(
    private val petDataSource: PetDataSource
) : PetRepository{

    override fun getFamilyPet(familyCode: String): Flow<PetResponse> = callbackFlow {
        val snapshotListener =
            petDataSource.getFamilyPet(familyCode).addSnapshotListener { snapshot, e ->
                val response = if (snapshot != null) {
                    val pet = snapshot.toObject(DomainFamilyPetDTO::class.java)
                    ResultType.Success(pet)
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