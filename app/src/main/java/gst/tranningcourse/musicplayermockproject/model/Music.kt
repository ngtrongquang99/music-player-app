package gst.tranningcourse.musicplayermockproject.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable
import kotlinx.parcelize.Parcelize

/**
 * data class Music
 *
 * name: music name
 * artist: music artist
 * img: music Image source
 * duration: music duration for seekbar text view
 * link: music link from storage
 * album: music album
 */
data class Music(
    val name: String?,
    val artist: String,
    val link: String,
    val album: String
) : Serializable
