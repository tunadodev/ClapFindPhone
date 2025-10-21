package com.ibl.tool.clapfindphone.base

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAddFragmentsMainAdapter(private val context: Context, fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    private var pageSize = 4
    val mFragmentList: MutableList<Fragment> = ArrayList()

    init {

    }

    override fun createFragment(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getItemCount(): Int {
        return pageSize
    }

    fun addFrag(fragment: Fragment) {
        mFragmentList.add(fragment)
    }

    fun removeFag(pos: Int) {
        if (pos in mFragmentList.indices)
            mFragmentList.removeAt(pos)
    }

    fun clear() {
        mFragmentList.clear()
    }

    override fun getItemId(position: Int): Long {
        return mFragmentList[position].hashCode().toLong() // make sure notifyDataSetChanged() works
    }

    override fun containsItem(itemId: Long): Boolean {
        val pageIds = mFragmentList.map { it.hashCode().toLong() }
        return pageIds.contains(itemId)
    }
}