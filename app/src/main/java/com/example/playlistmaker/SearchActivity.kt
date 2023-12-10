package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import com.google.android.material.appbar.MaterialToolbar

class SearchActivity : AppCompatActivity() {

    private lateinit var inputEditText: EditText
    private val SEARCH_QUERY_KEY = "searchQuery"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val inputEditText = findViewById<EditText>(R.id.searchInputTextField)
        val clearButton = findViewById<ImageView>(R.id.clearButton)
        val toolbar = findViewById<MaterialToolbar>(R.id.searchToolbar)

        if (savedInstanceState != null) {
            val savedSearchQuery = savedInstanceState.getString(SEARCH_QUERY_KEY)
            inputEditText.setText(savedSearchQuery)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {}

        }
        inputEditText.addTextChangedListener(simpleTextWatcher)

        clearButton.setOnClickListener {
            inputEditText.setText("")
            val view: View? = this.currentFocus
            if (view != null) {
                val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY_KEY, inputEditText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val savedSearchQuery = savedInstanceState.getString(SEARCH_QUERY_KEY)
        inputEditText.setText(savedSearchQuery)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

}
