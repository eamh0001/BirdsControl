package com.eamh.birdcontrol;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.eamh.birdcontrol.data.BirdsContentProviderPersistenceManager;
import com.eamh.birdcontrol.data.PersistenceManager;
import com.eamh.birdcontrol.data.models.Bird;
import com.eamh.birdcontrol.fragments.birddetail.BirdDetailsFragment;
import com.eamh.birdcontrol.fragments.imagecontainer.ImageFragment;

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

        persistenceManager = BirdsContentProviderPersistenceManager.builder()
                .setLoaderId(ID_BIRD_DETAIL_LOADER)
                .setContentResolver(getContentResolver())
                .setLoaderManager(getSupportLoaderManager())
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
            showSnackBar(getString(R.string.bird_updated));
        } else {
            persistenceManager.createBird(bird);
            showSnackBar(getString(R.string.bird_created));
        }
    }

    @Override
    public void onBirdImageClicked(String imagePath) {
        ImageFragment imageFragment = ImageFragment.newInstance(imagePath);
        replaceFragment(imageFragment);
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
        showSnackBar(error);
    }

    private void showBirdFragment(Bird bird) {
        BirdDetailsFragment birdFragment = BirdDetailsFragment.newInstance(bird);
        replaceFragment(birdFragment);
    }

    private void showSnackBar(String info) {
        Snackbar.make(toolbar, info, Snackbar.LENGTH_SHORT).show();
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment).commit();
    }
}
