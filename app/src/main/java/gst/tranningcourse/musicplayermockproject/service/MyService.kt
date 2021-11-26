package gst.tranningcourse.musicplayermockproject.service

import android.annotation.SuppressLint
import android.app.Application
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.os.*
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import gst.trainingcourse.servicemockproject.broadcast.MyBroadcast
import gst.tranningcourse.musicplayermockproject.R
import gst.tranningcourse.musicplayermockproject.model.Music
import gst.tranningcourse.musicplayermockproject.utils.Constains
import gst.tranningcourse.musicplayermockproject.utils.Constains.CHANNEL_ID
import gst.tranningcourse.musicplayermockproject.view.bottommusiccontrol.BottomMusicControlFragment
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class MyService : Service(){
    private lateinit var mediaPlayer: MediaPlayer
    private val musicBind: IBinder = MusicBinder()
    private var listMusic: ArrayList<Music> = arrayListOf()
    private var songName: String = ""
    private var songArtist: String = ""
    private var position = 0

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
        initMusicPlayer()
    }

    private fun initMusicPlayer() {
        mediaPlayer.setWakeMode(applicationContext, PowerManager.PARTIAL_WAKE_LOCK);
    }

    fun setListMusic(listMs: ArrayList<Music>) {
        listMusic = listMs
    }

    fun setPositionSong(musicPosition: Int) {
        position = musicPosition
    }

    fun playSong() {
        mediaPlayer.reset()
        val playMusic = listMusic[position]
        songName = listMusic[position].name.toString()
        songArtist = listMusic[position].artist.toString()
        mediaPlayer = MediaPlayer.create(applicationContext, playMusic.link.toUri())
        mediaPlayer.start()
    }

    fun nextSong() {
        position++
        if (position >= listMusic.size) {
            position = 0
        }
        playSong()
        sendNotification()
    }

    fun previousSong() {
        position--
        if (position < 0) {
            position = listMusic.size - 1
        }
        playSong()
        sendNotification()
    }

    fun getDuration() : Int{
        return mediaPlayer.duration
    }

    fun seekTo(progress : Int){
        mediaPlayer.seekTo(progress)
    }

    fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    inner class MusicBinder : Binder() {
        fun getService(): MyService {
            return this@MyService
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        return musicBind
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        sendNotification()
        val actionMusic = intent?.getIntExtra("action_service_music", -1)
        handleActionMusic(actionMusic!!)

        return START_NOT_STICKY
    }

    fun sendNotification() {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(listMusic[position].link)
        val art : ByteArray? = retriever.embeddedPicture
        val opt = BitmapFactory.Options()
        opt.inSampleSize = 2
        val bitMap: Bitmap =
            BitmapFactory.decodeByteArray(art,0,art!!.size,opt)
        val mediaSessionCompat = MediaSessionCompat(this, "tag")
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.disc)
                .setSubText("ToanVan")
                .setContentTitle(songName)
                .setContentText(songArtist)
                .setLargeIcon(bitMap)
                .setStyle(
                    androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(1)
                        .setMediaSession(mediaSessionCompat.sessionToken)
                )

        if (mediaPlayer.isPlaying) {
            notificationBuilder
                .addAction(
                    R.drawable.prev_icon,
                    "Previous",
                    getPendingIntent(this, Constains.ACTION_PREV)
                )
                .addAction(
                    R.drawable.pause_icon,
                    "Pause",
                    getPendingIntent(this, Constains.ACTION_PAUSE)
                )
                .addAction(
                    R.drawable.next_icon,
                    "Next",
                    getPendingIntent(this, Constains.ACTION_NEXT)
                )
        } else {
            notificationBuilder
                .addAction(
                    R.drawable.prev_icon,
                    "Previous",
                    getPendingIntent(this, Constains.ACTION_PREV)
                )
                .addAction(
                    R.drawable.play_icon,
                    "RESUME",
                    getPendingIntent(this, Constains.ACTION_RESUME)
                )
                .addAction(
                    R.drawable.next_icon,
                    "Next",
                    getPendingIntent(this, Constains.ACTION_NEXT)
                )
        }
        val notification = notificationBuilder.build()
        startForeground(1, notification)
    }

    private fun getPendingIntent(context: Context, action: Int): PendingIntent {
        val intent = Intent(this, MyBroadcast::class.java)
        intent.putExtra("action_music", action)
        return PendingIntent.getBroadcast(
            context,
            action,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun handleActionMusic(action: Int) {
        when (action) {
            Constains.ACTION_PAUSE -> {
                sendActionToBottomMusicFragmentAndPlayMusicActivity(Constains.ACTION_PAUSE)
            }
            Constains.ACTION_RESUME -> {
                sendActionToBottomMusicFragmentAndPlayMusicActivity(Constains.ACTION_RESUME)
            }
            Constains.ACTION_NEXT -> {
                sendActionToBottomMusicFragmentAndPlayMusicActivity(Constains.ACTION_NEXT)
            }
            Constains.ACTION_PREV -> {
                sendActionToBottomMusicFragmentAndPlayMusicActivity(Constains.ACTION_PREV)
            }
        }
    }

    fun resumeMusic() {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
            sendNotification()
            sendActionToBottomMusicFragmentAndPlayMusicActivity(Constains.ACTION_RESUME)
        }
    }

    fun pauseMusic() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            sendNotification()
            sendActionToBottomMusicFragmentAndPlayMusicActivity(Constains.ACTION_PAUSE)
        }
    }

    fun startMusic() {
        mediaPlayer.start()
        sendNotification()
    }

    fun moveToTheNextPost() {
        mediaPlayer.setOnCompletionListener {
            val sharedPreferencesLoop = getSharedPreferences("checkLoop",MODE_PRIVATE)
            val checkLoop = sharedPreferencesLoop.getBoolean("loop", false)
            println(checkLoop)
            val sharedPreferencesRandom = getSharedPreferences("checkRandom", MODE_PRIVATE)
            val checkRandom = sharedPreferencesRandom.getBoolean("random", false)

            if (checkRandom) {
                val random = Random()
                position = random.nextInt(listMusic.size)
                playSong()
                BottomMusicControlFragment.myService.sendNotification()
                sendActionToBottomMusicFragmentAndPlayMusicActivity(Constains.ACTION_RANDOM)
            }
            if (checkLoop) {
                playSong()
            }
            if (!checkLoop && !checkRandom){
                sendActionToBottomMusicFragmentAndPlayMusicActivity(Constains.ACTION_NEXT)
            }
        }
    }

    private fun sendActionToBottomMusicFragmentAndPlayMusicActivity(action: Int) {
        val intent = Intent("send_data_to_fragment_and_activity")
        val bundle = Bundle()
        bundle.putInt("action", action)
        bundle.putInt("positionCurrentSong",position)
        intent.putExtras(bundle)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }
}