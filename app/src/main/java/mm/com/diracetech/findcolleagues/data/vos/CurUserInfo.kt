package mm.com.diracetech.findcolleagues.data.vos

data class CurUserInfo(
    val lat: Double,
    val long: Double,
    val name: String,
    val phone: String,
    val staffId: String,
    val street: String,
    val township: String,
    val showMeOnMap: Boolean
)