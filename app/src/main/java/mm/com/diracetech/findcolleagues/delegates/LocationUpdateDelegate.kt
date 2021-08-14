package mm.com.diracetech.findcolleagues.delegates

import mm.com.diracetech.findcolleagues.data.vos.CurUserInfo

interface LocationUpdateDelegate {
    fun onLocationUpdated(curUsers: List<CurUserInfo>)
}