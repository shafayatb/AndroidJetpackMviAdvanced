package com.baldystudios.androidjetpackmviadvanced.ui

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
import com.afollestad.materialdialogs.MaterialDialog
import com.baldystudios.androidjetpackmviadvanced.R
import com.baldystudios.androidjetpackmviadvanced.util.StateMessageCallback

private val TAG: String = "AppDebug"

fun Activity.displayToast(
    @StringRes message:Int,
    stateMessageCallback: StateMessageCallback
){
    Toast.makeText(this, message,Toast.LENGTH_LONG).show()
    stateMessageCallback.removeMessageFromStack()
}

fun Activity.displayToast(
    message:String,
    stateMessageCallback: StateMessageCallback
){
    Toast.makeText(this,message,Toast.LENGTH_LONG).show()
    stateMessageCallback.removeMessageFromStack()
}

fun Activity.displaySuccessDialog(
    message: String?,
    stateMessageCallback: StateMessageCallback
): MaterialDialog {
    return MaterialDialog(this)
        .show{
            title(R.string.text_success)
            message(text = message)
            positiveButton(R.string.text_ok){
                stateMessageCallback.removeMessageFromStack()
            }
        }
}

fun Activity.displayErrorDialog(
    message: String?,
    stateMessageCallback: StateMessageCallback
): MaterialDialog {
    return MaterialDialog(this)
        .show{
            title(R.string.text_error)
            message(text = message)
            positiveButton(R.string.text_ok){
                stateMessageCallback.removeMessageFromStack()
            }
        }
}

fun Activity.displayInfoDialog(
    message: String?,
    stateMessageCallback: StateMessageCallback
): MaterialDialog {
    return MaterialDialog(this)
        .show{
            title(R.string.text_info)
            message(text = message)
            positiveButton(R.string.text_ok){
                stateMessageCallback.removeMessageFromStack()
            }
        }
}

fun Activity.areYouSureDialog(
    message: String,
    callback: AreYouSureCallback,
    stateMessageCallback: StateMessageCallback
): MaterialDialog {
    return MaterialDialog(this)
        .show{
            title(R.string.are_you_sure)
            message(text = message)
            negativeButton(R.string.text_cancel){
                callback.cancel()
                stateMessageCallback.removeMessageFromStack()
            }
            positiveButton(R.string.text_yes){
                callback.proceed()
                stateMessageCallback.removeMessageFromStack()
            }
        }
}


interface AreYouSureCallback {

    fun proceed()

    fun cancel()
}