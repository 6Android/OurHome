package com.ssafy.data.datasource.pet

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query

interface PetDataSource {

    fun getFamilyPet(familyCode: String) : DocumentReference
}