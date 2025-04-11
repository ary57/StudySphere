package com.example.studysphere.ui.meets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MeetsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is meets Fragment"
    }
    val text: LiveData<String> = _text
}