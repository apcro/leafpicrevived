@file: JvmName("MediaUtils")

package com.alienpants.leafpic.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.alienpants.leafpic.R
import com.alienpants.leafpic.data.Media
import com.alienpants.leafpic.data.MediaHelper
import com.alienpants.leafpic.progress.ProgressBottomSheet
import java.util.*

/**
 * Share the given Media with an application.
 */
fun shareMedia(context: Context, mediaList: List<Media>) {
    val intent = Intent(Intent.ACTION_SEND_MULTIPLE)

    val types = HashMap<String, Int>()
    val files = ArrayList<Uri>()

    for (f in mediaList) {
        val mimeType = MimeTypeUtils.getTypeMime(f.mimeType)
        var count = 0
        if (types.containsKey(mimeType)) {
            count = types[mimeType]!!
        }
        types[mimeType] = count
        files.add(LegacyCompatFileProvider.getUri(context, f.file))
    }

    val fileTypes = types.keys
    if (fileTypes.size > 1) {
        Toast.makeText(context, R.string.waring_share_multiple_file_types, Toast.LENGTH_SHORT).show()
    }

    val max = -1
    var type: String? = null
    for (fileType in fileTypes) {
        val count = types[fileType]!!
        if (count > max) {
            type = fileType
        }
    }

    intent.type = type!! + "/*"

    intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files)
    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    context.startActivity(Intent.createChooser(intent, context.getString(R.string.send_to)))
}

fun deleteMedia(context: Context, mediaList: List<Media>, fragmentManager: FragmentManager, deleteListener: ProgressBottomSheet.Listener<Media>) {
    val sources = ArrayList<io.reactivex.Observable<Media>>(mediaList.size)
    for (media in mediaList)
        sources.add(MediaHelper.deleteMedia(context.applicationContext, media))

    val bottomSheet = ProgressBottomSheet.Builder<Media>(R.string.delete_bottom_sheet_title)
            .autoDismiss(false)
            .sources(sources)
            .listener(deleteListener)
            .build()

    bottomSheet.showNow(fragmentManager, null)
}
