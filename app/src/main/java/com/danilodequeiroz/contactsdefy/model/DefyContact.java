package com.danilodequeiroz.contactsdefy.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Danilo on 8/05/2015.
 */
public class DefyContact {
    private long mId;
    private String mName;
    public String mSurname;
    private Date mBirthDate;
    private String mCompanyName;
    private String mPhone;
    private String mMobilePhone;
    private String mWorkPhone;
    public DefyContact(String nName, String mPhone) {
        this.mName = mName;
        this.mPhone = mPhone;
    }

    public DefyContact() {

    }
    //                "CREATE TABLE tb_contacts(id LONG PRIMARY KEY AUTOINCREMENT, "
//                        + "name VARCHAR(75) NOT NULL, " + "surname VARCHAR(75), "
//                        + "birthdate VARCHAR(8) , "
//                        + "companyName VARCHAR(75) , "
//                        + "phone VARCHAR(25) NOT NULL, " + "mobilePhone VARCHAR(25), "
//                        + "workPhone VARCHAR(25);",
    public DefyContact(long id, String name, String surname, String birthday, String conpany, String phone, String mobile, String workphone) {
        mId = id;
        mName = name;
        mSurname = surname;
        if( !birthday.isEmpty()) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            try {
                mBirthDate = formatter.parse(birthday);
            } catch (ParseException e) {
                e.printStackTrace();
                mBirthDate = null;
            }
        }else{  mBirthDate = null; }
        mCompanyName = conpany;
        mPhone = phone;
        mMobilePhone = mobile;
        mWorkPhone = workphone;
    }


    public enum ColummValue{
        ID(0),NAME(1),SURNAME(2),BIRTHDATE(3),COMPANY(4),PHONE(5),MOBILEPHONE(6),WORKPHONE(7);

        private int value;
        ColummValue(int valor) {
            this.value = valor;
        }

        public int getValue() {
            return value;
        }
    }

    public long getmId() {
        return mId;
    }

    public void setmId(long mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmSurname() {
        return mSurname;
    }

    public void setmSurname(String mSurname) {
        this.mSurname = mSurname;
    }

    public Date getBirthDate() {
        return mBirthDate;
    }

    public String getBirthYyyyMMdd() {
        SimpleDateFormat birthFormat=new SimpleDateFormat("yyyy-MM-dd");
        String formatted = "";
        if(getBirthDate()!=null)
            formatted = birthFormat.format(getBirthDate());
        return formatted;
    }

    public String getBirthDdMMyyyy() {
        SimpleDateFormat birthFormat=new SimpleDateFormat("dd/MM/yyyy");
        String formatted = "";
        if(getBirthDate()!=null)
            formatted = birthFormat.format(getBirthDate());
        return formatted;
    }

    public void setBirthDate(Date mBirthDate) {
        this.mBirthDate = mBirthDate;
    }

    public void setBirthDdMMyyyy(String ddMMyyyy) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        try {
            this.setBirthDate(formatter.parse(ddMMyyyy.toString().replace('/', '-')));
        } catch (ParseException e) {
            e.printStackTrace();
        };
    }

    public String getCompanyName() {
        return mCompanyName;
    }

    public void setCompanyName(String mCompanyName) {
        this.mCompanyName = mCompanyName;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public String getMobilePhone() {
        return mMobilePhone;
    }

    public void setMobilePhone(String mMobilePhone) {
        this.mMobilePhone = mMobilePhone;
    }

    public String getWorkPhone() {
        return mWorkPhone;
    }

    public void setWorkPhone(String mWorkPhone) {
        this.mWorkPhone = mWorkPhone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DefyContact that = (DefyContact) o;

        return mId == that.mId;

    }

    @Override
    public int hashCode() {
        return (int) (mId ^ (mId >>> 32));
    }
}
