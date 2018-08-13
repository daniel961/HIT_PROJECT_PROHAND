package com.hit.daniel.hit_project_prohand;


import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.lang.System;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


public class SystemHomeFragment extends Fragment implements View.OnClickListener  {
    LinearLayout RegularUserGpsLayout,RegularUserChosseCategory,PROUserChosseCategory,UpdateProffesionalCategoryMSG,customerComments;

    TextView Title, LocationTv,CurrentMedals_tv;
    Button GpsBtn;
    Button btn0,btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btn10;//category Btns for regular users

    private List<RegularUser> regularUsers = new ArrayList<RegularUser>();
    private List<ProffesionalUser> proffesionalUsers = new ArrayList<ProffesionalUser>();

    private List<ProffesionalUser> proffesionalUsersToShow = new ArrayList<ProffesionalUser>();

    Button pbtn0,pbtn1,pbtn2,pbtn3,pbtn4,pbtn5,pbtn6,pbtn7,pbtn8,pbtn9,pbtn10;//category Btns for Proffisional users
    boolean isProffesionalUser = false, isProffesionalUserFirstAccsess = false, locationOk = false;
    String CurrentUid, adminArea = null, TAGs = "danielCheck   ";
    String medalsCounter;
    String UserInputCategory;
    boolean isDataBaseObtained=false;
    RecyclerView recyclerView;

    ProgressDialog progressDialog;
    View view;

    public SystemHomeFragment()  { //CTOR
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_system_home, container, false);
        final Context thisContext = getContext(); //get this context

        Title = (TextView) view.findViewById(R.id.tv_RegularUserText);
        GpsBtn = (Button) view.findViewById(R.id.btnGps);
        LocationTv = (TextView) view.findViewById(R.id.locationToShow);
        RegularUserGpsLayout = (LinearLayout)view.findViewById(R.id.RegularUserGpsLayout);
        RegularUserChosseCategory = (LinearLayout)view.findViewById(R.id.RegularUserChosseCategory);
        PROUserChosseCategory = (LinearLayout)view.findViewById(R.id.PROUserChosseCategory);
        UpdateProffesionalCategoryMSG = (LinearLayout)view.findViewById(R.id.UpdateProffesionalCategoryMSG);
        customerComments = (LinearLayout)view.findViewById(R.id.customerComments);
        CurrentMedals_tv = (TextView)view.findViewById(R.id.CurrentMedals_tv);



        //clear ArrayLists Because when re enter from menu Arrays have data
        regularUsers.clear();
        proffesionalUsers.clear();
        proffesionalUsersToShow.clear();
        getDataBases();//get data from database and fill the regularUsersArr and ProffesionalUsers Arr



        /*RegularUser Category Buttons*/
        btn0 = (Button)view.findViewById(R.id.btnCategory0); btn0.setOnClickListener(this); //calling onClick() method
        btn1 = (Button)view.findViewById(R.id.btnCategory1);btn1.setOnClickListener(this);
        btn2 = (Button)view.findViewById(R.id.btnCategory2);btn2.setOnClickListener(this);
        btn3 = (Button)view.findViewById(R.id.btnCategory3);btn3.setOnClickListener(this);
        btn4 = (Button)view.findViewById(R.id.btnCategory4);btn4.setOnClickListener(this);
        btn5 = (Button)view.findViewById(R.id.btnCategory5);btn5.setOnClickListener(this);
        btn6 = (Button)view.findViewById(R.id.btnCategory6);btn6.setOnClickListener(this);
        btn7 = (Button)view.findViewById(R.id.btnCategory7);btn7.setOnClickListener(this);
        btn8 = (Button)view.findViewById(R.id.btnCategory8);btn8.setOnClickListener(this);
        btn9 = (Button)view.findViewById(R.id.btnCategory9);btn9.setOnClickListener(this);
        btn10 = (Button)view.findViewById(R.id.btnCategory10);btn10.setOnClickListener(this);


