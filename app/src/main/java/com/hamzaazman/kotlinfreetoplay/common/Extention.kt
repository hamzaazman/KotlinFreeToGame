package com.hamzaazman.kotlinfreetoplay

import android.graphics.Rect
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

fun RecyclerView.addVerticalMarginDecoration(margin: Int) {
    addItemDecoration(object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            if (parent.getChildAdapterPosition(view) != parent.adapter!!.itemCount - 1) {
                outRect.top = margin
                outRect.bottom = margin
            }
        }
    })
}

fun RecyclerView.addHorizontalMarginDecoration(margin: Int) {
    addItemDecoration(object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            if (parent.getChildAdapterPosition(view) != parent.adapter!!.itemCount - 1) {
                outRect.right = margin
                outRect.left = margin
            }
        }
    })
}

fun RecyclerView.customItemDecoration(padding: Int) {
    addItemDecoration(object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            if (parent.getChildAdapterPosition(view) != parent.adapter!!.itemCount - 1) {
                outRect.bottom = padding
            }
        }
    })
}


class VerticalItemDecoration(private val margin: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.apply {
            bottom = margin
            top = margin
        }
    }
}

class HorizontalItemDecoration(private val margin: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.apply {
            left = margin
            right = margin
        }
    }
}

fun String.capitalizeFirstLetter(): String {
    return replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
}

fun String.extractYearFromDateString(): String? {
    val date = try {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(this)
    } catch (e: ParseException) {
        e.printStackTrace()
        return null
    }
    return SimpleDateFormat("yyyy", Locale.getDefault()).format(date)
}

fun TextView.makeCollapsible(maxLinesCollapsed: Int, maxLinesExpanded: Int) {
    maxLines = maxLinesCollapsed

    setOnClickListener {
        maxLines = if (maxLines == maxLinesCollapsed) {
            maxLinesExpanded
        } else {
            maxLinesCollapsed
        }
        TransitionManager.beginDelayedTransition(parent as ViewGroup)
    }
}
