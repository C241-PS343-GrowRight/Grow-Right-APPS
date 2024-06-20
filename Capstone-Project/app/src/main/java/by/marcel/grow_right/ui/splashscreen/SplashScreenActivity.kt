package by.marcel.grow_right.ui.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import by.marcel.grow_right.R
import by.marcel.grow_right.ui.analyze.AnalyzeActivity
import by.marcel.grow_right.ui.login.LoginActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spalsh_screen)

        supportActionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed({
            goToMain()
        }, 3000)
    }
    private fun goToMain(){
        Intent(this, LoginActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }
}