        /*PROFESIONAL USER Category Buttons*/
        pbtn0 = (Button)view.findViewById(R.id.PRObtnCategory0);pbtn0.setOnClickListener(this); //calling onClick() method
        pbtn1 = (Button)view.findViewById(R.id.PRObtnCategory1);pbtn1.setOnClickListener(this);
        pbtn2 = (Button)view.findViewById(R.id.PRObtnCategory2);pbtn2.setOnClickListener(this);
        pbtn3 = (Button)view.findViewById(R.id.PRObtnCategory3);pbtn3.setOnClickListener(this);
        pbtn4 = (Button)view.findViewById(R.id.PRObtnCategory4);pbtn4.setOnClickListener(this);
        pbtn5 = (Button)view.findViewById(R.id.PRObtnCategory5);pbtn5.setOnClickListener(this);
        pbtn6 = (Button)view.findViewById(R.id.PRObtnCategory6);pbtn6.setOnClickListener(this);
        pbtn7 = (Button)view.findViewById(R.id.PRObtnCategory7);pbtn7.setOnClickListener(this);
        pbtn8 = (Button)view.findViewById(R.id.PRObtnCategory8);pbtn8.setOnClickListener(this);
        pbtn9 = (Button)view.findViewById(R.id.PRObtnCategory9);pbtn9.setOnClickListener(this);
        pbtn10 = (Button)view.findViewById(R.id.PRObtnCategory10);pbtn10.setOnClickListener(this);







        /*get Data From System Intent Bundle*/
        String strtext = getArguments().getString("strToPass");
        isProffesionalUser = getArguments().getBoolean("isProffesionalUser");
        isProffesionalUserFirstAccsess = getArguments().getBoolean("isProffesionalUserFirstLogin");
        CurrentUid = getArguments().getString("CurrentUid");
        if(isProffesionalUser == true){
            medalsCounter = getArguments().getString("medals");
            //bundle.putString("medals",currentProfesionalObject.getMedals());
        }










        if (isProffesionalUser == false) {

            RegularUserGpsLayout.setVisibility(View.VISIBLE); //make is visble only if needs
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);//reuest permision
            progressDialog = new ProgressDialog(thisContext);
            progressDialog.setTitle("מאתר מיקומך");
            progressDialog.setMessage(" ");
            progressDialog.setCancelable(false);
            progressDialog.show();




            GPStracker gpStracker = new GPStracker(thisContext);
            Location location;
            location = gpStracker.getLocation();
            if (location != null){
                double longtitude = location.getLongitude();
                double latitude = location.getLatitude();
                Geocoder geocoder = new Geocoder(thisContext);

                if(latitude < 30.5){
                    locationOk = false; //the location is downstair mizpe ramon and dont served yet.
                }else{
                    locationOk = true;
                }

                try {
                    List<Address> list = geocoder.getFromLocation(latitude,longtitude,1);
                    adminArea = list.get(0).getAdminArea();

                    java.lang.System.out.println(TAGs + adminArea);
                    java.lang.System.out.println(TAGs);


                    switch (adminArea.toString()){
                        case "Center District":  adminArea = "מרכז";
                            break;
                        case "South District":  adminArea = "דרום";
                            break;
                        case "North District":  adminArea = "צפון";
                            break;
                        case "Jerusalem District":  adminArea = "ירושלים";
                            break;
                        case "מחוז המרכז":  adminArea = "מרכז";
                            break;
                        case "מחוז הצפון":  adminArea = "צפון";
                            break;
                        case "מחוז ירושלים":  adminArea = "ירושלים";
                            break;
                        case "מחוז דרום":  adminArea = "דרום";
                            break;
                        case "מחוז תל אביב":  adminArea = "מרכז";
                            break;
                        case "מחוז חיפה":  adminArea = "צפון";
                            break;
                        default:
                            adminArea = "איזור לא נתמך";
                        locationOk = false;
                            break;
                    }

                    java.lang.System.out.println(TAGs + adminArea);

                    if(locationOk == true){
                        //LocationTv.setText(adminArea);
                        /*IF WE REACH HERE WE HAVE ALL WE NEED ABOUT USER LOCATION*/
                        Toast.makeText(thisContext, "המיקום שזוהה "+ adminArea, Toast.LENGTH_SHORT).show();
                        RegularUserGpsLayout.setVisibility(View.GONE); //dismiss GPS Layout
                        RegularUserChosseCategory.setVisibility(View.VISIBLE);



                    }else{
                        LocationTv.setTextSize(15);
                        GpsBtn.setVisibility(View.GONE);
                        Title.setText("אנו מצטערים, איזורך לא נמצא בשירות כעת");
                        Title.setTextSize(18);
                        LocationTv.setText("המערכת זיהתה שמיקומך דרומית ממצפה רמון\n" +
                                "איזור זה נמצא כרגע מחוץ לתחום השירות שלנו\n" +
                                "נשמח לשרתך בעתיד הישאר מעודכן...");

                    }


                }catch (Exception e) {
                    /*if we get here the geocoder give us zero results */

                e.printStackTrace();
                    locationOk = false;
                    LocationTv.setTextSize(15);
                    GpsBtn.setVisibility(View.GONE);
                    Title.setText("אנו מצטערים, איזורך לא נמצא בשירות כעת");
                    Title.setTextSize(18);
                    LocationTv.setText("המערכת זיהתה שמיקומך לא נמצא בשירותינו\n" +
                            "איזור זה נמצא כרגע מחוץ לתחום השירות שלנו\n" +
                            "נשמח לשרתך בעתיד הישאר מעודכן...");
                    progressDialog.dismiss();



            }
                progressDialog.dismiss();



            }else{

                LocationTv.setText(" לא הייתה אפשרות לאתר מיקום \n" +
                        "צא למקום פתוח \n" +
                        "וודא שה-GPS פועל");

                progressDialog.dismiss();



                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                dialog.dismiss();
                                break;

                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(thisContext);
                builder.setMessage("וודא שאת/ה נמצא/ת במקום פתוח לקליטת GPS").setTitle("שגיאת איתור מיקום").setPositiveButton("סגור", dialogClickListener)
                        .show();
            }


