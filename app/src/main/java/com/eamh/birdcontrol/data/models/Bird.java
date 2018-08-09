package com.eamh.birdcontrol.data.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Bird implements Parcelable {

    public static final Parcelable.Creator<Bird> CREATOR = new Parcelable.Creator<Bird>() {
        @Override
        public Bird createFromParcel(Parcel source) {
            return new Bird(source);
        }

        @Override
        public Bird[] newArray(int size) {
            return new Bird[size];
        }
    };

    private long _id;
    private Gender gender;
    private String imageUrl;
    private String race;
    private String variation;
    private String ring;
    private String cage;
    private String birthDate;
    private String origin;
    private String annotations;

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public Bird() {
    }

    protected Bird(Parcel in) {
        this._id = in.readLong();
        int tmpGender = in.readInt();
        this.gender = tmpGender == -1 ? null : Gender.values()[tmpGender];
        this.imageUrl = in.readString();
        this.race = in.readString();
        this.variation = in.readString();
        this.ring = in.readString();
        this.cage = in.readString();
        this.birthDate = in.readString();
        this.origin = in.readString();
        this.annotations = in.readString();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getVariation() {
        return variation;
    }

    public void setVariation(String variation) {
        this.variation = variation;
    }

    public String getRing() {
        return ring;
    }

    public void setRing(String ring) {
        this.ring = ring;
    }

    public String getCage() {
        return cage;
    }

    public void setCage(String cage) {
        this.cage = cage;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getAnnotations() {
        return annotations;
    }

    public void setAnnotations(String annotations) {
        this.annotations = annotations;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    @Override
    public String toString() {
        return "Bird{" +
                "_id=" + _id +
                ", gender=" + gender +
                ", imageUrl='" + imageUrl + '\'' +
                ", race='" + race + '\'' +
                ", variation='" + variation + '\'' +
                ", ring='" + ring + '\'' +
                ", cage='" + cage + '\'' +
                ", birthDate=" + birthDate +
                ", origin='" + origin + '\'' +
                ", annotations='" + annotations + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this._id);
        dest.writeInt(this.gender == null ? -1 : this.gender.ordinal());
        dest.writeString(this.imageUrl);
        dest.writeString(this.race);
        dest.writeString(this.variation);
        dest.writeString(this.ring);
        dest.writeString(this.cage);
        dest.writeString(this.birthDate);
        dest.writeString(this.origin);
        dest.writeString(this.annotations);
    }

    public enum Gender {
        UNDEFINED,
        FEMALE,
        MALE
    }
}
