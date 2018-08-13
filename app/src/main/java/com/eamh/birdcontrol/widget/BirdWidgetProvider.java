package com.eamh.birdcontrol.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.eamh.birdcontrol.BirdDetailsActivity;
import com.eamh.birdcontrol.R;
import com.eamh.birdcontrol.data.models.Bird;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Implementation of App Widget functionality.
 */
public class BirdWidgetProvider extends AppWidgetProvider {

    private static final String TAG = BirdWidgetProvider.class.getName();

    public static void updateBirdsWidgets(Context context, AppWidgetManager appWidgetManager,
                                          int[] appWidgetIds, Bird bird) {
        Log.d(TAG, "updateBirdsWidgets " + bird);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, bird);
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId,
                                Bird bird) {
        // Construct the RemoteViews object
        RemoteViews remoteViews;

        if (bird != null) {
            remoteViews = loadWithBirdDetails(context, bird, appWidgetId);
        } else {
            remoteViews = loadWithBirds(context);
        }
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    private static RemoteViews loadWithBirds(Context context) {
        Log.d(TAG, "loadWithBirds ");
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
        views.setViewVisibility(R.id.birdDataContainer, View.GONE);
        views.setViewVisibility(R.id.lvBirds, View.VISIBLE);
        Intent remoteAdapterIntent = new Intent(context, ListRemoteViewsService.class);
        views.setRemoteAdapter(R.id.lvBirds, remoteAdapterIntent);
        views.setEmptyView(R.id.lvBirds, R.id.appwidget_text2);

        //Mounts the intent that will acts as a template to be filled in with ingredients extra by ListRemoteViewsFactory
        Intent ingredientsIntentTemplate = new Intent(context, WidgetService.class);
        ingredientsIntentTemplate.setAction(WidgetService.ACTION_GET_BIRD_DETAILS);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, ingredientsIntentTemplate, PendingIntent.FLAG_UPDATE_CURRENT);

        views.setPendingIntentTemplate(R.id.lvBirds, pendingIntent);

        return views;
    }

    private static RemoteViews loadWithBirdDetails(Context context, final Bird bird, final int appWidgetId) {

        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);

        views.setViewVisibility(R.id.lvBirds, View.GONE);
        views.setViewVisibility(R.id.birdDataContainer, View.VISIBLE);
        Handler uiHandler = new Handler(Looper.getMainLooper());
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                Picasso.get().load(new File(bird.getImageUrl()))
                        .resize(100, 100)
                        .centerCrop()
                        .into(views, R.id.birdPhoto, new int[]{appWidgetId});
            }
        });


        String[] genderArray = context.getResources().getStringArray(R.array.genres);
        views.setTextViewText(R.id.tvGender, genderArray[bird.getGender().ordinal()]);

        views.setTextViewText(R.id.tvRing, bird.getRing());

        views.setTextViewText(R.id.tvBirthDate, bird.getBirthDate());

        views.setTextViewText(R.id.tvRace, bird.getRace());

        views.setTextViewText(R.id.tvVariation, bird.getVariation());

        // Fill in the onClick PendingIntent Template using the specific bird for each item individually
        Bundle extras = new Bundle();
        extras.putParcelable(BirdDetailsActivity.INTENT_KEY_BIRD_DETAIL, bird);
        Intent intent = new Intent(context, BirdDetailsActivity.class);
        intent.putExtras(extras);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.birdDataContainer, pendingIntent);

        return views;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        WidgetService.startActionGetBirds(context);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

