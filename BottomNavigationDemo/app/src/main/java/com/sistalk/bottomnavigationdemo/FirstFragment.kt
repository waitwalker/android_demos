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
import androidx.lifecycle.ViewModelProvider

class FirstFragment : Fragment() {

    companion object {
        fun newInstance() = FirstFragment()
    }

    private lateinit var viewModel: FirstViewModel
    private lateinit var imageView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = activity?.let { ViewModelProvider(it) }?.get(FirstViewModel::class.java)!!
        val view:View = inflater.inflate(R.layout.fragment_first, container, false)
        imageView = view.findViewById(R.id.imageView)
        imageView.rotation = viewModel.rotationPosition
        return view
    }

    @SuppressLint("Recycle")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val objectAnimator = ObjectAnimator.ofFloat(imageView,"rotation",0F,0F)
        objectAnimator.duration = 500
        imageView.setOnClickListener(View.OnClickListener {
            if (!objectAnimator.isRunning) {
                objectAnimator.setFloatValues(imageView.rotation,imageView.rotation+100F)
                viewModel.rotationPosition += 100F
                objectAnimator.start()
            }

        })
    }
}