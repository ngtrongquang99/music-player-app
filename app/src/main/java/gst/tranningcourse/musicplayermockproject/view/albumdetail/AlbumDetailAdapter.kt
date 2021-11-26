package gst.tranningcourse.musicplayermockproject.view.albumdetail

import android.media.MediaMetadataRetriever
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import gst.tranningcourse.musicplayermockproject.R
import gst.tranningcourse.musicplayermockproject.model.Album
import gst.tranningcourse.musicplayermockproject.model.Music

/**
 * class Album detail adapter for Album detail recycler view
 *
 * param:
 * musicList: for list of music in album
 * listener: for handle recycler view item click event
 */
class AlbumDetailAdapter(
    private val listAlbum: ArrayList<Album>,
    private val musicList: ArrayList<Music>,
    private val listener: OnAlbumDetailPlaylistItemClick
) : RecyclerView.Adapter<AlbumDetailAdapter.ViewHolder>() {

    /**
     * Class ViewHolder
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val albumImg: ImageView = itemView.findViewById(R.id.albumImg)
        val albumName: TextView = itemView.findViewById(R.id.albumName)
        val tvName: TextView = itemView.findViewById(R.id.tvNameMusicItem)
        val tvArtist: TextView = itemView.findViewById(R.id.tvArtistMusicItem)
        val img: ImageView = itemView.findViewById(R.id.imgMusicItem)
    }

    /**
     * create ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.list_music_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            albumName.text = listAlbum[position].name
            val albumImg = getImg(listAlbum[position].link)
            if(albumImg != null) {
                Glide.with(itemView).asBitmap()
                    .load(albumImg)
                    .into(holder.albumImg)
            } else {
                Glide.with(itemView).asBitmap()
                    .load(R.mipmap.ic_launcher)
                    .into(holder.albumImg)
            }

            /*tvName.text = musicList[position].name
            tvArtist.text = musicList[position].artist
            var musicImg = getImg(musicList[position].link)
            if(musicImg != null) {
                Glide.with(itemView).asBitmap()
                    .load(musicImg)
                    .into(holder.img)
            } else {
                Glide.with(itemView).asBitmap()
                    .load(R.mipmap.ic_launcher)
                    .into(holder.img)
            }*/

            /**
             * handle item click event
             */
            holder.itemView.setOnClickListener {
                listener.onAlbumDetailPlaylistItemClick(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return musicList.size
    }

    private fun getImg(uri: String): ByteArray? {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(uri)
        val art : ByteArray? = retriever.embeddedPicture
        retriever.release()
        return art
    }

    /**
     * Interface for handle item click event
     */
    interface OnAlbumDetailPlaylistItemClick {
        fun onAlbumDetailPlaylistItemClick(position: Int) {

        }
    }
}