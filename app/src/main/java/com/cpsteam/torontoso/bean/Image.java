package com.cpsteam.torontoso.bean;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dung on 4/7/2016.
 */
public class Image implements Parcelable {
    private int imageId;

    private int torsoId;

    private String imagePath;

    private Bitmap bitmap;

    public Image() {
    }

    protected Image(Parcel in) {
        imageId = in.readInt();
        torsoId = in.readInt();
        imagePath = in.readString();
        bitmap = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getTorsoId() {
        return torsoId;
    }

    public void setTorsoId(int torsoId) {
        this.torsoId = torsoId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(imageId);
        parcel.writeInt(torsoId);
        parcel.writeString(imagePath);
        parcel.writeParcelable(bitmap, i);
    }
}
