package com.alienpants.leafpicrevived.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import androidx.core.content.FileProvider;

import com.commonsware.cwac.provider.LegacyCompatCursorWrapper;

import java.io.File;

/**
 * Created by dnld on 16/10/17.
 */

public class LegacyCompatFileProvider extends FileProvider {

    public static Uri getUri(Context context, File file) {
        return getUriForFile(context, ApplicationUtils.getPackageName() + ".provider", file);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return (new LegacyCompatCursorWrapper(super.query(uri, projection, selection, selectionArgs, sortOrder)));
    }
}
