package com.fiqih.api

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_row.view.*

class MyAdapter (val context: Context, var dataList: ArrayList<CEOModel>?): RecyclerView.Adapter<MyAdapter.MyViewholder>() {
    class MyViewholder(view: View) : RecyclerView.ViewHolder(view) {
        val llMain = view.llMain
        val tvId = view.tvId
        val tvName = view.tvName
        val tvCompanyName = view.tvCompanyName
        val ivEdit = view.iv_edit
        val ivDelete = view.iv_delete
    }

    // method untuk membuat view holder
    // inflate = memunculkan data
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewholder {
        return MyViewholder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return dataList!!.size
    }

    override fun onBindViewHolder(holder: MyViewholder, position: Int) {
        val item = dataList?.get(position)

        holder.tvId.text = (position + 1).toString()
        holder.tvName.text = "(id:${item?.id.toString()}) ${item?.name.toString()}"
        holder.tvCompanyName.text = item?.company_name.toString()

        if (position % 2 == 0) {
            holder.llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.teal_200))
        } else {
            holder.llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        }

        holder.ivEdit.setOnClickListener {
            if(context is MainActivity){
                context.updateRecordDialog(item!!)
            }
        }
        holder.ivDelete.setOnClickListener{
            if (context is MainActivity) {
                context.deleteRecordDialog(item!!)
            }
        }

    }
}


