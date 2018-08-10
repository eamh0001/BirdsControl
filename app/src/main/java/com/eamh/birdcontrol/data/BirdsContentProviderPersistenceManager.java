package com.eamh.birdcontrol.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.eamh.birdcontrol.R;
import com.eamh.birdcontrol.data.models.Bird;

import java.util.ArrayList;
import java.util.List;

public class BirdsContentProviderPersistenceManager
        implements PersistenceManager {

    private static final String TAG = BirdsContentProviderPersistenceManager.class.getSimpleName();

    private ResponseListener responseListener;
    private ContentResolver contentResolver;
    private Context context;
    private LoaderManager loaderManager;
    private int loaderId;

    private BirdsContentProviderPersistenceManager(Builder builder) {
        this.loaderId = builder.loaderId;
        this.contentResolver = builder.contentResolver;
        this.loaderManager = builder.loaderManager;
        this.responseListener = builder.responseListener;
        this.context = builder.context;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static List<Bird> cursorToBirdList(Cursor cursor) {
        List<Bird> birdsFromCursor = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Log.d(TAG, "cursor.moveToNext() ");
                birdsFromCursor.add(cursorToBird(cursor));
            }
        }
        return birdsFromCursor;
    }

    public static Bird cursorToBird(Cursor cursor) {
        int columnIndexId = cursor.getColumnIndex(BirdsDbHelper.Contract.BirdEntry._ID);
        int columnIndexGender = cursor.getColumnIndex(BirdsDbHelper.Contract.BirdEntry.COLUMN_GENDER);
        int columnIndexImgPath = cursor.getColumnIndex(BirdsDbHelper.Contract.BirdEntry.COLUMN_IMG_PATH);
        int columnIndexRace = cursor.getColumnIndex(BirdsDbHelper.Contract.BirdEntry.COLUMN_RACE);
        int columnIndexVariation = cursor.getColumnIndex(BirdsDbHelper.Contract.BirdEntry.COLUMN_VARIATION);
        int columnIndexRing = cursor.getColumnIndex(BirdsDbHelper.Contract.BirdEntry.COLUMN_RING);
        int columnIndexCage = cursor.getColumnIndex(BirdsDbHelper.Contract.BirdEntry.COLUMN_CAGE);
        int columnIndexBirthDate = cursor.getColumnIndex(BirdsDbHelper.Contract.BirdEntry.COLUMN_BIRTH_DATE);
        int columnIndexOrigin = cursor.getColumnIndex(BirdsDbHelper.Contract.BirdEntry.COLUMN_ORIGIN);
        int columnIndexAnnotations = cursor.getColumnIndex(BirdsDbHelper.Contract.BirdEntry.COLUMN_ANNOTATIONS);


        Bird bird = new Bird();
        bird.set_id(cursor.getLong(columnIndexId));
        bird.setGender(getGenderFromDbInt(cursor.getInt(columnIndexGender)));
        bird.setImageUrl(cursor.getString(columnIndexImgPath));
        bird.setRace(cursor.getString(columnIndexRace));
        bird.setVariation(cursor.getString(columnIndexVariation));
        bird.setRing(cursor.getString(columnIndexRing));
        bird.setCage(cursor.getString(columnIndexCage));
        bird.setBirthDate(cursor.getString(columnIndexBirthDate));
        bird.setOrigin(cursor.getString(columnIndexOrigin));
        bird.setAnnotations(cursor.getString(columnIndexAnnotations));
        return bird;
    }

    private static Bird.Gender getGenderFromDbInt(int dbGender) {
        return Bird.Gender.values()[dbGender];
    }

    @Override
    public void retrieveAllBirds() {
        loaderManager.restartLoader(loaderId, null, createRetrieveAllBirdsLoaderCallbacks());
    }

    @Override
    public void createBird(Bird bird) {
        ContentValues contentValues = birdToContentValues(bird);
        contentResolver.insert(BirdsContentProvider.CONTENT_BIRDS_URI, contentValues);
    }

    @Override
    public void retrieveBird(long _idBird) {
        loaderManager.restartLoader(loaderId, null, createRetrieveBirdLoaderCallbacks(_idBird));
    }

    @Override
    public void updateBird(Bird bird) {
        ContentValues contentValues = birdToContentValues(bird);
        Uri uriToUpdate = ContentUris.withAppendedId(BirdsContentProvider.CONTENT_BIRDS_URI, bird.get_id());
        contentResolver.update(uriToUpdate, contentValues, null, null);
    }

    @Override
    public void deleteBird(long _idBird) {
        Uri uriToDelete = ContentUris.withAppendedId(BirdsContentProvider.CONTENT_BIRDS_URI, _idBird);
        contentResolver.delete(uriToDelete, null, null);
    }

    private LoaderManager.LoaderCallbacks<Cursor> createRetrieveAllBirdsLoaderCallbacks() {
        return new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                if (i == loaderId) {
                    return new CursorLoader(context,
                            BirdsContentProvider.CONTENT_BIRDS_URI,
                            null,
                            null,
                            null,
                            null);
                } else throw new RuntimeException("Loader Not Implemented: " + loaderId);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                Log.d(TAG, "createLoadAllFilmsCallbacks onLoadFinished " + cursor);
                if (loader.getId() == loaderId) {
                    responseListener.onDbAllBirdsRetrieved(cursorToBirdList(cursor));
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                if (loader.getId() == loaderId) {
                    responseListener.onDbAllBirdsRetrieved(new ArrayList<Bird>());
                }
            }
        };
    }

    private LoaderManager.LoaderCallbacks<Cursor> createRetrieveBirdLoaderCallbacks(final long birdId) {
        return new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                if (i == loaderId) {

                    Uri uriToQuery = ContentUris.withAppendedId(BirdsContentProvider.CONTENT_BIRDS_URI, birdId);
                    return new CursorLoader(context,
                            uriToQuery,
                            null,
                            null,
                            null,
                            null);
                } else throw new RuntimeException("Loader Not Implemented: " + loaderId);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                Log.d(TAG, "createGetFilmCallbacks onLoadFinished " + cursor);
                if (loader.getId() == loaderId) {
                    if (cursor != null && cursor.moveToFirst()) {
                        responseListener.onDbBirdRetrieved(cursorToBird(cursor));
                    } else {
                        responseListener.onDatabaseError(context.getResources().getString(R.string.error_loading_bird), ErrorCode.RETRIEVE);
                    }
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
            }
        };
    }

    private ContentValues birdToContentValues(Bird bird) {
        ContentValues contentValues = new ContentValues();
//        contentValues.put(BirdsDbHelper.Contract.BirdEntry._ID, bird.get_id());
        contentValues.put(BirdsDbHelper.Contract.BirdEntry.COLUMN_GENDER, getDbIntGender(bird.getGender()));
        contentValues.put(BirdsDbHelper.Contract.BirdEntry.COLUMN_IMG_PATH, bird.getImageUrl());
        contentValues.put(BirdsDbHelper.Contract.BirdEntry.COLUMN_RACE, bird.getRace());
        contentValues.put(BirdsDbHelper.Contract.BirdEntry.COLUMN_VARIATION, bird.getVariation());
        contentValues.put(BirdsDbHelper.Contract.BirdEntry.COLUMN_RING, bird.getRing());
        contentValues.put(BirdsDbHelper.Contract.BirdEntry.COLUMN_CAGE, bird.getCage());
        contentValues.put(BirdsDbHelper.Contract.BirdEntry.COLUMN_BIRTH_DATE, bird.getBirthDate());
        contentValues.put(BirdsDbHelper.Contract.BirdEntry.COLUMN_ORIGIN, bird.getOrigin());
        contentValues.put(BirdsDbHelper.Contract.BirdEntry.COLUMN_ANNOTATIONS, bird.getAnnotations());
        return contentValues;
    }

    private int getDbIntGender(Bird.Gender gender) {
        return gender.ordinal();
    }

    public static class Builder {

        private ResponseListener responseListener;
        private ContentResolver contentResolver;
        private LoaderManager loaderManager;
        private int loaderId;
        private Context context;

        Builder() {
        }

        public Builder setResponseListener(ResponseListener responseListener) {
            this.responseListener = responseListener;
            return this;
        }

        public Builder setContentResolver(ContentResolver contentResolver) {
            this.contentResolver = contentResolver;
            return this;
        }

        public Builder setLoaderManager(LoaderManager loaderManager) {
            this.loaderManager = loaderManager;
            return this;
        }

        public Builder setLoaderId(int loaderId) {
            this.loaderId = loaderId;
            return this;
        }

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public BirdsContentProviderPersistenceManager build() {
            return new BirdsContentProviderPersistenceManager(this);
        }
    }
}
