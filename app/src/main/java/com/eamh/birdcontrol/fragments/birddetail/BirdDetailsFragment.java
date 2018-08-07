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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.eamh.birdcontrol.R;
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
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnBirdDetailsFragmentInteractionListener mListener;

    private ImageView birdPhoto;
    private Spinner spGenre;
    private Button btnTakePhoto;
    private EditText etRaceValue;
    private EditText etVariationValue;
    private EditText etRingValue;
    private EditText etCodeValue;
    private EditText etBirthDate;
    private EditText etProcedence;
    private Button btOrigin;
    private Button btDescendants;
    private EditText etAnnotations;
    private FloatingActionButton fabSave;

    private String imagePath;

    public BirdDetailsFragment() {
    }

    public static BirdDetailsFragment newInstance(int columnCount) {
        BirdDetailsFragment fragment = new BirdDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnBirdDetailsFragmentInteractionListener) {
            mListener = (OnBirdDetailsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnBreedFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bird_details, container, false);

        birdPhoto = rootView.findViewById(R.id.birdPhoto);
        btnTakePhoto = rootView.findViewById(R.id.btnTakePhoto);
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchTakePhotoIntent();
            }
        });
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            savePhotoPathOnDb(imagePath);
            setBirdPhoto(imagePath);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
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

    private void savePhotoPathOnDb(String imagePath) {
        //TODO
    }

    private void setBirdPhoto(String imagePath) {
        Picasso.get().load(new File(imagePath))
                .resize(100, 100)
                .centerCrop()
                .into(birdPhoto);
    }

    public interface OnBirdDetailsFragmentInteractionListener {

    }
}
