package gst.tranningcourse.musicplayermockproject.view.musicplaylist

import android.media.MediaMetadataRetriever
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import gst.tranningcourse.musicplayermockproject.R
import gst.tranningcourse.musicplayermockproject.model.Music

/**
 * class MusicAdapter for list music recycler view
 * @param musicList for list all of music in storage
 * @param listener for handle item click to start music
 */
class MusicAdapter(
    private val musicList: ArrayList<Music>,
    private val listener: OnMusicPlaylistItemClick
) :
    RecyclerView.Adapter<MusicAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvNameMusicItem)
        val tvArtist: TextView = itemView.findViewById(R.id.tvArtistMusicItem)
        val img: ImageView = itemView.findViewById(R.id.imgMusicItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.list_music_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            tvName.text = musicList[position].name
            tvArtist.text = musicList[position].artist
            val musicImg = getImg(musicList[position].link)
            if(musicImg != null) {
                Glide.with(itemView).asBitmap()
                    .load(musicImg)
                    .into(holder.img)
            } else {
                Glide.with(itemView).asBitmap()
                    .load(R.mipmap.ic_launcher)
                    .into(holder.img)
            }
            /**
             * handle item click event
             */
            holder.itemView.setOnClickListener {
                listener.onMusicPlaylistItemClick(position, musicList[position])
            }
        }
    }

    private fun getImg(uri: String): ByteArray? {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(uri)
        val art : ByteArray? = retriever.embeddedPicture
        retriever.release()
        return art
    }

    override fun getItemCount(): Int {
        return musicList.size
    }

    /**
     * Interface OnMusicPlaylistItemClick
     */
    interface OnMusicPlaylistItemClick {

        /**
         * func to handle item click
         * @param music for music is playing
         */
        fun onMusicPlaylistItemClick(position: Int, music: Music) {

        }
    }
}