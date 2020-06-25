package com.engineerskasa.fickrbrowser

import android.os.Parcel
import android.os.Parcelable


class Photo(var title: String, var author: String, var authorId: String, var link: String, var tags: String,
            var image: String) : Parcelable {

    //Serializable UID
//    companion object{
//        private const val serialVersionUID = 1L
//    }

    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun toString(): String {
        return "Photo(title='$title', author='$author', authorId='$authorId', link='$link', tags='$tags', image='$image')"
    }


    //Codes to implemenent serializable properly
//    @Throws (IOException::class)
//    private fun writeObject(outStream: java.io.ObjectOutputStream){
//        Log.d("Photo", ".writeObject: called")
//        outStream.writeUTF(title)
//        outStream.writeUTF(author)
//        outStream.writeUTF(authorId)
//        outStream.writeUTF(link)
//        outStream.writeUTF(tags)
//        outStream.writeUTF(image)
//    }
//
//    @Throws (IOException::class, ClassNotFoundException::class)
//    private fun readObject(inStream: java.io.ObjectInputStream){
//        title = inStream.readUTF()
//        author = inStream.readUTF()
//        authorId = inStream.readUTF()
//        link = inStream.readUTF()
//        tags = inStream.readUTF()
//        image = inStream.readUTF()
//    }
//
//    @Throws (ObjectStreamException::class)
//    private fun readObjectNoData(){
//        Log.d("Photo", "readObjectNoData called")
//    }
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(author)
        parcel.writeString(authorId)
        parcel.writeString(link)
        parcel.writeString(tags)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Photo> {
        override fun createFromParcel(parcel: Parcel): Photo {
            return Photo(parcel)
        }

        override fun newArray(size: Int): Array<Photo?> {
            return arrayOfNulls(size)
        }
    }
}