package com.example.memorygame

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.memorygame.models.Boradsize
import kotlin.math.min

class ImagePickerViewAdapter(private val context: Context,
                             private val imageList: MutableList<Uri>,
                             private val boardsize: Boradsize,
                              private val imageClickListner:ImageClickListener

)
    : RecyclerView.Adapter<ImagePickerViewAdapter.myViewHolder>() {

        interface ImageClickListener{
            fun onPlaceholderClicked()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
       val view= LayoutInflater.from(context).inflate(R.layout.card_image,parent,false)
        val cardWidth=parent.width/boardsize.getwidth()
        val cardHeight=parent.height/boardsize.getheight()
        val cardSidelenght= min(cardHeight,cardWidth)
        val layoutParameters=view.findViewById<ImageView>(R.id.image_View).layoutParams
        layoutParameters.width=cardSidelenght
        layoutParameters.height=cardSidelenght
        return myViewHolder(view)
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        if (position < imageList.size) {
            holder.bind(imageList[position])
        } else {
            holder.bind()
        }
    }


    override fun getItemCount()=boardsize.getNumparis()


inner class myViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    private val ivCustonImage=itemView.findViewById<ImageView>(R.id.image_View)
    fun bind(uri: Uri) {
        ivCustonImage.setImageURI(uri)
        ivCustonImage.setOnClickListener(null)


    }
    fun bind(){
        ivCustonImage.setOnClickListener{imageClickListner.onPlaceholderClicked()}

    }

}
}
