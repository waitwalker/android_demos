package com.sistalk.main.navigator

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import com.sistalk.framework.log.LogUtil

/**
 * 重写FragmentNavigator的navigate()方法，通过反射获取mBackStack后退栈
 * */
@Navigator.Name("sumFragment")
class SumFragmentNavigator(context: Context, manager:FragmentManager, containerID:Int):FragmentNavigator(context, manager, containerID) {
    private val mContext = context
    private val mManager = manager
    private val mContainerID = containerID
    private val TAG = "SumFragmentNavigator"

    override fun navigate(
        destination: Destination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ): NavDestination? {
        if (mManager.isStateSaved) {
            LogUtil.i("Ignoring navigate() call: FragmentManager has already", tag = TAG)
            return null
        }

        var className = destination.className
        if (className[0] == '.') {
            className = mContext.packageName + className
        }

        return super.navigate(destination, args, navOptions, navigatorExtras)
    }

}