package com.carswaddle.carswaddleandroid.ui.activities

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.R.layout.activity_verify_code
import com.mukesh.OtpView


class VerifySMSCodeActivity: Activity() {

    private val verify: OtpView by lazy { findViewById(R.id.codeEntryView) as OtpView }
    private val verifyButton: Button by lazy { findViewById(R.id.verifyCodeButton) as Button }
    private val resendButton: Button by lazy { findViewById(R.id.resendCodeButton) as Button }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(activity_verify_code)

        verify.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
    }

}