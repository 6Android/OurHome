package com.ssafy.data.datasource.family

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ssafy.data.utils.FAMILY
import com.ssafy.data.utils.QUESTION
import com.ssafy.data.utils.USER
import javax.inject.Inject

class FamilyDataSourceImpl @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val fireAuth: FirebaseAuth,
) : FamilyDataSource {

    // family doc 반환 (family Collection -> family_code Doc)
    override fun getFamilyDoc(familyCode: String) =
        fireStore.collection(FAMILY).document(familyCode)

    // family/user Doc (family Collection -> family_code Doc ->  user Collection -> email Doc)
    override fun getFamilyUserDoc(familyCode: String, email: String) =
        getFamilyDoc(familyCode).collection(USER).document(email)

    // family/question Doc (family Collection -> family_code Doc ->  question Collection -> seq Doc)
    override fun getFamilyQuestionDoc(familyCode: String, seq: String) =
        getFamilyDoc(familyCode).collection(QUESTION).document(seq)
}