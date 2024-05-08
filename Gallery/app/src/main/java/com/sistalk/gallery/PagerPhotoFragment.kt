package com.sistalk.gallery

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.sistalk.gallery.databinding.FragmentPagerPhotoBinding
import kotlin.collections.ArrayList
import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import uk.co.senab.photoview.PhotoView
import java.io.File
import java.io.FileOutputStream

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PagerPhotoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PagerPhotoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val tag = "PagerPhotoFragment"

    private lateinit var binding: FragmentPagerPhotoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pager_photo, container, false)
        return binding.root
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_pager_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val photoList: ArrayList<String>? = arguments?.getStringArrayList("photos")
        PagerPhotoListAdapter().apply {
            binding.viewPager2.adapter = this
            val list: ArrayList<PhotoItem> = ArrayList()
            var index = 0;
            photoList?.map {
                val photoItem = PhotoItem(it, index, it, 0, "zhang", 100, 100)
                list.add(photoItem)
                index++
            }
            submitList(list)
        }

        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }

            @SuppressLint("SetTextI18n")
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.photoTag.text = "${arguments?.getInt("currentIndex")}/${photoList?.size}"

            }
        })

        binding.viewPager2.setCurrentItem(arguments?.getInt("currentIndex") ?: 0, false)

        binding.imageViewSave.setOnClickListener {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                Log.d("photo view","系统API小于29")
                val hasPermission = hasPermission(requireContext(), PERMISSIONS)
                if (hasPermission) {
                    Log.d("PagerPhotoFragment","已经动态获取了权限")
                    downloadImage()
                } else {
                    permissionRequestLauncher.launch(PERMISSIONS)
                }
            } else {
                Log.d("photo view","系统API大于29")
                downloadImage()
            }
        }
    }

    private fun downloadImage() {
       val viewHolder = (binding.viewPager2[0] as RecyclerView).findViewHolderForAdapterPosition(binding.viewPager2.currentItem) as PagerPhotoViewHolder
        val bitmap = viewHolder.itemView.findViewById<PhotoView>(R.id.pagerPhoto).drawable.toBitmap()
        saveImage(requireContext(),bitmap)
    }

    @SuppressLint("Recycle")
    fun saveImage(context: Context, image:Bitmap) {
       try {
           val values = ContentValues()
           values.put(MediaStore.Images.Media.DISPLAY_NAME,"${System.currentTimeMillis()}.png")
           values.put(MediaStore.Images.Media.MIME_TYPE,"image/png")
           values.put(MediaStore.Images.Media.RELATIVE_PATH,Environment.DIRECTORY_PICTURES)
           val saveRui = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,ContentValues())?:kotlin.run {
               Toast.makeText(context,"保存失败uri",Toast.LENGTH_LONG).show()
               return
           }

           context.contentResolver.openOutputStream(saveRui).use {
               /// 图片比较大的时候要开一个子线程
               val success = it?.let { it1 -> image.compress(Bitmap.CompressFormat.PNG,100, it1) }
               if (success != null && success) {
                   Toast.makeText(context,"保存成功",Toast.LENGTH_LONG).show()
               } else {
                   Toast.makeText(context,"保存失败compress",Toast.LENGTH_LONG).show()
               }
           }

       } catch (e:Exception) {
           Log.e(tag,"保存图片异常$e")
       }
    }

    private val permissionRequestLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        val granted = permissions.entries.all {
            it.value
        }
        if (granted) {
            Log.d(tag,"权限全部允许")
            downloadImage()
        } else {
            Toast.makeText(requireContext(),"请打开权限",Toast.LENGTH_LONG).show()
        }
    }

    companion object {

        var PERMISSIONS = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PagerPhotoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PagerPhotoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun hasPermission(context:Context, permissions:Array<String>):Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context,it) == PackageManager.PERMISSION_GRANTED
    }


}