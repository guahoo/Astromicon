package com.moon.astromicon.ui

import com.moon.astromicon.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_places.view.*

class ListItemPlace(val placeName: String): Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
    viewHolder.itemView.apply {
        tv_places.text = placeName
    }
    }

    override fun getLayout() = R.layout.item_places
}