package com.sistalk.bottomnavigationdemo

import android.animation.ObjectAnimator
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import kotlin.random.Random

class ThirdFragment : Fragment() {

    companion object {
        fun newInstance() = ThirdFragment()
    }

    private val viewModel: ThirdViewModel by viewModels()
    lateinit var imageView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_third, container, false)
        imageView = view.findViewById(R.id.imageView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val objectAnimator = ObjectAnimator.ofFloat(imageView,"x",0F)
        objectAnimator.duration = 500
        imageView.setOnClickListener(View.OnClickListener {
            if (!objectAnimator.isRunning) {
                var value = 0.0f
                val bool = Random.nextBoolean()
                value = if (bool) {
                    100.0f
                } else {
                    -100.0f
                }

                objectAnimator.setFloatValues(imageView.x,imageView.x+value)
                objectAnimator.start()
            }
        })
    }
}