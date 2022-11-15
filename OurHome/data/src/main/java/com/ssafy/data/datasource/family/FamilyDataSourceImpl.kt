package com.ssafy.data.datasource.family

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.ssafy.data.utils.*
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

    // family/pet Doc (family Collection -> family_code Doc ->  pet Collection -> out_pet Doc)
    override fun getFamilyPetDoc(familyCode: String) =
        getFamilyDoc(familyCode).collection(PET).document(OUR_PET)

    // family/album Doc (family Collection -> family_code Doc ->  album Collection -> init_date Doc)
    override fun getFamilyAlbumDoc(familyCode: String) =
        getFamilyDoc(familyCode).collection(ALBUM).document(INIT_DATE)

    // family/chat Doc (family Collection -> family_code Doc ->  chat Collection -> init_date Doc)
    override fun getFamilyChatDoc(familyCode: String) =
        getFamilyDoc(familyCode).collection(CHAT).document(INIT_DATE)

    // family 이미 있는지 검사
    override fun checkFamily(familyCode: String): Task<DocumentSnapshot> =
        fireStore.collection(FAMILY).document(familyCode).get()
}