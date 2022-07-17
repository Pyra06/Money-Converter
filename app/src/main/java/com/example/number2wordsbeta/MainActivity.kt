package com.example.number2wordsbeta

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    lateinit var rupeeAmount: EditText
    private lateinit var resultBox: TextView
    lateinit var conversionBtn: Button
    lateinit var paiseAmount: EditText
    private lateinit var resetBtn: Button

    private val x10List = arrayOf("", " TEN", " TWENTY", " THIRTY", " FORTY", " FIFTY", " SIXTY", " SEVENTY", " EIGHTY", " NINETY")

    private val numList = arrayOf("", " ONE", " TWO", " THREE", " FOUR", " FIVE", " SIX", " SEVEN", " EIGHT", " NINE", " TEN", " ELEVEN", " TWELVE", " THIRTEEN", " FOURTEEN", " FIFTEEN", " SIXTEEN", " SEVENTEEN", " EIGHTEEN", " NINETEEN")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rupeeAmount = findViewById<View>(R.id.editText) as EditText
        resultBox = findViewById<View>(R.id.textView) as TextView
        conversionBtn = findViewById<View>(R.id.button) as Button
        paiseAmount = findViewById<View>(R.id.editText2) as EditText
        resetBtn = findViewById<View>(R.id.button2) as Button
        rupeeAmount.addTextChangedListener(textWatcher)
        paiseAmount.addTextChangedListener(textWatcher)

        conversionBtn.setOnClickListener {
            when {
                rupeeAmount.text.toString().isEmpty() -> {
                    resultBox.text = convertToPaise(paiseAmount.text.toString().toInt()) + " PAISE  ONLY"
                }
                paiseAmount.text.toString().isEmpty() -> {
                    resultBox.text = convertToRupee(rupeeAmount.text.toString().toInt()) + "  RUPEE  ONLY"
                }
                else -> {
                    resultBox.text = convertToRupee(rupeeAmount.text.toString().toInt()) + "  RUPEE" + "   AND   " + convertToPaise(paiseAmount.text.toString().toInt()) + " PAISE  ONLY"
                }
            }
        }

        resetBtn.setOnClickListener {
            resultBox.text = ""
            rupeeAmount.setText("")
            paiseAmount.setText("")
        }
    }

    private fun convertToHundreds(number: Int): String {
        var number: Int = number
        var currentResult: String
        if (number % 100 < 20) {
            currentResult = numList[number % 100]
            number /= 100
        } else {
            currentResult = numList[number % 10]
            number /= 10
            currentResult = x10List[number % 10] + currentResult
            number /= 10
        }
        return if (number == 0) currentResult
        else numList[number] + " HUNDRED - " + currentResult
    }

    private fun convertToRupee(number: Int): String {
        if (number == 0) {
            return "ZERO"
        }

        val numberConversion = DecimalFormat("0000000").format(number)

        val lakhs = numberConversion.substring(0, 2).toInt()
        val tenThousands = numberConversion.substring(2, 4).toInt()
        val thousands = numberConversion.substring(4, 7).toInt()
        //========================================================================================
        val lakhsConversion: String
        lakhsConversion = when (lakhs) {
            0 -> ""
            1 -> "ONE LAKH - "
            else -> convertToHundreds(lakhs) + " LAKH - "
        }
        var result = lakhsConversion
        //========================================================================================
        val tenThousandsConversion: String
        tenThousandsConversion = when (tenThousands) {
            0 -> ""
            1 -> "ONE THOUSAND - "
            else -> convertToHundreds(tenThousands) + " THOUSAND - "
        }
        result += tenThousandsConversion
        //========================================================================================
        val thousandsConversion: String
        thousandsConversion = convertToHundreds(thousands)
        result += thousandsConversion

        return result
    }

    private fun convertToPaise(number: Int): String {
        if (number == 0) {
            return "ZERO"
        }

        val numberConversion = DecimalFormat("00").format(number)

        val paise = numberConversion.substring(0, 2).toInt()
        val paiseConversion: String
        paiseConversion = convertToHundreds(paise)

        return paiseConversion
    }

    private var textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val rupees = rupeeAmount.text.toString()
            val paises = paiseAmount.text.toString()
            conversionBtn.isEnabled = rupees.isNotEmpty() || paises.isNotEmpty()
        }

        override fun afterTextChanged(s: Editable) {}
    }
}