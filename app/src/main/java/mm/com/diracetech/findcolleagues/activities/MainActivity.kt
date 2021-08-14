package mm.com.diracetech.findcolleagues.activities

import android.os.Bundle
import mm.com.diracetech.findcolleagues.activities.MapViewActivity.Companion.mapViewIntent
import mm.com.diracetech.findcolleagues.activities.SignUpActivity.Companion.signUpIntent
import mm.com.diracetech.findcolleagues.utils.LocalStorageUtils

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launchApp()
    }

    private fun launchApp() {
        if (LocalStorageUtils.isSignedUpInfoExisted(applicationContext)) {
            startActivity(mapViewIntent(applicationContext))
            finish()
        } else {
            startActivity(signUpIntent(applicationContext))
            finish()
        }
    }
}