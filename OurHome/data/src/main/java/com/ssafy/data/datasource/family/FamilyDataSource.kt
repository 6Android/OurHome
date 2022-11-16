package com.ssafy.data.datasource.family

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot

interface FamilyDataSource {

    // family doc 반환 (family Collection -> family_code Doc)
    fun getFamilyDoc(familyCode: String): DocumentReference

    // family/user Doc (family Collection -> family_code Doc ->  user Collection -> email Doc)
    fun getFamilyUserDoc(familyCode: String, email: String): DocumentReference

    // family/question Doc (family Collection -> family_code Doc ->  question Collection -> seq Doc)
    fun getFamilyQuestionDoc(familyCode: String, seq: String): DocumentReference

    // family/pet Doc (family Collection -> family_code Doc ->  pet Collection -> out_pet Doc)
    fun getFamilyPetDoc(familyCode: String): DocumentReference

    // family/album Doc (family Collection -> family_code Doc ->  album Collection -> init_date Doc)
    fun getFamilyAlbumDoc(familyCode: String): DocumentReference

    // family/chat Doc (family Collection -> family_code Doc ->  chat Collection -> init_date Doc)
    fun getFamilyChatDoc(familyCode: String): DocumentReference

    // family 이미 있는지 검사
    fun checkFamily(email: String): Task<DocumentSnapshot>

    // 가족장 email 가져오기
    fun getFamilyManager(familyCode: String): DocumentReference

}