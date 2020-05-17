package com.alienpants.leafpicrevived.data;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.alienpants.leafpicrevived.R;
import com.alienpants.leafpicrevived.activities.SplashScreen;
import com.alienpants.leafpicrevived.data.sort.SortingMode;
import com.alienpants.leafpicrevived.data.sort.SortingOrder;
import com.alienpants.leafpicrevived.util.preferences.Prefs;
import com.orhanobut.hawk.Hawk;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.alienpants.leafpicrevived.data.MediaHelper.scanFile;
import static com.alienpants.leafpicrevived.util.BitmapUtils.addWhiteBorder;
import static com.alienpants.leafpicrevived.util.BitmapUtils.getCroppedBitmap;

/**
 * Created by dnld on 3/25/17.
 */

public class AlbumsHelper {

    public static void createShortcuts(Context context, List<Album> albums) {
        for (Album selectedAlbum : albums) {

            Intent shortcutIntent;
            shortcutIntent = new Intent(context, SplashScreen.class);
            shortcutIntent.setAction(SplashScreen.ACTION_OPEN_ALBUM);
            shortcutIntent.putExtra("albumPath", selectedAlbum.getPath());
            shortcutIntent.putExtra("albumId", selectedAlbum.getId());
            shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            Intent addIntent = new Intent();
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, selectedAlbum.getName());

            Media coverAlbum = selectedAlbum.getCover();
            File image = new File(coverAlbum.getPath());
            Bitmap bitmap = coverAlbum.isVideo() ? ThumbnailUtils.createVideoThumbnail(coverAlbum.getPath(), MediaStore.Images.Thumbnails.MINI_KIND)
                    : BitmapFactory.decodeFile(image.getAbsolutePath(), new BitmapFactory.Options());

            if (bitmap == null) {
                Toast.makeText(context, R.string.error_thumbnail, Toast.LENGTH_SHORT).show();
                // TODO: 12/31/16
                return;
            }

            bitmap = Bitmap.createScaledBitmap(getCroppedBitmap(bitmap), 128, 128, false);
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, addWhiteBorder(bitmap, 5));

            addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            context.sendBroadcast(addIntent);
        }
    }

    @NonNull
    public static SortingMode getSortingMode() {
        return Prefs.getAlbumSortingMode();
    }

    @NonNull
    public static SortingOrder getSortingOrder() {
        return Prefs.getAlbumSortingOrder();
    }

    public static void setSortingMode(@NonNull SortingMode sortingMode) {
        Prefs.setAlbumSortingMode(sortingMode);
    }

    public static void setSortingOrder(@NonNull SortingOrder sortingOrder) {
        Prefs.setAlbumSortingOrder(sortingOrder);
    }


    public static void hideAlbum(String path, Context context) {

        File dirName = new File(path);
        File file = new File(dirName, ".nomedia");
        if (!file.exists()) {
            try {
                FileOutputStream out = new FileOutputStream(file);
                out.flush();
                out.close();
                scanFile(context, new String[]{file.getAbsolutePath()});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void unHideAlbum(String path, Context context) {

        File dirName = new File(path);
        File file = new File(dirName, ".nomedia");
        if (file.exists()) {
            if (file.delete())
                scanFile(context, new String[]{file.getAbsolutePath()});
        }
    }

    public static boolean deleteAlbum(Album album, Context context) {
        return StorageHelper.deleteFilesInFolder(context, new File(album.getPath()));
    }

    public static void saveLastHiddenPaths(ArrayList<String> list) {
        Hawk.put("h", list);
    }

    public static ArrayList<String> getLastHiddenPaths() {
        return Hawk.get("h", new ArrayList<>());
    }

}
