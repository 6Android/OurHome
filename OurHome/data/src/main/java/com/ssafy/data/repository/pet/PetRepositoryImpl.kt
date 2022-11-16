package com.ssafy.data.repository.pet

import android.util.Log
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

    override fun updatePetExp(familyCode: String, exp: Int): Flow<ResultType<Unit>> =
        callbackFlow {
            petDataSource.updatePetExp(familyCode, exp).addOnCompleteListener {
                if(it.isSuccessful){
                    trySend(ResultType.Success(Unit))
                }else{
                    trySend(ResultType.Error(it.exception))
                }
            }.addOnFailureListener {
                trySend(ResultType.Error(it))
            }
            awaitClose {  }
        }

    override fun levelUp(familyCode: String, nextPetLevel: Int): Flow<ResultType<Unit>> = callbackFlow {
        petDataSource.levelUp(familyCode, nextPetLevel).addOnCompleteListener{
            if(it.isSuccessful){
                trySend(ResultType.Success(Unit))
            }else{
                trySend(ResultType.Error(it.exception))
            }
        }.addOnFailureListener {
            trySend(ResultType.Error(it))
        }
        awaitClose {  }
    }

}