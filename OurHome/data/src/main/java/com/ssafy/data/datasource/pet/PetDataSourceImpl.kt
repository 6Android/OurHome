package com.ssafy.data.datasource.pet

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.ssafy.data.utils.*
import javax.inject.Inject

class PetDataSourceImpl @Inject constructor(
    private val fireStore: FirebaseFirestore
) : PetDataSource {

    override fun getFamilyPet(familyCode: String): DocumentReference =
        fireStore.collection(FAMILY).document(familyCode).collection(PET).document(OUR_PET)

    // pet_info doc 반환 (question Collection -> seq Doc)
    override fun getPetInfoDoc(seq: String) =
        fireStore.collection(PET_INFO).document(seq)

    override fun updatePetExp(familyCode: String, exp: Int): Task<Void> =
        fireStore.collection(FAMILY).document(familyCode).collection(PET).document(OUR_PET).update(
            PET_EXP, FieldValue.increment(1L * exp))

    override fun levelUp(familyCode: String, nextPetLevel: Int): Task<Any> =
        fireStore.runTransaction { transaction ->
            val nextPet = transaction.get(fireStore.collection(PET_INFO).document(nextPetLevel.toString()))
            if(nextPet.exists()){
                val description = nextPet.getString(PET_DESCRIPTION)
                val image = nextPet.getString(PET_IMAGE)
                val next_level = nextPet.getLong(PET_NEXT_EXP)
                val level = nextPet.getLong(PET_LEVEL)

                val newPetMap = mapOf<String, Any>(
                    PET_DESCRIPTION to description!!,
                    PET_IMAGE to image!!,
                    PET_NEXT_EXP to next_level!!.toInt(),
                    PET_LEVEL to level!!.toInt()
                )

                transaction.set(fireStore.collection(FAMILY).document(familyCode).collection(PET).document(
                    OUR_PET), newPetMap, SetOptions.merge())
            }else{
                Exception("레벨업할 펫 정보를 가져오지 못했습니다.")
            }

        }
}
