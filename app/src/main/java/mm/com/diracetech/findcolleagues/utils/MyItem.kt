package mm.com.diracetech.findcolleagues.utils

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class MyItem : ClusterItem {

    private var mPosition: LatLng? = null
    private var mTitle: String? = null
    private var mSnippet: String? = null

    constructor(lat: Double, lng: Double) {
        mPosition = LatLng(lat, lng)
    }

    constructor(
        lat: Double,
        lng: Double,
        title: String,
        snippet: String
    ) {
        mPosition = LatLng(lat, lng)
        mTitle = title
        mSnippet = snippet
    }

    override fun getSnippet() = mSnippet
    override fun getTitle() = mTitle
    override fun getPosition(): LatLng = mPosition!!
}