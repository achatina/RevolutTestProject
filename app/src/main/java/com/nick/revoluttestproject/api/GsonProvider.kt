package com.nick.revoluttestproject.api

import com.google.gson.Gson

object GsonProvider {

    //for using custom converters in possible future
    private val gson = Gson()

    fun provideGson() = gson

}