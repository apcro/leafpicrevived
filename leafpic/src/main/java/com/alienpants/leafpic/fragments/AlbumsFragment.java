package com.alienpants.leafpic.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alienpants.leafpic.util.AnimationUtils;
import com.mikepenz.iconics.typeface.library.googlematerial.GoogleMaterial;
import com.orhanobut.hawk.Hawk;

import com.alienpants.leafpic.R;
import com.alienpants.leafpic.adapters.AlbumsAdapter;
import com.alienpants.leafpic.data.Album;
import com.alienpants.leafpic.data.AlbumsHelper;
import com.alienpants.leafpic.data.HandlingAlbums;
import com.alienpants.leafpic.data.MediaHelper;
import com.alienpants.leafpic.data.provider.CPHelper;
import com.alienpants.leafpic.data.sort.SortingMode;
import com.alienpants.leafpic.data.sort.SortingOrder;
import com.alienpants.leafpic.progress.ProgressBottomSheet;
import com.alienpants.leafpic.util.AlertDialogsHelper;
import com.alienpants.leafpic.util.DeviceUtils;
import com.alienpants.leafpic.util.Measure;
import com.alienpants.leafpic.util.Security;
import com.alienpants.leafpic.util.preferences.Prefs;
import com.alienpants.leafpic.views.GridSpacingItemDecoration;
import org.horaapps.liz.ThemeHelper;
import org.horaapps.liz.ThemedActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.recyclerview.animators.LandingAnimator;

/**
 * Created by dnld on 3/13/17.
 */

public class AlbumsFragment extends BaseMediaGridFragment {

    public static final String TAG = "AlbumsFragment";

    @BindView(R.id.albums) RecyclerView mAlbumsRecyclerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout refresh;

    private AlbumsAdapter mAdapter;
    private GridSpacingItemDecoration spacingDecoration;
    private AlbumClickListener mListener;

    private boolean hidden = false;
    private ArrayList<String> mExcluded = new ArrayList<>();
    private Context mContext;

    public interface AlbumClickListener {
        void onAlbumClick(Album album);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mExcluded = db().getExcludedFolders(getContext());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AlbumClickListener) mListener = (AlbumClickListener) context;
        mContext = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!clearSelected()) {
            updateToolbar();
        }
        setUpColumns();
    }

    public void displayAlbums(boolean hidden) {
        this.hidden = hidden;
        displayAlbums();
    }

    private void displayAlbums() {
        mAdapter.clear();

        // always check if the list of excluded albums has changed
//        mExcluded = db().getExcludedFolders(getContext());
        mExcluded = db().getExcludedFolders(mContext);

//        SQLiteDatabase db = HandlingAlbums.getInstance(getContext().getApplicationContext()).getReadableDatabase();
        SQLiteDatabase db = HandlingAlbums.getInstance(mContext).getReadableDatabase();

        CPHelper.getAlbums(getContext(), hidden, mExcluded, sortingMode(), sortingOrder())
                .subscribeOn(Schedulers.io())
                .map(album -> album.withSettings(HandlingAlbums.getSettings(db, album.getPath())))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        album -> mAdapter.add(album),
                        throwable -> {
                            refresh.setRefreshing(false);
                            throwable.printStackTrace();
                        },
                        () -> {
                            db.close();
                            if (getNothingToShowListener() != null)
                                getNothingToShowListener().changedNothingToShow(getCount() == 0);
                            refresh.setRefreshing(false);

                            Hawk.put(hidden ? "h" : "albums", mAdapter.getAlbumsPaths());
                        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        displayAlbums();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setUpColumns();
    }

    private void setUpColumns() {
        int columnsCount = columnsCount();

        if (columnsCount != ((GridLayoutManager) mAlbumsRecyclerView.getLayoutManager()).getSpanCount()) {
            mAlbumsRecyclerView.removeItemDecoration(spacingDecoration);
            spacingDecoration = new GridSpacingItemDecoration(columnsCount, Measure.pxToDp(3, getContext()), true);
            mAlbumsRecyclerView.addItemDecoration(spacingDecoration);
            mAlbumsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), columnsCount));
        }
    }

    private int columnsCount() {
        return DeviceUtils.isPortrait(getResources())
                ? Prefs.getFolderColumnsPortrait()
                : Prefs.getFolderColumnsLandscape();
    }

    @Override
    public int getTotalCount() {
        return mAdapter.getItemCount();
    }

    @Override
    public View.OnClickListener getToolbarButtonListener(boolean editMode) {
        if (editMode) return null;
        else return v -> mAdapter.clearSelected();
    }

    @Override
    public String getToolbarTitle() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_albums, container, false);
        ButterKnife.bind(this, v);

        int spanCount = columnsCount();
