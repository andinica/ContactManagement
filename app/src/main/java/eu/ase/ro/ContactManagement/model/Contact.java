package eu.ase.ro.ContactManagement.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Contact implements Parcelable {



    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private String birthday;



    public Contact(String firstName, String lastName, String phoneNumber, String address, String birthday) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.birthday = birthday;
    }



    protected Contact(Parcel parcel) {
        this.firstName = parcel.readString();
        this.lastName = parcel.readString();
        this.phoneNumber = parcel.readString();
        this.address = parcel.readString();
        this.birthday = parcel.readString();
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };


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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "Contact: Name:" + firstName + " " + lastName + ", phone number " + phoneNumber + ", address:  " + address + ", birthday: " + birthday;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(phoneNumber);
        parcel.writeString(address);
        parcel.writeString(birthday);
    }
}
