package com.hydrogen.hospitalapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class UserModel implements Parcelable {

    private int id;
    private String username;
    private String name;
    private String email;
    private String gender;
    private String district;
    private String zone;
    private int age;
    private String password;


    public UserModel(int id, String username, String name, String email, String gender, String district, String zone, int age, String password) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.district = district;
        this.zone = zone;
        this.age = age;
        this.password = password;
    }

    public UserModel(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    protected UserModel(Parcel in) {
        id = in.readInt();
        username = in.readString();
        name = in.readString();
        email = in.readString();
        gender = in.readString();
        district = in.readString();
        zone = in.readString();
        age = in.readInt();
        password = in.readString();
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(username);
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(gender);
        parcel.writeString(district);
        parcel.writeString(zone);
        parcel.writeInt(age);
        parcel.writeString(password);
    }
}
