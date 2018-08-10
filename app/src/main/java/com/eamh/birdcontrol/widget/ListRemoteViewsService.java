package com.eamh.birdcontrol.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.eamh.birdcontrol.R;
import com.eamh.birdcontrol.data.BirdsContentProvider;
import com.eamh.birdcontrol.data.BirdsContentProviderPersistenceManager;
import com.eamh.birdcontrol.data.models.Bird;

import java.util.List;


public class ListRemoteViewsService extends RemoteViewsService {


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = ListRemoteViewsFactory.class.getName();
    private final Context context;
    private Cursor mCursor;
    private List<Bird> birds;

    public ListRemoteViewsFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        final long identityToken = Binder.clearCallingIdentity();
        if (mCursor != null) mCursor.close();
        mCursor = context.getContentResolver().query(
                BirdsContentProvider.CONTENT_BIRDS_URI,
                null,
                null,
                null,
                null
        );
        Binder.restoreCallingIdentity(identityToken);
        birds = BirdsContentProviderPersistenceManager.cursorToBirdList(mCursor);
    }

    @Override
    public void onDestroy() {
        if (birds != null)
            birds.clear();
    }

    @Override
    public int getCount() {
        return birds != null ? birds.size() : 0;
    }

    @Override
    public RemoteViews getViewAt(int i) {

        Log.d(TAG, "getViewAt " + i);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);
        if (birds != null) {
            Bird recipe = birds.get(i);
            Log.d(TAG, recipe.toString());
            remoteViews.setTextViewText(R.id.tvInfo, recipe.getRing());

            //Fills the IntentTemplate from BirdWidgetProvider
            Bundle extras = new Bundle();
            extras.putParcelable(WidgetService.INTENT_KEY_BIRD_DETAILS, recipe);
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);
            remoteViews.setOnClickFillInIntent(R.id.flRoot, fillInIntent);
        }
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}