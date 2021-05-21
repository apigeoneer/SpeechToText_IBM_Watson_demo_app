package com.gmail.apigeoneer.texttospeech

import android.media.MediaRecorder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.gmail.apigeoneer.texttospeech.databinding.FragmentRecordAudioBinding
import java.io.IOException
import java.util.*

class RecordAudioFragment : Fragment() {

    // data binding
    private lateinit var binding: FragmentRecordAudioBinding

    private var mediaRecorder = MediaRecorder()
    private var recordedFileName: String = ""
    private var convertedFileName: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_record_audio, container, false)

        binding.toggleBtn.setOnClickListener {
            try {
                startAudioRecording()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return binding.root
    }

    @Throws(IOException::class)
    private fun startAudioRecording() {
        // Use a random UUID
        val uuid = UUID.randomUUID().toString()

        recordedFileName = context?.filesDir?.path + "/" + uuid + ".3gp";
        convertedFileName = context?.filesDir?.path + "/" + uuid + ".mp3";

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)               // record from the device's mic
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)          // use the Adaptive Multi-Rate audio codec for audio encoding
        mediaRecorder.setOutputFile(recordedFileName)
        mediaRecorder.prepare()
        mediaRecorder.start()
    }

}