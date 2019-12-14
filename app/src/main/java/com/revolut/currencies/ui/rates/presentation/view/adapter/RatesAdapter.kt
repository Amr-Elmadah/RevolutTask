package com.revolut.currencies.ui.rates.presentation.view.adapter

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.revolut.currencies.R
import com.revolut.currencies.base.presentation.view.adapter.BaseRecyclerAdapter
import com.revolut.currencies.base.presentation.view.extension.getInflatedView
import com.revolut.currencies.base.presentation.view.extension.loadFromUrl
import com.revolut.currencies.network.response.Rate
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_rate.view.*

class RatesAdapter : BaseRecyclerAdapter<Rate>() {

    private val mViewClickSubject = PublishSubject.create<Rate>()

    fun getViewClickedObservable(): Observable<Rate> {
        return mViewClickSubject
    }

    override fun getAdapterPageSize(): Int {
        return PAGE_SIZE
    }

    override fun mainItemView(parent: ViewGroup): View {
        return parent.getInflatedView(R.layout.item_rate)
    }

    override fun mainItemViewHolder(view: View): RecyclerView.ViewHolder {
        return RateViewHolder(view)
    }

    override fun onBindMainViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RateViewHolder) {
            holder.bind(getItems()[position])
            holder.itemView.setOnClickListener {
                mViewClickSubject.onNext(getItems()[position])
            }
        }
    }

    private class RateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Rate) = with(itemView) {
            tvCurrency.text = item.currency
            tvCurrencyName.text = item.currencyName

            if (item.value != 0.0) {
                tvRate.text = item.value.toString()
            } else {
                tvRate.text = ""
            }
            imgRate.loadFromUrl(
                url = item.image,
                isRounded = true,
                placeholder = R.color.colorPrimary
            )
        }
    }
}