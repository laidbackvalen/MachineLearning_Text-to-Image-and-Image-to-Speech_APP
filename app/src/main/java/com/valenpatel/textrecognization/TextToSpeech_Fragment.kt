package com.valenpatel.textrecognization


import android.os.Bundle
import android.provider.CalendarContract.Colors
import android.speech.tts.TextToSpeech
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.valenpatel.textrecognization.ViewModel.TextToSpeechViewModel
import com.valenpatel.textrecognization.databinding.FragmentTextToSpeechBinding
import java.util.Locale

class TextToSpeech_Fragment : Fragment(), TextToSpeech.OnInitListener {

    private lateinit var binding: FragmentTextToSpeechBinding
    private lateinit var textToSpeech: TextToSpeech
    private val viewModel: TextToSpeechViewModel by viewModels()
    private var isSpeaking = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTextToSpeechBinding.inflate(inflater, container, false)
        textToSpeech = TextToSpeech(requireContext(), this)

        // Initialize SeekBar for pitch
        binding.pitchSeekBar.max = 20
        binding.pitchSeekBar.progress = 10 // Default to middle value
        binding.pitchSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val pitch = 0.1f + progress / 10.0f
                Log.d("TextToSpeech", "Pitch changed to: $pitch")
                viewModel.setPitch(pitch)
                textToSpeech.setPitch(pitch) // Update pitch in real-time
                if (isSpeaking) {
                    speak(binding.editText2.text.toString())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Initialize SeekBar for speed
        binding.speedSeekBar.max = 20
        binding.speedSeekBar.progress = 10 // Default to middle value
        binding.speedSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val speed = 0.1f + progress / 10.0f
                Log.d("TextToSpeech", "Speed changed to: $speed")
                viewModel.setSpeed(speed)
                textToSpeech.setSpeechRate(speed) // Update speech rate in real-time
                if (isSpeaking) {
                    speak(binding.editText2.text.toString())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Set up button click listeners
        binding.talkButton.setOnClickListener {
            val text = binding.editText2.text.toString()
            if (text.isNotEmpty()) {
                isSpeaking = true
                speak(text)
            }
        }

        binding.stopButton.setOnClickListener {
            textToSpeech.stop()
            isSpeaking = false
        }

        return binding.root
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val localeResult = textToSpeech.setLanguage(Locale.US)
            if (localeResult == TextToSpeech.LANG_MISSING_DATA || localeResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TextToSpeech", "Language is not supported or missing data")
            }
        } else {
            Log.e("TextToSpeech", "Initialization failed")
        }
    }

    private fun speak(text: String) {
        if (text.isNotEmpty()) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        textToSpeech.shutdown()
    }
}
