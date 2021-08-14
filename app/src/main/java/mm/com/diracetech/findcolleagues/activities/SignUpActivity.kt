package mm.com.diracetech.findcolleagues.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import kotlinx.android.synthetic.main.activity_sign_up.*
import mm.com.diracetech.findcolleagues.R
import mm.com.diracetech.findcolleagues.activities.MapViewActivity.Companion.mapViewIntent
import mm.com.diracetech.findcolleagues.data.vos.SignedUpInfo
import mm.com.diracetech.findcolleagues.mvp.presenters.SignUpPresenter
import mm.com.diracetech.findcolleagues.mvp.views.SignUpView
import mm.com.diracetech.findcolleagues.utils.LocalStorageUtils

class SignUpActivity : BaseActivity(), SignUpView {

    private lateinit var mPresenter: SignUpPresenter

    companion object {
        fun signUpIntent(context: Context): Intent {
            return Intent(context, SignUpActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        setUpPresenter()
        setUpListeners()
    }

    override fun onSignedUp(signedUpInfo: SignedUpInfo) {
        LocalStorageUtils.setSignedUpInfo(applicationContext, signedUpInfo)
        LocalStorageUtils.setUpInitKey(applicationContext, signedUpInfo?.staffId)
        startActivity(mapViewIntent(applicationContext))
        finish()
    }

    private fun setUpPresenter() {
        mPresenter = SignUpPresenter()
        mPresenter.initPresenter(this)
    }

    private fun setUpListeners() {

        btnSignUp.setOnClickListener {
            if (isFormValid(getSignUpFormData())) {
                mPresenter.doSignUp(getSignUpFormData())
            } else {
                displaySnackBarMessage("SignUp Form not completed.")
            }
        }

        textUserName.addTextChangedListener(object : TextWatcher {
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

        textPhoneNo.addTextChangedListener(object : TextWatcher {
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

        textStaffId.addTextChangedListener(object : TextWatcher {
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

    }

    private fun getSignUpFormData() = SignedUpInfo(
        textUserName.text.toString(),
        textPhoneNo.text.toString(),
        textStaffId.text.toString(),
        true
    )

    private fun isFormValid(signedUpInfo: SignedUpInfo): Boolean {
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
        layoutUserName.error = "Name required."
    }

    private fun hideNameError() {
        layoutUserName.error = null
    }

    private fun displayPasswordError() {
        layoutPhoneNo.error = "Phone Number required."
    }

    private fun hidePhoneError() {
        layoutPhoneNo.error = null
    }

    private fun displayStaffIdError() {
        layoutStaffId.error = "Staff ID required."
    }

    private fun hideStaffIdError() {
        layoutStaffId.error = null
    }

}