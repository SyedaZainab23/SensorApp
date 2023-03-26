package com.example.mysensorapp
import android.annotation.SuppressLint
import java.time.LocalDateTime
import java.time.Duration
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlin.math.sqrt
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometerSensor: Sensor
    private var isStill = true
    private var isWalking = false
    private var isRunning = false
    private var isDriving = false
    private var state = "Walking"
    @RequiresApi(Build.VERSION_CODES.O)
    var time1 = LocalDateTime.now()
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {

            if (it.sensor == accelerometerSensor) {
                val acceleration = sqrt(it.values[0] * it.values[0] + it.values[1] * it.values[1] + it.values[2] * it.values[2])
                //var time1 = LocalDateTime.now()
                if (!isWalking && acceleration > 11 && acceleration < 18) {
                    isWalking = true
                    state="Walking"
                    showToast(this, "WALKING!")
                    var time2 = LocalDateTime.now()
                    val duration = Duration.between(time1, time2)
                    time1=time2
                    val x= duration.seconds
                    storeinDB(x.toString())
                    isDriving=false
                    isStill=false
                    isRunning=false
                    // Do something when walking is detected
                } else if (acceleration < 11 && !isStill) {

                    isStill = true
                    state="Still"
                    showToast(this, "STILL!")
                    // Do something when stillness is detected
                    var time2 = LocalDateTime.now()
                    val duration = Duration.between(time1, time2)
                    time1=time2
                    val x= duration.seconds
                    storeinDB(x.toString())
                    isDriving=false
                    isWalking=false
                    isRunning=false

                }
                else if (acceleration < 25 && acceleration> 18 && !isRunning) {

                    isRunning = true
                    state="Running"
                    showToast(this, "RUNNING!")
                    // Do something when stillness is detected
                    var time2 = LocalDateTime.now()
                    val duration = Duration.between(time1, time2)
                    time1=time2
                    val x= duration.seconds
                    storeinDB(x.toString())
                    isDriving=false
                    isStill=false
                    isWalking=false


                }
                else if (acceleration > 25 && !isDriving) {

                    isDriving = true
                    state="Driving"
                    showToast(this, "DRIVING!")
                    // Do something when stillness is detected
                    var time2 = LocalDateTime.now()
                    val duration = Duration.between(time1, time2)
                    time1=time2
                    val x= duration.seconds
                    storeinDB(x.toString())
                    isWalking=false
                    isStill=false
                    isRunning=false


                }}
        }}
    @SuppressLint("NewApi")
    fun storeinDB(slot1: String){
        val db = DBHelper(this, null)

        // creating variables for values
        // in name and age edit texts
        val time = LocalDateTime.now().toString()
        val slot = slot1
        val activity = state
        // calling method to add
        // name to our database
        db.addName(time, slot, activity)

        // Toast to message on the screen
        Toast.makeText(this, state + " added to database with time  "+ slot+ " seconds ", Toast.LENGTH_LONG).show()


}}
