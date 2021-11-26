package gst.tranningcourse.musicplayermockproject.view.musicplaylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import gst.tranningcourse.musicplayermockproject.R
import gst.tranningcourse.musicplayermockproject.model.Music
import gst.tranningcourse.musicplayermockproject.view.bottommusiccontrol.BottomMusicControlFragment
import java.util.Locale
import kotlin.collections.ArrayList

/**
 * Class MusicFragment for view list of music
 *
 * @param musicList: list of music
 */
class MusicFragment(private val musicList: ArrayList<Music>) : Fragment(),
    MusicAdapter.OnMusicPlaylistItemClick {
    var recyclerView: RecyclerView? = null
    private lateinit var adapter: MusicAdapter
    lateinit var arrList: ArrayList<Music>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_music_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        (activity as AppCompatActivity).supportActionBar?.title = "Songs list"

        recyclerView = view.findViewById(R.id.recyclerViewListMusic)
        arrList = arrayListOf()// for handle search view
        arrList.addAll(musicList)
        adapter = MusicAdapter(arrList, this)

        recyclerViewCustom()
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
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
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
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                arrList.clear()
                val searchText = newText!!.lowercase(Locale.getDefault())
                if (searchText.isNotEmpty()) {
                    musicList.forEach {
                        if (it.name?.lowercase(Locale.getDefault())
                                ?.contains(searchText) == true
                        ) {
                            arrList.add(it)
                        }
                    }
                    recyclerView?.adapter?.notifyDataSetChanged()
                } else {
                    arrList.clear()
                    arrList.addAll(musicList)
                    recyclerView?.adapter!!.notifyDataSetChanged()
                }
                return false
            }

        })
    }

    override fun onMusicPlaylistItemClick(position: Int, music: Music) {
        super.onMusicPlaylistItemClick(position, music)
        val bottomFragment = BottomMusicControlFragment(position, arrList)
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.bottomMusicControlFragment, bottomFragment)?.commit()
    }
}