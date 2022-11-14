package com.ssafy.ourhome.navigation

enum class OurHomeScreens {

    SplashScreen,
    NextScreen,
    LoginScreen,
    JoinEmailScreen,
    JoinPasswordScreen,
    JoinNickNameScreen,
    QuestionListScreen,
    PetDetailScreen,
    QuestionDetailScreen,
    SettingScreen,
    ManageFamilyScreen,
    ChatScreen,
    EnterHomeScreen,
    AddScheduleScreen,
    AddMemberScreen,
    ScheduleDetailScreen,
    AlbumScreen,
    AlbumDetailScreen,
    UserPageScreen,
    EditProfileScreen,
    MapScreen
    ;

    companion object {
        fun fromRoute(route: String?): OurHomeScreens = when (route?.substringBefore("/")) {
            SplashScreen.name -> SplashScreen
            NextScreen.name -> NextScreen
            LoginScreen.name -> LoginScreen
            JoinEmailScreen.name -> JoinEmailScreen
            JoinPasswordScreen.name -> JoinPasswordScreen
            JoinNickNameScreen.name -> JoinNickNameScreen
            QuestionListScreen.name -> QuestionListScreen
            PetDetailScreen.name -> PetDetailScreen
            QuestionDetailScreen.name -> QuestionDetailScreen
            SettingScreen.name -> SettingScreen
            ManageFamilyScreen.name -> ManageFamilyScreen
            ChatScreen.name -> ChatScreen
            EnterHomeScreen.name -> EnterHomeScreen
            AddScheduleScreen.name -> AddScheduleScreen
            AddMemberScreen.name -> AddMemberScreen
            ScheduleDetailScreen.name -> ScheduleDetailScreen
            AlbumScreen.name -> AlbumScreen
            AlbumDetailScreen.name -> AlbumDetailScreen
            UserPageScreen.name -> UserPageScreen
            EditProfileScreen.name -> EditProfileScreen
            MapScreen.name -> MapScreen

            else -> throw IllegalStateException("Route $route is not recognized")
        }
    }
}