package com.cpsteam.torontoso.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dung on 4/7/2016.
 */
public class Patient implements Parcelable {
    private int id;

    private int userId;

    private String firstName;

    private String lastName;

    private String fullName;

    private String sex;

    private String dob;

    private String braClass;

    private String race;

    private int service;

    private int hyphoKypho;

    private String entryDate;

    private List<Image> images = new ArrayList<>();

    public Patient() {
    }

    protected Patient(Parcel in) {
        id = in.readInt();
        userId = in.readInt();
        firstName = in.readString();
        lastName = in.readString();
        fullName = in.readString();
        sex = in.readString();
        dob = in.readString();
        braClass = in.readString();
        race = in.readString();
        service = in.readInt();
        hyphoKypho = in.readInt();
        entryDate = in.readString();
        images = in.createTypedArrayList(Image.CREATOR);
    }

    public static final Creator<Patient> CREATOR = new Creator<Patient>() {
        @Override
        public Patient createFromParcel(Parcel in) {
            return new Patient(in);
        }

        @Override
        public Patient[] newArray(int size) {
            return new Patient[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        if (firstName == null) {
            firstName = "";
        }
        if (lastName == null) {
            lastName = "";
        }
        return firstName + " " + lastName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getBraClass() {
        return braClass;
    }

    public void setBraClass(String braClass) {
        this.braClass = braClass;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public int getService() {
        return service;
    }

    public void setService(int service) {
        this.service = service;
    }

    public int getHyphoKypho() {
        return hyphoKypho;
    }

    public void setHyphoKypho(int hyphoKypho) {
        this.hyphoKypho = hyphoKypho;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(userId);
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(fullName);
        parcel.writeString(sex);
        parcel.writeString(dob);
        parcel.writeString(braClass);
        parcel.writeString(race);
        parcel.writeInt(service);
        parcel.writeInt(hyphoKypho);
        parcel.writeString(entryDate);
        parcel.writeTypedList(images);
    }
}