            /*Button Check Location*/
            GpsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog = new ProgressDialog(thisContext);
                    progressDialog.setTitle("מאתר מיקומך");
                    progressDialog.setMessage(" ");
                    progressDialog.setCancelable(false);
                    progressDialog.show();




                    GPStracker gpStracker = new GPStracker(thisContext);
                    Location location;
                    location = gpStracker.getLocation();
                    if (location != null){
                        double longtitude = location.getLongitude();
                        double latitude = location.getLatitude();
                        Geocoder geocoder = new Geocoder(thisContext);//

                        if(latitude < 30.5){
                            locationOk = false; //the location is downstair mizpe ramon and dont served yet.
                        }else{
                            locationOk = true;
                        }

                        try {
                            List<Address> list = geocoder.getFromLocation(latitude,longtitude,1);//ariel coordinates
                            adminArea = list.get(0).getAdminArea();

                            java.lang.System.out.println(TAGs + adminArea);
                            java.lang.System.out.println(TAGs);


                            switch (adminArea.toString()){
                                case "Center District":  adminArea = "center";
                                    break;
                                case "South District":  adminArea = "south";
                                    break;
                                case "North District":  adminArea = "north";
                                    break;
                                case "Jerusalem District":  adminArea = "Jerusalem";
                                    break;
                                case "מחוז המרכז":  adminArea = "center";
                                    break;
                                case "מחוז הצפון":  adminArea = "north";
                                    break;
                                case "מחוז ירושלים":  adminArea = "Jerusalem";
                                    break;
                                case "מחוז דרום":  adminArea = "south";
                                    break;
                                case "מחוז תל אביב":  adminArea = "center";
                                    break;
                                case "מחוז חיפה":  adminArea = "north";
                                    break;
                                default:
                                    adminArea = "איזור לא נתמך";
                                    locationOk = false;
                                    break;
                            }

                            java.lang.System.out.println(TAGs + adminArea);

                            if(locationOk == true){
                                //LocationTv.setText(adminArea);
                                /*IF WE REACH HERE WE HAVE ALL WE NEED ABOUT USER LOCATION*/
                                Toast.makeText(thisContext, "המיקום שזוהה "+ adminArea, Toast.LENGTH_SHORT).show();
                                RegularUserGpsLayout.setVisibility(View.GONE); //dismiss GPS Layout
                                RegularUserChosseCategory.setVisibility(View.VISIBLE);
                            }else{
                                LocationTv.setTextSize(15);
                                GpsBtn.setVisibility(View.GONE);
                                Title.setText("אנו מצטערים, איזורך לא נמצא בשירות כעת");
                                Title.setTextSize(18);
                                LocationTv.setText("המערכת זיהתה שמיקומך דרומית ממצפה רמון\n" +
                                        "איזור זה נמצא כרגע מחוץ לתחום השירות שלנו\n" +
                                        "נשמח לשרתך בעתיד הישאר מעודכן...");

                            }


                        }catch (Exception e) {


                            e.printStackTrace();
                            locationOk = false;
                            LocationTv.setTextSize(15);
                            GpsBtn.setVisibility(View.GONE);
                            Title.setText("אנו מצטערים, איזורך לא נמצא בשירות כעת");
                            Title.setTextSize(18);
                            LocationTv.setText("המערכת זיהתה שמיקומך לא נמצא בשירותינו\n" +
                                    "איזור זה נמצא כרגע מחוץ לתחום השירות שלנו\n" +
                                    "נשמח לשרתך בעתיד הישאר מעודכן...");
                            progressDialog.dismiss();



                        }
                        progressDialog.dismiss();
                    }else{

                        LocationTv.setText(" לא הייתה אפשרות לאתר מיקום \n" +
                                "צא למקום פתוח \n" +
                                "וודא שה-GPS פועל");

                        progressDialog.dismiss();



                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        dialog.dismiss();
                                        break;

                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(thisContext);
                        builder.setMessage("וודא שאת/ה נמצא/ת במקום פתוח לקליטת GPS").setTitle("שגיאת איתור מיקום").setPositiveButton("סגור", dialogClickListener)
                                .show();
                    }







                }
            });


            /*After Getting Location*/





        }











            if (isProffesionalUser == true && isProffesionalUserFirstAccsess == false) { //old pro User
                //todo only read bikorot on Your SELF
                 customerComments.setVisibility(View.VISIBLE);
                 CurrentMedals_tv.setText(medalsCounter);



            }


            if (isProffesionalUser == true && isProffesionalUserFirstAccsess == true) {//new pro user //todo new pro user to choose category
                // CHOSSE  CATEGORY
                //Toast.makeText(thisContext, "מקצוען כניסה ראשונה ", Toast.LENGTH_SHORT).show();
                PROUserChosseCategory.setVisibility(View.VISIBLE);
                //after the pro choose category its update inside the database




            }


            // Inflate the layout for this fragment
        return view;

        }







    //Btns Functions
    //--------------------------------------------------------------------------------------------------------------------
    @Override
    public void onClick(View v) { //GET ALL BTNS HERE
        switch (v.getId()) {


            //*Regular user Swich case choose category *//
            case R.id.btnCategory0:
                UserInputCategory = "1";
                DialogChoose("המערכת תחפש עבורך טכנאי מזגנים באיזור " + adminArea);
                break;
            case R.id.btnCategory1:
                UserInputCategory = "2";
                DialogChoose("המערכת תחפש עבורך טכנאי מקררים באיזור " + adminArea);
                break;
            case R.id.btnCategory2:
                UserInputCategory = "3";
                DialogChoose("המערכת תחפש עבורך טכנאי מכונות כביסה באיזור " + adminArea);
                break;
            case R.id.btnCategory3:
                UserInputCategory = "4";
                DialogChoose("המערכת תחפש עבורך טכנאי תריסים חשמליים באיזור " + adminArea);
                break;
            case R.id.btnCategory4:
                UserInputCategory = "5";
                DialogChoose("המערכת תחפש עבורך טכנאי מחשבים באיזור " + adminArea);
                break;
            case R.id.btnCategory5:
                UserInputCategory = "6";
                DialogChoose("המערכת תחפש עבורך טכנאי מצלמות באיזור " + adminArea);
                break;
            case R.id.btnCategory6:
                UserInputCategory = "7";
                DialogChoose("המערכת תחפש עבורך טכנאי מדיחי כלים באיזור " + adminArea);
                break;
            case R.id.btnCategory7:
                UserInputCategory = "8";
                DialogChoose("המערכת תחפש עבורך טכנאיי מוצרי חשמל כללי באיזור " + adminArea);
                break;
            case R.id.btnCategory8:
                UserInputCategory = "9";
                DialogChoose("המערכת תחפש עבורך טכנאי סלולר באיזור " + adminArea);
                break;
            case R.id.btnCategory9:
                UserInputCategory = "10";
                DialogChoose("המערכת תחפש עבורך טכנאי תנורים באיזור " + adminArea);
                break;
            case R.id.btnCategory10:
                UserInputCategory = "11";
                DialogChoose("המערכת תחפש עבורך טכנאי טלוויזיות באיזור " + adminArea);
                break;


            //*PRO!!! user Swich case choose category of his skills*//

            //todo add new function DIALOG
            case R.id.PRObtnCategory0:
                UserInputCategory = "1";
                DialogChooseForPro(UserInputCategory);
                break;
            case R.id.PRObtnCategory1:
                UserInputCategory = "2";
                DialogChooseForPro(UserInputCategory);
                break;
            case R.id.PRObtnCategory2:
                UserInputCategory = "3";
                DialogChooseForPro(UserInputCategory);
                break;
            case R.id.PRObtnCategory3:
                UserInputCategory = "4";
                DialogChooseForPro(UserInputCategory);
                break;
            case R.id.PRObtnCategory4:
                UserInputCategory = "5";
                DialogChooseForPro(UserInputCategory);
                break;
            case R.id.PRObtnCategory5:
                UserInputCategory = "6";
                DialogChooseForPro(UserInputCategory);
                break;
            case R.id.PRObtnCategory6:
                UserInputCategory = "7";
                DialogChooseForPro(UserInputCategory);
                break;
            case R.id.PRObtnCategory7:
                UserInputCategory = "8";
                DialogChooseForPro(UserInputCategory);
                break;
            case R.id.PRObtnCategory8:
                UserInputCategory = "9";
                DialogChooseForPro(UserInputCategory);
                break;
            case R.id.PRObtnCategory9:
                UserInputCategory = "10";
                DialogChooseForPro(UserInputCategory);
                break;
            case R.id.PRObtnCategory10:
                UserInputCategory = "11";
                DialogChooseForPro(UserInputCategory);
                break;









        }


    }

    //--------------------------------------------------------------------------------------------------------------------
    void DialogChoose(String Input){//makes dialog for Regular user Chosser
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        fixLocation();
                        //close Category Layout
                        //todo show to the regular user Proffisional users with the correct skills in recycler view
                        //takes the proArrayList and Decode the best for the regular user chooise and create a new list for him
                        //UserInputCategory
                        //adminArea
                        //proffesionalUsersToShow
                        for (int i=0;i<proffesionalUsers.size();i++){ //run on the list and check for correct properties
                            if(proffesionalUsers.get(i).getArea().equals(adminArea) &&
                               proffesionalUsers.get(i).getCategory().equals(UserInputCategory)){
                                //Toast.makeText(getContext(), "found match" + i , Toast.LENGTH_SHORT).show();
                               proffesionalUsersToShow.add(proffesionalUsers.get(i));

                            }

                        }

                            //if there is no ProUsers as you wanted to look for
                        if (proffesionalUsersToShow.isEmpty()){
                            Toast.makeText(getContext(), "לא נמצאו אנשי מקצוע לפי בקשותיך", Toast.LENGTH_SHORT).show();

                        }else{
                            /*Sort The array By medals*/
                            Collections.sort(proffesionalUsersToShow, new Comparator<ProffesionalUser>() {
                                @Override
                                public int compare(ProffesionalUser o1, ProffesionalUser o2) {
                                    return o2.getMedals().compareTo(o1.getMedals());
                                }
                            });



                            RegularUserChosseCategory.setVisibility(View.GONE);
                            //todo Call function to make the RecyclerView
                            for(int i =0 ;i<proffesionalUsersToShow.size();i++){
                                System.out.println(TAGs + proffesionalUsersToShow.get(i).getName());
                            }
                            RecyclerViewSetup();
                        }









                        break;
                     case DialogInterface.BUTTON_NEGATIVE:
                         dialog.dismiss();

                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(Input).setTitle("חיפוש מערכת:").setPositiveButton("כן", dialogClickListener).
                setNegativeButton("לא",dialogClickListener)
                .show();
    }

    //--------------------------------------------------------------------------------------------------------------------

    //--------------------------------------------------------------------------------------------------------------------
    void DialogChooseForPro(final String Input){//makes dialog for Proffesional users Chosser
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        getCurrentProUserAndPutCategoryToDataBase(Input);//call function to update category in database
                        PROUserChosseCategory.setVisibility(View.GONE);
                        //close Category Layout
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();

                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("האם הכל נכון?").setTitle("המערכת תכניס אותך למאגר טכנאים מסוג שבחרת").setPositiveButton("כן", dialogClickListener).
                setNegativeButton("לא",dialogClickListener)//todo add  more discription about which Category i pressed
                .show();
    }



    //--------------------------------------------------------------------------------------------------------------------

    private void getCurrentProUserAndPutCategoryToDataBase(final String Category){ //update data in the database

        //procces dialog
        //final int cat = Integer.parseInt(Category);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref;
            myref = database.getReference();
            myref.child("server").child("ProffesionalUsers").child(CurrentUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    dataSnapshot.getRef().child("category").setValue(Category);


                    //todo ask the user to exit and REenter the app for update
                    UpdateProffesionalCategoryMSG.setVisibility(View.VISIBLE);


                }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getContext(), "אירעה שגיאה בקבלת הנתונים מהשרת נסה מאוחר יותר", Toast.LENGTH_SHORT).show();
                }
            });







    }

    //--------------------------------------------------------------------------------------------------------------------
    /*download databases and store it in Accounts Array*/
