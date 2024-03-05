package com.example.playlistmaker

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.model.Track
import java.text.SimpleDateFormat
import java.util.*

class PlayerActivity : AppCompatActivity() {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 400L
    }

    private var timerHandler: Handler? = null

    private var playerState = STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()

    private lateinit var binding: ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        timerHandler = Handler(Looper.getMainLooper())

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
        binding.currentTimeTv.text = getPosition()

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

        preparePlayer(track!!.previewUrl)

        binding.playIv.setOnClickListener {
            playbackControl()
        }

    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onResume() {
        super.onResume()
        startPlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private val timerRunnable = object : Runnable {
        override fun run() {
            val formattedTime = getPosition()
            getPosition()
            binding.currentTimeTv.text = formattedTime
            timerHandler?.postDelayed(this, DELAY)
            print(formattedTime)
        }
    }

    private fun getPosition(): String? {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
    }

    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            binding.playIv.setImageDrawable(getDrawable(R.drawable.ic_play))
            playerState = STATE_PREPARED
            timerHandler?.removeCallbacks(timerRunnable)
            binding.currentTimeTv.text = getString(R.string._00_00)
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        binding.playIv.setImageDrawable(getDrawable(R.drawable.ic_pause))
        playerState = STATE_PLAYING
        timerHandler?.post(timerRunnable)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        binding.playIv.setImageDrawable(getDrawable(R.drawable.ic_play))
        playerState = STATE_PAUSED
        timerHandler?.removeCallbacks(timerRunnable)
    }

}
