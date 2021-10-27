package pro.network.mpchicken.map;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class LocalPlace implements Serializable, Parcelable {
    public static final Creator<LocalPlace> CREATOR = new Creator<LocalPlace>() {
        @Override
        public LocalPlace createFromParcel(Parcel in) {
            return new LocalPlace(in);
        }

        @Override
        public LocalPlace[] newArray(int size) {
            return new LocalPlace[size];
        }
    };
    String address;
    String name;
    LatLng latLng;
    String pincode;

    public LocalPlace(String address, String name, LatLng latLng, String pincode) {
        this.address = address;
        this.name = name;
        this.latLng = latLng;
        this.pincode = pincode;
    }

    protected LocalPlace(Parcel in) {
        address = in.readString();
        name = in.readString();
        pincode = in.readString();
        latLng = in.readParcelable(LatLng.class.getClassLoader());
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(address);
        parcel.writeString(name);
        parcel.writeString(pincode);
        parcel.writeParcelable(latLng, i);
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}
