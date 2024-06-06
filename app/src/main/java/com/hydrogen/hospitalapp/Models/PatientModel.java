package com.hydrogen.hospitalapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class PatientModel implements Parcelable {


    private String PatientFullName;
    private int Age;
    private String Phone;
    private String District;
    private String Town;
    private String Gender;
    private String Password;

    public PatientModel(String patientFullName, int age, String phone, String district, String town, String gender, String password) {
        this.PatientFullName = patientFullName;
        this.Age = age;
        this.Phone = phone;
        this.District = district;
        this.Town = town;
        this.Gender = gender;
        this.Password = password;
    }

    protected PatientModel(Parcel in) {
        PatientFullName = in.readString();
        Age = in.readInt();
        Phone = in.readString();
        District = in.readString();
        Town = in.readString();
        Gender = in.readString();
        Password = in.readString();
    }

    public static final Creator<PatientModel> CREATOR = new Creator<PatientModel>() {
        @Override
        public PatientModel createFromParcel(Parcel in) {
            return new PatientModel(in);
        }

        @Override
        public PatientModel[] newArray(int size) {
            return new PatientModel[size];
        }
    };

    public String getPatientFullName() {
        return PatientFullName;
    }

    public void setPatientFullName(String patientFullName) {
        PatientFullName = patientFullName;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public String getTown() {
        return Town;
    }

    public void setTown(String town) {
        Town = town;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(PatientFullName);
        parcel.writeInt(Age);
        parcel.writeString(Phone);
        parcel.writeString(District);
        parcel.writeString(Town);
        parcel.writeString(Gender);
        parcel.writeString(Password);
    }
}
