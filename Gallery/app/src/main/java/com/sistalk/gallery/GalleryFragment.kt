package com.sistalk.gallery

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GalleryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GalleryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var viewModel: GalleryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        val menuHost:MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.swipeIndicator -> {
                        view?.findViewById<SwipeRefreshLayout>(R.id.swipeLayoutGallery)?.isRefreshing = true
                        Handler().postDelayed({
                            viewModel.fetchData()
                        }, 1000)
                    }
                }
                return false
            }
        })

    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
//        inflater.inflate(R.menu.menu, menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.swipeIndicator -> {
//                view?.findViewById<SwipeRefreshLayout>(R.id.swipeLayoutGallery)?.isRefreshing = true
//                Handler().postDelayed(Runnable {
//                    viewModel.fetchData()
//                }, 1000)
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setHasOptionsMenu(true)
        val galleryAdapter = GalleryAdapter()
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycleView)
        recyclerView.apply {
            adapter = galleryAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        )[GalleryViewModel::class.java]
        viewModel.photoListLive.observe(viewLifecycleOwner) {
            galleryAdapter.submitList(it)
            view.findViewById<SwipeRefreshLayout>(R.id.swipeLayoutGallery).isRefreshing = false
        }
        viewModel.photoListLive.value ?: viewModel.fetchData()
        view.findViewById<SwipeRefreshLayout>(R.id.swipeLayoutGallery).setOnRefreshListener {
            viewModel.fetchData()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GalleryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GalleryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}