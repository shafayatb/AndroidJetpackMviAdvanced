package com.baldystudios.androidjetpackmviadvanced.session

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.baldystudios.androidjetpackmviadvanced.models.AuthToken
import com.baldystudios.androidjetpackmviadvanced.persistence.AuthTokenDao
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager
@Inject
constructor(
    val authTokenDao: AuthTokenDao,
    val application: Application
) {

    private val TAG: String = "AppDebug"

    private val _cachedToken = MutableLiveData<AuthToken>()

    val cachedToken: LiveData<AuthToken>
        get() = _cachedToken

    fun login(newValue: AuthToken){
        setValue(newValue)
    }

    fun logout(){
        Log.d(TAG, "logout: ")


        CoroutineScope(IO).launch{
            var errorMessage: String? = null
            try{
                _cachedToken.value!!.account_pk?.let { authTokenDao.nullifyToken(it)
                } ?: throw CancellationException("Token Error. Logging out user.")
            }catch (e: CancellationException) {
                Log.e(TAG, "logout: ${e.message}")
                errorMessage = e.message
            }
            catch (e: Exception) {
                Log.e(TAG, "logout: ${e.message}")
                errorMessage = errorMessage + "\n" + e.message
            }
            finally {
                errorMessage?.let{
                    Log.e(TAG, "logout: ${errorMessage}" )
                }
                Log.d(TAG, "logout: finally")
                setValue(null)
            }
        }
    }

    fun setValue(newValue: AuthToken?) {
        GlobalScope.launch(Main) {
            if (_cachedToken.value != newValue) {
                _cachedToken.value = newValue
            }
        }
    }

    fun isConnectedToTheInternet(): Boolean {
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        try {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val activeNetwork = cm.getNetworkCapabilities(cm.activeNetwork) ?: return false
                when {
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } else {
                cm.activeNetworkInfo.isConnected
            }
        } catch (e: Exception) {
            Log.e(TAG, "isConnectedToInternet: ${e.message}")
        }

        return false
    }

}