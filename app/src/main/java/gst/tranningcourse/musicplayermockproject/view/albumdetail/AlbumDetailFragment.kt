package gst.tranningcourse.musicplayermockproject.view.albumdetail

import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import gst.tranningcourse.musicplayermockproject.MainActivity.Companion.musicList
import gst.tranningcourse.musicplayermockproject.R
import gst.tranningcourse.musicplayermockproject.model.Album
import gst.tranningcourse.musicplayermockproject.model.Music
import gst.tranningcourse.musicplayermockproject.view.bottommusiccontrol.BottomMusicControlFragment
import java.util.Locale
import kotlin.collections.ArrayList

/**
 * Class AlbumDetailFragment for view list of music in album
 *
 * @param musicList: list of music in album
 */
class AlbumDetailFragment : Fragment(),
    AlbumDetailAdapter.OnAlbumDetailPlaylistItemClick {

    companion object {
        private var listAlbum = ArrayList<Album>()
    }

    var recyclerView: RecyclerView? = null
    private var albumName: String? = null
    private var albumMusic = ArrayList<Music>()
    private lateinit var adapter: AlbumDetailAdapter
    lateinit var arrList: ArrayList<Music>
    lateinit var arrAlbum: ArrayList<Album>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.album_detail_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        (activity as AppCompatActivity).supportActionBar?.title = "Songs by album list"

        val albumName = arguments?.getString("albumName")
        val albumImg = arguments?.getString("albumImg")
        arrAlbum.add(Album(albumName.toString(), albumImg.toString()))
        recyclerView = view.findViewById(R.id.recyclerViewListMusicByAlbum)
        arrList = arrayListOf() // for handle Search View
        arrList.addAll(getMusicListByAlbum())
        adapter = AlbumDetailAdapter(arrAlbum, arrList, this)

        recyclerViewCustom()
    }

    private fun getMusicListByAlbum() : ArrayList<Music> {
        albumName = arguments?.getString("albumName")
        var j = 0
        for (i in 0 until musicList.size) {
            if (albumName?.equals(musicList[i].album) == true) {
                albumMusic.add(j, musicList[i])
                j++
            }
        }
        return albumMusic
    }

    /**
     * fun recyclerViewCustom to customize recycler view and set adapter
     */
    private fun recyclerViewCustom() {
        recyclerView?.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.adapter = adapter
    }

    /**
     * for create option menu with Search view
     */
   /* override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.action_menu, menu)
        val search = menu.findItem(R.id.searchMenu)
        val searchView = search?.actionView as SearchView
        searchView.queryHint = "Search song!"

        /**
         * for update adapter every change of search view
         */
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(newText: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                arrList.clear()
                val searchText = newText!!.lowercase(Locale.getDefault())
                if (searchText.isNotEmpty()) {
                    albumMusic.forEach {
                        if (it.name?.lowercase(Locale.getDefault())
                                ?.contains(searchText) == true
                        ) {
                            arrList.add(it)
                        }
                    }
                    recyclerView?.adapter?.notifyDataSetChanged()
                } else {
                    arrList.clear()
                    arrList.addAll(albumMusic)
                    recyclerView?.adapter!!.notifyDataSetChanged()
                }
                return false
            }

        })
    }*/

    /**
     * handle recycler view item click to bottom music player appear
     */
    override fun onAlbumDetailPlaylistItemClick(position: Int) {
        super.onAlbumDetailPlaylistItemClick(position)
        val bottomFragment = BottomMusicControlFragment(position, albumMusic)
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.bottomMusicControlFragment, bottomFragment)?.commit()
    }
}