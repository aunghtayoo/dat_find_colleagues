package mm.com.diracetech.findcolleagues.repositories

import androidx.lifecycle.MutableLiveData
import mm.com.diracetech.findcolleagues.data.vos.CurUserInfo

object UpdateLocationRepository {

    var mMutableLiveData = MutableLiveData<MutableList<CurUserInfo>>()

    fun getCurUsers(): MutableLiveData<MutableList<CurUserInfo>> {
        return mMutableLiveData
    }

    fun setCurUsers(mutableLiveData: MutableLiveData<MutableList<CurUserInfo>>) {
        mMutableLiveData = mutableLiveData
    }
}