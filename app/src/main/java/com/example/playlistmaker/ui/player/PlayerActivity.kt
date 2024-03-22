package com.example.playlistmaker.ui.player

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.creator.Creator
import com.example.playlistmaker.Constants
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.player.PlayerPresenter
import com.example.playlistmaker.presentation.player.PlayerView

class PlayerActivity : AppCompatActivity(), PlayerView {

    private lateinit var presenter: PlayerPresenter

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

        presenter = Creator.providePlayerPresenter(track!!, MediaPlayer())
        presenter.attachView(this)
        presenter.timerRunnable.run()

        binding.playIv.setOnClickListener {
            presenter.playbackControl()
        }

    }

    override fun onPause() {
        super.onPause()
        presenter.pausePlayer()
    }

    override fun onResume() {
        super.onResume()
        presenter.startPlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun setTrackName(name: String) {
        binding.trackName.text = name
    }

    override fun setArtistName(name: String) {
        binding.artistName.text = name
    }

    override fun setTrackTime(time: String) {
        binding.durationValueTv.text = time
    }

    override fun setArtwork(url: String) {
        val roundedCornersRadius = binding.coverIv.context.resources.getInteger(R.integer.glide_corner_radius_player)
        Glide.with(binding.coverIv)
            .load(url.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.ic_player_placeholder)
            .fitCenter()
            .transform(RoundedCorners(roundedCornersRadius))
            .into(binding.coverIv)
    }

    override fun setCollection(name: String) {
        binding.albumValueTv.text = name
    }

    override fun setReleaseDate(date: String) {
        binding.yearValueTv.text = date
    }

    override fun setPrimaryGenre(name: String) {
        binding.genreValueTv.text = name
    }

    override fun setCountry(name: String) {
        binding.countryValueTv.text = name
    }

    override fun setPlayButtonImage(resId: Int) {
        binding.playIv.setImageResource(resId)
    }

    override fun setPlayTimeText(time: String) {
        binding.currentTimeTv.text = time
    }

}
