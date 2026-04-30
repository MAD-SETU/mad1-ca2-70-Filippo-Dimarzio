package org.wit.recyclingTracker.views.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.wit.recyclingTracker.R
import org.wit.recyclingTracker.databinding.ActivityLoginBinding
import org.wit.recyclingTracker.views.recylingtrackerlist.RecyclingTrackerListView

class LoginView : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = getString(R.string.login)
        setSupportActionBar(binding.toolbar)

        binding.btnLogin.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, R.string.error_empty_credentials, Toast.LENGTH_SHORT).show()
            } else if (email == "filippodimarzio@wit.ie" && password == "password") {
                startActivity(Intent(this, RecyclingTrackerListView::class.java))
                finish()
            } else {
                Toast.makeText(this, R.string.error_invalid_credentials, Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnRegister.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, R.string.register_empty_credentials, Toast.LENGTH_SHORT).show()
            } else {
                val message = getString(R.string.register_success, email)
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }
    }
}
