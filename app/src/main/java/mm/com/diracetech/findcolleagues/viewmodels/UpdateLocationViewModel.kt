package mm.com.diracetech.findcolleagues.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mm.com.diracetech.findcolleagues.data.vos.CurUserInfo
import mm.com.diracetech.findcolleagues.repositories.UpdateLocationRepository

class UpdateLocationViewModel : ViewModel() {

    private var mMutableLiveData = MutableLiveData<MutableList<CurUserInfo>>()

    init {
        mMutableLiveData = UpdateLocationRepository.getCurUsers()
    }

    fun getMutableLiveData(): LiveData<MutableList<CurUserInfo>> {
        return mMutableLiveData
    }

}