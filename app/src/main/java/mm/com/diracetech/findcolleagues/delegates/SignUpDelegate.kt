package mm.com.diracetech.findcolleagues.delegates

import mm.com.diracetech.findcolleagues.data.vos.SignedUpInfo

interface SignUpDelegate {
    fun doSignUp(signedUpInfo: SignedUpInfo)
}