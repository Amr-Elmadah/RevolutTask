package com.revolut.currencies.base.presentation.view.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 *base class for recycler view adapter that can has header & footer to recycler view
 *
 * @property showHeader Boolean
 * @property showFooter Boolean
 * @property count Int
 * @author Ahmed mousa
 *
 */
abstract class BaseRecyclerAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var showHeader = false
    private var showFooter = false
    var data: MutableList<T> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                val v = headerItemView(parent)
                getHeaderItemHolder(v)
            }
            TYPE_FOOTER -> {
                val v = footerItemView(parent)
                getFooterItemHolder(v)
            }
            else -> {
                val v = mainItemView(parent)
                mainItemViewHolder(v)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var newPos = position

        if (showHeader) newPos = position - 1

        onBindMainViewHolder(holder, newPos)
    }

    override fun getItemCount(): Int {
        return if (showFooter && showHeader)
            getItems().size + 2   //showing footer & header
        else if (showFooter || showHeader)
            getItems().size + 1   //showing footer or header
        else
            getItems().size       // default
    }

    override fun getItemViewType(position: Int): Int {
        if (isShowHeader(position))
            return TYPE_HEADER
        else if (isShowFooter(position))
            return TYPE_FOOTER

        return position
    }

    open fun headerItemView(parent: ViewGroup): View {
        return View(parent.context)
    }

    open fun getHeaderItemHolder(view: View): RecyclerView.ViewHolder {
        return EmptyViewHolder(view)
    }

    open fun footerItemView(parent: ViewGroup): View {
        return View(parent.context)
    }

    open fun getFooterItemHolder(view: View): RecyclerView.ViewHolder {
        return EmptyViewHolder(view)
    }

    private fun isShowHeader(position: Int): Boolean {
        return position == 0 && showHeader
    }

    private fun isShowFooter(position: Int): Boolean {
        return position == itemCount - 1 && showFooter
    }

    fun showFooter(showFooter: Boolean) {
        this.showFooter = showFooter
        notifyDataSetChanged()
    }

    fun showHeader(showHeader: Boolean) {
        this.showHeader = showHeader
        notifyDataSetChanged()
    }

    fun addOnLoadMoreListener(
        onLoadMoreListener: OnLoadMoreListener,
        recyclerView: RecyclerView,
        linearLayoutManager: LinearLayoutManager
    ) {
        recyclerView.addOnScrollListener(object :
            EndlessRecyclerOnScrollListener(linearLayoutManager) {
            private var itemCountWithoutHeader: Int = 0
            override fun onLoadMore() {
                itemCountWithoutHeader = if (showHeader) {
                    itemCount - 1
                } else {
                    itemCount
                }
                if (itemCountWithoutHeader >= getAdapterPageSize()) {
                    showFooter(true)
                    onLoadMoreListener.onLoadMore()
                }
            }
        })
    }

    fun removeOnLoadMoreListener(
        recyclerView: RecyclerView
    ) {
        recyclerView.clearOnScrollListeners()
    }

    fun getItems(): MutableList<T> {
        return data
    }


    fun addMoreItems(items: MutableList<T>) {
        val lastIndex = itemCount
        getItems().addAll(items)
        notifyItemRangeInserted(lastIndex, items.size)
    }


    fun addMoreItemsFirst(items: MutableList<T>) {
        getItems().addAll(0, items)
        if (showHeader)
            notifyItemRangeInserted(1, items.size)
        else notifyItemRangeInserted(0, items.size)
    }

    fun replaceAllItems(items: MutableList<T>) {
        data = items
        if (showHeader)
            notifyItemRangeChanged(1, items.size)
        else notifyDataSetChanged()
    }

    fun moveItemToFirst(item: T) {
        removeItem(item)
        getItems().add(0, item)
        if (showHeader)
            notifyItemInserted(1)
        else notifyItemInserted(0)
    }

    fun addItemFirst(item: T) {
        getItems().add(0, item)
        if (showHeader)
            notifyItemInserted(1)
        else notifyItemInserted(0)
    }

    fun removeItem(item: T) {
        val index = getItems().indexOf(item)
        if (index != -1) {
            getItems().removeAt(index)
            if (showHeader)
                notifyItemRemoved(index + 1)
            else notifyItemRemoved(index)
        }
    }

    fun removeItems(items: MutableList<T>) {
        getItems().removeAll(items)
        notifyDataSetChanged()
    }

    fun getLastItem(): T {
        return if (showHeader && showFooter)
            getItems()[itemCount - 3]
        else if (showHeader || showFooter)
            getItems()[itemCount - 2]
        else getItems()[itemCount - 1]
    }

    fun getFirstItem(): T {
        return if (showHeader)
            getItems()[1]
        else getItems()[0]
    }

    fun hasItem(item: T): Boolean {
        var index = getItems().indexOf(item)
        if (index == -1)
            return false
        return true
    }

    fun updateItem(item: T) {
        val index = getItems().indexOf(item)
        if (index != -1) {
            getItems()[index] = item
            if (showHeader)
                notifyItemChanged(index + 1)
            else notifyItemChanged(index)
        }
    }

    fun onLoadComplete() {
        showFooter(false)
    }

    protected abstract fun mainItemView(parent: ViewGroup): View

    protected abstract fun mainItemViewHolder(view: View): RecyclerView.ViewHolder

    protected abstract fun onBindMainViewHolder(holder: RecyclerView.ViewHolder, position: Int)
    protected abstract fun getAdapterPageSize(): Int
    private inner class EmptyViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView)

    interface OnLoadMoreListener {
        fun onLoadMore()
    }

    companion object {
        private const val TYPE_HEADER = -1
        private const val TYPE_FOOTER = -2
    }
}
