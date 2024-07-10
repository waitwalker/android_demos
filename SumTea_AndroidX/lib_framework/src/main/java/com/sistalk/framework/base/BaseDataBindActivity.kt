package com.sistalk.framework.base
import android.os.Bundle
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.sistalk.framework.ext.saveAs
import com.sistalk.framework.ext.saveAsUnChecked
import java.lang.reflect.ParameterizedType

abstract class BaseDataBindActivity<DB : ViewBinding> : BaseActivity() {

    lateinit var mBinding: DB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val type = javaClass.genericSuperclass
        val vbClass:Class<DB> = type!!.saveAs<ParameterizedType>().actualTypeArguments[0].saveAs()
        val method = vbClass.getDeclaredMethod("inflate",LayoutInflater::class.java)
        mBinding = method.invoke(this, layoutInflater)!!.saveAsUnChecked()
        setContentView(mBinding.root)
    }
}