package com.eamh.birdcontrol.data;

import com.eamh.birdcontrol.data.models.Bird;

import java.util.List;

public interface PersistenceManager {
    void retrieveAllBirds();

    void createBird(Bird bird);

    void retrieveBird(long _idBird);

    void updateBird(Bird bird);

    void deleteBird(long _idBird);

    enum ErrorCode {
        CREATE,
        RETRIEVE,
        UPDATE,
        DELETE
    }

    interface ResponseListener {
        void onDbAllBirdsRetrieved(List<Bird> birds);

        void onDbBirdRetrieved(Bird bird);

        void onDatabaseError(String error, ErrorCode errorCode);
    }
}