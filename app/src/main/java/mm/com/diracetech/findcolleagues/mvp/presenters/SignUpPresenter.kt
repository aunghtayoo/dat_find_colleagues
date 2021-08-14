package mm.com.diracetech.findcolleagues.mvp.presenters

import mm.com.diracetech.findcolleagues.data.vos.SignedUpInfo
import mm.com.diracetech.findcolleagues.delegates.SignUpDelegate
import mm.com.diracetech.findcolleagues.mvp.views.SignUpView
import mm.com.diracetech.shared.mvp.presenters.BasePresenter

class SignUpPresenter : BasePresenter<SignUpView>(), SignUpDelegate {

    override fun doSignUp(signedUpInfo: SignedUpInfo) {
        mView.onSignedUp(signedUpInfo)
    }
}