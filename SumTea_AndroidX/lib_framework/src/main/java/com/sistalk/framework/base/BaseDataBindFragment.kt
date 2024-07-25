package com.sistalk.framework.base
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.sistalk.framework.ext.saveAs
import com.sistalk.framework.ext.saveAsUnChecked
import java.lang.reflect.ParameterizedType

/**
 * databinding Fragment 基类
 * */
abstract class BaseDataBindFragment<DB:ViewBinding>:BaseFragment() {

    var mBinding:DB? = null

    override fun getContentView(inflater: LayoutInflater, container: ViewGroup?): View {
        val type = javaClass.genericSuperclass
        val vbClass:Class<DB> = type!!.saveAs<ParameterizedType>().actualTypeArguments[0].saveAs()
        val method = vbClass.getDeclaredMethod("inflate",LayoutInflater::class.java)
        mBinding = method.invoke(this,layoutInflater)!!.saveAsUnChecked()
        return mBinding!!.root
    }

    override fun getLayoutResId(): Int {
        return 0
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }


}