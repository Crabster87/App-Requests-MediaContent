package crabster.rudakov.requestsmediacontent.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import crabster.rudakov.requestsmediacontent.R
import crabster.rudakov.requestsmediacontent.view.fragments.AttachedFilesFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()
//        supportFragmentManager.beginTransaction().add(R.id.container, AttachedFilesFragment()).commit()
    }

}