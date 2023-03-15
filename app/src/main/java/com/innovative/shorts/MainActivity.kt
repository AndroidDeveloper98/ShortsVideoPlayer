package com.innovative.shorts

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.Player
import com.innovative.shorts.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), Player.EventListener {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        replaceFragment(HomeFragment())
    }

    private fun replaceFragment(
        fragment: Fragment
    ) {

        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.frameContainer, fragment)
            .commit()
    }

}