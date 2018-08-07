package com.eamh.birdcontrol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.eamh.birdcontrol.data.models.Bird;
import com.eamh.birdcontrol.fragments.birds.BirdsFragment;

public class MainActivity extends AppCompatActivity
        implements BirdsFragment.OnBirdsFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onBirdsFragmentListClicked(Bird bird) {
        launchBirdDetails();
    }

    private void launchBirdDetails() {
        Intent intent = new Intent(this, BirdDetailsActivity.class);
        startActivity(intent);
    }
}
