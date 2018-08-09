package com.eamh.birdcontrol;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.eamh.birdcontrol.data.ContentProviderPersistenceManager;
import com.eamh.birdcontrol.data.PersistenceManager;
import com.eamh.birdcontrol.data.models.Bird;
import com.eamh.birdcontrol.fragments.birds.BirdsFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements BirdsFragment.OnBirdsFragmentInteractionListener,
        PersistenceManager.ResponseListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int ID_BIRDS_MAIN_LOADER = 0;

    private ProgressBar mLoadingIndicator;
    private Toolbar toolbar;

    private PersistenceManager persistenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        persistenceManager = ContentProviderPersistenceManager.builder()
                .setLoaderId(ID_BIRDS_MAIN_LOADER)
                .setContentResolver(getContentResolver())
                .setLoaderManager(getLoaderManager())
                .setResponseListener(this)
                .setContext(this)
                .build();
        showLoadingBar(true);
        persistenceManager.retrieveAllBirds();
    }

    @Override
    public void onBirdsFragmentListClicked(Bird bird) {
        launchBirdDetails(bird);
    }

    @Override
    public void onDbAllBirdsRetrieved(List<Bird> birds) {
        Log.d(TAG, "onDbAllBirdsRetrieved " + birds.size());
        for (Bird bird : birds) {
            Log.d(TAG, "bird " + bird);
        }
        BirdsFragment birdsFragment = BirdsFragment.newInstance(new ArrayList<>(birds));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, birdsFragment).commit();
        showLoadingBar(false);
    }

    @Override
    public void onDbBirdRetrieved(Bird bird) {
        Log.w(TAG, "onDbBirdRetrieved " + bird);
        showLoadingBar(false);
    }

    @Override
    public void onDatabaseError(String error, PersistenceManager.ErrorCode errorCode) {
        Log.e(TAG, "onDatabaseError " + error);
        showLoadingBar(false);
        Snackbar.make(toolbar, error, Snackbar.LENGTH_SHORT).show();
    }

    private void launchBirdDetails(Bird bird) {
        Intent intent = new Intent(this, BirdDetailsActivity.class);
        if (bird != null) {
            intent.putExtra(BirdDetailsActivity.INTENT_KEY_BIRD_DETAIL, bird);
        }
        startActivity(intent);
    }

    private void showLoadingBar(boolean show) {
        mLoadingIndicator.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }
}
