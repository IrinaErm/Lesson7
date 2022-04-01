package com.example.android.lesson7

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CustomObject(var userId: Long, var id: Long, var title: String, var body: String) :
    Parcelable