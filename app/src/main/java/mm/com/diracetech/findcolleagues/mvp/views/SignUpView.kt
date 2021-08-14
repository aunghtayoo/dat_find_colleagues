package mm.com.diracetech.findcolleagues.mvp.views

import mm.com.diracetech.findcolleagues.data.vos.SignedUpInfo
import mm.com.diracetech.shared.mvp.views.BaseView

interface SignUpView : BaseView {
    fun onSignedUp(signedUpInfo: SignedUpInfo)
}