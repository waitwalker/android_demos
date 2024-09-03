package com.sistalk.banner.manager

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.sistalk.framework.utils.ViewUtils

// 通过反射修改滑动的时间
object ReflectLayoutManager {

    fun reflectLayoutManager(viewPager2: ViewPager2, scrollDuration: Int) {
        try {
            val recyclerView = viewPager2.getChildAt(0) as RecyclerView
            recyclerView.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager? ?: return
            val scrollDurationManager =
                ScrollDurationManager(viewPager2, scrollDuration, linearLayoutManager)
            recyclerView.layoutManager = scrollDurationManager
            val mRecyclerField =
                RecyclerView.LayoutManager::class.java.getDeclaredField("mRecyclerView")
            mRecyclerField.isAccessible = true
            mRecyclerField[linearLayoutManager] = recyclerView
            val layoutManagerField = ViewPager2::class.java.getDeclaredField("mLayoutManager")
            layoutManagerField.isAccessible = true
            layoutManagerField[viewPager2] = scrollDurationManager
            val pageTransformerAdapterField =
                ViewPager2::class.java.getDeclaredField("mPageTransformerAdapter")
            pageTransformerAdapterField.isAccessible = true
            val mPageTransformerAdapter = pageTransformerAdapterField[viewPager2]
            if (mPageTransformerAdapter != null) {
                val aClass: Class<*> = mPageTransformerAdapter.javaClass
                val layoutManager = aClass.getDeclaredField("mLayoutManager")
                layoutManager.isAccessible = true
                layoutManager[mPageTransformerAdapter] = scrollDurationManager
            }
            val scrollEventAdapterField =
                ViewPager2::class.java.getDeclaredField("mScrollEventAdapter")
            scrollEventAdapterField.isAccessible = true
            val mScrollEventAdapter = scrollEventAdapterField[viewPager2]
            if (mScrollEventAdapter != null) {
                val aClass: Class<*> = mScrollEventAdapter.javaClass
                val layoutManager = aClass.getDeclaredField("mLayoutManager")
                layoutManager.isAccessible = true
                layoutManager[mScrollEventAdapter] = scrollDurationManager
            }
        } catch (e: NoSuchFileException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }
}