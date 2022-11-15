package com.ssafy.data.datasource.pet

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.ssafy.data.utils.FAMILY
import com.ssafy.data.utils.OUR_PET
import com.ssafy.data.utils.PET
import com.ssafy.data.utils.PET_INFO
import javax.inject.Inject

class PetDataSourceImpl @Inject constructor(
    private val fireStore: FirebaseFirestore
) : PetDataSource {

    override fun getFamilyPet(familyCode: String): DocumentReference =
        fireStore.collection(FAMILY).document(familyCode).collection(PET).document(OUR_PET)

    // pet_info doc 반환 (question Collection -> seq Doc)
    override fun getPetInfoDoc(seq: String) =
        fireStore.collection(PET_INFO).document(seq)
}