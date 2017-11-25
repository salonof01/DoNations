package com.onofre.salvador.donations;

/**
 * Created by MAIN-ONO on 10/8/2017.
 */

public class Donation {


    String  address;
    String  name;
    String donateTo;
    String description;
    String email;
    String phone;
    String category;
    String url;
    String key;

    public Donation(){




    }


    public Donation(String address, String name, String donateTo, String description, String email, String phone, String category, String url ) {
        this.address = address;
        this.name = name;
        this.donateTo = donateTo;
        this.description = description;
        this.email = email;
        this.phone = phone;
        this.category = category;
        this.url = url;
        //this.key = key;
    }
    public String getCategory( ){return category;}
    public String getAddress() {
        return address;
    }
    public String getUrl() { return url;
    }
 // public String getKey(){return key;}
    public String getName() {
        return name;
    }

    public String getDonateTo() {
        return donateTo;
    }

    public String getDescription() {
        return description;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }



}
