package com.gmail.apigeoneer.texttospeech

import android.content.pm.PackageManager
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

/**
 * Ask for run time permissions
 * Create a MediaRecorder obj & record audio
 */
class RecordAudioFragment : Fragment() {

    companion object {
        private const val REQUEST_RECORD_AUDIO_PERMISSION = 200             // what can go here?
    }

    // data binding
    private lateinit var binding: FragmentRecordAudioBinding

    private val permissions = arrayOf(android.Manifest.permission.RECORD_AUDIO)
    private var isAudioRecordingPermissionGranted: Boolean = false

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

        // For Activity
        //ActivityCompat.requestPermissions(MainActivity, permissions, REQUEST_RECORD_AUDIO_PERMISSION)
        // For fragment
        requestPermissions(permissions, REQUEST_RECORD_AUDIO_PERMISSION)

        return binding.root
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        isAudioRecordingPermissionGranted = when (requestCode) {
            REQUEST_RECORD_AUDIO_PERMISSION ->
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            else -> false
        }

//        if (!isAudioRecordingPermissionGranted) {
//            finish()
//        }
    }

    @Throws(IOException::class)
    fun startAudioRecording() {
        // Use a random UUID
        val uuid = UUID.randomUUID().toString()

        recordedFileName = requireContext().filesDir.path + "/" + uuid + ".3gp"
        convertedFileName = requireContext().filesDir.path + "/" + uuid + ".mp3"

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)               // record from the device's mic
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)          // use the Adaptive Multi-Rate audio codec for audio encoding
        mediaRecorder.setOutputFile(recordedFileName)
        mediaRecorder.prepare()
        mediaRecorder.start()
    }

}