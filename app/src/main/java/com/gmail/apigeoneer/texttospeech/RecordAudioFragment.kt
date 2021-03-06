package com.gmail.apigeoneer.texttospeech

import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaRecorder
import android.media.audiofx.DynamicsProcessing
import android.os.AsyncTask.execute
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.gmail.apigeoneer.texttospeech.RecordAudioFragment.Companion.TAG
import com.gmail.apigeoneer.texttospeech.databinding.FragmentRecordAudioBinding
import nl.bravobit.ffmpeg.FFmpeg
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*

/**
 * Ask for run time permissions
 * Create a MediaRecorder obj & record audio
 */
class RecordAudioFragment : Fragment() {

    companion object {
        private const val TAG = "RecordAudioFragment"
        private const val REQUEST_RECORD_AUDIO_PERMISSION = 200             // what can go here?
    }

    // data binding
    private lateinit var binding: FragmentRecordAudioBinding

    private val permissions = arrayOf(android.Manifest.permission.RECORD_AUDIO)
    private var isAudioRecordingPermissionGranted: Boolean = false

    private var mediaRecorder = MediaRecorder()
    private var recordedFileName: String = ""
    private var convertedFileName: String = ""
    private var isRecording: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_record_audio, container, false)

        binding.toggleBtn.setOnClickListener {
            if (!isRecording) {
                if (isAudioRecordingPermissionGranted) {
                    try {
                        startAudioRecording()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            } else {
                // if recording, schedule a thread to convert the speech being recorded into text
                val thread = Thread(Runnable {
                    try {
                        convertSpeech()
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }
                })
                thread.start()
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

    @Throws(FileNotFoundException::class)
    private fun convertSpeech() {
        toggleRecording()

        mediaRecorder.stop()
        mediaRecorder.reset()
        mediaRecorder.release()

        val ffmpeg = FFmpeg.getInstance(context?.applicationContext)

//        val rc = FFmpeg.execute(String
//                .format("-i %s -c:a libmp3lame %s", recordedFileName, convertedFileName))
//
//        when (rc) {
//            RETURN_CODE_SUCCESS -> Log.i(Config.TAG, "Command execution completed successfully")
//            RETURN_CODE_CANCEL -> Log.i(Config.TAG, "Command execution cancelled by the user")
//            else -> {
//                Log.i(DynamicsProcessing.Config.TAG, "Command execution completed successfully"
//                Config.printLastCommandOutput(Log.INFO))
//            }
//        }
    }

    private fun toggleRecording() {
        TODO("Not yet implemented")
    }

}