package com.ssafy.data.datasource.family

import com.google.firebase.firestore.DocumentReference

interface FamilyDataSource {

    // family doc 반환 (family Collection -> family_code Doc)
    fun getFamilyDoc(familyCode: String): DocumentReference

    // family/user Doc (family Collection -> family_code Doc ->  user Collection -> email Doc)
    fun getFamilyUserDoc(familyCode: String, email: String): DocumentReference

    // family/question Doc (family Collection -> family_code Doc ->  question Collection -> seq Doc)
    fun getFamilyQuestionDoc(familyCode: String, seq: String): DocumentReference
}