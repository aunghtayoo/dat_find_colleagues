package mm.com.diracetech.findcolleagues.activities

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.CALL_PHONE
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.LocationManager
import android.location.LocationManager.GPS_PROVIDER
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.maps.android.ui.IconGenerator
import kotlinx.android.synthetic.main.activity_map_view.*
import kotlinx.android.synthetic.main.activity_map_view_edit_info_bottom_sheet.*
import kotlinx.android.synthetic.main.activity_map_view_option_bottom_sheet.*
import mm.com.diracetech.findcolleagues.R
import mm.com.diracetech.findcolleagues.data.vos.CurUserInfo
import mm.com.diracetech.findcolleagues.data.vos.SignedUpInfo
import mm.com.diracetech.findcolleagues.mvp.presenters.MapViewPresenter
import mm.com.diracetech.findcolleagues.mvp.views.MapView
import mm.com.diracetech.findcolleagues.services.TrackingService
import mm.com.diracetech.findcolleagues.utils.LocalStorageUtils
import mm.com.diracetech.shared.utils.UiUtils.hideKeyboard

class MapViewActivity : BaseActivity(), MapView, OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

    private lateinit var locationManager: LocationManager
    private lateinit var signedUpInfo: SignedUpInfo

    private lateinit var mPresenter: MapViewPresenter

    private lateinit var map: GoogleMap
    private lateinit var mActivatedUsers: MutableList<CurUserInfo>

    lateinit var mEditBottomSheetBehavior: BottomSheetBehavior<CoordinatorLayout>

    private lateinit var mCallBottomSheetView: View
    private lateinit var mCallBottomSheetDialog: BottomSheetDialog

    private lateinit var mPhoneNo: String

    companion object {
        fun mapViewIntent(context: Context): Intent {
            return Intent(context, MapViewActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_view)
        setUpMap()
        setUpInitData()
        setUpInitView()
        registerLocReceiver()
        setUpPresenter()
        setUpListeners()
        checkGpsTrackingEnable()
        checkPermissionGranted()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onSetting() {
        displayEditBottomSheet()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap!!
        map?.setOnMarkerClickListener(this)
        map?.setOnInfoWindowClickListener(this)
        zoomMap()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTrackerService()
    }

    override fun onCloseSetting() {
        closeEditBottomSheet()
    }

    override fun onInfoUpdate(signedUpInfo: SignedUpInfo) {
        LocalStorageUtils.setSignedUpInfo(applicationContext, signedUpInfo)
        displaySnackBarMessage("Data Updated.")
        refreshServiceCall()
        closeEditBottomSheet()
    }

    override fun onBackPressed() {
        if (isEditBottomSheetDisplayed()) {
            closeEditBottomSheet()
        } else {
            super.onBackPressed()
        }
    }

    override fun onInfoWindowClick(marker: Marker?) {
        setUpCallDataAndListeners(marker!!)
        displayCallDialog()
    }

    private fun registerLocReceiver() {
        var intentFilter = IntentFilter()
        intentFilter.addAction(TrackingService.myServiceAction)
        registerReceiver(broadcastReceiver(), intentFilter)
    }

    private fun setUpInitData() {
        mActivatedUsers = mutableListOf()
        signedUpInfo = LocalStorageUtils.getSignedUpInfo(applicationContext)
        switchVisibleOnMap.isChecked = signedUpInfo?.showMeOnMap
    }

    private fun setUpInitView() {
        setUpBottomSheet()
    }

    private fun setUpListeners() {

        imgSetting.setOnClickListener {
            mPresenter.doSetting()
        }

        mEditBottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    mEditBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                //Nothing to do.
            }
        })

        imgSettingClose.setOnClickListener {
            mPresenter.closeSetting()
        }

        btnEditSignUp.setOnClickListener {
            if (isEditFormValid(getSignUpEditFormData())) {
                mPresenter.updateInfo(getSignUpEditFormData())
            } else {
                displaySnackBarMessage("SignUp Form not completed.")
            }
        }

        textEditUserName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                //Nothing to do.
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //Nothing to do.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                hideNameError()
            }
        })

        textEditPhoneNo.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                //Nothing to do.
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //Nothing to do.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                hidePhoneError()
            }
        })

        textEditStaffId.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                //Nothing to do.
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //Nothing to do.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                hideStaffIdError()
            }
        })

        switchVisibleOnMap.setOnCheckedChangeListener { _, isChecked ->
            LocalStorageUtils.setSignedUpInfo(
                applicationContext,
                SignedUpInfo(
                    signedUpInfo?.userName,
                    signedUpInfo?.phoneNo,
                    signedUpInfo?.staffId,
                    isChecked
                )
            )
            refreshServiceCall()
        }

    }

    private fun setUpPresenter() {
        mPresenter = MapViewPresenter()
        mPresenter.initPresenter(this)
    }

    private fun checkGpsTrackingEnable() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(GPS_PROVIDER)) {
            //finish()
        }
    }

    private fun checkPermissionGranted() {
        val permission =
            ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
        if (permission == PackageManager.PERMISSION_GRANTED) {
            startTrackerService()
        } else {
            requestLocationPermission()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 797 && grantResults?.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                zoomMap()
                startTrackerService()
            } else {
                zoomMap()
            }
        } else if (requestCode == 959 && grantResults?.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callColleague()
            }
        }
    }

    private fun startTrackerService() {
        var intent = Intent(this, TrackingService::class.java)
        startService(intent)
    }

    private fun stopTrackerService() {
        var intent = Intent(this, TrackingService::class.java)
        stopService(intent)
    }

    private fun broadcastReceiver(): BroadcastReceiver {
        return object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val curUsersJson = intent?.getStringExtra("B787")
                curUsersJson ?: return
                val listType = object : TypeToken<List<CurUserInfo>>() {}.type
                mActivatedUsers = Gson().fromJson(curUsersJson, listType)
                clearMarkers()
                for (user in mActivatedUsers) {
                    updateMap(user)
                }
            }
        }
    }

    private fun setUpBottomSheet() {
        mEditBottomSheetBehavior = BottomSheetBehavior.from(edit_info_bs)
        mCallBottomSheetView =
            layoutInflater.inflate(R.layout.activity_map_view_option_bottom_sheet, null)
        mCallBottomSheetDialog = BottomSheetDialog(this)
        mCallBottomSheetDialog.setContentView(mCallBottomSheetView)
    }

    private fun setUpMap() {
        val mMapView = mapView as SupportMapFragment
        mMapView.getMapAsync(this)
    }

    private fun updateMap(user: CurUserInfo) {
        map.addMarker(MarkerOptions().apply {
            position(LatLng(user.lat, user.long))
            title(user.name).visible(true)
            snippet(user.phone).visible(true)
            if (user?.showMeOnMap) {
                visible(true)
            } else {
                visible(false)
            }
            icon(BitmapDescriptorFactory.fromBitmap(getMarkerIcon(user?.name, user?.phone)))
        })
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        setUpCallDataAndListeners(marker!!)
        displayCallDialog()
        return true
    }

    private fun clearMarkers() {
        map.clear()
    }

    private fun setUpCallDataAndListeners(marker: Marker?) {
        mPhoneNo = marker?.snippet!!
        mCallBottomSheetDialog?.lblValCallPhoneNo.text = marker?.snippet
        mCallBottomSheetDialog?.lblValCall.text = marker?.title
        mCallBottomSheetDialog?.btnCall.setOnClickListener {
            callColleague()
        }
    }

    private fun displayEditBottomSheet() {
        setUpInitEditFormData()
        mEditBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun isEditBottomSheetDisplayed(): Boolean {
        if (mEditBottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            return true
        }
        return false
    }

    private fun closeEditBottomSheet() {
        hideKeyboard(this)
        if (mEditBottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            mEditBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun setUpInitEditFormData() {
        textEditUserName.setText(signedUpInfo.userName)
        textEditPhoneNo.setText(signedUpInfo.phoneNo)
        textEditStaffId.setText(signedUpInfo.staffId)
    }

    private fun getSignUpEditFormData() = SignedUpInfo(
        textEditUserName.text.toString(),
        textEditPhoneNo.text.toString(),
        textEditStaffId.text.toString(),
        switchVisibleOnMap.isChecked
    )

    private fun isEditFormValid(signedUpInfo: SignedUpInfo): Boolean {
        var isValid = true
        if (signedUpInfo.userName?.isEmpty()) {
            displayNameError()
            isValid = false
        }
        if (signedUpInfo.phoneNo?.isEmpty()) {
            displayPasswordError()
            isValid = false
        }
        if (signedUpInfo.staffId?.isEmpty()) {
            displayStaffIdError()
            isValid = false
        }
        return isValid
    }

    private fun displayNameError() {
        layoutEditUserName.error = "Name required."
    }

    private fun hideNameError() {
        layoutEditUserName.error = null
    }

    private fun displayPasswordError() {
        layoutEditPhoneNo.error = "Phone Number required."
    }

    private fun hidePhoneError() {
        layoutEditPhoneNo.error = null
    }

    private fun displayStaffIdError() {
        layoutEditStaffId.error = "Staff ID required."
    }

    private fun hideStaffIdError() {
        layoutEditStaffId.error = null
    }

    private fun refreshServiceCall() {
        setUpInitData()
        stopTrackerService()
        startTrackerService()
    }

    private fun getMarkerIcon(name: String, phone: String): Bitmap? {
        val icg = IconGenerator(this)
        icg.setColor(R.color.colorPrimary) // green background
        icg.setTextAppearance(R.style.BlackText) // black text
        return icg.makeIcon("$name\n$phone")
    }

    private fun displayCallDialog() {
        mCallBottomSheetDialog.show()
    }

    private fun closeCallDialog() {
        mCallBottomSheetDialog.dismiss()
    }

    private fun callColleague() {
        val permission = ContextCompat.checkSelfPermission(this, CALL_PHONE)
        if (permission == PackageManager.PERMISSION_GRANTED) {
            phoneCall()
        } else {
            requestPhoneCallPermission()
        }
    }

    private fun requestPhoneCallPermission() {
        requestPermissions(arrayOf(CALL_PHONE), 959)
    }

    private fun requestLocationPermission() {
        requestPermissions(arrayOf(ACCESS_FINE_LOCATION), 797)
    }

    private fun phoneCall() {
        try {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$mPhoneNo"))
            startActivity(intent)
        } catch (e: Exception) {
            displaySnackBarMessage("Cannot make call : ${e.localizedMessage}")
        }
    }

    private fun zoomMap() {
        map ?: return
        val permission = ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
        if (permission == PackageManager.PERMISSION_GRANTED) {
            val cameraPosition =
                CameraPosition.builder().target(LatLng(16.8005901, 96.148891)).zoom(12F).build()
            map.apply {
                isMyLocationEnabled = true
                moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            }
        }
    }

}