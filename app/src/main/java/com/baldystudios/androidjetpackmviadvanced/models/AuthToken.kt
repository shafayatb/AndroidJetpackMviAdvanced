package com.baldystudios.androidjetpackmviadvanced.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

const val AUTH_TOKEN_BUNDLE_KEY = "com.baldystudios.androidjetpackmviadvanced.models.AuthToken"

@Parcelize
@Entity(
    tableName = "auth_token",
    foreignKeys = [
        ForeignKey(
            entity = AccountProperties::class,
            parentColumns = ["pk"],
            childColumns = ["account_pk"],
            onDelete = CASCADE
        )
    ]
)
data class AuthToken(

    @PrimaryKey
    @ColumnInfo(name = "account_pk")
    var account_pk: Int? = -1,

    @Json(name = "token")
    @ColumnInfo(name = "token")
    var token: String? = null
): Parcelable