package gst.tranningcourse.musicplayermockproject

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import gst.tranningcourse.musicplayermockproject.model.Music
import gst.tranningcourse.musicplayermockproject.utils.Constains.REQUEST_CODE
import gst.tranningcourse.musicplayermockproject.view.albumdetail.AlbumDetailFragment
import gst.tranningcourse.musicplayermockproject.view.albumplaylist.AlbumFragment
import gst.tranningcourse.musicplayermockproject.view.musicplaylist.MusicFragment

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, Communicator {
    var navigationView: NavigationView? = null
    var drawerLayout: DrawerLayout? = null
    var toggle: ActionBarDrawerToggle? = null

    companion object {
        var musicList: ArrayList<Music> = arrayListOf()
    }
//    private var musicList: ArrayList<Music> = arrayListOf() // for get all music in storage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        customNavView()
        permission()
        navigationView?.setNavigationItemSelectedListener(this)
    }

    private fun permission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_CODE
            )
        } else {
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
            musicList = getAllMusic()
            val musicFragment = MusicFragment(musicList)
            supportFragmentManager.beginTransaction().replace(R.id.mainView, musicFragment)
                .commit()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
            val musicFragment = MusicFragment(musicList)
            supportFragmentManager.beginTransaction().replace(R.id.mainView, musicFragment)
                .commit()
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show()
        }
    }

    fun getAllMusic(): ArrayList<Music> {
        val tempMusicList = arrayListOf<Music>()
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection: Array<String> = arrayOf(
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ARTIST
        )
        val cursor = contentResolver.query(uri, projection, null, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val album = cursor.getString(0)
                val title = cursor.getString(1)
                val duration = cursor.getString(2)
                val link = cursor.getString(3)
                val artist = cursor.getString(4)
                val music = Music(title, artist, link, album)
                tempMusicList.add(music)
            }
            cursor.close()
        }
        return tempMusicList
    }

    /**
     * init view
     */
    private fun init() {
        navigationView = findViewById(R.id.navView)
        drawerLayout = findViewById(R.id.drawer_layout)

    }

    /**
     * custom side bar
     */
    private fun customNavView() {
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout?.addDrawerListener(toggle!!)
        toggle!!.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    /**
     * for replace view with fragment by choosing item in side bar
     */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.menuPlaylist) {
            val musicFragment = MusicFragment(musicList)
            supportFragmentManager.beginTransaction().replace(R.id.mainView, musicFragment)
                .commit()
        } else if (id == R.id.menuAlbum) {
            val albumFragment = AlbumFragment()
            supportFragmentManager.beginTransaction().replace(R.id.mainView, albumFragment)
                .commit()
        }
        drawerLayout?.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle!!.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun passData(albumName: String, albumImg: String) {
        val bundle = Bundle()

        bundle.putString("albumName", albumName)
        bundle.putString("albumImg", albumImg)
        val albumDetailFragment = AlbumDetailFragment()
        albumDetailFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainView, albumDetailFragment).addToBackStack(null).commit()
    }

}