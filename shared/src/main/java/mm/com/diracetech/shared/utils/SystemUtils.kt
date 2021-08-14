/*
 * Copyright (c) 2020. AhTuTu App Development Team.
 * All Right Reserved.
 * Created in [ 6/10/20 2:37 PM ]
 */

package mm.com.diracetech.shared.utils

import android.content.Context
import android.content.pm.PackageInfo

object SystemUtils {
    fun getCurrentAppVersion(context: Context) =
        context.packageManager.getPackageInfo(context.packageName, 0).versionName

}