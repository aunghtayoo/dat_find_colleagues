package mm.com.diracetech.shared.fragments

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

open class BaseFragment : Fragment() {
    // display snack-bar message.
    protected fun displaySnackBarMessage(message: String, activity: Activity) =
        Snackbar.make(activity.window.decorView, message, Snackbar.LENGTH_SHORT).show()
}