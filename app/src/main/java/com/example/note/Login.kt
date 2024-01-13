package com.example.note

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlin.math.log

class Login : AppCompatActivity() {
    private lateinit var btnLogin: Button
    private lateinit var btnSignup: Button
    private lateinit var edtEmail:EditText
    private lateinit var edtPass: EditText
    private lateinit var checkMail:TextView
    private lateinit var checkPass:TextView

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
    }

    private fun init(){
        btnLogin = findViewById(R.id.btn_login)
        btnSignup = findViewById(R.id.btn_signup)
        edtEmail = findViewById(R.id.username)
        edtPass = findViewById(R.id.user_password)
        checkMail = findViewById(R.id.check_username)
        checkPass = findViewById(R.id.check_userpass)

        btnLogin.setOnClickListener(listenerLogin)
        btnSignup.setOnClickListener(listenerSignup)
    }

    fun checkInput(): Boolean{
        if (edtEmail.text.isNullOrBlank()){
            checkMail.visibility = View.VISIBLE
            return false
        }

        if (edtPass.text.isNullOrBlank()){
            checkPass.visibility = View.VISIBLE
            return false
        }
        return true
    }

    private fun getAccount(){
            val gmail = edtEmail.text.toString().trim()
            val pass = edtPass.text.toString().trim()

            db.collection("users").document(gmail).get().addOnSuccessListener{
                if (it.exists()){
                    if (pass == it.get("pass").toString()){
                        val sharePref = getSharedPreferences("PREFERENCE_LOGIN",Context.MODE_PRIVATE)
                        var edit = sharePref.edit()
                        edit.putString("GMAIL",gmail)
                        edit.putString("PASSWORD",pass)
                        edit.apply()
                        val intent = Intent(this,MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(this,"tai khoản hoặc mật khẩu không đúng",Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
    private val listenerLogin = View.OnClickListener {
        if(checkInput()){
            getAccount()
        }
    }

    private val listenerSignup = View.OnClickListener {

    }
}
