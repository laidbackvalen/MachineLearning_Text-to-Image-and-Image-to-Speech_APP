package com.valenpatel.textrecognization.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TextToSpeechViewModel : ViewModel() {

    // LiveData to hold pitch and speed values
    private val _pitch = MutableLiveData<Float>().apply { value = 1.0f }
    val pitch: LiveData<Float> = _pitch

    private val _speed = MutableLiveData<Float>().apply { value = 1.0f }
    val speed: LiveData<Float> = _speed

    // Methods to update pitch and speed values
    fun setPitch(newPitch: Float) {
        _pitch.value = newPitch
    }

    fun setSpeed(newSpeed: Float) {
        _speed.value = newSpeed
    }
}