//-----------------------------------------------------------------------------------------------------
    private void getDataBases(){


        //procces dialog
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("server").child("RegularUsers");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,Object> users = (Map<String,Object>) dataSnapshot.getValue();
                String email,password,phone,name,uid;
                int i=0;
                for (Map.Entry<String,Object> entry : users.entrySet()){

                    //Get user map
                    Map singleUser = (Map)entry.getValue();
                    //Get ALL field to strings and append to list
                    email = singleUser.get("email").toString();
                    password = singleUser.get("password").toString();
                    name = singleUser.get("name").toString();
                    phone = singleUser.get("phone").toString();
                    uid = singleUser.get("uid").toString();
                    //adding the regular user to the list from the strings we achived
                    regularUsers.add(new RegularUser(email,password,name,phone));
                    regularUsers.get(i).setUid(uid); //set the uid to the index
                    i++;

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "משהו השתבש בקריאת הנתונים מהשרת אנא נסה מאוחר יותר", Toast.LENGTH_SHORT).show();
            }
        });


        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("server").child("ProffesionalUsers");
        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,Object> users = (Map<String,Object>) dataSnapshot.getValue();
                String email,password,phone,name,uid,category,area,medals;
                int i=0;
                for (Map.Entry<String,Object> entry : users.entrySet()){

                    //Get user map
                    Map singleUser = (Map)entry.getValue();
                    //Get ALL field to strings and append to list
                    email = singleUser.get("email").toString();
                    password = singleUser.get("password").toString();
                    name = singleUser.get("name").toString();
                    phone = singleUser.get("phone").toString();
                    uid = singleUser.get("uid").toString();
                    area = singleUser.get("area").toString();
                    category = singleUser.get("category").toString();
                    medals = singleUser.get("medals").toString();

                    //adding the proLIST user to the list from the strings we achived
                    proffesionalUsers.add(new ProffesionalUser(email,password,name,phone,area));
                    proffesionalUsers.get(i).setUid(uid); //set the uid to the index
                    proffesionalUsers.get(i).setCategory(category);
                    proffesionalUsers.get(i).setMedals(medals);
                    i++;

                }


                isDataBaseObtained = true;






            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "משהו השתבש בקריאת הנתונים מהשרת אנא נסה מאוחר יותר", Toast.LENGTH_SHORT).show();
            }
        });





    }
    //-----------------------------------------------------------------------------------------------------
        public void RecyclerViewSetup(){
        //todo here the Recycler View setUp


            recyclerView = view.findViewById(R.id.recyclerView);
            recyclerView.setVisibility(View.VISIBLE);
            RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter((ArrayList<ProffesionalUser>)proffesionalUsersToShow,getContext());
            recyclerView.setAdapter(recyclerViewAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


            //pbtn0 = (Button)view.findViewById(R.id.PRObtnCategory0);pbtn0.setOnClickListener(this);
        }

    //--------------------------------------------------------------------------------------------------------------------
    public void fixLocation(){
        switch (adminArea.toString()){
            case "center":  adminArea = "center";
                break;
            case "south":  adminArea = "south";
                break;
            case "north":  adminArea = "north";
                break;
            case "Jerusalem":  adminArea = "Jerusalem";
                break;

            case "מרכז":  adminArea = "center";
                break;
            case "המרכז":  adminArea = "center";
                break;
            case "דרום":  adminArea = "south";
                break;
            case "הדרום":  adminArea = "south";
                break;

            case "צפון":  adminArea = "north";
                break;
            case "הצפון":  adminArea = "north";
                break;
            case "ירושלים":  adminArea = "Jerusalem";
                break;
            default:
                adminArea = "איזור לא נתמך";
                locationOk = false;
                break;
        }
    }


    //--------------------------------------------------------------------------------------------------------------------
  }






