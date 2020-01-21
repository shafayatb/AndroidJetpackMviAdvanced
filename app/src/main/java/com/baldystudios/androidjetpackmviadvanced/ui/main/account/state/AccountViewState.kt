package com.baldystudios.androidjetpackmviadvanced.ui.main.account.state

import android.os.Parcelable
import com.baldystudios.androidjetpackmviadvanced.models.AccountProperties
import kotlinx.android.parcel.Parcelize

const val ACCOUNT_VIEW_STATE_BUNDLE_KEY = "com.baldystudios.androidjetpackmviadvanced.ui.main.blog.state.AccountViewState"

@Parcelize
class AccountViewState(
    var accountProperties: AccountProperties? = null
):Parcelable