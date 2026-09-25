package com.example.a24012011225_mad_project

import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var txtConnection: TextView

    private lateinit var btnDownload: LinearLayout
    private lateinit var btnUpload: LinearLayout
    private lateinit var btnPing: LinearLayout
    private lateinit var btnNetworkTest: LinearLayout
    private lateinit var btnNetworkAssistant: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)



        txtConnection =
            findViewById(R.id.txtConnection)

        btnDownload =
            findViewById(R.id.btnDownload)

        btnUpload =
            findViewById(R.id.btnUpload)

        btnPing =
            findViewById(R.id.btnPing)

        btnNetworkTest =
            findViewById(R.id.btnNetworkTest)

        btnNetworkAssistant =
            findViewById(R.id.btnNetworkAssistant)



        checkNetwork()




        btnDownload.setOnClickListener {

            val intent =
                Intent(
                    this,
                    Download_test::class.java
                )

            startActivity(intent)
        }


        // --------------------------------
        // UPLOAD
        // --------------------------------

        btnUpload.setOnClickListener {

            val intent =
                Intent(
                    this,
                    Upload_test::class.java
                )

            startActivity(intent)
        }




        btnPing.setOnClickListener {

            val intent =
                Intent(
                    this,
                    Ping_test::class.java
                )

            startActivity(intent)
        }



        btnNetworkTest.setOnClickListener {

            val intent =
                Intent(
                    this,
                    Network_test::class.java
                )

            startActivity(intent)
        }



        btnNetworkAssistant.setOnClickListener {

            val intent =
                Intent(
                    this,
                    NetworkAssistant::class.java
                )

            startActivity(intent)
        }
    }




    private fun checkNetwork() {

        val connectivityManager =
            getSystemService(
                CONNECTIVITY_SERVICE
            ) as ConnectivityManager


        val network =
            connectivityManager.activeNetwork


        if (network == null) {

            txtConnection.text =
                "No Internet Connection"

            return
        }


        val capabilities =
            connectivityManager.getNetworkCapabilities(
                network
            )


        if (
            capabilities != null &&
            capabilities.hasCapability(
                NetworkCapabilities.NET_CAPABILITY_INTERNET
            )
        ) {

            // Wi-Fi
            if (
                capabilities.hasTransport(
                    NetworkCapabilities.TRANSPORT_WIFI
                )
            ) {

                txtConnection.text =
                    "Connected through Wi-Fi"

            }

            // Mobile Data
            else if (
                capabilities.hasTransport(
                    NetworkCapabilities.TRANSPORT_CELLULAR
                )
            ) {

                txtConnection.text =
                    "Connected through Mobile Data"

            }


            else {

                txtConnection.text =
                    "Network Connected"
            }

        } else {

            txtConnection.text =
                "No Internet Connection"
        }
    }
}