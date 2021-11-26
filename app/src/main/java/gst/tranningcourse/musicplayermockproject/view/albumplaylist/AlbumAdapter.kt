package gst.tranningcourse.musicplayermockproject.view.albumplaylist

import android.media.MediaMetadataRetriever
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import gst.tranningcourse.musicplayermockproject.MainActivity.Companion.musicList
import gst.tranningcourse.musicplayermockproject.R
import gst.tranningcourse.musicplayermockproject.model.Album
import gst.tranningcourse.musicplayermockproject.model.Music

/**
 * class Album detail adapter for Album detail recycler view
 *
 * param:
 * listAlbum: for list of album
 * listener: for handle recycler view item click event
 */
class AlbumAdapter(
    private var listAlbum: ArrayList<Album>,
    private var listener: OnAlbumListOnClick
) : RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val albumImg: ImageView = itemView.findViewById(R.id.albumImg)
        val albumName: TextView = itemView.findViewById(R.id.albumName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.album_item_cardview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            albumName.text = listAlbum[position].name
            val albumImg = getImg(musicList[position].link)
            if(albumImg != null) {
                Glide.with(itemView).asBitmap()
                    .load(albumImg)
                    .into(holder.albumImg)
            } else {
                Glide.with(itemView).asBitmap()
                    .load(R.mipmap.ic_launcher)
                    .into(holder.albumImg)
            }

        }

        /**
         * handle item click
         */
        holder.itemView.setOnClickListener() {
            listener.onAlbumListOnClick(position/*,getMusicListByAlbum(listAlbum, position)*/)
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
        return listAlbum.size
    }

    /**
     * get list of music by album
     */
    /*private fun getMusicListByAlbum(listMusic: ArrayList<Album>, position: Int): ArrayList<Music> {
        var list: ArrayList<Music> = arrayListOf()
        for (music in listMusic) {
            if (music.album == listAlbum[position].name) {
                list.add(music)
            }
        }
        return list
    }*/

    /**
     * Interface OnAlbumListOnClick
     * to handle album list item click
     */
    interface OnAlbumListOnClick {
        fun onAlbumListOnClick(position: Int/*,list: ArrayList<Music>*/) {

        }
    }
}