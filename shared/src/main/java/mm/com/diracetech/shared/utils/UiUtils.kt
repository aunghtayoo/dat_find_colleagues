/*
 * Copyright (c) 2020. AhTuTu App Development Team.
 * All Right Reserved.
 * Created in [ 6/7/20 3:56 PM ]
 */

package mm.com.diracetech.shared.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import coil.api.load
import coil.transform.CircleCropTransformation
import java.io.File
import java.util.*

object UiUtils {

    fun setLocale(code: String, context: Context) {
        val locale = Locale(code)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        val res = context.resources
        res.updateConfiguration(
            config,
            res.displayMetrics
        )
    }

    fun loadImageWithUrl(context: Context, view: ImageView, imgUrl: String) {
        //view.load(imgUrl)
    }

    fun loadProfileImage(context: Context, view: ImageView, imgUrl: String) {
        /*view.load(imgUrl) {
            crossfade(true)
            transformations(CircleCropTransformation())
        }*/
    }

    fun loadImageWithResource(context: Context, view: ImageView, resourceId: Int) {
        /*view.load(resourceId) {
            crossfade(true)
            transformations(CircleCropTransformation())
        }*/
    }

    fun loadImageWithFile(context: Context, view: ImageView, absFilePath: String) {
        /*view.load(File(absFilePath)) {
            crossfade(true)
            transformations(CircleCropTransformation())
        }*/
    }

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}