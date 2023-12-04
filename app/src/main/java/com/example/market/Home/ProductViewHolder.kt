package com.example.market.Home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.market.R


class ProductViewHolder(view: View, private val fromProfile: Boolean) : RecyclerView.ViewHolder(view) {
    val imageView: ImageView = view.findViewById(R.id.product_image)
    val titleView: TextView = view.findViewById(R.id.product_title)
    val priceView: TextView = view.findViewById(R.id.product_price)
    val statusView : TextView = view.findViewById(R.id.product_status)
    
    fun bind(product: Product, context: Context, navController: NavController) {
        Glide.with(context).load(product.imageUrl).error(R.drawable.ic_default_image).into(imageView)
        titleView.text = product.title
        itemView.setOnClickListener {
            val bundle = Bundle().apply {
                Log.d("productview", product.id)
                putString("productId", product.id)
            }
            if (fromProfile) {
              
                Log.d("ProductViewHolder", "수정하기")
                navController.navigate(R.id.editFragment, bundle)
            }
            else {
               
                Log.d("ProductViewHolder", "상세보기")
                navController.navigate(R.id.sellDetailFragment, bundle)
            }
        }
    }

}
