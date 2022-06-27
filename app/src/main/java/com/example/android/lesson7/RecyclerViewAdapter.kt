package com.example.android.lesson7

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(private val dataSet: MutableList<CustomObject>) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userIdTextView: TextView
        val idTextView: TextView
        val titleTextView: TextView
        val bodyTextView: TextView

        init {
            userIdTextView = view.findViewById(R.id.user_id_text_view)
            idTextView = view.findViewById(R.id.id_text_view)
            titleTextView = view.findViewById(R.id.title_text_view)
            bodyTextView = view.findViewById(R.id.body_text_view)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recycler_view_item_layout, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.userIdTextView.text = dataSet[position].userId.toString()
        viewHolder.idTextView.text = dataSet[position].id.toString()
        viewHolder.titleTextView.text = dataSet[position].title
        viewHolder.bodyTextView.text = dataSet[position].body
    }

    override fun getItemCount() = dataSet.size

}