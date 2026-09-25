package com.example.a24012011225_mad_project

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.telephony.TelephonyManager
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class NetworkAssistant : AppCompatActivity() {

    private lateinit var txtCurrentNetwork: TextView
    private lateinit var txtCurrentSignal: TextView
    private lateinit var txtSignalStatus: TextView
    private lateinit var txtScanStatus: TextView
    private lateinit var txtBestLocation: TextView
    private lateinit var txtBestSignal: TextView

    private lateinit var progressAssistant: ProgressBar
    private lateinit var btnStartAssistant: Button
    private lateinit var btnStartGuidance: Button
    private lateinit var btnAssistantBack: Button

    private lateinit var locationManager: LocationManager
    private lateinit var telephonyManager: TelephonyManager

    private var bestSignal = -1
    private var bestLatitude = 0.0
    private var bestLongitude = 0.0

    companion object {
        private const val LOCATION_PERMISSION_REQUEST = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_network_assistant)

        txtCurrentNetwork =
            findViewById(R.id.txtCurrentNetwork)

        txtCurrentSignal =
            findViewById(R.id.txtCurrentSignal)

        txtSignalStatus =
            findViewById(R.id.txtSignalStatus)

        txtScanStatus =
            findViewById(R.id.txtScanStatus)

        txtBestLocation =
            findViewById(R.id.txtBestLocation)

        txtBestSignal =
            findViewById(R.id.txtBestSignal)

        progressAssistant =
            findViewById(R.id.progressAssistant)

        btnStartAssistant =
            findViewById(R.id.btnStartAssistant)

        btnStartGuidance =
            findViewById(R.id.btnStartGuidance)

        btnAssistantBack =
            findViewById(R.id.btnAssistantBack)

        locationManager =
            getSystemService(LOCATION_SERVICE) as LocationManager

        telephonyManager =
            getSystemService(TELEPHONY_SERVICE) as TelephonyManager

        btnStartAssistant.setOnClickListener {
            startAreaScan()
        }

        btnStartGuidance.setOnClickListener {
            showBestLocation()
        }

        btnAssistantBack.setOnClickListener {
            finish()
        }

        showCurrentNetwork()
    }



    private fun startAreaScan() {

        if (
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST
            )

            return
        }

        bestSignal = -1
        bestLatitude = 0.0
        bestLongitude = 0.0

        btnStartAssistant.isEnabled = false
        btnStartGuidance.isEnabled = false

        progressAssistant.visibility =
            ProgressBar.VISIBLE

        txtScanStatus.text =
            "Scanning nearby area...\nMove around slowly"

        txtBestLocation.text =
            "Best Location: Searching..."

        txtBestSignal.text =
            "Best Signal: Searching..."

        try {

            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                3000L,
                5f,
                locationListener
            )


            Handler(
                Looper.getMainLooper()
            ).postDelayed({

                stopAreaScan()

                if (bestSignal >= 0) {

                    txtScanStatus.text =
                        "Scan completed!"

                } else {

                    txtScanStatus.text =
                        "No location found. Try again."
                }

            }, 15000)

        } catch (e: Exception) {

            txtScanStatus.text =
                "Unable to start location scan"

            stopAreaScan()
        }
    }

    private val locationListener =
        object : LocationListener {

            override fun onLocationChanged(
                location: Location
            ) {

                val signal =
                    getSignalStrength()

                txtCurrentSignal.text =
                    "Current Signal: $signal / 4"

                txtSignalStatus.text =
                    getSignalStatus(signal)

                txtScanStatus.text =
                    "Scanning...\nMove around slowly"

                if (signal > bestSignal) {

                    bestSignal = signal

                    bestLatitude =
                        location.latitude

                    bestLongitude =
                        location.longitude

                    txtBestLocation.text =
                        String.format(
                            "Best Location:\n%.5f, %.5f",
                            bestLatitude,
                            bestLongitude
                        )

                    txtBestSignal.text =
                        "Best Signal: $bestSignal / 4"

                    btnStartGuidance.isEnabled =
                        true
                }
            }
        }

    // --------------------------------
    // GET SIGNAL STRENGTH
    // --------------------------------

    private fun getSignalStrength(): Int {

        return try {

            val signal =
                telephonyManager.signalStrength

            if (signal != null) {

                signal.level

            } else {

                0
            }

        } catch (e: Exception) {

            0
        }
    }

    // --------------------------------
    // SIGNAL STATUS
    // --------------------------------

    private fun getSignalStatus(
        signal: Int
    ): String {

        return when {

            signal >= 3 ->
                "Status: Strong Signal"

            signal == 2 ->
                "Status: Good Signal"

            signal == 1 ->
                "Status: Weak Signal"

            else ->
                "Status: Very Weak Signal"
        }
    }


    private fun showCurrentNetwork() {

        try {

            val networkType =
                telephonyManager.dataNetworkType

            val networkName =
                when (networkType) {

                    20 ->
                        "5G"

                    13 ->
                        "4G LTE"

                    15, 14, 12, 10, 9, 8 ->
                        "3G"

                    3, 2, 1 ->
                        "2G"

                    else ->
                        "Mobile Network"
                }

            txtCurrentNetwork.text =
                "Current Network: $networkName"

        } catch (e: Exception) {

            txtCurrentNetwork.text =
                "Current Network: Mobile Network"
        }
    }


    private fun stopAreaScan() {

        if (
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            locationManager.removeUpdates(
                locationListener
            )
        }

        progressAssistant.visibility =
            ProgressBar.GONE

        btnStartAssistant.isEnabled =
            true
    }

    // --------------------------------
    // SHOW BEST LOCATION
    // --------------------------------

    private fun showBestLocation() {

        if (bestSignal == -1) {

            txtScanStatus.text =
                "Scan the area first"

            return
        }

        txtScanStatus.text =
            String.format(
                "Better network found!\n\n" +
                        "Move towards:\n" +
                        "%.5f, %.5f\n\n" +
                        "Best Signal: %d / 4",
                bestLatitude,
                bestLongitude,
                bestSignal
            )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )

        if (
            requestCode ==
            LOCATION_PERMISSION_REQUEST
        ) {

            if (
                grantResults.isNotEmpty() &&
                grantResults[0] ==
                PackageManager.PERMISSION_GRANTED
            ) {

                startAreaScan()

            } else {

                txtScanStatus.text =
                    "Location permission required"
            }
        }
    }


    override fun onDestroy() {

        if (
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            locationManager.removeUpdates(
                locationListener
            )
        }

        super.onDestroy()
    }
}