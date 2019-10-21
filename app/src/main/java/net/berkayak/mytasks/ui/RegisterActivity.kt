package net.berkayak.mytasks.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_register.*
import net.berkayak.mytasks.R
import net.berkayak.mytasks.utilities.Consts

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerBtn.setOnClickListener {
            val username = userNameET.text.toString()
            val password = passwordET.text.toString()
            val passwordApprove = passwordApproveET.text.toString()
            var i = Intent()
            i.putExtra(Consts.USERNAME, username)
            i.putExtra(Consts.PASSWORD, password)
            i.putExtra(Consts.PASSWORD_APPROVE, passwordApprove)
            setResult(Consts.RESULT_CODE_DONE, i)
            finish()
        }
    }
}
