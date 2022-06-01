package com.example.places.models.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.places.utils.other.Constants;

import java.io.Serializable;

@Entity
public class Place implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    public int id = 0;

    @ColumnInfo(name = Constants.KEY_NAME)
    public String name;

    @ColumnInfo(name = Constants.KEY_DESC)
    public String description;

    @ColumnInfo(name = Constants.KEY_DATE)
    public String date;

    @ColumnInfo(name = Constants.KEY_LOCATION)
    public String location;

    @ColumnInfo(name = Constants.KEY_LAT)
    public Double latitude;

    @ColumnInfo(name = Constants.KEY_LONG)
    public Double longitude;

    public Place(String name, String description, String date, String location, Double latitude,
                 Double longitude) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected Place(Parcel in) {
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        date = in.readString();
        location = in.readString();
        if (in.readByte() == 0) {
            latitude = null;
        } else {
            latitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            longitude = null;
        } else {
            longitude = in.readDouble();
        }
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(date);
        dest.writeString(location);
        if (latitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(latitude);
        }
        if (longitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(longitude);
        }
    }
}
