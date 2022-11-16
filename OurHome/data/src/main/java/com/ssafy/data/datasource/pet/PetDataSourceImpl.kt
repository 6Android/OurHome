package com.ssafy.data.datasource.pet

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
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
}