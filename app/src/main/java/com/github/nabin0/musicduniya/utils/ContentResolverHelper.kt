package com.github.nabin0.musicduniya.utils

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.WorkerThread
import com.github.nabin0.audioplayer.models.Audio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

var globalAudioList: MutableStateFlow<List<Audio>> = MutableStateFlow(listOf())

class ContentResolverHelper(private val context: Context) {
    private var mCursor: Cursor? = null

    private val projection: Array<String> = arrayOf(
        MediaStore.Audio.AudioColumns.DISPLAY_NAME,
        MediaStore.Audio.AudioColumns._ID,
        MediaStore.Audio.AudioColumns.ARTIST,
        MediaStore.Audio.AudioColumns.DATA,
        MediaStore.Audio.AudioColumns.DURATION,
        MediaStore.Audio.AudioColumns.TITLE,
        MediaStore.Audio.AudioColumns.ALBUM_ID,
    )

    private var selectionClause: String? =
        "${MediaStore.Audio.AudioColumns.IS_MUSIC} = ? AND ${MediaStore.Audio.Media.MIME_TYPE} NOT IN (?, ?, ?)"
    private var selectionArg = arrayOf("1", "audio/amr", "audio/3gpp", "audio/aac")

    private val sortOrder = "${MediaStore.Audio.AudioColumns.DISPLAY_NAME} ASC"

    init {
        fetchLatestAudioList()
    }

    fun fetchLatestAudioList() {
        GlobalScope.launch(Dispatchers.IO) {
            globalAudioList.value = getAudioList()
        }
    }

    @WorkerThread
    fun getAudioData(): List<Audio> {
        return getCursorData()
    }


    private fun getCursorData(): MutableList<Audio> {
        val audioList = mutableListOf<Audio>()

        mCursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selectionClause,
            selectionArg,
            sortOrder
        )

        mCursor?.use { cursor ->
            val idColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns._ID)
            val displayNameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DISPLAY_NAME)
            val artistColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST)
            val dataColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATA)
            val durationColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DURATION)
            val titleColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE)
            val albumIdColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM_ID)

            cursor.apply {
                if (count == 0) {
                    Log.e("Cursor", "getCursorData: Cursor is Empty")
                } else {
                    while (cursor.moveToNext()) {
                        val displayName = getString(displayNameColumn)
                        val id = getLong(idColumn)
                        val artist = getString(artistColumn)
                        val data = getString(dataColumn)
                        val duration = getInt(durationColumn)
                        val title = getString(titleColumn)
                        val uri = ContentUris.withAppendedId(
                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                            id
                        )
                        val albumId = getLong(albumIdColumn)
                        val imagePath = Uri.parse("content://media/external/audio/albumart")
                        val imagePathUri = ContentUris.withAppendedId(imagePath, albumId)
                        try {
                            audioList += Audio(
                                audioUri = uri,
                                displayName = displayName,
                                id = id,
                                artist = artist, data = data,
                                title = title,
                                albumArtUrl = imagePathUri.toString(),
                                duration = duration
                            )
                        }catch (e: Exception){
                            e.printStackTrace()
                        }
                    }

                }
            }


        }

        return audioList
    }


    suspend fun getAudioList(): List<Audio> = withContext(Dispatchers.IO) {
        getAudioData()
    }

}


data class AudioData(
    val id: Long?,
    val audioUri: Uri?,
    val displayName: String?,
    val albumArtUrl: String?,
    val artist: String?,
    val title: String?,
    val data: String?,
    val duration: Int?,
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readParcelable(Uri::class.java.classLoader),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeParcelable(audioUri, flags)
        parcel.writeString(displayName)
        parcel.writeString(albumArtUrl)
        parcel.writeString(artist)
        parcel.writeString(title)
        parcel.writeString(data)
        parcel.writeValue(duration)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AudioData> {
        override fun createFromParcel(parcel: Parcel): AudioData {
            return AudioData(parcel)
        }

        override fun newArray(size: Int): Array<AudioData?> {
            return arrayOfNulls(size)
        }
    }
}