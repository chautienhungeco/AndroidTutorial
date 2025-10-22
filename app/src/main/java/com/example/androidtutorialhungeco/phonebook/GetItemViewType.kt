package com.example.androidtutorialhungeco.phonebook

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidtutorialhungeco.R
import com.example.androidtutorialhungeco.common.Constants
import com.example.androidtutorialhungeco.common.DataPhone
import java.lang.IllegalArgumentException

class ContactAdapter(private val items: List<DataPhone>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class HeaderViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val headerTextView: TextView = view.findViewById(R.id.tvheader)
        fun bind(header: DataPhone.Header){
            headerTextView.text = header.letter.toString()
        }
    }

    class ContactViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val nameTextView: TextView = view.findViewById(R.id.tvName)
        private val avatarTextView: TextView = view.findViewById(R.id.tvAvatar)

        private fun getAvatarColor(firstLetter: Char): Int{
            return when(firstLetter.uppercaseChar()){
                'A' -> R.drawable.cr_background_green
                'B' -> R.drawable.cr_background_orange
                'C' -> R.drawable.cr_background_pink
                'D' -> R.drawable.cr_background_red
                else -> R.drawable.cr_background_blue
            }
        }
        fun bind(dataPhone: DataPhone.Contact){
            nameTextView.text = dataPhone.name
            val firstLetter = dataPhone.name.take(1).uppercase()
            avatarTextView.text = firstLetter
            avatarTextView.setBackgroundResource(getAvatarColor(firstLetter.first()))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(items[position]){
            is DataPhone.Header -> Constants.VIEW_TYPE_HEADER
            is DataPhone.Contact -> Constants.VIEW_TYPE_CONTACT
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType){
            Constants.VIEW_TYPE_HEADER -> HeaderViewHolder(inflater.inflate(R.layout.activity_title_header, parent,false))
            Constants.VIEW_TYPE_CONTACT -> ContactViewHolder(inflater.inflate(R.layout.activity_info_contact, parent,false))
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]){
            is DataPhone.Header -> (holder as HeaderViewHolder).bind(item)
            is DataPhone.Contact -> (holder as ContactViewHolder).bind(item)
            else -> {
                Log.d(TAG, "KHÔNG HỢP LỆ")}
        }
    }
}