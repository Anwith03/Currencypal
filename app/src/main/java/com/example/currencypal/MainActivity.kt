package com.example.currencypal

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.json.JSONObject
import java.net.URL
import java.util.Locale
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var etAmount: EditText
    private lateinit var spinnerFrom: Spinner
    private lateinit var spinnerTo: Spinner
    private lateinit var btnSwap: ImageButton
    private lateinit var btnConvert: Button
    private lateinit var btnReset: Button
    private lateinit var tvResult: TextView
    private lateinit var progressBar: ProgressBar

    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        private const val PREFS_NAME = "CurrencyPalPrefs"
        private const val KEY_FROM_CURRENCY = "from_currency"
        private const val KEY_TO_CURRENCY = "to_currency"
        private const val KEY_LAST_RATE = "last_rate"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

        loadSavedState()

        btnSwap.setOnClickListener {
            val fromIndex = spinnerFrom.selectedItemPosition
            val toIndex = spinnerTo.selectedItemPosition
            spinnerFrom.setSelection(toIndex)
            spinnerTo.setSelection(fromIndex)
        }

        btnReset.setOnClickListener {
            etAmount.text.clear()
            tvResult.text = ""
        }

        btnConvert.setOnClickListener {
            performConversion()
        }
    }

    private fun initViews() {
        etAmount = findViewById(R.id.etAmount)
        spinnerFrom = findViewById(R.id.spinnerFrom)
        spinnerTo = findViewById(R.id.spinnerTo)
        btnSwap = findViewById(R.id.btnSwap)
        btnConvert = findViewById(R.id.btnConvert)
        btnReset = findViewById(R.id.btnReset)
        tvResult = findViewById(R.id.tvResult)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun loadSavedState() {
        val fromIndex = sharedPreferences.getInt(KEY_FROM_CURRENCY, 0)
        val toIndex = sharedPreferences.getInt(KEY_TO_CURRENCY, 1)

        spinnerFrom.setSelection(fromIndex)
        spinnerTo.setSelection(toIndex)
    }

    private fun performConversion() {
        val amountStr = etAmount.text.toString()
        if (amountStr.isEmpty()) {
            Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show()
            return
        }

        val amount = amountStr.toDoubleOrNull()
        if (amount == null) {
            Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show()
            return
        }

        val fromCurrency = spinnerFrom.selectedItem.toString()
        val toCurrency = spinnerTo.selectedItem.toString()

        if (fromCurrency == toCurrency) {
            tvResult.text = String.format(Locale.getDefault(), "%.2f %s", amount, toCurrency)
            return
        }

        progressBar.visibility = View.VISIBLE
        tvResult.text = ""
        btnConvert.isEnabled = false

        Executors.newSingleThreadExecutor().execute {
            try {
                // Using a free public API for live rates
                val url = "https://open.er-api.com/v6/latest/$fromCurrency"
                val response = URL(url).readText()
                val jsonObject = JSONObject(response)
                val rates = jsonObject.getJSONObject("rates")
                val rate = rates.getDouble(toCurrency)

                // Save successful rate and selections
                sharedPreferences.edit().apply {
                    putInt(KEY_FROM_CURRENCY, spinnerFrom.selectedItemPosition)
                    putInt(KEY_TO_CURRENCY, spinnerTo.selectedItemPosition)
                    putFloat("${fromCurrency}_${toCurrency}_$KEY_LAST_RATE", rate.toFloat())
                    apply()
                }

                val result = amount * rate

                Handler(Looper.getMainLooper()).post {
                    progressBar.visibility = View.GONE
                    btnConvert.isEnabled = true
                    tvResult.text = String.format(Locale.getDefault(), "%.2f %s", result, toCurrency)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                // Fetch last known rate on network failure
                val savedRate = sharedPreferences.getFloat("${fromCurrency}_${toCurrency}_$KEY_LAST_RATE", -1f)

                Handler(Looper.getMainLooper()).post {
                    progressBar.visibility = View.GONE
                    btnConvert.isEnabled = true

                    if (savedRate != -1f) {
                        val result = amount * savedRate
                        tvResult.text = String.format(Locale.getDefault(), "%.2f %s (Offline Rate)", result, toCurrency)
                        Toast.makeText(this@MainActivity, "Network error. Using last known rate.", Toast.LENGTH_SHORT).show()
                    } else {
                        tvResult.text = "Error fetching rate"
                        Toast.makeText(this@MainActivity, "Network error and no saved rate available.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
