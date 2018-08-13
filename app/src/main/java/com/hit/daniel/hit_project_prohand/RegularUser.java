package com.hit.daniel.hit_project_prohand;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class RegularUser {

    private String Email;
    private String Password;
    private String Name;
    private String Phone;
    private String Uid;

    RegularUser(String Email,String Password,String Name,String Phone){ //constructor
        this.Email = Email;
        this.Password = Password;
        this.Name = Name;
        this.Phone = Phone;

    }


    //Saving Person to the dataBase key = uid
    public void SaveToDatabase(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref;
        DatabaseReference usersRef;


        ref = database.getReference("server/RegularUsers");

        usersRef = ref.child(this.Uid);
        usersRef.setValue(this);

    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
















}
