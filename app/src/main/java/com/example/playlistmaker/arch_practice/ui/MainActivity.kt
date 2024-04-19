package com.example.playlistmaker.arch_practice.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.databinding.ActivityMain2Binding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding
    private lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[ViewModel::class.java]

        viewModel.liveData.observe(this) { value ->
            Log.d("button", "LiveData value: $value")
        }

        binding.button.setOnClickListener {
            viewModel.liveData.value = viewModel.liveData.value?.plus(1)
        }

        binding.button2.setOnClickListener {
            viewModel.liveData.value = viewModel.liveData.value?.minus(1)
        }
    }

    override fun onDestroy() {
        viewModel.liveData.observe(this) { value ->
            Log.d("onDestroy", "LiveData value: $value")
        }
        super.onDestroy()
    }

    override fun onRestart() {
        viewModel.liveData.observe(this) { value ->
            Log.d("onRestart", "LiveData value: $value")
        }
        super.onRestart()
    }
}
