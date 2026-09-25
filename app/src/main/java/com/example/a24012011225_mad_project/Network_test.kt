package com.example.a24012011225_mad_project

import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.net.HttpURLConnection
import java.net.InetAddress
import java.net.URL

class Network_test : AppCompatActivity() {

    private lateinit var txtNetworkDownload: TextView
    private lateinit var txtNetworkUpload: TextView
    private lateinit var txtNetworkPing: TextView
    private lateinit var txtNetworkTestStatus: TextView

    private lateinit var progressNetworkTest: ProgressBar
    private lateinit var btnStartNetworkTest: Button
    private lateinit var btnNetworkTestBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_network_test)

        txtNetworkDownload =
            findViewById(R.id.txtNetworkDownload)

        txtNetworkUpload =
            findViewById(R.id.txtNetworkUpload)

        txtNetworkPing =
            findViewById(R.id.txtNetworkPing)

        txtNetworkTestStatus =
            findViewById(R.id.txtNetworkTestStatus)

        progressNetworkTest =
            findViewById(R.id.progressNetworkTest)

        btnStartNetworkTest =
            findViewById(R.id.btnStartNetworkTest)

        btnNetworkTestBack =
            findViewById(R.id.btnNetworkTestBack)

        btnStartNetworkTest.setOnClickListener {
            startNetworkTest()
        }

        btnNetworkTestBack.setOnClickListener {
            finish()
        }
    }

    private fun startNetworkTest() {

        btnStartNetworkTest.isEnabled = false

        progressNetworkTest.visibility =
            ProgressBar.VISIBLE

        txtNetworkDownload.text =
            "Download: Testing..."

        txtNetworkUpload.text =
            "Upload: Waiting..."

        txtNetworkPing.text =
            "Ping: Waiting..."

        txtNetworkTestStatus.text =
            "Starting network test..."

        Thread {

            runOnUiThread {
                txtNetworkTestStatus.text =
                    "Testing download speed..."
            }

            val downloadSpeed =
                testDownload()

            runOnUiThread {

                if (downloadSpeed >= 0) {

                    txtNetworkDownload.text =
                        String.format(
                            "Download: %.2f Mbps",
                            downloadSpeed
                        )

                } else {

                    txtNetworkDownload.text =
                        "Download: Failed"
                }
            }

            runOnUiThread {

                txtNetworkUpload.text =
                    "Upload: Testing..."

                txtNetworkTestStatus.text =
                    "Testing upload speed..."
            }

            val uploadSpeed =
                testUpload()

            runOnUiThread {

                if (uploadSpeed >= 0) {

                    txtNetworkUpload.text =
                        String.format(
                            "Upload: %.2f Mbps",
                            uploadSpeed
                        )

                } else {

                    txtNetworkUpload.text =
                        "Upload: Failed"
                }
            }

            runOnUiThread {

                txtNetworkPing.text =
                    "Ping: Testing..."

                txtNetworkTestStatus.text =
                    "Testing ping..."
            }

            val ping =
                testPing()

            runOnUiThread {

                if (ping >= 0) {

                    txtNetworkPing.text =
                        "Ping: $ping ms"

                } else {

                    txtNetworkPing.text =
                        "Ping: Failed"
                }

                txtNetworkTestStatus.text =
                    "Network test completed"

                progressNetworkTest.visibility =
                    ProgressBar.GONE

                btnStartNetworkTest.isEnabled =
                    true
            }

        }.start()
    }

    private fun testDownload(): Double {

        var connection: HttpURLConnection? = null

        return try {

            val url = URL(
                "https://speed.cloudflare.com/__down?bytes=5000000"
            )

            connection =
                url.openConnection() as HttpURLConnection

            connection.connectTimeout = 15000
            connection.readTimeout = 15000
            connection.requestMethod = "GET"

            connection.connect()

            if (connection.responseCode != 200) {
                return -1.0
            }

            val inputStream =
                BufferedInputStream(
                    connection.inputStream
                )

            val buffer =
                ByteArray(8192)

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
            }

            inputStream.close()

            val endTime =
                System.currentTimeMillis()

            val timeSeconds =
                (endTime - startTime) / 1000.0

            if (timeSeconds <= 0 ||
                totalBytes <= 0
            ) {
                return -1.0
            }

            (totalBytes * 8.0) /
                    timeSeconds /
                    1_000_000.0

        } catch (e: Exception) {

            -1.0

        } finally {

            connection?.disconnect()
        }
    }

    private fun testUpload(): Double {

        var connection: HttpURLConnection? = null

        return try {

            val url =
                URL("https://speed.cloudflare.com/__up")

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

            val testSize =
                5 * 1024 * 1024

            val buffer =
                ByteArray(8192)

            for (i in buffer.indices) {

                buffer[i] =
                    (i % 256).toByte()
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
            }

            outputStream.flush()
            outputStream.close()

            val endTime =
                System.currentTimeMillis()

            val responseCode =
                connection.responseCode

            if (responseCode !in 200..299) {
                return -1.0
            }

            val timeSeconds =
                (endTime - startTime) / 1000.0

            if (timeSeconds <= 0) {
                return -1.0
            }

            (totalBytes * 8.0) /
                    timeSeconds /
                    1_000_000.0

        } catch (e: Exception) {

            -1.0

        } finally {

            connection?.disconnect()
        }
    }

    private fun testPing(): Long {

        return try {

            val address =
                InetAddress.getByName("google.com")

            val startTime =
                System.currentTimeMillis()

            val reachable =
                address.isReachable(5000)

            val endTime =
                System.currentTimeMillis()

            if (reachable) {

                endTime - startTime

            } else {

                -1L
            }

        } catch (e: Exception) {

            -1L
        }
    }
}