package com.eamh.birdcontrol.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.eamh.birdcontrol.R;
import com.eamh.birdcontrol.data.models.Bird;

public class WidgetService extends IntentService {

    public static final String ACTION_GET_BIRDS =
            "com.eamh.birdcontrol.action.get_birds";
    public static final String ACTION_GET_BIRD_DETAILS =
            "com.eamh.birdcontrol.action.get_bird_details";
    public static final String INTENT_KEY_BIRD_DETAILS = "IKBD";
    private static final String TAG = WidgetService.class.getName();

    public WidgetService() {
        super(TAG);
    }

    public static void startActionGetBirds(Context context) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(ACTION_GET_BIRDS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            Log.d(TAG, "onHandleIntent " + action);
            switch (action) {
                case ACTION_GET_BIRDS:
                    handleActionGetBirds();
                    break;
                case ACTION_GET_BIRD_DETAILS:
                    if (intent.hasExtra(INTENT_KEY_BIRD_DETAILS)) {
                        Bird bird = intent.getParcelableExtra(INTENT_KEY_BIRD_DETAILS);
                        handleActionGetBirdDetails(bird);
                    }
                    break;
                default:
                    Log.e(TAG, "Action doesn't found: " + action);
            }
        }
    }

    private void handleActionGetBirds() {
        Log.d(TAG, "handleActionGetBirds");
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager
                .getAppWidgetIds(new ComponentName(this, BirdWidgetProvider.class));

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.lvBirds);
        //Now update all widgets
        BirdWidgetProvider.updateBirdsWidgets(this, appWidgetManager, appWidgetIds, null);
    }

    private void handleActionGetBirdDetails(Bird bird) {
        Log.d(TAG, "handleActionGetBirdDetails " + bird);
        if (bird != null) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BirdWidgetProvider.class));
            //Now update all widgets
            BirdWidgetProvider.updateBirdsWidgets(this, appWidgetManager, appWidgetIds, bird);
        }
    }
}
