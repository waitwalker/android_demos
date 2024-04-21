package com.sistalk.bottomnavigationdemo

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

class SecondFragment : Fragment() {

    companion object {
        fun newInstance() = SecondFragment()
    }

    private val viewModel: SecondViewModel by viewModels()
    lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_second,container,false)
        imageView = view.findViewById(R.id.imageView)
        return view
    }

    @SuppressLint("Recycle")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val objectAnimatorX = ObjectAnimator.ofFloat(imageView,"scaleX",0.0F,0F)
        val objectAnimatorY = ObjectAnimator.ofFloat(imageView,"scaleY",0.0F,0F)
        objectAnimatorX.duration = 500
        objectAnimatorY.duration = 500
        imageView.setOnClickListener(View.OnClickListener {
            if (!objectAnimatorX.isRunning) {
                objectAnimatorX.setFloatValues(imageView.scaleX+0.1f)
                objectAnimatorY.setFloatValues(imageView.scaleY+0.1f)
                objectAnimatorX.start()
                objectAnimatorY.start()
            }


        })

    }
}