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
    public static final String ACTION_UPDATE_BIRD_WIDGETS =
            "com.eamh.birdcontrol.action.update_bird_widgets";

    public static final String INTENT_KEY_BIRD_DETAILS = "IKBD";
    private static final String TAG = WidgetService.class.getName();

    public WidgetService() {
        super(TAG);
    }

    /**
     * Starts this service to perform a retrieve birds from persistence action with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionGetBirds(Context context) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(ACTION_GET_BIRDS);
        context.startService(intent);
    }

    /**
     * Starts this service to update the widgets with new data. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionUpdateBirdWidgets(Context context) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(ACTION_UPDATE_BIRD_WIDGETS);
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
                case ACTION_UPDATE_BIRD_WIDGETS:
                    handleActionUpdateBirdWidgets();
                    break;
                default:
                    Log.e(TAG, "Action doesn't found: " + action);
            }
        }
    }

    private void handleActionUpdateBirdWidgets() {
        Log.d(TAG, "handleActionUpdateBirdWidgets");
        //TODO
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
