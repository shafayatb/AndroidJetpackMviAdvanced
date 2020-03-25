package com.baldystudios.androidjetpackmviadvanced.util

class Constants {

    companion object {

        const val BASE_URL = "https://open-api.xyz/api/"
        const val PASSWORD_RESET_URL = "https://open-api.xyz/password_reset/"

        const val UNKNOWN_ERROR = "Unknown error"
        const val NETWORK_TIMEOUT = 6000L
        const val CACHE_TIMEOUT = 2000L
        const val NETWORK_ERROR = "Network error"
        const val NETWORK_ERROR_TIMEOUT = "Network timeout"
        const val CACHE_ERROR_TIMEOUT = "Cache timeout"
        const val TESTING_NETWORK_DELAY = 0L
        const val TESTING_CACHE_DELAY = 0L
        const val INVALID_STATE_EVENT = "Invalid state event"
        const val CANNOT_BE_UNDONE = "This can't be undone."

        const val PAGINATION_PAGE_SIZE = 10

        const val GALLERY_REQUEST_CODE: Int = 201
        const val PERMISSIONS_REQUEST_READ_STORAGE: Int = 301
        const val CROP_IMAGE_INTENT_CODE: Int = 401

    }

}