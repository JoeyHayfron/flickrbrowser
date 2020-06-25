package com.engineerskasa.fickrbrowser

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView

class RecyclerItemClickListener(
    context: Context,
    recyclerView: RecyclerView,
    private val listener: OnRecyclerItemClicked
) : RecyclerView.SimpleOnItemTouchListener() {

    private val TAG = "RVItemClickListener"

    interface OnRecyclerItemClicked {
        fun onItemClicked(view: View, position: Int)
        fun onItemLongClicked(view: View, position: Int)
    }

    private val gestureDetector =
        GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {

            override fun onSingleTapUp(e: MotionEvent): Boolean {
                Log.d(TAG, ".onSingleTapUp started")
                val childView = recyclerView.findChildViewUnder(e.x, e.y)
                Log.d(TAG, ".onSingleTapUp calling ItemClickedListener")
                if (childView != null) {
                    listener.onItemClicked(childView, recyclerView.getChildAdapterPosition(childView))
                }
                return true
            }

            override fun onLongPress(e: MotionEvent) {
                Log.d(TAG, ".onLongPress started")
                val childView = recyclerView.findChildViewUnder(e.x, e.y)
                Log.d(TAG, ".onLongPress calling ItemLongClickedListener")
                if (childView != null) {
                    listener.onItemLongClicked(childView, recyclerView.getChildAdapterPosition(childView))
                }
            }
        })

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
//        return super.onInterceptTouchEvent(rv, e)
        val result = gestureDetector.onTouchEvent(e)
        Log.d(TAG, ".onInterceptTouchEvent returning result")

        return result
    }
}