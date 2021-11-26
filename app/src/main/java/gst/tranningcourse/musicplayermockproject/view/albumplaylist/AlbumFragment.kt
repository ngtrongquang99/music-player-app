package gst.tranningcourse.musicplayermockproject.view.albumplaylist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import gst.tranningcourse.musicplayermockproject.Communicator
import gst.tranningcourse.musicplayermockproject.MainActivity.Companion.musicList
import gst.tranningcourse.musicplayermockproject.R
import gst.tranningcourse.musicplayermockproject.model.Album
import java.util.Locale
import kotlin.collections.ArrayList

/**
 * class AlbumFragment for view
 * param: albumList for list of album
 */
class AlbumFragment : Fragment(), AlbumAdapter.OnAlbumListOnClick {

    companion object {
        private var listAlbum = ArrayList<Album>()
    }
    var recyclerView: RecyclerView? = null
    lateinit var albumAdapter: AlbumAdapter
    lateinit var arrList: ArrayList<Album>
    private lateinit var communicator: Communicator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.album_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        (activity as AppCompatActivity).supportActionBar?.title = "Albums list"

        recyclerView = view.findViewById(R.id.recyclerViewListAlbum)
        arrList = arrayListOf()// for handle search view
        arrList.addAll(getAlbum())
        albumAdapter = AlbumAdapter(arrList, this)
        recyclerViewCustom()
    }

    private fun getAlbum() : ArrayList<Album> {
        var p = 0
        for (i in 0 until musicList.size) {
            var c = 0
            for (j in 0 until p) {
                if (musicList[i].album == listAlbum[j].name) {
                    c++
                }
            }
            if (c == 0) {
                val tittle = musicList[i].album
                val img = musicList[i].link
                listAlbum.add(p, Album(tittle, img))
                p++
            }
        }
        return listAlbum
    }

    /**
     * fun recyclerViewCustom to customize recycler view and set adapter
     */
    private fun recyclerViewCustom() {
        recyclerView?.layoutManager = GridLayoutManager(context, 2)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.adapter = albumAdapter
    }

    /**
     * for create option menu with Search view
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.action_menu, menu)
        val search = menu.findItem(R.id.searchMenu)
        val searchView = search?.actionView as SearchView
        searchView.queryHint = "Search album!"

        /**
         * for create option menu with Search view
         */
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(newText: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                arrList.clear()
                val searchText = newText!!.lowercase(Locale.getDefault())
                if (searchText.isNotEmpty()) {
                    listAlbum.forEach {
                        if (it.name.lowercase(Locale.getDefault())
                                .contains(searchText)
                        ) {
                            arrList.add(it)
                        }
                    }
                    recyclerView?.adapter?.notifyDataSetChanged()
                } else {
                    arrList.clear()
                    arrList.addAll(listAlbum)
                    recyclerView?.adapter!!.notifyDataSetChanged()
                }
                return false
            }

        })
    }

    /**
     * to send list of music in album to Album Detail Fragment and get fragment appear
     */
    override fun onAlbumListOnClick(position: Int) {
        super.onAlbumListOnClick(position)

        communicator = activity as Communicator
        communicator.passData(listAlbum[position].name, listAlbum[position].link)
    }
}