package com.engineerskasa.fickrbrowser

import android.os.AsyncTask
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception

class GetFlickrJsonData (private val listner: OnDataAvable) : AsyncTask<String, Void, ArrayList<Photo>>(){
    private val TAG = "GetFlickrJsonData"

    interface OnDataAvable{
        fun onDataAvailable(data: List<Photo>)
        fun onError(E: Exception)
    }

    override fun onPostExecute(result: ArrayList<Photo>) {
        listner.onDataAvailable(result)
        Log.d(TAG, ".onPostExecute; complete with data $result")
    }

    override fun doInBackground(vararg params: String): ArrayList<Photo> {
        val photoList = ArrayList<Photo>()

        try{
            val jsonData = JSONObject(params[0])
            val itemArray = jsonData.getJSONArray("items")

            for(i in 0 until itemArray.length()) {
                val jsonPhoto = itemArray.getJSONObject(i)
                val title = jsonPhoto.getString("title")
                val author = jsonPhoto.getString("author")
                val authorId = jsonPhoto.getString("author_id")
                val tags = jsonPhoto.getString("tags")

                val jsonMedia = jsonPhoto.getJSONObject("media")
                val photoUrl = jsonMedia.getString("m")
                val link = photoUrl.replaceFirst("_m.jpg", "_b.jpg ")

                val photoObject = Photo(title, author, authorId, link, tags, photoUrl)

                photoList.add(photoObject)
            }
        }catch (e: JSONException){
            e.printStackTrace()
            Log.e(TAG, ".doInBackground completed with error ${e.message}")
            cancel(true)
            listner.onError(e)
        }

        return photoList
    }
}