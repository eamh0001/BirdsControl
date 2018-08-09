package com.eamh.birdcontrol.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Based upon
 * https://github.com/googlesamples/android-architecture-components/tree/master/PersistenceContentProviderSample
 */
public class BirdsContentProvider extends ContentProvider {

    public static final String AUTHORITY = "com.eamh.birdcontrol";
    public static final Uri CONTENT_BASE_URI = Uri.parse("content://" + AUTHORITY);
    public static final Uri CONTENT_BIRDS_URI = CONTENT_BASE_URI.buildUpon()
            .appendPath(BirdsDbHelper.Contract.BirdEntry.TABLE_NAME)
            .build();

    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    /**
     * The match code for some items in the Birds table.
     */
    private static final int CODE_BIRDS_DIR = 1;

    /**
     * The match code for an item in the Birds table.
     */
    private static final int CODE_BIRD_ITEM = 2;

    static {
        MATCHER.addURI(AUTHORITY, BirdsDbHelper.Contract.BirdEntry.TABLE_NAME, CODE_BIRDS_DIR);
        MATCHER.addURI(AUTHORITY, BirdsDbHelper.Contract.BirdEntry.TABLE_NAME + "/#", CODE_BIRD_ITEM);
    }


    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor cursor;
        SQLiteDatabase db;
        final Context context = getContext();
        if (context == null) {
            return null;
        }
        switch (MATCHER.match(uri)) {

            case CODE_BIRDS_DIR:
                db = new BirdsDbHelper(context).getReadableDatabase();
                cursor = db.query(BirdsDbHelper.Contract.BirdEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case CODE_BIRD_ITEM:
                db = new BirdsDbHelper(context).getReadableDatabase();
                String birdId = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{birdId};
                cursor = db.query(BirdsDbHelper.Contract.BirdEntry.TABLE_NAME,
                        projection,
                        BirdsDbHelper.Contract.BirdEntry._ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

//        db.close();
        cursor.setNotificationUri(context.getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (MATCHER.match(uri)) {
            case CODE_BIRDS_DIR:
                return "vnd.android.cursor.dir/" + AUTHORITY + "." +
                        BirdsDbHelper.Contract.BirdEntry.TABLE_NAME;
            case CODE_BIRD_ITEM:
                return "vnd.android.cursor.item/" + AUTHORITY + "." +
                        BirdsDbHelper.Contract.BirdEntry.TABLE_NAME;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @NonNull ContentValues contentValues) {

        int uriMatch = MATCHER.match(uri);
        System.out.println("insert " + uri + "\nMatch " + uriMatch);

        if (uriMatch == CODE_BIRDS_DIR || uriMatch == CODE_BIRD_ITEM) {
            final Context context = getContext();
            if (context == null) {
                return null;
            }

            SQLiteDatabase db = new BirdsDbHelper(context).getWritableDatabase();
            long rowId = db.insert(BirdsDbHelper.Contract.BirdEntry.TABLE_NAME,
                    null,
                    contentValues);
//            db.close();
            if ((rowId != -1)) {
                context.getContentResolver().notifyChange(uri, null);
                return CONTENT_BIRDS_URI.buildUpon().appendPath(Long.toString(rowId)).build();
            } else throw new RuntimeException("Couldn't insert: " + uri);

        } else throw new UnsupportedOperationException("Unknown uri: " + uri);

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        int rowsDeleted;

        /*
         * If we pass null as the selection to SQLiteDatabase#delete, our entire table will be
         * deleted. However, if we do pass null and delete all of the rows in the table, we won't
         * know how many rows were deleted. According to the documentation for SQLiteDatabase,
         * passing "1" for the selection will delete all rows and return the number of rows
         * deleted, which is what the caller of this method expects.
         */

        switch (MATCHER.match(uri)) {
            case CODE_BIRD_ITEM:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                String birdId = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{birdId};
                SQLiteDatabase db = new BirdsDbHelper(context).getWritableDatabase();
                rowsDeleted = db.delete(
                        BirdsDbHelper.Contract.BirdEntry.TABLE_NAME,
                        BirdsDbHelper.Contract.BirdEntry._ID + " = ? ",
                        selectionArguments);
//                db.close();
                context.getContentResolver().notifyChange(uri, null);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        switch (MATCHER.match(uri)) {

            case CODE_BIRDS_DIR:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);

            case CODE_BIRD_ITEM:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                SQLiteDatabase db = new BirdsDbHelper(context).getWritableDatabase();

                String where = BirdsDbHelper.Contract.BirdEntry._ID + " = ? ";
                String birdId = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{birdId};

                int count = db.update(BirdsDbHelper.Contract.BirdEntry.TABLE_NAME,
                        contentValues,
                        where,
                        selectionArguments);
                context.getContentResolver().notifyChange(uri, null);
                return count;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }
}
