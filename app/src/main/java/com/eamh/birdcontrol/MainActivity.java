package com.eamh.birdcontrol;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.eamh.birdcontrol.data.BirdsContentProviderPersistenceManager;
import com.eamh.birdcontrol.data.PersistenceManager;
import com.eamh.birdcontrol.data.models.Bird;
import com.eamh.birdcontrol.fragments.birds.BirdsFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements BirdsFragment.OnBirdsFragmentInteractionListener,
        PersistenceManager.ResponseListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int ID_BIRDS_MAIN_LOADER = 0;
    private static final String KEY_SAVED_INSTANCE_BIRDS_FRAGMENT = "KEY_SAVED_INSTANCE_BIRDS_FRAGMENT";

    private ProgressBar mLoadingIndicator;
    private Toolbar toolbar;

    private PersistenceManager persistenceManager;
    private BirdsFragment birdsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        persistenceManager = BirdsContentProviderPersistenceManager.builder()
                .setLoaderId(ID_BIRDS_MAIN_LOADER)
                .setContentResolver(getContentResolver())
                .setLoaderManager(getSupportLoaderManager())
                .setResponseListener(this)
                .setContext(this)
                .build();
        showLoadingBar(true);
        if (savedInstanceState != null) {
            //Restore the fragment's instance
            birdsFragment = (BirdsFragment) getSupportFragmentManager()
                    .getFragment(savedInstanceState, KEY_SAVED_INSTANCE_BIRDS_FRAGMENT);
        } else {
            birdsFragment = new BirdsFragment();
        }
        showFragment(birdsFragment);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, KEY_SAVED_INSTANCE_BIRDS_FRAGMENT, birdsFragment);
    }

    @Override
    protected void onPause() {
        super.onPause();
        showLoadingBar(false);
    }

    @Override
    public void onBirdsRequested() {
        persistenceManager.retrieveAllBirds();
    }

    @Override
    public void onBirdsFragmentListClicked(Bird bird) {
        launchBirdDetails(bird);
    }

    @Override
    public void onBirdsFragmentDeleteItem(long birdId) {
        showSnackBar("Deleted bird " + birdId);
        persistenceManager.deleteBird(birdId);
        persistenceManager.retrieveAllBirds();
    }

    @Override
    public void onDbAllBirdsRetrieved(List<Bird> birds) {
        Log.d(TAG, "onDbAllBirdsRetrieved " + birds.size());
        birdsFragment.setBirds(birds);
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
        showSnackBar(error);
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment).commit();
        showLoadingBar(false);
    }

    private void launchBirdDetails(Bird bird) {
        showLoadingBar(true);
        Intent intent = new Intent(this, BirdDetailsActivity.class);
        if (bird != null) {
            intent.putExtra(BirdDetailsActivity.INTENT_KEY_BIRD_DETAIL, bird);
        }
        startActivity(intent);
    }

    private void showLoadingBar(boolean show) {
        mLoadingIndicator.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    private void showSnackBar(String info) {
        Snackbar.make(toolbar, info, Snackbar.LENGTH_SHORT).show();
    }
}
