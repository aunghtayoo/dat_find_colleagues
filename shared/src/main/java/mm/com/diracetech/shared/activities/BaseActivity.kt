package mm.com.diracetech.shared.activities

import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {

    // display snack-bar message.
    protected fun displaySnackBarMessage(message: String) {
        Snackbar.make(window.decorView, message, Snackbar.LENGTH_SHORT).show()
    }

    protected fun startProgress(message: String) {
        // start animation.
    }

    protected fun stopProgress(message: String) {
        // stop animation.
    }

    override fun onStart() {
        super.onStart()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
    }

    override fun onDestroy() {
        super.onDestroy()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
    }
}