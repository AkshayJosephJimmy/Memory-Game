package com.example.memorygame

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.memorygame.models.Boradsize
import com.example.memorygame.models.MemoryCard
import kotlin.math.min

class MemoryGameAdapter(
    private val context: Context,
    private val boardsize: Boradsize,
    private val MemoryCard: List<MemoryCard>,
    private val cardClickListener:CardClickListener

) : RecyclerView.Adapter<MemoryGameAdapter.ViewHolder>() {
   companion object{
       const val margin =10
   }
    interface CardClickListener{
        fun onCardClicked(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cardheight = parent.height/boardsize.getheight()-(2* margin)
        val cardwidth = parent.width/boardsize.getwidth()-(2* margin)
        val cardlenght = min(cardheight,cardwidth)
        val view =LayoutInflater.from(context).inflate(R.layout.memory_crad,parent,false)
        val layoutparameter= view.findViewById<CardView>(R.id.memory_cardview).layoutParams as MarginLayoutParams
        layoutparameter.width=cardlenght
        layoutparameter.height=cardlenght
        layoutparameter.setMargins(margin, margin, margin, margin )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount()=boardsize.numCards


  inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

      val imagebutton = itemView.findViewById<ImageButton>(R.id.image_button)


      fun bind(position: Int) {
          val card= MemoryCard[position]
          imagebutton.setImageResource(if (card.isfaceup){card.identifer}else R.drawable.ic_launcher_background)
          imagebutton.alpha=if (card.isMatched).4f else 1.0f
          val colorStateList=if (card.isMatched)ContextCompat.getColorStateList(context, com.google.android.material.R.color.material_grey_100)else null
          ViewCompat.setBackgroundTintList(imagebutton,colorStateList)
          imagebutton.setOnClickListener(View.OnClickListener {

              Log.i("Clicked","clicked on position $position")
              cardClickListener.onCardClicked(position)
          })

      }
  }
}