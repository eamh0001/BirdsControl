package com.eamh.birdcontrol.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class BirdsDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Birds.db";

    public BirdsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + Contract.BirdEntry.TABLE_NAME + " (" +
                        Contract.BirdEntry._ID + " INTEGER PRIMARY KEY," +
                        Contract.BirdEntry.COLUMN_GENDER + " TEXT," +
                        Contract.BirdEntry.COLUMN_IMG_PATH + " TEXT," +
                        Contract.BirdEntry.COLUMN_RACE + " TEXT," +
                        Contract.BirdEntry.COLUMN_VARIATION + " TEXT," +
                        Contract.BirdEntry.COLUMN_RING + " TEXT," +
                        Contract.BirdEntry.COLUMN_CAGE + " TEXT," +
                        Contract.BirdEntry.COLUMN_BIRTH_DATE + " TEXT," +
                        Contract.BirdEntry.COLUMN_ORIGIN + " TEXT," +
                        Contract.BirdEntry.COLUMN_ANNOTATIONS + " TEXT)";

        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + Contract.BirdEntry.TABLE_NAME;
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    public static class Contract {
        private Contract() {
        }

        public static class BirdEntry implements BaseColumns {
            public static final String TABLE_NAME = "birds";
            //            public static final String COLUMN_ID = "_id";
            public static final String COLUMN_GENDER = "gender";
            public static final String COLUMN_IMG_PATH = "img_path";
            public static final String COLUMN_RACE = "race";
            public static final String COLUMN_VARIATION = "variation";
            public static final String COLUMN_RING = "ring";
            public static final String COLUMN_CAGE = "cage";
            public static final String COLUMN_BIRTH_DATE = "birth_date";
            public static final String COLUMN_ORIGIN = "origin";
            public static final String COLUMN_ANNOTATIONS = "annotations";
        }
    }
}
