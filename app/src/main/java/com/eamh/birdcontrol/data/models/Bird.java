package com.eamh.birdcontrol.data.models;

import java.util.Date;

public class Bird {

    private long _id;
    private long _idBreedOrigin;
    private String petName;
    private String anilla;
    private String imageUrl;
    private Genre genre;
    private Date birthDate;
    private String race;
    private String colorMutation;
    private String annotations;
    private boolean isDead;
    private boolean isBreeding;

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getAnilla() {
        return anilla;
    }

    public void setAnilla(String anilla) {
        this.anilla = anilla;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getColorMutation() {
        return colorMutation;
    }

    public void setColorMutation(String colorMutation) {
        this.colorMutation = colorMutation;
    }

    public String getAnnotations() {
        return annotations;
    }

    public void setAnnotations(String annotations) {
        this.annotations = annotations;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public boolean isBreeding() {
        return isBreeding;
    }

    public void setBreeding(boolean breeding) {
        isBreeding = breeding;
    }

    public long get_idBreedOrigin() {
        return _idBreedOrigin;
    }

    public void set_idBreedOrigin(long _idBreedOrigin) {
        this._idBreedOrigin = _idBreedOrigin;
    }

    @Override
    public String toString() {
        return "Bird{" +
                "_id=" + _id +
                ", petName='" + petName + '\'' +
                ", anilla='" + anilla + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", genre=" + genre +
                ", birthDate=" + birthDate +
                ", race='" + race + '\'' +
                ", colorMutation='" + colorMutation + '\'' +
                ", annotations='" + annotations + '\'' +
                ", isDead=" + isDead +
                ", isBreeding=" + isBreeding +
                ", _idBreedOrigin=" + _idBreedOrigin +
                '}';
    }

    public enum Genre {
        MALE,
        FEMALE,
        UNDEFINED
    }
}
