package com.ssafy.data.datasource.pet

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query

interface PetDataSource {

    fun getFamilyPet(familyCode: String) : DocumentReference

    // pet_info doc 반환 (question Collection -> seq Doc)
    fun getPetInfoDoc(seq: String): DocumentReference
}