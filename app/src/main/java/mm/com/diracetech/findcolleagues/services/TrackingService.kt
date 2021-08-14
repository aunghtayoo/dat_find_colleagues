package mm.com.diracetech.findcolleagues.services

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.IBinder
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.Gson
import mm.com.diracetech.findcolleagues.BuildConfig
import mm.com.diracetech.findcolleagues.data.vos.CurUserInfo
import mm.com.diracetech.findcolleagues.data.vos.SignedUpInfo
import mm.com.diracetech.findcolleagues.data.vos.UserFbInfo
import mm.com.diracetech.findcolleagues.utils.LocalStorageUtils
import java.io.IOException
import java.util.*

class TrackingService : Service() {

    lateinit var signedUpInfo: SignedUpInfo

    companion object {
        const val myServiceAction = "A380"
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        super.onCreate()
        loginToFirebase()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopSelf()
    }

    private fun loginToFirebase() {
        val email = BuildConfig.EMAIL
        val password = BuildConfig.PASSWORD
        FirebaseAuth.getInstance().signInWithEmailAndPassword(
            email,
            password
        ).addOnCompleteListener(OnCompleteListener {
            if (it.isSuccessful) {
                requestLocationUpdates()
            }
        })
    }

    private fun requestLocationUpdates() {

        var request = LocationRequest()
        request.apply {
            interval = 5 * 1000 //in seconds.
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val client = LocationServices.getFusedLocationProviderClient(this)
        val path = BuildConfig.FB_PATH
        val permission = ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)

        if (permission == PackageManager.PERMISSION_GRANTED) {
            val database = FirebaseDatabase.getInstance().getReference(path)
            database.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(data: DataSnapshot) {
                    try {
                        var curUsers = mutableListOf<CurUserInfo>()
                        val user = data.value as HashMap<String, HashMap<String, String>>
                        for (entry in user?.entries) {
                            val name = user[entry.key]?.get("name")
                            val phone = user[entry.key]?.get("phone")
                            val lat = user[entry.key]?.get("lat")?.toDouble()
                            val long = user[entry.key]?.get("long")?.toDouble()
                            val staffId = user[entry.key]?.get("staffId")
                            val street = user[entry.key]?.get("street")
                            val township = user[entry.key]?.get("township")
                            var showMeOnMap = user[entry.key]?.get("showMeOnMap")?.toBoolean()
                            showMeOnMap = showMeOnMap ?: true
                            curUsers.add(
                                CurUserInfo(
                                    lat!!,
                                    long!!,
                                    name!!,
                                    phone!!,
                                    staffId!!,
                                    street!!,
                                    township!!,
                                    showMeOnMap!!
                                )
                            )
                        }
                        updateUI(curUsers)
                    } catch (e: Exception) {
                        //show something.
                    }
                }

                override fun onCancelled(data: DatabaseError) {
                    //Noting to do.
                }
            })

            client.requestLocationUpdates(request, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    //Get a reference to the database, so your app can perform read and write operations//
                    val location = locationResult?.lastLocation
                    location ?: return
                    val key = LocalStorageUtils.getKey(applicationContext)
                    signedUpInfo = LocalStorageUtils.getSignedUpInfo(applicationContext)
                    val geoCoder = Geocoder(applicationContext, Locale.ENGLISH)
                    try {
                        val addresses =
                            geoCoder.getFromLocation(location?.latitude, location?.longitude, 1)
                        if (addresses.size > 0) {
                            val address = addresses[0]
                            val road = address.thoroughfare
                            var township =
                                address.subLocality ?: address.subAdminArea ?: address.adminArea
                                ?: address.locality ?: address.countryName
                            database.child(key!!).setValue(
                                UserFbInfo(
                                    location?.latitude.toString(),
                                    location?.longitude.toString(),
                                    signedUpInfo.userName,
                                    signedUpInfo.phoneNo,
                                    signedUpInfo.staffId,
                                    road ?: "",
                                    township ?: "",
                                    signedUpInfo.showMeOnMap.toString()
                                )
                            )
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }

                override fun onLocationAvailability(p0: LocationAvailability?) {
                    super.onLocationAvailability(p0)
                }
            }, null)
        }
    }

    fun updateUI(curUsers: MutableList<CurUserInfo>) {
        val curUsersJson = Gson().toJson(curUsers)
        var intent = Intent()
        intent.apply {
            action = myServiceAction
            putExtra("B787", curUsersJson)
        }
        sendBroadcast(intent)
    }

}

