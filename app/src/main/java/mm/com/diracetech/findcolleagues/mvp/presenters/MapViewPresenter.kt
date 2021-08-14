package mm.com.diracetech.findcolleagues.mvp.presenters

import mm.com.diracetech.findcolleagues.data.vos.SignedUpInfo
import mm.com.diracetech.findcolleagues.delegates.MapViewDelegate
import mm.com.diracetech.findcolleagues.mvp.views.MapView
import mm.com.diracetech.shared.mvp.presenters.BasePresenter

class MapViewPresenter : BasePresenter<MapView>(), MapViewDelegate {

    override fun doSetting() {
        mView.onSetting()
    }

    override fun closeSetting() {
        mView.onCloseSetting()
    }

    override fun updateInfo(signedUpInfo: SignedUpInfo) {
        mView.onInfoUpdate(signedUpInfo)
    }


}