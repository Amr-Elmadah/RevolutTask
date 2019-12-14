package com.revolut.currencies.base.presentation.view.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * class that handle load more items in recycler view
 *
 * @property linearLayoutManager LinearLayoutManager
 * @property visibleThreshold Int
 * @property mPreviousTotal Int
 * @property mLoading Boolean
 * @constructor
 *
 */
abstract class EndlessRecyclerOnScrollListener(
    private val linearLayoutManager: LinearLayoutManager,
    private val visibleThreshold: Int = 0
) : RecyclerView.OnScrollListener() {
    /**
     * The total number of items in the dataset after the last load
     */
    private var mPreviousTotal = 0
    /**
     * True if we are still waiting for the last set of data to load.
     */
    private var mLoading = true

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount = recyclerView.childCount
        val totalItemCount = linearLayoutManager.itemCount
        val firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition()
        if (mLoading) {
            if (totalItemCount > mPreviousTotal) {
                mLoading = false
                mPreviousTotal = totalItemCount
            }
        }
        if (!mLoading && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {
            // End has been reached
            onLoadMore()
            mLoading = true
        }
    }

    abstract fun onLoadMore()
}
