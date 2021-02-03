package com.nick.revoluttestproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

fun inflate(@LayoutRes layout: Int, parent: ViewGroup): View {
    return LayoutInflater.from(parent.context).inflate(layout, parent, false)
}