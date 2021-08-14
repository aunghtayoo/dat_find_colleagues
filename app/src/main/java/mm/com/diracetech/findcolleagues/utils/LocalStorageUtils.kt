package mm.com.diracetech.findcolleagues.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import mm.com.diracetech.findcolleagues.data.vos.SignedUpInfo

object LocalStorageUtils {

    private fun getUserPreference(context: Context): SharedPreferences =
        context.getSharedPreferences("user_preference", Context.MODE_PRIVATE)

    private fun getAppPreference(context: Context): SharedPreferences =
        context.getSharedPreferences("app_preference", Context.MODE_PRIVATE)

    fun setSignedUpInfo(context: Context, signedUpInfo: SignedUpInfo) {
        val signedUpInfoJson = Gson().toJson(signedUpInfo)
        getUserPreference(context).edit().apply {
            putString("singed_up_info", signedUpInfoJson)
            commit()
        }
    }

    fun setUpInitKey(context: Context, key: String) {
        getAppPreference(context).edit().apply {
            putString("db_key", key)
            commit()
        }
    }

    fun getKey(context: Context) = getAppPreference(context).getString("db_key", "")

    fun getSignedUpInfo(context: Context): SignedUpInfo {
        val signedUpInfoJson = getUserPreference(context).getString("singed_up_info", "")
        if (signedUpInfoJson == "") {
            return SignedUpInfo("", "", "", true)
        } else {
            return Gson().fromJson(signedUpInfoJson, SignedUpInfo::class.java)
        }
    }

    fun isMyPhone(context: Context, phone: String): Boolean {
        val signedUpInfo = getSignedUpInfo(context)
        signedUpInfo ?: return false
        if (phone == signedUpInfo?.phoneNo) {
            return true
        }
        return false
    }

    fun isSignedUpInfoExisted(context: Context): Boolean {
        val signedUpInfoJson = getUserPreference(context).getString("singed_up_info", "")
        if (signedUpInfoJson?.isEmpty()!!) {
            return false
        }
        return true
    }

}