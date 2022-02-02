package com.moon.astromicon.ui

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel



class MainViewModel : ViewModel() {



    @SuppressLint("StaticFieldLeak")

    val moonInfo = MutableLiveData<String>()


}