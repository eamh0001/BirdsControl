package com.eamh.birdcontrol;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.eamh.birdcontrol.data.ContentProviderPersistenceManager;
import com.eamh.birdcontrol.data.PersistenceManager;
import com.eamh.birdcontrol.data.models.Bird;
import com.eamh.birdcontrol.fragments.birddetail.BirdDetailsFragment;

import java.util.List;

public class BirdDetailsActivity extends AppCompatActivity
        implements BirdDetailsFragment.OnBirdDetailsFragmentInteractionListener,
        PersistenceManager.ResponseListener {

    public static final String INTENT_KEY_BIRD_DETAIL = "INTENT_KEY_BIRD_DETAIL";
    private static final String TAG = BirdDetailsActivity.class.getSimpleName();
    private static final int ID_BIRD_DETAIL_LOADER = 1;
    private PersistenceManager persistenceManager;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bird_details);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        persistenceManager = ContentProviderPersistenceManager.builder()
                .setLoaderId(ID_BIRD_DETAIL_LOADER)
                .setContentResolver(getContentResolver())
                .setLoaderManager(getLoaderManager())
                .setResponseListener(this)
                .setContext(this)
                .build();

        Bird bird = getBirdFromIntent();
        showBirdFragment(bird);
    }

    private Bird getBirdFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(INTENT_KEY_BIRD_DETAIL)) {
                return intent.getParcelableExtra(INTENT_KEY_BIRD_DETAIL);
            }
        }
        return null;
    }

    @Override
    public void onSaveBirdClicked(Bird bird) {
        if (bird.get_id() > -1) {
            persistenceManager.updateBird(bird);
        } else {
            persistenceManager.createBird(bird);
        }
    }

    @Override
    public void onDbAllBirdsRetrieved(List<Bird> birds) {
        Log.e(TAG, "onDbAllBirdsRetrieved " + birds.size());
    }

    @Override
    public void onDbBirdRetrieved(Bird bird) {
        Log.e(TAG, "onDbBirdRetrieved " + bird);
        showBirdFragment(bird);
    }

    @Override
    public void onDatabaseError(String error, PersistenceManager.ErrorCode errorCode) {
        Snackbar.make(toolbar, error, Snackbar.LENGTH_SHORT).show();
    }

    private void showBirdFragment(Bird bird) {
        BirdDetailsFragment birdFragment = BirdDetailsFragment.newInstance(bird);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, birdFragment).commit();
    }
}
