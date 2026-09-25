package com.example.a24012011225_mad_project

import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedOutputStream
import java.net.HttpURLConnection
import java.net.URL

class Upload_test : AppCompatActivity() {

    private lateinit var simGroup: RadioGroup
    private lateinit var radioSim1: RadioButton
    private lateinit var radioSim2: RadioButton

    private lateinit var txtSelectedSim: TextView
    private lateinit var txtUploadSpeed: TextView
    private lateinit var txtUploadStatus: TextView

    private lateinit var progressUpload: ProgressBar
    private lateinit var btnStartUpload: Button
    private lateinit var btnUploadBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_upload)

        simGroup = findViewById(R.id.uploadSimGroup)

        radioSim1 = findViewById(R.id.uploadRadioSim1)
        radioSim2 = findViewById(R.id.uploadRadioSim2)

        txtSelectedSim = findViewById(R.id.txtUploadSelectedSim)
        txtUploadSpeed = findViewById(R.id.txtUploadSpeed)
        txtUploadStatus = findViewById(R.id.txtUploadStatus)

        progressUpload = findViewById(R.id.progressUpload)

        btnStartUpload = findViewById(R.id.btnStartUpload)
        btnUploadBack = findViewById(R.id.btnUploadBack)

        radioSim1.isChecked = true

        simGroup.setOnCheckedChangeListener { _, checkedId ->

            when (checkedId) {

                R.id.uploadRadioSim1 -> {
                    txtSelectedSim.text =
                        "Selected SIM: SIM 1"
                }

                R.id.uploadRadioSim2 -> {
                    txtSelectedSim.text =
                        "Selected SIM: SIM 2"
                }
            }
        }

        btnStartUpload.setOnClickListener {
            startUploadTest()
        }

        btnUploadBack.setOnClickListener {
            finish()
        }
    }

    private fun startUploadTest() {

        btnStartUpload.isEnabled = false

        progressUpload.visibility =
            ProgressBar.VISIBLE

        txtUploadSpeed.text =
            "0.00 Mbps"

        txtUploadStatus.text =
            "Connecting..."

        Thread {

            var connection: HttpURLConnection? = null

            try {

                // Upload test server
                val url = URL(
                    "https://speed.cloudflare.com/__up"
                )

                connection =
                    url.openConnection() as HttpURLConnection

                connection.connectTimeout = 15000
                connection.readTimeout = 15000

                connection.requestMethod = "POST"

                connection.doOutput = true

                connection.setRequestProperty(
                    "Content-Type",
                    "application/octet-stream"
                )

                val testSize = 5 * 1024 * 1024

                val buffer = ByteArray(8192)

                for (i in buffer.indices) {
                    buffer[i] = (i % 256).toByte()
                }

                connection.connect()

                val outputStream =
                    BufferedOutputStream(
                        connection.outputStream
                    )

                var totalBytes = 0L

                val startTime =
                    System.currentTimeMillis()

                while (totalBytes < testSize) {

                    val remaining =
                        testSize - totalBytes

                    val bytesToWrite =
                        minOf(
                            buffer.size.toLong(),
                            remaining
                        ).toInt()

                    outputStream.write(
                        buffer,
                        0,
                        bytesToWrite
                    )

                    totalBytes += bytesToWrite

                    val uploadedKB =
                        totalBytes / 1024

                    runOnUiThread {

                        txtUploadStatus.text =
                            "Uploading... ${uploadedKB} KB"
                    }
                }

                outputStream.flush()
                outputStream.close()

                val endTime =
                    System.currentTimeMillis()

                val timeSeconds =
                    (endTime - startTime) / 1000.0

                if (timeSeconds <= 0 ||
                    totalBytes <= 0
                ) {

                    runOnUiThread {

                        showUploadError()
                    }

                    return@Thread
                }

                val speedMbps =
                    (totalBytes * 8.0) /
                            timeSeconds /
                            1_000_000.0

                // Check server response
                val responseCode =
                    connection.responseCode

                if (responseCode !in 200..299) {

                    runOnUiThread {

                        txtUploadStatus.text =
                            "Server error: $responseCode"

                        progressUpload.visibility =
                            ProgressBar.GONE

                        btnStartUpload.isEnabled =
                            true
                    }

                    return@Thread
                }

                // Display final result
                runOnUiThread {

                    txtUploadSpeed.text =
                        String.format(
                            "%.2f Mbps",
                            speedMbps
                        )

                    txtUploadStatus.text =
                        "Upload test completed"

                    progressUpload.visibility =
                        ProgressBar.GONE

                    btnStartUpload.isEnabled =
                        true
                }

            } catch (e: Exception) {

                runOnUiThread {

                    progressUpload.visibility =
                        ProgressBar.GONE

                    txtUploadSpeed.text =
                        "0.00 Mbps"

                    txtUploadStatus.text =
                        "Error: ${e.message}"

                    btnStartUpload.isEnabled =
                        true
                }

            } finally {

                connection?.disconnect()
            }

        }.start()
    }

    private fun showUploadError() {

        progressUpload.visibility =
            ProgressBar.GONE

        txtUploadSpeed.text =
            "0.00 Mbps"

        txtUploadStatus.text =
            "Upload test failed"

        btnStartUpload.isEnabled =
            true
    }
}