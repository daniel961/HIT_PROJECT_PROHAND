package com.hit.daniel.hit_project_prohand;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class ProffesionalUser {

    private String Email;
    private String Password;
    private String Name;
    private String Phone;
    private String Area;
    private String Category;
    private String Medals;
    private String Uid;

    public ProffesionalUser(String Email, String Password, String Name, String Phone, String Area) {
        this.Email = Email;
        this.Password = Password;
        this.Name = Name;
        this.Phone = Phone;
        this.Area = Area;
        this.Category = "0"; //init when first connected
        this.Medals = "0";//init by users :)

    }



    //Saving Person to the dataBase key = uid
    public void SaveToDatabase(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref;
        DatabaseReference usersRef;



        ref = database.getReference("server/ProffesionalUsers");

        usersRef = ref.child(Uid);
        usersRef.setValue(this);


    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
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

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getMedals() { return Medals; }

    public void setMedals(String medals) { Medals = medals; }

}
