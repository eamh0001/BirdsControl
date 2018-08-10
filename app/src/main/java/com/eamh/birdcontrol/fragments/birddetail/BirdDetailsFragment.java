package com.eamh.birdcontrol.fragments.birddetail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.eamh.birdcontrol.R;
import com.eamh.birdcontrol.data.models.Bird;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

/**
 * A placeholder fragment containing a simple view.
 */
public class BirdDetailsFragment extends Fragment {

    private static final String TAG = BirdDetailsFragment.class.getName();
    private static final int REQUEST_TAKE_PHOTO = 1;

    private static final String BUNDLE_KEY_BIRD_DETAILS = "BUNDLE_KEY_BIRD_DETAILS";
    private OnBirdDetailsFragmentInteractionListener birdDetailsFragmentInteractionListener;

    private Bird bird;

    private ImageView birdPhoto;
    private Spinner spGenre;
    private Button btnTakePhoto;
    private EditText etRaceValue;
    private EditText etVariationValue;
    private EditText etRingValue;
    private EditText etBirthDate;
    private EditText etOrigin;
    private EditText etCage;
    private EditText etAnnotations;
    private FloatingActionButton fabSave;

    private long _idBird = -1;
    private String imagePath = "";

    public BirdDetailsFragment() {
    }

    public static BirdDetailsFragment newInstance(Bird bird) {
        BirdDetailsFragment fragment = new BirdDetailsFragment();
        if (bird != null) {
            Bundle args = new Bundle();
            args.putParcelable(BUNDLE_KEY_BIRD_DETAILS, bird);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bird = getArguments().getParcelable(BUNDLE_KEY_BIRD_DETAILS);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnBirdDetailsFragmentInteractionListener) {
            birdDetailsFragmentInteractionListener = (OnBirdDetailsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnBreedFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        birdDetailsFragmentInteractionListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bird_details, container, false);
        bindViews(rootView);
        setUIListeners();
        if (bird != null) {
            showBirdDataOnUI(bird);
        }
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            setBirdPhoto(imagePath);
        }
    }

    private void bindViews(View rootView) {
        birdPhoto = rootView.findViewById(R.id.birdPhoto);
        btnTakePhoto = rootView.findViewById(R.id.btnTakePhoto);
        spGenre = rootView.findViewById(R.id.spGenre);
        etRaceValue = rootView.findViewById(R.id.etRaceValue);
        etVariationValue = rootView.findViewById(R.id.etVariationValue);
        etRingValue = rootView.findViewById(R.id.etRingValue);
        etBirthDate = rootView.findViewById(R.id.etBirthDate);
        etOrigin = rootView.findViewById(R.id.etProcedence);
        etCage = rootView.findViewById(R.id.etCage);
        etAnnotations = rootView.findViewById(R.id.etAnnotations);
        fabSave = rootView.findViewById(R.id.fabSave);
    }

    private void setUIListeners() {
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchTakePhotoIntent();
            }
        });
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveBirdClicked();
            }
        });
        birdPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(imagePath)) {
//                    birdDetailsFragmentInteractionListener.onBirdImageClicked(imagePath);
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(imagePath), "image/*");
                    startActivity(intent);
                }
            }
        });
    }

    private void showBirdDataOnUI(Bird bird) {
        setBirdPhoto(bird.getImageUrl());
        spGenre.setSelection(bird.getGender().ordinal());
        _idBird = bird.get_id();
        etRaceValue.setText(bird.getRace());
        etVariationValue.setText(bird.getVariation());
        etRingValue.setText(bird.getRing());
        etBirthDate.setText(bird.getBirthDate());
        etOrigin.setText(bird.getOrigin());
        etCage.setText(bird.getCage());
        etAnnotations.setText(bird.getAnnotations());
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",   /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        imagePath = image.getAbsolutePath();
        Log.d(TAG, "imagePath " + imagePath);
        return image;
    }

    private void launchTakePhotoIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e(TAG, ex.getMessage());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Context context = getContext();
                Uri photoURI = FileProvider.getUriForFile(context,
                        context.getPackageName() + ".fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private void setBirdPhoto(String imagePath) {
        if (!TextUtils.isEmpty(imagePath)) {
            Picasso.get().load(new File(imagePath))
                    .fit()
                    .centerCrop()
                    .into(birdPhoto);
            this.imagePath = imagePath;
        }
    }

    private void saveBirdClicked() {
        Bird bird = createBirdFromUIData();
        Log.d(TAG, "SaveDataOnDb " + bird);
        birdDetailsFragmentInteractionListener.onSaveBirdClicked(bird);
    }

    private Bird createBirdFromUIData() {
        Bird bird = new Bird();
        bird.set_id(_idBird);
        bird.setBirthDate(etBirthDate.getText().toString());
        bird.setGender(getGenreFromSpinner());
        bird.setImageUrl(imagePath);
        bird.setRace(etRaceValue.getText().toString());
        bird.setVariation(etVariationValue.getText().toString());
        bird.setRing(etRingValue.getText().toString());
        bird.setCage(etCage.getText().toString());
        bird.setOrigin(etOrigin.getText().toString());
        bird.setAnnotations(etAnnotations.getText().toString());
        return bird;
    }

    public Bird.Gender getGenreFromSpinner() {
        int genreIndex = spGenre.getSelectedItemPosition();
        return Bird.Gender.values()[genreIndex];
    }

    public interface OnBirdDetailsFragmentInteractionListener {
        void onSaveBirdClicked(Bird bird);

        void onBirdImageClicked(String imagePath);
    }
}
