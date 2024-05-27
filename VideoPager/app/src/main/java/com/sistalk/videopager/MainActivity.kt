package com.sistalk.videopager

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.sistalk.videopager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var mainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.mainViewPager.apply {
            // 通过匿名内部类创建adapter
            adapter = object :FragmentStateAdapter(this@MainActivity) {
                override fun getItemCount(): Int {
                    return 3
                }

                override fun createFragment(position: Int): Fragment {
                    return when (position) {
                        1->VideoFragment()
                        else -> FooFragment()
                    }
                }
            }
            // 设置缺省值
            setCurrentItem(1,false)
        }

        TabLayoutMediator(mainBinding.tabLayout,mainBinding.mainViewPager) { tab: TabLayout.Tab, i: Int ->
            tab.text = when(i) {
                1->"video" else ->"foo"
            }
        }.attach()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.FrameLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}