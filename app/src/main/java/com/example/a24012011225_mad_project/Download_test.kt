package com.example.a24012011225_mad_project

import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedInputStream
import java.net.HttpURLConnection
import java.net.URL

class Download_test : AppCompatActivity() {

    private lateinit var simGroup: RadioGroup
    private lateinit var radioSim1: RadioButton
    private lateinit var radioSim2: RadioButton

    private lateinit var txtSelectedSim: TextView
    private lateinit var txtDownloadSpeed: TextView
    private lateinit var txtDownloadStatus: TextView

    private lateinit var progressDownload: ProgressBar
    private lateinit var btnStartDownload: Button
    private lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_download)

        simGroup = findViewById(R.id.simGroup)

        radioSim1 = findViewById(R.id.radioSim1)
        radioSim2 = findViewById(R.id.radioSim2)

        txtSelectedSim = findViewById(R.id.txtSelectedSim)
        txtDownloadSpeed = findViewById(R.id.txtDownloadSpeed)
        txtDownloadStatus = findViewById(R.id.txtDownloadStatus)

        progressDownload = findViewById(R.id.progressDownload)

        btnStartDownload = findViewById(R.id.btnStartDownload)
        btnBack = findViewById(R.id.btnBack)

        radioSim1.isChecked = true

        simGroup.setOnCheckedChangeListener { _, checkedId ->

            when (checkedId) {

                R.id.radioSim1 -> {
                    txtSelectedSim.text = "Selected SIM: SIM 1"
                }

                R.id.radioSim2 -> {
                    txtSelectedSim.text = "Selected SIM: SIM 2"
                }
            }
        }

        btnStartDownload.setOnClickListener {
            startDownloadTest()
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun startDownloadTest() {

        btnStartDownload.isEnabled = false

        progressDownload.visibility = ProgressBar.VISIBLE

        txtDownloadSpeed.text = "0.00 Mbps"
        txtDownloadStatus.text = "Connecting..."

        Thread {

            var connection: HttpURLConnection? = null

            try {

                val url = URL(
                    "https://speed.cloudflare.com/__down?bytes=10000000"
                )

                connection =
                    url.openConnection() as HttpURLConnection

                connection.connectTimeout = 15000
                connection.readTimeout = 15000
                connection.requestMethod = "GET"

                connection.connect()

                if (connection.responseCode != HttpURLConnection.HTTP_OK) {

                    runOnUiThread {

                        progressDownload.visibility =
                            ProgressBar.GONE

                        txtDownloadStatus.text =
                            "Server error: ${connection.responseCode}"

                        btnStartDownload.isEnabled = true
                    }

                    return@Thread
                }

                runOnUiThread {
                    txtDownloadStatus.text =
                        "Downloading..."
                }

                val inputStream =
                    BufferedInputStream(connection.inputStream)

                val buffer = ByteArray(8192)

                var totalBytes = 0L

                val startTime =
                    System.currentTimeMillis()

                while (true) {

                    val bytesRead =
                        inputStream.read(buffer)

                    if (bytesRead == -1) {
                        break
                    }

                    totalBytes += bytesRead

                    val downloadedKB =
                        totalBytes / 1024

                    runOnUiThread {

                        txtDownloadStatus.text =
                            "Downloading... ${downloadedKB} KB"
                    }
                }

                inputStream.close()

                val endTime =
                    System.currentTimeMillis()

                val timeSeconds =
                    (endTime - startTime) / 1000.0

                if (timeSeconds <= 0 || totalBytes <= 0) {

                    runOnUiThread {

                        progressDownload.visibility =
                            ProgressBar.GONE

                        txtDownloadSpeed.text =
                            "0.00 Mbps"

                        txtDownloadStatus.text =
                            "No data downloaded"

                        btnStartDownload.isEnabled = true
                    }

                    return@Thread
                }

                val speedMbps =
                    (totalBytes * 8.0) /
                            timeSeconds /
                            1_000_000.0

                runOnUiThread {

                    txtDownloadSpeed.text =
                        String.format(
                            "%.2f Mbps",
                            speedMbps
                        )

                    txtDownloadStatus.text =
                        "Download test completed"

                    progressDownload.visibility =
                        ProgressBar.GONE

                    btnStartDownload.isEnabled = true
                }

            } catch (e: Exception) {

                runOnUiThread {

                    progressDownload.visibility =
                        ProgressBar.GONE

                    txtDownloadSpeed.text =
                        "0.00 Mbps"

                    txtDownloadStatus.text =
                        "Error: ${e.message}"

                    btnStartDownload.isEnabled = true
                }

            } finally {

                connection?.disconnect()
            }

        }.start()
    }
}