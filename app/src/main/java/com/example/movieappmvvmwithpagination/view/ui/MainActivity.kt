package com.example.movieappmvvmwithpagination.view.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.movieappmvvmwithpagination.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        single_movie_btn.setOnClickListener {
            val intent = Intent(this, SingleMovieActivity::class.java)
            intent.putExtra("id", 419704)
            startActivity(intent)
        }
    }
}
