package com.example.dsm_frontend.ui.storeModule.searchedStores.adapter

import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.dsm_frontend.R
import com.example.dsm_frontend.api.Maps
import com.example.dsm_frontend.databinding.ItemStoreBinding
import com.example.dsm_frontend.data.model.Store
import java.util.*
import kotlin.collections.ArrayList

class StoreAdapter(private var stores: List<Store>) :
    RecyclerView.Adapter<StoreAdapter.ViewHolder>(), Filterable {

    private var currentLocation: Location? = null
    private var storesFilter: List<Store> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_store, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val store = stores[position]
        holder.setData(store)
    }

    override fun getItemCount(): Int = stores.size

    fun setStores(data: List<Store>, currentLocation: Location) {
        this.stores = data
        this.currentLocation = currentLocation
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val charSearch = p0.toString()
                if (charSearch.isEmpty()) {
                    storesFilter = stores
                } else {
                    val resultList = ArrayList<Store>()
                    for (row in stores) {
                        if (row.name?.lowercase(Locale.ROOT)!!
                                ?.contains(charSearch.lowercase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    storesFilter = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = storesFilter
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {

            }
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemStoreBinding.bind(view)

        fun setData(store: Store) {
            binding.apply {
                tvNameStore.text = store.name
                currentLocation?.let {
                    tvDistance.text = Maps.calculateDistanceKm(
                        it.latitude,
                        it.longitude,
                        store.location?.latitude!!,
                        store.location?.longitude!!
                    ).toString()
                }
                tvRating.text = store.rating.toString()
                Glide.with(binding.root)
                    .load(store.photoUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(imgPhotoStore)
            }
        }
    }
}