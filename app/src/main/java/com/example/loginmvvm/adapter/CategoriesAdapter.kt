package com.example.loginmvvm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.loginmvvm.R
import com.example.loginmvvm.model.ItemModel

class CategoriesAdapter(private val callback: OnClickItem):RecyclerView.Adapter<CategoriesAdapter.ViewHolder>(){
    private var list:List<ItemModel> = listOf()


    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val tvTitle: TextView
        val rlItem: RelativeLayout

        init {
            tvTitle = itemView.findViewById(R.id.itemCategories_tvTitle)
            rlItem = itemView.findViewById(R.id.itemCategories_rlItem)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_categories,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvTitle.text = list[position].name
        holder.itemView.setOnClickListener{
            list[position].selected = !list[position].selected
            if (list[position].selected){
                holder.rlItem.setBackgroundResource(R.drawable.bg_border_item_selected)
            }else{
                holder.rlItem.setBackgroundResource(R.drawable.bg_border_item)
            }
            callback.onClick(list[position],list)
        }
    }

    fun setData(list: List<ItemModel>){
        this.list = list
    }

    interface OnClickItem{
        fun onClick(itemModel: ItemModel,list:List<ItemModel>)
    }
}