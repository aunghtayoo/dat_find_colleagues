package mm.com.diracetech.findcolleagues.delegates

import mm.com.diracetech.findcolleagues.data.vos.SignedUpInfo

interface MapViewDelegate {
    fun doSetting()
    fun closeSetting()
    fun updateInfo(signedUpInfo: SignedUpInfo)
}