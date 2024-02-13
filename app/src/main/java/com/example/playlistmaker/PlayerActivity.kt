package com.example.playlistmaker

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.model.Track

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra(Constants.TRACK_KEY, Track::class.java)
        } else {
            intent?.getParcelableExtra(Constants.TRACK_KEY)
        }

        val roundedCornersRadius = binding.coverIv.context.resources.getInteger(R.integer.glide_corner_radius_player)

        binding.trackName.text = track?.trackName
        binding.artistName.text = track?.artistName
        binding.currentTimeTv.text = track?.trackTime

        Glide.with(binding.coverIv)
            .load(track?.getCoverArtwork())
            .placeholder(R.drawable.ic_player_placeholder)
            .fitCenter()
            .transform(RoundedCorners(roundedCornersRadius))
            .into(binding.coverIv)

        binding.albumValueTv.text = track?.collectionName
        binding.yearValueTv.text = track?.getYearFromDate()
        binding.genreValueTv.text = track?.primaryGenreName
        binding.countryValueTv.text = track?.country
        binding.durationValueTv.text = track?.trackTime

    }

}
