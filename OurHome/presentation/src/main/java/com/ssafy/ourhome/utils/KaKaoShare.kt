package com.ssafy.ourhome.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.util.Log
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import com.kakao.sdk.template.model.Button
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.Link

fun shareFamilyCode(context: Context, familyCode: String) {

    val ourHomeShare = FeedTemplate(
        content = Content(
            title = "우리집 코드",
            description = familyCode,
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/ourhome-3896b.appspot.com/o/ic_launcher_our_home-playstore.png?alt=media&token=30ab42b5-10a4-4b73-ab6f-db775e308098",
            link = Link(
                mobileWebUrl = "https://play.google.com/store/apps"
            )
        ),
        buttons = listOf(
            Button(
                "우리집으로 입주하기",
                Link(  )
            )
        )
    )

    // 피드 메시지 보내기
    // 카카오톡 설치여부 확인
    if (ShareClient.instance.isKakaoTalkSharingAvailable(context)) {
        // 카카오톡으로 카카오톡 공유 가능
        ShareClient.instance.shareDefault(context, ourHomeShare) { sharingResult, error ->
            if (error != null) {
                Log.e("TAG", "카카오톡 공유 실패", error)
            } else if (sharingResult != null) {
                Log.d("TAG", "카카오톡 공유 성공 ${sharingResult.intent}")
                context.startActivity(sharingResult.intent)

                // 카카오톡 공유에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
                Log.w("TAG", "Warning Msg: ${sharingResult.warningMsg}")
                Log.w("TAG", "Argument Msg: ${sharingResult.argumentMsg}")
            }
        }
    } else {
        // 카카오톡 미설치: 웹 공유 사용 권장
        // 웹 공유 예시 코드
        val sharerUrl = WebSharerClient.instance.makeDefaultUrl(ourHomeShare)

        // CustomTabs으로 웹 브라우저 열기

        // 1. CustomTabsServiceConnection 지원 브라우저 열기
        // ex) Chrome, 삼성 인터넷, FireFox, 웨일 등
        try {
            KakaoCustomTabsClient.openWithDefault(context, sharerUrl)
        } catch (e: UnsupportedOperationException) {
            // CustomTabsServiceConnection 지원 브라우저가 없을 때 예외처리
        }

        // 2. CustomTabsServiceConnection 미지원 브라우저 열기
        // ex) 다음, 네이버 등
        try {
            KakaoCustomTabsClient.open(context, sharerUrl)
        } catch (e: ActivityNotFoundException) {
            // 디바이스에 설치된 인터넷 브라우저가 없을 때 예외처리
        }
    }
}
