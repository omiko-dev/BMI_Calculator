package com.example.bmicalculator

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlin.math.pow

class MainActivity : AppCompatActivity() {

    private lateinit var weightText: EditText
    private lateinit var heightText: EditText
    private lateinit var calculateBtn: Button
    private lateinit var bodyMassIndex: TextView
    private lateinit var bodyMassIndexMessage: TextView
    private lateinit var bodyMassIndexNormalRange: TextView

    private lateinit var sf: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // inputs
        weightText = findViewById(R.id.etWeight)
        heightText = findViewById(R.id.etHeight)

        // memorize data
        sf = getSharedPreferences("my_sf", MODE_PRIVATE)
        editor = sf.edit()

        // outputs
        bodyMassIndex = findViewById(R.id.etNum)
        bodyMassIndexMessage = findViewById(R.id.etMessage)
        bodyMassIndexNormalRange = findViewById(R.id.etNormalRang)

        // button
        calculateBtn = findViewById(R.id.calcButton)


        // calculate operation
        calculateBtn.setOnClickListener {
            if(!weightText.text.isEmpty() || !weightText.text.isEmpty()){
                val weight = weightText.text.toString().toFloat()
                val height = heightText.text.toString().toFloat()
//              Log.i("omiko", "this is Result ${calcBMI(weight, height)}")


                val result = calcBMI(weight, height)

                val resultInString = String.format("%.2f", result)
                bodyMassIndex.text = resultInString

                bodyMassIndexNormalRange.text = messageForRangeBMI(result)

                messageForBMI(result, bodyMassIndexMessage)
            }else{
                Toast.makeText(this@MainActivity, "Please fill out this Fields", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onPause() {
        super.onPause()

        val weight = weightText.text.toString().toFloat()
        val height = heightText.text.toString().toFloat()
        editor.apply {
            putFloat("sf_weight", weight)
            putFloat("sf_height", height)
            commit()
        }
    }

    override fun onResume() {
        super.onResume()

        val weight = sf.getFloat("sf_weight", 0f)
        val height = sf.getFloat("sf_height", 0f)

        weightText.setText(weight.toString())
        heightText.setText(height.toString())
    }




    private fun calcBMI(weight: Float, height: Float): Float = weight / height.pow(2)

    private fun messageForRangeBMI(res: Float): String {
        return when{
            res < 16.5 -> "Seriously Underweight (< 16.49)"
            res >= 16.5 && res < 18.5 -> "Underweight (16.5 - 18.49)"
            res >= 18.5 && res < 25 -> "Normal (18.5 - 24.99)"
            res >= 25 && res < 30 -> "Overweight (25 - 29.99)"
            res >= 30 -> "Obese (30 >)"
            else -> "0-0"
        }
    }

    private fun messageForBMI(res: Float, etView: TextView) {

        if(res < 18.5) {
            etView.text = "You Are Weak"
            etView.setTextColor(ContextCompat.getColor(this, android.R.color.holo_orange_light))
        }else if(res >= 18.5 && res < 30){
            etView.text = "You Are Healthy"
            etView.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_light))
        }else{
            etView.text = "You Are Overweight"
            etView.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_light))
        }

    }


}