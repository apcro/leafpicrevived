package com.alienpants.leafpicrevived.data.metadata;

import android.content.Context;

import com.alienpants.leafpicrevived.R;
import com.alienpants.leafpicrevived.adapters.AlbumsAdapter;
import com.alienpants.leafpicrevived.data.Album;
import com.alienpants.leafpicrevived.data.Media;
import com.alienpants.leafpicrevived.util.StringUtils;
import com.drew.imaging.ImageMetadataReader;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dnld on 4/10/17.
 */

public class MetadataHelper {

    public MediaDetailsMap<String, String> getMainDetails(Context context, Media m) {
        MediaDetailsMap<String, String> details = new MediaDetailsMap<>();
        details.put(context.getString(R.string.path), m.getDisplayPath());
        details.put(context.getString(R.string.type), m.getMimeType());
        if (m.getSize() != -1)
            details.put(context.getString(R.string.size), StringUtils.humanReadableByteCount(m.getSize(), true));
        // TODO should i add this always?
        details.put(context.getString(R.string.orientation), m.getOrientation() + "");
        MetaDataItem metadata = MetaDataItem.getMetadata(context, m.getUri());
        details.put(context.getString(R.string.resolution), metadata.getResolution());
        details.put(context.getString(R.string.date), SimpleDateFormat.getDateTimeInstance().format(new Date(m.getDateModified())));
        Date dateOriginal = metadata.getDateOriginal();
        if (dateOriginal != null)
            details.put(context.getString(R.string.date_taken), SimpleDateFormat.getDateTimeInstance().format(dateOriginal));

        String tmp;
        if ((tmp = metadata.getCameraInfo()) != null)
            details.put(context.getString(R.string.camera), tmp);
        if ((tmp = metadata.getExifInfo()) != null)
            details.put(context.getString(R.string.exif), tmp);
        GeoLocation location;
        if ((location = metadata.getLocation()) != null)
            details.put(context.getString(R.string.location), location.toDMSString());


        return details;
    }

    public MediaDetailsMap<String, String> getAllDetails(Context context, Media media) {
        MediaDetailsMap<String, String> data = new MediaDetailsMap<String, String>();
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(context.getContentResolver().openInputStream(media.getUri()));
            for (Directory directory : metadata.getDirectories()) {

                for (Tag tag : directory.getTags()) {
                    data.put(tag.getTagName(), directory.getObject(tag.getTagType()) + "");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public MediaDetailsMap<String, String> getFirstSelectedAlbumDetails(Context context, Album album) {
        MediaDetailsMap<String, String> albumdetials = new MediaDetailsMap<>();
        albumdetials.put(context.getString(R.string.name), album.getName());
        albumdetials.put(context.getString(R.string.type), context.getString(R.string.folder));
        albumdetials.put(context.getString(R.string.storage), album.getStorage(context));
        albumdetials.put(context.getString(R.string.free_space), StringUtils.humanReadableByteCount(album.getFile().getFreeSpace(), true));
        albumdetials.put(context.getString(R.string.path), album.getPath());
        albumdetials.put(context.getString(R.string.size), StringUtils.humanReadableByteCount(album.getFolderSize(album.getFile()), true));
        albumdetials.put(context.getString(R.string.files), album.getCount() + "");
        albumdetials.put(context.getString(R.string.date), SimpleDateFormat.getDateTimeInstance().format(new Date(album.getDateModified())));

        return albumdetials;
    }

    public MediaDetailsMap<String, String> getSelectedAlbumsDetails(Context context, AlbumsAdapter adapter) {
        MediaDetailsMap<String, String> albumdetials = new MediaDetailsMap<>();
        albumdetials.put(context.getString(R.string.type), context.getString(R.string.folder));
        albumdetials.put(context.getString(R.string.size), StringUtils.humanReadableByteCount(adapter.getSelectedAlbumsSize(), true));
        albumdetials.put(context.getString(R.string.folders), adapter.getSelectedCount() + "");
        albumdetials.put(context.getString(R.string.files), adapter.getSelectedAlbumsFilesCount() + "");
        return albumdetials;
    }

}
