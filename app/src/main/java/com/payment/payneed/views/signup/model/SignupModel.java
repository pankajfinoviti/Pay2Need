package com.payment.payneed.views.signup.model;


import android.os.Parcel;
import android.os.Parcelable;

public class SignupModel implements Parcelable {
    private String Name;
    private String Mobile;
    private String Email;
    private String Shop;
    private String Pan;
    private String Aadhar;
    private String State;
    private String City;
    private String Address;
    private String Pincode;
    private String slug ;
    private String slugPrice ;

    public SignupModel() {
    }

    protected SignupModel(Parcel in) {
        Name = in.readString();
        Mobile = in.readString();
        Email = in.readString();
        Shop = in.readString();
        Pan = in.readString();
        Aadhar = in.readString();
        State = in.readString();
        City = in.readString();
        Address = in.readString();
        Pincode = in.readString();
        slug = in.readString();
        slugPrice = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Name);
        dest.writeString(Mobile);
        dest.writeString(Email);
        dest.writeString(Shop);
        dest.writeString(Pan);
        dest.writeString(Aadhar);
        dest.writeString(State);
        dest.writeString(City);
        dest.writeString(Address);
        dest.writeString(Pincode);
        dest.writeString(slug);
        dest.writeString(slugPrice);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SignupModel> CREATOR = new Creator<SignupModel>() {
        @Override
        public SignupModel createFromParcel(Parcel in) {
            return new SignupModel(in);
        }

        @Override
        public SignupModel[] newArray(int size) {
            return new SignupModel[size];
        }
    };

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getShop() {
        return Shop;
    }

    public void setShop(String shop) {
        Shop = shop;
    }

    public String getPan() {
        return Pan;
    }

    public void setPan(String pan) {
        Pan = pan;
    }

    public String getAadhar() {
        return Aadhar;
    }

    public void setAadhar(String aadhar) {
        Aadhar = aadhar;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPincode() {
        return Pincode;
    }

    public void setPincode(String pincode) {
        Pincode = pincode;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getSlugPrice() {
        return slugPrice;
    }

    public void setSlugPrice(String slugPrice) {
        this.slugPrice = slugPrice;
    }
}
