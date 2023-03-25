package com.example.mysensorapp

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlin.math.sqrt
import android.content.Context
import android.widget.Toast

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometerSensor: Sensor
    private var isWalking = true
    private var state = "Walking"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

            // below we have created
            // a new DBHelper class,
            // and passed context to it


    }

    override fun onResume() {
        super.onResume()

        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()

        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Do nothing
 }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor == accelerometerSensor) {
                val acceleration = sqrt(it.values[0] * it.values[0] + it.values[1] * it.values[1] + it.values[2] * it.values[2])
                if (!isWalking && acceleration > 11) {
                    isWalking = true
                    state="Walking"
                    showToast(this, "WALKING!")
                    storeinDB()
                    // Do something when walking is detected
                } else if (acceleration < 11 && isWalking) {

                    isWalking = false
                    state="Still"
                    showToast(this, "STILL!")
                    // Do something when stillness is detected
                    storeinDB()
                }
            }
        }

    }
    fun storeinDB(){
        val db = DBHelper(this, null)

        // creating variables for values
        // in name and age edit texts
        val name = "check"
        val age = "1234567890"
        val activity = state
        // calling method to add
        // name to our database
        db.addName(name, age, activity)

        // Toast to message on the screen
        Toast.makeText(this, name + " added to database", Toast.LENGTH_LONG).show()

    }
}
