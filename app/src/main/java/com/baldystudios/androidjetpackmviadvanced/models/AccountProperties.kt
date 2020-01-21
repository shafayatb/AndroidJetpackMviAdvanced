package com.baldystudios.androidjetpackmviadvanced.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "account_properties")
data class AccountProperties(

    @Json(name = "pk")
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "pk")
    var pk: Int,

    @Json(name = "email")
    @ColumnInfo(name = "email")
    var email: String,

    @Json(name = "username")
    @ColumnInfo(name = "username")
    var username: String

) : Parcelable