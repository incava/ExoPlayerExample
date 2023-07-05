package com.incava.exoplayerexample

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import com.incava.exoplayerexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val manualBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    var manualPlayer : ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(manualBinding.root)
        manualPlay()
    }

    override fun onStart() {
        super.onStart()
        initSetManualPlayer()
    }

    override fun onStop() {
        super.onStop()
        manualStop()
    }

    override fun onResume() {
        super.onResume()
        manualPlay()
    }

    fun getRawFilePath(path: String): Uri {
        val rawResourceDataSource = RawResourceDataSource(this@MainActivity)
        val resID: Int =
            this@MainActivity.resources.getIdentifier(path.split(".")[0], "raw", this@MainActivity.packageName)
        rawResourceDataSource.open(DataSpec(RawResourceDataSource.buildRawResourceUri(resID)))
        return rawResourceDataSource.uri!!
    }

    fun manualPlay(){
        val mediaItem1 = MediaItem.fromUri(getRawFilePath("frozen"))
        manualPlayer?.setMediaItem(mediaItem1)
        manualPlayer?.seekToDefaultPosition()
        manualPlayer?.prepare()
        manualPlayer?.play()
    }
    fun manualStop(){
        manualPlayer?.stop()
       // manualBinding.playerManual.player?.playWhenReady = false
    }

    // 사용 방법이 담긴 플레이어 설정
    fun initSetManualPlayer() {
        if (manualPlayer != null) return
        manualPlayer = ExoPlayer.Builder(this).build() // 메뉴얼 플레이어.
            .also {exoPlayer->
                manualBinding.playerManual.player = exoPlayer
            }
        manualPlayer?.apply {
            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    if (state == Player.STATE_ENDED) {
                        //끝난 뒤, 멈추고 레이아웃 보이지 않게 설정.
                        manualStop()
                    }
                }
                override fun onPlayerError(error: PlaybackException) {
                    super.onPlayerError(error)
                    Log.i("isPlay","error")
                }
            })
        }
    }
}