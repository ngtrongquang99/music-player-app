package gst.trainingcourse.servicemockproject.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import gst.tranningcourse.musicplayermockproject.service.MyService

class MyBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val actionMusic = intent?.getIntExtra("action_music",-1)
        val intentService = Intent(context, MyService::class.java)
        intentService.putExtra("action_service_music",actionMusic)
        context?.startService(intentService)
    }
}