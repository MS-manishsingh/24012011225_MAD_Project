package com.example.a24012011225_mad_project

import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.net.InetAddress

class Ping_test : AppCompatActivity() {

    private lateinit var txtPingValue: TextView
    private lateinit var txtPingStatus: TextView

    private lateinit var progressPing: ProgressBar
    private lateinit var btnStartPing: Button
    private lateinit var btnPingBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_ping)

        // Connect XML elements
        txtPingValue = findViewById(R.id.txtPingValue)
        txtPingStatus = findViewById(R.id.txtPingStatus)

        progressPing = findViewById(R.id.progressPing)

        btnStartPing = findViewById(R.id.btnStartPing)
        btnPingBack = findViewById(R.id.btnPingBack)

        // Start Ping Test
        btnStartPing.setOnClickListener {
            startPingTest()
        }

        // Back button
        btnPingBack.setOnClickListener {
            finish()
        }
    }

    private fun startPingTest() {

        // Disable button while testing
        btnStartPing.isEnabled = false

        // Show loading animation
        progressPing.visibility = ProgressBar.VISIBLE

        // Reset values
        txtPingValue.text = "0 ms"
        txtPingStatus.text = "Testing..."

        Thread {

            try {


                val address =
                    InetAddress.getByName("google.com")


                val startTime =
                    System.currentTimeMillis()

                val reachable =
                    address.isReachable(5000)

                val endTime =
                    System.currentTimeMillis()

                val pingTime =
                    endTime - startTime

                runOnUiThread {

                    progressPing.visibility =
                        ProgressBar.GONE

                    if (reachable) {

                        txtPingValue.text =
                            "$pingTime ms"

                        txtPingStatus.text =
                            "Ping test completed"

                    } else {

                        txtPingValue.text =
                            "Failed"

                        txtPingStatus.text =
                            "Server not reachable"
                    }

                    btnStartPing.isEnabled = true
                }

            } catch (e: Exception) {

                runOnUiThread {

                    progressPing.visibility =
                        ProgressBar.GONE

                    txtPingValue.text =
                        "Failed"

                    txtPingStatus.text =
                        "Error: ${e.message}"

                    btnStartPing.isEnabled = true
                }
            }

        }.start()
    }
}