//        spacingDecoration = new GridSpacingItemDecoration(spanCount, Measure.pxToDp(3, getContext()), true);
        spacingDecoration = new GridSpacingItemDecoration(spanCount, Measure.pxToDp(3, mContext), true);
        mAlbumsRecyclerView.setHasFixedSize(true);
        mAlbumsRecyclerView.addItemDecoration(spacingDecoration);
        mAlbumsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        if (Prefs.animationsEnabled()) {
            mAlbumsRecyclerView.setItemAnimator(
                    AnimationUtils.getItemAnimator(
                            new LandingAnimator(new OvershootInterpolator(1f))
                    ));
        }

        mAdapter = new AlbumsAdapter(getContext(), this);

        refresh.setOnRefreshListener(this::displayAlbums);
        mAlbumsRecyclerView.setAdapter(mAdapter);
        return v;
    }

    public SortingMode sortingMode() {
        return mAdapter.sortingMode();
    }

    public SortingOrder sortingOrder() {
        return mAdapter.sortingOrder();
    }

    private HandlingAlbums db() {
//        return HandlingAlbums.getInstance(getContext().getApplicationContext());
        return HandlingAlbums.getInstance(mContext);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.grid_albums, menu);

        menu.findItem(R.id.select_all).setIcon(ThemeHelper.getToolbarIcon(getContext(), GoogleMaterial.Icon.gmd_select_all));
        menu.findItem(R.id.delete).setIcon(ThemeHelper.getToolbarIcon(getContext(), (GoogleMaterial.Icon.gmd_delete)));
        menu.findItem(R.id.sort_action).setIcon(ThemeHelper.getToolbarIcon(getContext(),(GoogleMaterial.Icon.gmd_sort)));
        menu.findItem(R.id.search_action).setIcon(ThemeHelper.getToolbarIcon(getContext(), (GoogleMaterial.Icon.gmd_search)));

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        boolean editMode = editMode();
        boolean oneSelected = getSelectedCount() == 1;

        menu.setGroupVisible(R.id.general_album_items, !editMode);
        menu.setGroupVisible(R.id.edit_mode_items, editMode);
        menu.setGroupVisible(R.id.one_selected_items, oneSelected);

        menu.findItem(R.id.select_all).setTitle(
                getSelectedCount() == getCount()
                        ? R.string.clear_selected
                        : R.string.select_all);

        if (editMode) {
            menu.findItem(R.id.hide).setTitle(hidden ? R.string.unhide : R.string.hide);
        } else {
            menu.findItem(R.id.ascending_sort_order).setChecked(sortingOrder() == SortingOrder.ASCENDING);
            switch (sortingMode()) {
                case NAME:  menu.findItem(R.id.name_sort_mode).setChecked(true); break;
                case SIZE:  menu.findItem(R.id.size_sort_mode).setChecked(true); break;
                case DATE: default:
                    menu.findItem(R.id.date_taken_sort_mode).setChecked(true); break;
                case NUMERIC:  menu.findItem(R.id.numeric_sort_mode).setChecked(true); break;
            }
        }

        if (oneSelected) {
            Album selectedAlbum = mAdapter.getFirstSelectedAlbum();
            menu.findItem(R.id.pin_album).setTitle(selectedAlbum.isPinned() ? getString(R.string.un_pin) : getString(R.string.pin));
            menu.findItem(R.id.clear_album_cover).setVisible(selectedAlbum.hasCover());
        }

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Album selectedAlbum = mAdapter.getFirstSelectedAlbum();
        switch (item.getItemId()) {

            case R.id.select_all:
                if (mAdapter.getSelectedCount() == mAdapter.getItemCount())
                    mAdapter.clearSelected();
                else mAdapter.selectAll();
                return true;

            case R.id.pin_album:
                if (selectedAlbum != null) {
                    boolean b = selectedAlbum.togglePinAlbum();
                    db().setPined(selectedAlbum.getPath(), b);
                    mAdapter.clearSelected();
                    mAdapter.sort();
                }
                return true;

            case R.id.clear_album_cover:
                if (selectedAlbum != null) {
                    selectedAlbum.removeCoverAlbum();
                    db().setCover(selectedAlbum.getPath(), null);
                    mAdapter.clearSelected();
                    mAdapter.notifyItemChanaged(selectedAlbum);
                    // TODO: 4/5/17 updateui
                    return true;
                }

                return false;

            case R.id.hide:
                final AlertDialog hideDialog = AlertDialogsHelper.getTextDialog(((ThemedActivity) getActivity()),
                        hidden ? R.string.unhide : R.string.hide,
                        hidden ? R.string.unhide_album_message : R.string.hide_album_message);

                hideDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(hidden ? R.string.unhide : R.string.hide).toUpperCase(), (dialog, id) -> {
                    ArrayList<String> hiddenPaths = AlbumsHelper.getLastHiddenPaths();

                    for (Album album : mAdapter.getSelectedAlbums()) {
                        if (hidden) { // unhide
                            AlbumsHelper.unHideAlbum(album.getPath(), getContext());
                            hiddenPaths.remove(album.getPath());
                        } else { // hide
                            AlbumsHelper.hideAlbum(album.getPath(), getContext());
                            hiddenPaths.add(album.getPath());
                        }
                    }
                    AlbumsHelper.saveLastHiddenPaths(hiddenPaths);
                    mAdapter.removeSelectedAlbums();
                    updateToolbar();
                });

                if (!hidden) {
                    hideDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.exclude).toUpperCase(), (dialog, which) -> {
                        for (Album album : mAdapter.getSelectedAlbums()) {
                            db().excludeAlbum(album.getPath());
                            mExcluded.add(album.getPath());
                        }
                        mAdapter.removeSelectedAlbums();
                    });
                }
                hideDialog.setButton(DialogInterface.BUTTON_NEGATIVE, this.getString(R.string.cancel).toUpperCase(), (dialogInterface, i) -> hideDialog.dismiss());
                hideDialog.show();
                return true;

            case R.id.shortcut:
                AlbumsHelper.createShortcuts(getContext(), mAdapter.getSelectedAlbums());
                mAdapter.clearSelected();
                return true;

            case R.id.name_sort_mode:
                mAdapter.changeSortingMode(SortingMode.NAME);
                AlbumsHelper.setSortingMode(SortingMode.NAME);
                item.setChecked(true);
                return true;

            case R.id.date_taken_sort_mode:
                mAdapter.changeSortingMode(SortingMode.DATE);
                AlbumsHelper.setSortingMode(SortingMode.DATE);
                item.setChecked(true);
                return true;

            case R.id.size_sort_mode:
                mAdapter.changeSortingMode(SortingMode.SIZE);
                AlbumsHelper.setSortingMode(SortingMode.SIZE);
                item.setChecked(true);
                return true;

            case R.id.numeric_sort_mode:
                mAdapter.changeSortingMode(SortingMode.NUMERIC);
                AlbumsHelper.setSortingMode(SortingMode.NUMERIC);
                item.setChecked(true);
                return true;

            case R.id.ascending_sort_order:
                item.setChecked(!item.isChecked());
                SortingOrder sortingOrder = SortingOrder.fromValue(item.isChecked());
                mAdapter.changeSortingOrder(sortingOrder);
                AlbumsHelper.setSortingOrder(sortingOrder);
                return true;

            case R.id.exclude:
                final AlertDialog.Builder excludeDialogBuilder = new AlertDialog.Builder(getActivity(), getDialogStyle());

                final View excludeDialogLayout = LayoutInflater.from(getContext()).inflate(R.layout.dialog_exclude, null);
                TextView textViewExcludeTitle = excludeDialogLayout.findViewById(R.id.text_dialog_title);
                TextView textViewExcludeMessage = excludeDialogLayout.findViewById(R.id.text_dialog_message);
                final Spinner spinnerParents = excludeDialogLayout.findViewById(R.id.parents_folder);

                spinnerParents.getBackground().setColorFilter(getIconColor(), PorterDuff.Mode.SRC_ATOP);

                ((CardView) excludeDialogLayout.findViewById(R.id.message_card)).setCardBackgroundColor(getCardBackgroundColor());
                textViewExcludeTitle.setBackgroundColor(getPrimaryColor());
                textViewExcludeTitle.setText(getString(R.string.exclude));

                if (mAdapter.getSelectedCount() > 1) {
                    textViewExcludeMessage.setText(R.string.exclude_albums_message);
                    spinnerParents.setVisibility(View.GONE);
                } else {
                    textViewExcludeMessage.setText(R.string.exclude_album_message);
                    spinnerParents.setAdapter(getThemeHelper().getSpinnerAdapter(mAdapter.getFirstSelectedAlbum().getParentsFolders()));
                }

                textViewExcludeMessage.setTextColor(getTextColor());
                excludeDialogBuilder.setView(excludeDialogLayout);

                excludeDialogBuilder.setPositiveButton(this.getString(R.string.exclude).toUpperCase(), (dialog, id) -> {

                    if (mAdapter.getSelectedCount() > 1) {
                        for (Album album : mAdapter.getSelectedAlbums()) {
                            db().excludeAlbum(album.getPath());
                            mExcluded.add(album.getPath());
                        }
                        mAdapter.removeSelectedAlbums();

                    } else {
                        String path = spinnerParents.getSelectedItem().toString();
                        db().excludeAlbum(path);
                        mExcluded.add(path);
                        mAdapter.removeAlbumsThatStartsWith(path);
                        mAdapter.forceSelectedCount(0);
                    }
                    updateToolbar();
                });
                excludeDialogBuilder.setNegativeButton(this.getString(R.string.cancel).toUpperCase(), null);
                excludeDialogBuilder.show();
                return true;

            case R.id.delete:

                final AlertDialog alertDialog = AlertDialogsHelper.getTextDialog(((ThemedActivity) getActivity()), R.string.delete, R.string.delete_album_message);

                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, this.getString(R.string.cancel).toUpperCase(), (dialogInterface, i) -> alertDialog.dismiss());

                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, this.getString(R.string.delete).toUpperCase(), (dialog1, id) -> {
                    if (Security.isPasswordOnDelete()) {

                        Security.authenticateUser(((ThemedActivity) getActivity()), new Security.AuthCallBack() {
                            @Override
                            public void onAuthenticated() {
                                showDeleteBottomSheet();
                            }

                            @Override
                            public void onError() {
                                Toast.makeText(getContext(), R.string.wrong_password, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        showDeleteBottomSheet();
                    }
                });
                alertDialog.show();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void showDeleteBottomSheet() {
        List<Album> selected = mAdapter.getSelectedAlbums();
        ArrayList<io.reactivex.Observable<Album>> sources = new ArrayList<>(selected.size());
        for (Album media : selected) {
//            sources.add(MediaHelper.deleteAlbum(getContext().getApplicationContext(), media));
            sources.add(MediaHelper.deleteAlbum(mContext, media));
        }

        ProgressBottomSheet<Album> bottomSheet = new ProgressBottomSheet.Builder<Album>(R.string.delete_bottom_sheet_title)
                .autoDismiss(false)
                .sources(sources)
                .listener(new ProgressBottomSheet.Listener<Album>() {
                    @Override
                    public void onCompleted() {
                        mAdapter.invalidateSelectedCount();
                    }

                    @Override
                    public void onProgress(Album item) {
                        mAdapter.removeAlbum(item);
                    }
                })
                .build();

        bottomSheet.showNow(getChildFragmentManager(), null);
    }

    public int getCount() {
        return mAdapter.getItemCount();
    }

    public int getSelectedCount() {
        return mAdapter.getSelectedCount();
    }

    @Override
    public boolean editMode() {
        return mAdapter.selecting();
    }

    @Override
    public boolean clearSelected() {
        return mAdapter.clearSelected();
    }

    @Override
    public void onItemSelected(int position) {
        if (mListener != null) mListener.onAlbumClick(mAdapter.get(position));
    }

    @Override
    public void onSelectMode(boolean selectMode) {
        refresh.setEnabled(!selectMode);
        updateToolbar();
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onSelectionCountChanged(int selectionCount, int totalCount) {
        getEditModeListener().onItemsSelected(selectionCount, totalCount);
    }

    @Override
    public void refreshTheme(ThemeHelper t) {
        mAlbumsRecyclerView.setBackgroundColor(t.getBackgroundColor());
        mAdapter.refreshTheme(t);
        refresh.setColorSchemeColors(t.getAccentColor());
        refresh.setProgressBackgroundColorSchemeColor(t.getBackgroundColor());
    }
}
