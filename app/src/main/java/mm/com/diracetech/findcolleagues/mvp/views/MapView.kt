package mm.com.diracetech.findcolleagues.mvp.views

import mm.com.diracetech.findcolleagues.data.vos.SignedUpInfo
import mm.com.diracetech.shared.mvp.views.BaseView

interface MapView : BaseView {
    fun onSetting()
    fun onCloseSetting()
    fun onInfoUpdate(signedUpInfo: SignedUpInfo)
}