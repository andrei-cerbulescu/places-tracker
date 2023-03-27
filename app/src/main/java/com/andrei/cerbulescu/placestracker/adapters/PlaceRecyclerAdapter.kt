package com.andrei.cerbulescu.placestracker.adapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.andrei.cerbulescu.placestracker.R
import com.andrei.cerbulescu.placestracker.data.Place
import com.google.android.material.imageview.ShapeableImageView

class PlaceRecyclerAdapter(private val places : Array<Place>) : RecyclerView.Adapter<PlaceRecyclerAdapter.PlaceViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaceRecyclerAdapter.PlaceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_place_element, parent, false)
        return PlaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaceRecyclerAdapter.PlaceViewHolder, position: Int) {
        val currentItem = places[position]
        holder.image.setImageBitmap(BitmapFactory.decodeByteArray(currentItem.image, 0, currentItem.image.size))
    }

    override fun getItemCount(): Int {
        return places.size
    }

    class PlaceViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val image : ImageView = itemView.findViewById(R.id.place_image)
    }
}
