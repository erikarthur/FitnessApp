package org.arthurweb.fitnessapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by erik on 3/13/16.
 */
public class gpsValues implements Parcelable {
    private double _lat = Float.MIN_VALUE;
    private double _lon = Float.MIN_VALUE;
    private float _heading = Float.MIN_VALUE;
    private float _speed = Float.MIN_VALUE;
    private float _elevation = Float.MIN_VALUE;
    private float _hrm = Float.MIN_VALUE;
    private float _distance = Float.MIN_VALUE;
    private long _elapsedTime = Long.MIN_VALUE;
    private String _pace = null;

    public void set_lat (double lat) {
        _lat = lat;
    }
    public double get_lat() {
        return _lat;
    }

    public void set_lon (double lon) {
        _lon = lon;
    }
    public double get_lon() {
        return _lon;
    }

    public void set_heading (float h) {
        _heading = h;
    }
    public float get_heading() {
        return _heading;
    }

    public void set_speed (float s) {
        _speed = s;
    }
    public float get_speed() {
        return _speed;
    }

    public void set_elevation (float e) {
        _elevation = e;
    }
    public float get_elevation () {
        return _elevation;
    }

    public void set_hrm (float h) {
        _hrm = h;
    }
    public float get_hrm() {
        return _hrm;
    }

    public void set_distance(float d) { _distance = d; }
    public float get_distance() { return _distance; }

    public void set_elapsedTime(long t) {_elapsedTime = t;}
    public long get_elapsedTime() {return _elapsedTime;}

    public void set_pace (String p) { _pace = p;}
    public String get_pace() {return _pace;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this._lat);
        dest.writeDouble(this._lon);
        dest.writeFloat(this._heading);
        dest.writeFloat(this._speed);
        dest.writeFloat(this._elevation);
        dest.writeFloat(this._hrm);
        dest.writeFloat(this._distance);
        dest.writeLong(this._elapsedTime);
        dest.writeString(this._pace);
    }

    public gpsValues() {
    }

    protected gpsValues(Parcel in) {
        this._lat = in.readDouble();
        this._lon = in.readDouble();
        this._heading = in.readFloat();
        this._speed = in.readFloat();
        this._elevation = in.readFloat();
        this._hrm = in.readFloat();
        this._distance = in.readFloat();
        this._elapsedTime = in.readLong();
        this._pace = in.readString();
    }

    public static final Creator<gpsValues> CREATOR = new Creator<gpsValues>() {
        public gpsValues createFromParcel(Parcel source) {
            return new gpsValues(source);
        }

        public gpsValues[] newArray(int size) {
            return new gpsValues[size];
        }
    };
}
