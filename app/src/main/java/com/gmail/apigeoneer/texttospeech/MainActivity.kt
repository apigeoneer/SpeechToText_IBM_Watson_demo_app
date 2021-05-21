package com.gmail.apigeoneer.texttospeech

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_RECORD_AUDIO_PERMISSION = 200             // what can go here?
    }

    private val permissions = arrayOf(android.Manifest.permission.RECORD_AUDIO)
    private var isAudioRecordingPermissionGranted: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)
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

        if (!isAudioRecordingPermissionGranted) {
            finish()
        }
    }
}