package com.example.market.Home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.market.R



class ProductAdapter(

    private val context: Context,
    private var productList: List<Products>,
    private val navController: NavController,
    private val fromProfile: Boolean
)
    : RecyclerView.Adapter<ProductViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product, parent, false)
        return ProductViewHolder(view, fromProfile)
    }


    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

        val product = productList[position]

        Glide.with(context).load(product.urlImg).error(R.drawable.ic_default_image).into(holder.imageView)

        holder.titleView.text = product.titleName

        holder.priceView.text = product.price.toString()
        holder.statusView.text = if (product.sell == true) "🟢 판매중" else "🔴 판매완료"

        holder.bind(product, context, navController)
    }

    override fun getItemCount() = productList.size

    fun setData(newList: List<Products>) {
        productList = newList
        notifyDataSetChanged()
        Log.d("ProductAdapter", "productList: $productList")
    }
}
