package somiah.jad.signup_login_app

import android.content.Intent
import android.icu.util.TimeUnit
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import javax.xml.datatype.DatatypeConstants.SECONDS

class SignUp : AppCompatActivity() {
    lateinit var newEmail: EditText
    lateinit var newPassword: EditText
    lateinit var newPhoneNum: EditText
    lateinit var signUp: Button
    lateinit var goToLogin: TextView
    lateinit var auth: FirebaseAuth
    lateinit var storedVerificationId:String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()
        newEmail = findViewById(R.id.new_email)
        newPassword = findViewById(R.id.new_password)
        newPhoneNum = findViewById(R.id.new_phone_num)
        signUp = findViewById(R.id.sign_up)
        goToLogin = findViewById(R.id.go_to_login)

        signUp.setOnClickListener {
            registration()
        }
        goToLogin.setOnClickListener {
            var intent = Intent(this,LogIn::class.java)
            startActivity(intent)
        }
        var currentUser = auth.currentUser
        if(currentUser != null) {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Toast.makeText(applicationContext, "Failed", Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                Log.d("TAG","onCodeSent:$p0")
                storedVerificationId = p0
                resendToken = p1
                var intent = Intent(applicationContext,Verify::class.java)
                intent.putExtra("storedVerificationId",storedVerificationId)
                startActivity(intent)
            }
        }


    }

    fun registration(){
        auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(
            newEmail.text.toString(),
            newPassword.text.toString())
            .addOnCompleteListener (this){
                if(it.isSuccessful){
                    Toast.makeText(this,"user Created Successful ", Toast.LENGTH_LONG).show()
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this,"Can not Create ", Toast.LENGTH_LONG).show()
                    Log.d("failed", it.exception.toString())
                }
            }
    }

    private fun checkCorrectPhoneNumber() {

        var number=newPhoneNum.text.toString().trim()

        if(!number.isEmpty()){
            number = "+967 " + number
            sendVerificationcode(number)
        }else{
            Toast.makeText(this,"Enter mobile number",Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendVerificationcode(number: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, java.util.concurrent.TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}