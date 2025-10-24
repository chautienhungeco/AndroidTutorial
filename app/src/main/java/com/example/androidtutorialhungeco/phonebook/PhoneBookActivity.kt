package com.eco.musicplayer.audioplayer.music.phonebook

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.eco.musicplayer.audioplayer.music.R
import com.eco.musicplayer.audioplayer.music.common.DataPhone

class PhoneBookActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_phone)

        val recyclerView: RecyclerView = findViewById(R.id.rcvDataPhone)

        val rawData = listOf(
            DataPhone.Contact("Anh Tài","012345689"),
            DataPhone.Contact("Bạn Tài","012345689"),
            DataPhone.Contact("Cô Tài","012345689"),
            DataPhone.Contact("Đánh Tài","012345689"),
            DataPhone.Contact("Em Tài","012345689"),
            DataPhone.Contact("Gia Tài","012345689"),
            DataPhone.Contact("Hoa Tài","012345689"),
            DataPhone.Contact("Anh Hưng","012345689"),
            DataPhone.Contact("Thằng Tài","012345689"),
            DataPhone.Contact("Văn Tài","012345689"),
            DataPhone.Contact("Tú Tài","012345689"),
        )
        val finalData = createContactListItem(rawData)

        recyclerView.adapter = ContactAdapter(finalData)
    }

    private fun createContactListItem(rawContacts: List<DataPhone.Contact>): List<DataPhone>{
        val sortedContacts = rawContacts.sortedBy { it.name.uppercase() }
        val resultList = mutableListOf<DataPhone>()
        var currentHeader: Char? = null

        for (contact in sortedContacts){
            val firstLetter = contact.name.uppercase().first()

            if(firstLetter != currentHeader){
                resultList.add(DataPhone.Header(firstLetter))
                currentHeader = firstLetter
            }

            resultList.add(contact)
        }
        return resultList
    }
}