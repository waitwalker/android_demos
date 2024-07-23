package com.sistalk.main.navigator

import android.content.Context
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import com.sistalk.framework.log.LogUtil
import java.util.ArrayDeque

/**
 * 重写FragmentNavigator的navigate()方法，通过反射获取mBackStack后退栈
 * */
@Navigator.Name("sumFragment")
class SumFragmentNavigator(context: Context, manager: FragmentManager, containerID: Int) :
    FragmentNavigator(context, manager, containerID) {
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
        val ft = mManager.beginTransaction()

        var enterAnim = navOptions?.enterAnim ?: -1
        var exitAnim = navOptions?.exitAnim ?: -1
        var popEnterAnim = navOptions?.popEnterAnim ?: -1
        var popExitAnim = navOptions?.popExitAnim ?: -1
        if (enterAnim != -1 || exitAnim != -1 || popEnterAnim != -1 || popExitAnim != -1) {
            enterAnim = if (enterAnim != -1) enterAnim else 0
            exitAnim = if (exitAnim != -1) exitAnim else 0
            popEnterAnim = if (popEnterAnim != -1) popEnterAnim else 0
            popExitAnim = if (popExitAnim != -1) popExitAnim else 0
            ft.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
        }

        // 1.先查询当前显示的fragment，不为空则将其hide
        val fragment = mManager.primaryNavigationFragment // 当前显示的fragment
        fragment?.let { ft.hide(it) }

        var frag: Fragment?
        val tag = destination.id.toString()
        frag = mManager.findFragmentByTag(tag)

        // 2.根据tag查询当前添加的fragment是否不为null，不为null则将其直接show
        if (frag != null) {
            ft.show(frag)
        } else {
            // 3. 为null则通过通过工厂方法创建fragment实例
            val factory = mManager.fragmentFactory
            frag = factory.instantiate(mContext.classLoader, className)
            frag.arguments = args
            ft.add(mContainerID, frag, tag)
//            frag = instantiateFragment(mContext, mManager, className, args)
        }
        ft.setPrimaryNavigationFragment(frag)
        @IdRes val destID = destination.id
        // 通过反射方式获取mBackStack
        val mBackStack: ArrayDeque<Int>
        val field = FragmentNavigator::class.java.getDeclaredField("mBackStack")
        field.isAccessible = true
        mBackStack = field.get(this) as ArrayDeque<Int>

        val initialNavigation = mBackStack.isEmpty()
        val isSingleTopReplacement =
            (navOptions != null
                    && !initialNavigation
                    && navOptions.shouldLaunchSingleTop()
                    && mBackStack.peekLast() == destID)
        val isAdded: Boolean
        if (initialNavigation) {
            isAdded = true
        } else if (isSingleTopReplacement) {
            if (mBackStack.size > 1) {
                mManager.popBackStack(
                    generateBackStackName(mBackStack.size, mBackStack.peekLast() ?: 0),
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )
                ft.addToBackStack(generateBackStackName(mBackStack.size, destID))
            }
            isAdded = false
        } else {
            ft.addToBackStack(generateBackStackName(mBackStack.size + 1, destID))
            isAdded = true
        }
        if (navigatorExtras is Extras) {
            val extras = navigatorExtras as Extras?
            for ((key, value) in extras!!.sharedElements) {
                ft.addSharedElement(key, value)
            }
        }

        // 4.将创建的实例添加在事务中
        ft.setReorderingAllowed(true)
        ft.commit()
        if (isAdded) {
            mBackStack.add(destID)
            return destination
        } else {
            return null
        }
    }

    private fun generateBackStackName(backIndex: Int, destID: Int): String {
        return "$backIndex - $destID"
    }

}