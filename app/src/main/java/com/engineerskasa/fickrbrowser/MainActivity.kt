package com.engineerskasa.fickrbrowser

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.content_main.*
import java.lang.Exception

class MainActivity : BaseActivity(), GetRawData.OnDownloadComplete,
        GetFlickrJsonData.OnDataAvable,
        RecyclerItemClickListener.OnRecyclerItemClicked {

    private val TAG = "MainActivity"
    private val flickrRecyclerViewAdapter = FlickrRecyclerViewDapter(ArrayList())

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activateToolbar(false)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.addOnItemTouchListener(RecyclerItemClickListener(this, recyclerview, this))
        recyclerview.adapter = flickrRecyclerViewAdapter

        Log.d(TAG, "onCreate ended")
    }

    private fun createUri(baseUrl: String, tags: String, lang: String, matchAll: Boolean): String{

        return Uri.parse(baseUrl)
            .buildUpon()
            .appendQueryParameter("tags", tags)
            .appendQueryParameter("lang", lang)
            .appendQueryParameter("tagmode", if(matchAll) "ALL" else "ANY")
            .appendQueryParameter("format","json")
            .appendQueryParameter("nojsoncallback", "1")
            .build().toString()
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                startActivity(Intent(this, SearchActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDownloadComplete(data: String, status: DownloadStatus){
        if(status == DownloadStatus.OK){
            Log.d(TAG, "onDownloadComplete and data is $data")
            val getFlickrJsonData = GetFlickrJsonData(this)
            getFlickrJsonData.execute(data)
        }else{
            Log.d(TAG, "onDownloadComplete download failed with status $status and error message is $data")
        }
    }

    override fun onDataAvailable(data: List<Photo>) {
        Log.d(TAG, "onDataAvailable called")
        flickrRecyclerViewAdapter.loadNewData(data)
    }

    override fun onError(E: Exception) {
        Log.e(TAG, ".onError with error ${E.message}")
    }

    override fun onItemClicked(view: View, position: Int) {
        Log.d(TAG, ".onItemClicked: Item at $position clicked")
    }

    override fun onItemLongClicked(view: View, position: Int) {
        Log.d(TAG, ".onItemLongClicked: Item at $position long clicked")

        val photo = flickrRecyclerViewAdapter.getPhoto(position)
        if(photo != null){
            val intent = Intent(this, PhotoDetailsActivity::class.java)
            intent.putExtra(PHOTO_TRANSFER, photo)
            startActivity(intent)
        }
    }

    override fun onResume() {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val resultQuery = sharedPref.getString(FLICKR_QUERY,"")
        if(resultQuery != null){
            val url = createUri("https://www.flickr.com/services/feeds/photos_public.gne", resultQuery, "en-us", true)
            val getRawData = GetRawData(this)
            getRawData.execute(url)
        }
        super.onResume()
    }
}