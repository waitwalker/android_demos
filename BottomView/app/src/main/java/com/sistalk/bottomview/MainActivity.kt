package com.sistalk.bottomview

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.sistalk.bottomview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        //setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onStart() {
        super.onStart()
        navController = findNavController(R.id.fragmentContainerView)
        findViewById<MotionLayout>(R.id.messageMotionLayout).setOnClickListener{
            navController.navigate(R.id.messageFragment)
        }
        findViewById<MotionLayout>(R.id.contactMotionLayout).setOnClickListener{
            navController.navigate(R.id.contactFragment)
        }
        findViewById<MotionLayout>(R.id.exploreMotionLayout).setOnClickListener{
            navController.navigate(R.id.exploreFragment)
        }
        findViewById<MotionLayout>(R.id.accountMotionLayout).setOnClickListener{
            navController.navigate(R.id.exploreFragment)
        }
    }
}