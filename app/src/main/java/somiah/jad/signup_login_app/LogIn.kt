package somiah.jad.signup_login_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LogIn : AppCompatActivity() {

    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var login: Button
    lateinit var goToSignUp: TextView

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        auth = FirebaseAuth.getInstance()

        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        login = findViewById(R.id.log_in)
        goToSignUp  = findViewById(R.id.go_to_sign_up)

        login.setOnClickListener {
            Login()
        }
        goToSignUp.setOnClickListener {
            var intent = Intent(this,SignUp::class.java)
            startActivity(intent)
        }

    }

    fun Login(){
        auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(
            email.text.toString(),
            password.text.toString())
            .addOnCompleteListener (this){
                if(it.isSuccessful){
                    Toast.makeText(this,"user Login Successful ", Toast.LENGTH_LONG).show()
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this,"Can not Login ", Toast.LENGTH_LONG).show()
                    Log.d("failed", it.exception.toString())
                }
            }
    }


//    public override fun onStart() {
//        super.onStart()
//        val currentUser = auth.currentUser
//        updateUI(currentUser)
//    }
//
//    fun updateUI(currentUser: FirebaseUser?){
//
//    }
}