package com.sistalk.bottomnavigationdemo

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider

class FirstFragment : Fragment() {

    companion object {
        fun newInstance() = FirstFragment()
    }

    private val viewModel: FirstViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val firstViewModel = activity?.let { ViewModelProvider(it) }?.get(FirstViewModel::class.java)
        return inflater.inflate(R.layout.fragment_first, container, false)
    }
}