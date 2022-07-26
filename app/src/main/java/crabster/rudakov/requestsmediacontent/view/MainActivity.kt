package crabster.rudakov.requestsmediacontent.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import crabster.rudakov.requestsmediacontent.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()
        supportFragmentManager.beginTransaction().add(com.google.android.material.R.id.container, TestMediaFragment()).commit()
    }

}