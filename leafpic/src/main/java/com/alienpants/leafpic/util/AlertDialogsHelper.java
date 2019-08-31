package com.alienpants.leafpic.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;

import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import com.alienpants.leafpic.adapters.AlbumsAdapter;
import com.alienpants.leafpic.data.Album;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.drew.lang.GeoLocation;
import com.orhanobut.hawk.Hawk;

import com.alienpants.leafpic.R;
import com.alienpants.leafpic.data.Media;
import com.alienpants.leafpic.data.metadata.MediaDetailsMap;
import com.alienpants.leafpic.data.metadata.MetadataHelper;
import org.horaapps.liz.ThemeHelper;
import org.horaapps.liz.ThemedActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import in.uncod.android.bypass.Bypass;

import static com.alienpants.leafpic.util.ServerConstants.LEAFPIC_CHANGELOG;

/**
 * Created by dnld on 19/05/16.
 */
public class AlertDialogsHelper {

    public static AlertDialog getInsertTextDialog(ThemedActivity activity, EditText editText, @StringRes int title) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity, activity.getDialogStyle());
        View dialogLayout = activity.getLayoutInflater().inflate(com.alienpants.leafpic.R.layout.dialog_insert_text, null);
        TextView textViewTitle = dialogLayout.findViewById(R.id.rename_title);

        ((CardView) dialogLayout.findViewById(com.alienpants.leafpic.R.id.dialog_chose_provider_title)).setCardBackgroundColor(activity.getCardBackgroundColor());
        textViewTitle.setBackgroundColor(activity.getPrimaryColor());
        textViewTitle.setText(title);
        ThemeHelper.setCursorColor(editText, activity.getTextColor());

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        editText.setLayoutParams(layoutParams);
        editText.setSingleLine(true);
        editText.getBackground().mutate().setColorFilter(activity.getTextColor(), PorterDuff.Mode.SRC_IN);
        editText.setTextColor(activity.getTextColor());

        try {
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(editText, null);
        } catch (Exception ignored) { }

        ((RelativeLayout) dialogLayout.findViewById(com.alienpants.leafpic.R.id.container_edit_text)).addView(editText);

        dialogBuilder.setView(dialogLayout);
        return dialogBuilder.create();
    }

    public static  AlertDialog getAdvancedSharingDialog(ThemedActivity activity, Uri uri, Intent advancedShare, int imgWidth, int imgHeight) {

        AlertDialog.Builder advancedSharingBuilder = new AlertDialog.Builder(activity, activity.getDialogStyle());
        View dialogLayout = activity.getLayoutInflater().inflate(com.alienpants.leafpic.R.layout.dialog_advanced_sharing, null);
        TextView textViewTitle = dialogLayout.findViewById(R.id.advanced_sharing_title);
        textViewTitle.setBackgroundColor(activity.getPrimaryColor());
        ((CardView) dialogLayout.findViewById(R.id.advanced_sharing_card)).setCardBackgroundColor(activity.getCardBackgroundColor());
        advancedSharingBuilder.setView(dialogLayout);

        final RadioGroup rGroup = dialogLayout.findViewById(R.id.radio_group_advanced_sharing);
        RadioButton rOriginal = dialogLayout.findViewById(R.id.radio_original);
        RadioButton r25lighter = dialogLayout.findViewById(R.id.radio_25lighter);
        RadioButton r50lighter = dialogLayout.findViewById(R.id.radio_50lighter);
        RadioButton r70lighter = dialogLayout.findViewById(R.id.radio_70lighter);

        String original = " ("+imgWidth+"x"+imgHeight+")";
        rOriginal.setText(rOriginal.getText()+original);
        r25lighter.setText(r25lighter.getText()+" ("+String.valueOf(getDim(imgWidth, 75))+"x"+String.valueOf(getDim(imgHeight, 75))+")");
        r50lighter.setText(r50lighter.getText()+" ("+String.valueOf(getDim(imgWidth, 50))+"x"+String.valueOf(getDim(imgHeight, 50))+")");
        r70lighter.setText(r70lighter.getText()+" ("+String.valueOf(getDim(imgWidth, 30))+"x"+String.valueOf(getDim(imgHeight, 30))+")");

        activity.themeRadioButton(rOriginal);
        activity.themeRadioButton(r25lighter);
        activity.themeRadioButton(r50lighter);
        activity.themeRadioButton(r70lighter);

        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radio_original:
                        advancedShare.putExtra(Intent.EXTRA_STREAM, uri);
                        break;
                    case R.id.radio_25lighter:
                        resizeImage(activity, uri, getDim(imgWidth, 75), getDim(imgHeight, 75), advancedShare);
                        break;
                    case R.id.radio_50lighter:
                        resizeImage(activity, uri,getDim(imgWidth, 50), getDim(imgHeight, 50), advancedShare);
                        break;
                    case R.id.radio_70lighter:
                        resizeImage(activity, uri,getDim(imgWidth, 30), getDim(imgHeight, 30), advancedShare);
                        break;
                }
            }
        });
        rOriginal.setChecked(true);
        return advancedSharingBuilder.create();
    }

    public static void resizeImage(Activity activity, Uri uri, int width_px, int height_px, Intent adv){
        Glide.with(activity)
                .asBitmap()
                .load(uri)
                .into(new SimpleTarget<Bitmap>(width_px, height_px) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap bitmap, Transition<? super Bitmap> transition) {
                        saveResizedImage(bitmap, activity, uri, adv);
                    }
                });
    }

    public static void saveResizedImage(Bitmap bitmap, Activity activity, Uri uri, Intent advancedShare){
        try {
            String fileName = uri.getLastPathSegment();
            File tempImage;
            OutputStream os;
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US);;
            if (fileName.toLowerCase().endsWith(".png"))
            {
                tempImage = File.createTempFile("img_" + dateFormatter.format(new Date()).toString(), ".png", activity.getApplicationContext().getCacheDir());
                os = new FileOutputStream(tempImage);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            }else{
                tempImage = File.createTempFile("img_" + dateFormatter.format(new Date()).toString(), ".jpg", activity.getApplicationContext().getCacheDir());
                os = new FileOutputStream(tempImage);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, os);
            }
            Uri newU = LegacyCompatFileProvider.getUri(activity, tempImage);
            advancedShare.putExtra(Intent.EXTRA_STREAM, newU);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e("resized image not saved", e.getMessage());
        }
    }

    public static int getDim (int number, int percent){
        return Math.round(number*percent/100);
    }

    public static AlertDialog getTextDialog(ThemedActivity activity, @StringRes int title, @StringRes int Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity,activity.getDialogStyle());
        View dialogLayout = activity.getLayoutInflater().inflate(R.layout.dialog_text, null);

        TextView dialogTitle = dialogLayout.findViewById(R.id.text_dialog_title);
        TextView dialogMessage = dialogLayout.findViewById(R.id.text_dialog_message);

        ((CardView) dialogLayout.findViewById(com.alienpants.leafpic.R.id.message_card)).setCardBackgroundColor(activity.getCardBackgroundColor());
        dialogTitle.setBackgroundColor(activity.getPrimaryColor());
        dialogTitle.setText(title);
        dialogMessage.setText(Message);
        dialogMessage.setTextColor(activity.getTextColor());
        builder.setView(dialogLayout);
        return builder.create();
    }

    public static AlertDialog getProgressDialog(final ThemedActivity activity,  String title, String message){
        AlertDialog.Builder progressDialog = new AlertDialog.Builder(activity, activity.getDialogStyle());
        View dialogLayout = activity.getLayoutInflater().inflate(com.alienpants.leafpic.R.layout.dialog_progress, null);
        TextView dialogTitle = dialogLayout.findViewById(R.id.progress_dialog_title);
        TextView dialogMessage = dialogLayout.findViewById(R.id.progress_dialog_text);

        dialogTitle.setBackgroundColor(activity.getPrimaryColor());
        ((CardView) dialogLayout.findViewById(com.alienpants.leafpic.R.id.progress_dialog_card)).setCardBackgroundColor(activity.getCardBackgroundColor());
        ((ProgressBar) dialogLayout.findViewById(com.alienpants.leafpic.R.id.progress_dialog_loading)).getIndeterminateDrawable().setColorFilter(activity.getPrimaryColor(), android.graphics
                .PorterDuff.Mode.SRC_ATOP);

        dialogTitle.setText(title);
        dialogMessage.setText(message);
        dialogMessage.setTextColor(activity.getTextColor());

        progressDialog.setCancelable(false);
        progressDialog.setView(dialogLayout);
        return progressDialog.create();
    }

    public static AlertDialog getDetailsDialog(final ThemedActivity activity, final Media f) {
        AlertDialog.Builder detailsDialogBuilder = new AlertDialog.Builder(activity, activity.getDialogStyle());
        MetadataHelper mdhelper = new MetadataHelper();
        MediaDetailsMap<String, String> mainDetails = mdhelper.getMainDetails(activity, f);
        final View dialogLayout = activity.getLayoutInflater().inflate(com.alienpants.leafpic.R.layout.dialog_media_detail, null);
        ImageView imgMap = dialogLayout.findViewById(R.id.photo_map);
        dialogLayout.findViewById(com.alienpants.leafpic.R.id.details_title).setBackgroundColor(activity.getPrimaryColor());
        ((CardView) dialogLayout.findViewById(com.alienpants.leafpic.R.id.photo_details_card)).setCardBackgroundColor(activity.getCardBackgroundColor());

        final GeoLocation location;
        if ((location = f.getGeoLocation()) != null) {

            StaticMapProvider staticMapProvider = StaticMapProvider.fromValue(
                    Hawk.get(activity.getString(R.string.preference_map_provider), StaticMapProvider.GOOGLE_MAPS.getValue()));

            Glide.with(activity.getApplicationContext())
                    .load(staticMapProvider.getUrl(location))
                    .into(imgMap);

            imgMap.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        activity.startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse(String.format(Locale.ENGLISH, "geo:%f,%f?z=%d", location.getLatitude(), location.getLongitude(), 17))));
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(activity, R.string.no_app_to_perform, Toast.LENGTH_SHORT).show();
                    }

                }
            });

            imgMap.setVisibility(View.VISIBLE);
            dialogLayout.findViewById(com.alienpants.leafpic.R.id.details_title).setVisibility(View.GONE);

        } else imgMap.setVisibility(View.GONE);

        final TextView showMoreText = dialogLayout.findViewById(R.id.details_showmore);
        showMoreText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMoreDetails(dialogLayout, activity, f);
                showMoreText.setVisibility(View.GONE);
            }
        });

        detailsDialogBuilder.setView(dialogLayout);
        loadDetails(dialogLayout,activity, mainDetails);
        return detailsDialogBuilder.create();
    }

    public static AlertDialog getAlbumDetailsDialog(final ThemedActivity activity, final Album album, final AlbumsAdapter adapter) {
        AlertDialog.Builder detailsDialogBuilder = new AlertDialog.Builder(activity, activity.getDialogStyle());
        MetadataHelper mdHelper = new MetadataHelper();
        MediaDetailsMap<String, String> mainAlbumDetails;
        if(adapter.getSelectedCount() > 1){
            mainAlbumDetails = mdHelper.getSelectedAlbumsDetails(activity, adapter);}
        else {
            mainAlbumDetails = mdHelper.getFirstSelectedAlbumDetails(activity, album);
        }
        final View dialogLayout = activity.getLayoutInflater().inflate(com.alienpants.leafpic.R.layout.dialog_media_detail, null);
        dialogLayout.findViewById(com.alienpants.leafpic.R.id.details_title).setBackgroundColor(activity.getPrimaryColor());
        ((CardView) dialogLayout.findViewById(com.alienpants.leafpic.R.id.photo_details_card)).setCardBackgroundColor(activity.getCardBackgroundColor());

        detailsDialogBuilder.setView(dialogLayout);
        loadDetails(dialogLayout, activity, mainAlbumDetails);
        return detailsDialogBuilder.create();
    }

    private static void loadDetails(View dialogLayout, ThemedActivity activity, MediaDetailsMap<String, String> metadata) {
        LinearLayout detailsTable = dialogLayout.findViewById(R.id.ll_list_details);

        int tenPxInDp = Measure.pxToDp (10, activity);

        for (int index : metadata.getKeySet()) {
            LinearLayout row = new LinearLayout(activity.getApplicationContext());
            row.setOrientation(LinearLayout.VERTICAL);

            TextView label = new TextView(activity.getApplicationContext());
            TextView value = new TextView(activity.getApplicationContext());
            label.setText(metadata.getLabel(index));
            label.setLayoutParams((new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)));
            value.setText(metadata.getValue(index));
            value.setLayoutParams((new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)));
            label.setTextColor(activity.getTextColor());
            label.setTypeface(null, Typeface.BOLD);
            label.setGravity(Gravity.START);
            label.setTextSize(16);
            label.setPaddingRelative(tenPxInDp, tenPxInDp, tenPxInDp, tenPxInDp);
            value.setTextColor(activity.getTextColor());
            value.setTextSize(16);
            value.setPaddingRelative(tenPxInDp, tenPxInDp, tenPxInDp, tenPxInDp);
            row.addView(label);
            row.addView(value);
            detailsTable.addView(row, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }
    }

    private static void showMoreDetails(View dialogLayout, ThemedActivity activity, Media media) {
        MediaDetailsMap<String, String> metadata = new MediaDetailsMap<>();//media.getAllDetails();
        loadDetails(dialogLayout ,activity , metadata);
    }

    public static AlertDialog showChangelogDialog(final ThemedActivity activity) {
        final AlertDialog.Builder changelogDialogBuilder = new AlertDialog.Builder(activity, activity.getDialogStyle());
        View dialogLayout = activity.getLayoutInflater().inflate(R.layout.dialog_changelog, null);

        TextView dialogTitle = dialogLayout.findViewById(R.id.dialog_changelog_title);
        TextView dialogMessage = dialogLayout.findViewById(R.id.dialog_changelog_text);
        CardView cvBackground = dialogLayout.findViewById(R.id.dialog_changelog_card);
        ScrollView scrChangelog = dialogLayout.findViewById(R.id.changelog_scrollview);

        cvBackground.setCardBackgroundColor(activity.getCardBackgroundColor());
        dialogTitle.setBackgroundColor(activity.getPrimaryColor());
        activity.getThemeHelper().setScrollViewColor(scrChangelog);

        dialogTitle.setText(activity.getString(R.string.changelog));

        Bypass bypass = new Bypass(activity);

        String markdownString;
        try {
            markdownString = getChangeLogFromAssets(activity);
        } catch (IOException e) {
            ChromeCustomTabs.launchUrl(activity, LEAFPIC_CHANGELOG);
            return null;
        }
        CharSequence string = bypass.markdownToSpannable(markdownString);
        dialogMessage.setText(string);
        dialogMessage.setMovementMethod(LinkMovementMethod.getInstance());
        dialogMessage.setTextColor(activity.getTextColor());

        changelogDialogBuilder.setView(dialogLayout);
        changelogDialogBuilder.setPositiveButton(activity.getString(R.string.ok_action).toUpperCase(), null);
        changelogDialogBuilder.setNeutralButton(activity.getString(R.string.show_full).toUpperCase(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ChromeCustomTabs.launchUrl(activity, LEAFPIC_CHANGELOG);
            }
        });
        return changelogDialogBuilder.show();
    }

    private static String getChangeLogFromAssets(Context context) throws IOException {
        InputStream inputStream = context.getAssets().open("latest_changelog.md");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        int i;
        while ((i = inputStream.read()) != -1)
            outputStream.write(i);

        inputStream.close();
        return outputStream.toString();
    }
}
