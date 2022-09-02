package me.kofesst.android.motivatorapp.presentation.screen

sealed class ScreenConstants {
    companion object {
        const val ID_ARG_NAME = "id"
    }

    class Auth private constructor() {
        companion object {
            const val ROUTE_NAME = "auth"
        }
    }

    class Feed private constructor() {
        companion object {
            const val ROUTE_NAME = "feed"
        }
    }

    class ProfileOverview private constructor() {
        companion object {
            const val ROUTE_NAME = "profile"
        }
    }

    class EditProfile private constructor() {
        companion object {
            const val ROUTE_NAME = "profile/edit"
        }
    }

    class PostOverview private constructor() {
        companion object {
            const val ROUTE_NAME = "post"
        }
    }

    class CreatePost private constructor() {
        companion object {
            const val ROUTE_NAME = "post/create"
        }
    }
}