package com.hit.daniel.hit_project_prohand;

import android.app.ActivityManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build.*;
import android.app.FragmentTransaction;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;


public class System extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FragmentTransaction fragmentTransaction;
    Toolbar toolbar;
    ActionBarDrawerToggle mToggle;
    boolean isProffesionalUser = false,isProffesionalUserFirstLogin = false;
    private List<RegularUser> regularUsers = new ArrayList<RegularUser>();
    private List<ProffesionalUser> proffesionalUsers = new ArrayList<ProffesionalUser>();
    private boolean isDataBaseObtained = false;
    ProgressDialog progressDialog;
    String CurrentUid=null,TAG="danieltest9611652:     ";
    int medalsCount;
    RegularUser currentRegularObject = null;
    ProffesionalUser currentProfesionalObject = null;
    TextView tvWelcome,header_userDetails_tv;
    SystemHomeFragment fragobj;
    RelativeLayout AppSettingsLayout,SystemLayout,AccountSettingsLayout;

    boolean isAppSettingsOpen = false;
    boolean isAccountSettingsOpen = false;
    boolean CurrntTheme = true;//true mean Black Theme
    Button BlackThemeBtn,BlueThemeBtn,SubmitNameAndPhoneChange_btn;
    EditText nameToChange_et,phoneToChange_et;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system);
        setUpToolBar(); //function setUp the toolbar and drawer
        getCurrentUserUid(savedInstanceState); //get the exra from login intent
        tvWelcome = (TextView)findViewById(R.id.tvWelcome);

        AppSettingsLayout = (RelativeLayout)findViewById(R.id.AppSettingsLayout); //local layout
        AccountSettingsLayout = (RelativeLayout)findViewById(R.id.AccountSettingsLayout); //local layout
        SystemLayout = (RelativeLayout)findViewById(R.id.SystemLayout); //local layout
        BlackThemeBtn = (Button)findViewById(R.id.BlackThemeBtn);//local button
        BlueThemeBtn = (Button)findViewById(R.id.BlueThemeBtn);//local button

        //AccountSettingsLayout vars
        nameToChange_et = (EditText)findViewById(R.id.nameToChange_et);//local ET
        phoneToChange_et = (EditText)findViewById(R.id.phoneToChange_et); //local ET
        SubmitNameAndPhoneChange_btn = (Button)findViewById(R.id.SubmitNameAndPhoneChange_btn);//local button submit for Account settings



        //todo get reference to text view inside header
        /*
        View headerLayout = navigationView.inflateHeaderView(R.layout.navigation_drawer_header);
        header_userDetails_tv = (TextView)headerLayout.findViewById(R.id.header_userDetails_tv);
        header_userDetails_tv.setText("check one two");
        */


        //////////////////////////////////////////////////////////////////////////////////
        getDataBases(); //function getDataBases of RegularUsers and Profesional users to Array Lists from Firebase
                        //put the decoder inside getDataBases method and the first fragment shows
        //todo check how to wait untill this function ends his job! (Critical thing!.)
        progressDialog = new ProgressDialog(System.this);
        progressDialog.setTitle("מאחזר נתונים מהשרת");
        progressDialog.setMessage("אנא המתן בזמן איחזור הנתונים...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                progressDialog.dismiss();

            }
        }, 3000);

        //todo 1. install (first) home fragment in any cases



     //////////////////////////////////////////////////////////////////////////////////






        //todo 2. download database to Array lists (Regular users Array) and (Professional users Arrray)




        //todo 3. build Decoder check which user is it (pro or regular) and if its Pro First entery
        //todo 3.1 if its proffesional user check if its his first login (by checking if  his category already filled)


/*
* AccountSettingsLayout.setVisibility(View.VISIBLE);
            isAccountSettingsOpen = true;
* */


        //todo 1.1 link the drawerMenu Buttons to the relevant content
        navigationView = (NavigationView) findViewById(R.id.menuDrawer);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        //todo send Bundle information to fragment
                        AccountSettingsLayout.setVisibility(View.GONE);
                        isAccountSettingsOpen = false;
                        AppSettingsLayout.setVisibility(View.GONE);
                        isAppSettingsOpen=false;
                        openCurrectHomeFragment(); //function test if its Regular user or newPro/Profesional user and replace to current fragment
                        break;
                    case R.id.updateAccount:
                        AppSettingsLayout.setVisibility(View.GONE);
                        isAppSettingsOpen=false;
                        UpdateAccountSettings();

                        break;
                    case R.id.appSettings:
                        AccountSettingsLayout.setVisibility(View.GONE);
                        isAccountSettingsOpen = false;
                        ApplicationSettings();

                        break;
                    case R.id.AboutUs:
                        openAbout();//open dialog Box
                        break;
                    case R.id.disconectUser:
                        dcDialogUser();//clear all data and must reEnter APP
                        break;
                    case R.id.exit:
                        onBackPressed();// dialog ask for exit
                        break;
                }



                return false;
            }
        });


        //todo 4 make the content relevent to the kind of user
        //todo 5 check if GPS is open and if not ask to open it and take the cordinates and translate them to City and Street







    }

    //-----------------------------------------------------------------------------------------------------
    //function setUp the toolbar and drawer
    private void setUpToolBar(){
        drawerLayout = (DrawerLayout)findViewById(R.id.RootSystemDrawerLayout);
        toolbar = (Toolbar) findViewById(R.id.SystemToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(""); //bypass the defult makes title of the app

        mToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.open);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.menuDrawer);
        navigationView.bringToFront();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //-----------------------------------------------------------------------------------------------------

    private void dcDialogUser(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        dcUser();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("אם כן, לאחר מכן פתח מחדש את האפליקציה").setTitle("האם אתה בטוח שאתה רוצה להתנתק?").setPositiveButton("כן", dialogClickListener)
                .setNegativeButton("לא!", dialogClickListener).show();

    }
//-----------------------------------------------------------------------------------------------------

    private void dcUser(){
        ((ActivityManager) this.getSystemService(this.ACTIVITY_SERVICE)).clearApplicationUserData(); //delete user data app
        Intent LoginIntent = new Intent(System.this, LoginScreen.class); //start login activity
        System.this.startActivity(LoginIntent);
        System.this.finish();
    }
//-----------------------------------------------------------------------------------------------------


    @Override
    public void onBackPressed() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        finish();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("").setTitle("האם אתה בטוח שאתה רוצה לצאת מהאפליקציה?").setPositiveButton("כן", dialogClickListener)
                .setNegativeButton("לא!", dialogClickListener).show();

    }
    //-----------------------------------------------------------------------------------------------------

    private void openAbout(){
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

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(" האפליקציה הוקמה כחלק מפרוייקט לתעשייה במכון הטכנולוגי H.I.T" +
                " השימוש באפליקציה פתוח לקהל הרחב בחינם" +
                " שמות המפתחים דניאל כהן danielglx961@gmail.com" +
                " ושני סיוני shaniS@gmail.com").setTitle("מפתחי האפליקציה").setPositiveButton("סגור", dialogClickListener)
                .show();

    }
    //-----------------------------------------------------------------------------------------------------
    private void openCurrectHomeFragment(){
        if(isProffesionalUser == false){ //so its a regular user
            //do here Home fragment for regular users
            getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer,fragobj).commit();
        }
        else if(isProffesionalUser == true && isProffesionalUserFirstLogin == false){//so its a proffesional user
            getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer,fragobj).commit();
        }
        else if(isProffesionalUser == true && isProffesionalUserFirstLogin == true){ //so its a new proffesional user
            getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer,fragobj).commit();
        }

    }
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
                        Toast.makeText(System.this, "משהו השתבש בקריאת הנתונים מהשרת", Toast.LENGTH_SHORT).show();
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

                    //adding the regular user to the list from the strings we achived
                    proffesionalUsers.add(new ProffesionalUser(email,password,name,phone,area));
                    proffesionalUsers.get(i).setUid(uid); //set the uid to the index
                    proffesionalUsers.get(i).setCategory(category);
                    proffesionalUsers.get(i).setMedals(medals);
                    i++;

                }


                isDataBaseObtained = true;

                Decoder();//this function check which user is it and save the data
                if(isProffesionalUser == false) {
                    tvWelcome.setText("שלום " +currentRegularObject.getName().toString());
                    //header_userDetails_tv.setText(currentRegularObject.getName().toString());


                    Bundle bundle = new Bundle(); //simple String


                    bundle.putString("strToPass", CurrentUid + currentRegularObject.getName());
                    bundle.putString("CurrentUid",CurrentUid);
                    bundle.putBoolean("isProffesionalUser",isProffesionalUser);
                    bundle.putBoolean("isProffesionalUserFirstLogin",isProffesionalUserFirstLogin);
                    /*
                    if(isProffesionalUser == true){
                        bundle.putString("medals",currentProfesionalObject.getMedals());


                    }
                    */



                    fragobj = new SystemHomeFragment();
                    fragobj.setArguments(bundle);


                }


                if(isProffesionalUser == true){
                    tvWelcome.setText("שלום " +currentProfesionalObject.getName().toString());
                    //header_userDetails_tv.setText(currentProfesionalObject.getName().toString());

                    Bundle bundle = new Bundle(); //simple String



                    bundle.putString("strToPass", CurrentUid + currentProfesionalObject.getName());
                    bundle.putString("CurrentUid",CurrentUid);
                    bundle.putBoolean("isProffesionalUser",isProffesionalUser);
                    bundle.putBoolean("isProffesionalUserFirstLogin",isProffesionalUserFirstLogin);
                    bundle.putString("medals",currentProfesionalObject.getMedals());

                    fragobj = new SystemHomeFragment();
                    fragobj.setArguments(bundle);

                }






                getSupportFragmentManager().beginTransaction().add(R.id.mainContainer, fragobj).commit();






            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(System.this, "משהו השתבש בקריאת הנתונים מהשרת", Toast.LENGTH_SHORT).show();
            }
        });





    }
    //-----------------------------------------------------------------------------------------------------
    private void Decoder(){

        if(isDataBaseObtained == true && CurrentUid != null){
            java.lang.System.out.println(TAG +"entered to isDataBaseObtained == true && CurrentUid != null From DECODER ");
            int RegularUsersArrayLength,ProfesionalUsersArrayLength;
            RegularUsersArrayLength = regularUsers.size();
            ProfesionalUsersArrayLength = proffesionalUsers.size();
            for(int i=0;i<RegularUsersArrayLength;i++){//find the current person from  ArrayList
                if(regularUsers.get(i).getUid().equals(CurrentUid)){
                    currentRegularObject = 
                            new RegularUser(regularUsers.get(i).getEmail().toString(),
                            regularUsers.get(i).getPassword().toString(),
                            regularUsers.get(i).getName().toString(),
                            regularUsers.get(i).getPhone().toString());
                    currentRegularObject.setUid(CurrentUid);
                    isProffesionalUser = false;
                    isProffesionalUserFirstLogin = false;
                    return;
                }

            }
            for(int i=0;i<ProfesionalUsersArrayLength;i++){
                if(proffesionalUsers.get(i).getUid().equals(CurrentUid)){
                    currentProfesionalObject = new ProffesionalUser(
                            proffesionalUsers.get(i).getEmail().toString(),
                            proffesionalUsers.get(i).getPassword().toString(),
                            proffesionalUsers.get(i).getName().toString(),
                            proffesionalUsers.get(i).getPhone().toString(),
                            proffesionalUsers.get(i).getArea().toString());
                    currentProfesionalObject.setUid(CurrentUid);
                    currentProfesionalObject.setCategory(proffesionalUsers.get(i).getCategory().toString());
                    currentProfesionalObject.setMedals(proffesionalUsers.get(i).getMedals().toString());//set medals from Array achived
                    if(proffesionalUsers.get(i).getCategory().toString().equals("0")){
                        isProffesionalUser = true;
                        isProffesionalUserFirstLogin = true;
                    }else{
                        isProffesionalUser = true;
                        isProffesionalUserFirstLogin = false;
                    }
                    return;
                }
            }
            
        }else{
            Toast.makeText(this, "משהו השתבש עם התקשורת לשרת", Toast.LENGTH_SHORT).show();
        }




    }
    
    //-----------------------------------------------------------------------------------------------------
    private void getCurrentUserUid(Bundle savedInstanceState){
        //CurrentUid = extras
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                CurrentUid = null;
                Toast.makeText(this, "שגיאה קריטית לא מזהה משתמש מlogin", Toast.LENGTH_SHORT).show();
                //todo exit from app and delete storage
            } else {
                CurrentUid = extras.getString("UidToSend");
            }
        } else {
            CurrentUid = (String)savedInstanceState.getSerializable("UidToSend");
        }


    }
    //-----------------------------------------------------------------------------------------------------
    private void UpdateAccountSettings(){


        for (android.support.v4.app.Fragment fragment:getSupportFragmentManager().getFragments()) { //remove all fragments
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }

        nameToChange_et.requestFocus();

        final String name,phone;
        if(isAccountSettingsOpen==false) {
            AccountSettingsLayout.setVisibility(View.VISIBLE);
            isAccountSettingsOpen = true;
        }
        if(isProffesionalUser == true){//take the values
            //get data of pro user
            phone = currentProfesionalObject.getPhone();
            name = currentProfesionalObject.getName();
        }else{
            //get data of regular user
            name = currentRegularObject.getName();
            phone = currentRegularObject.getPhone();
        }

        nameToChange_et.setHint(name);//show the user the current name
        phoneToChange_et.setHint(phone);//show the user the current phone



        SubmitNameAndPhoneChange_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(nameToChange_et.getText().toString().isEmpty())&&!(nameToChange_et.getText().toString().equals(name))){
                    //do the changes for name
                    //pro user change
                    if (isProffesionalUser == true){
                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myref;
                        myref = database.getReference();
                        myref.child("server").child("ProffesionalUsers").child(CurrentUid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                dataSnapshot.getRef().child("name").setValue(nameToChange_et.getText().toString());
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(System.this, "שגיאת רשת נסה מאוחר יותר", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{//regular user change
                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myref;
                        myref = database.getReference();
                        myref.child("server").child("RegularUsers").child(CurrentUid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                dataSnapshot.getRef().child("name").setValue(nameToChange_et.getText().toString());
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(System.this, "שגיאת רשת נסה מאוחר יותר", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }



                    Toast.makeText(System.this, "השינויים נעשו תוכל לראות אותם בהתחברות הבאה", Toast.LENGTH_SHORT).show();
                }
                if(!(phoneToChange_et.getText().toString().isEmpty())&&!(phoneToChange_et.getText().toString().equals(phone))) {
                    //do the changes for phone in database
                    if (isProffesionalUser == true){
                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myref;
                        myref = database.getReference();
                        myref.child("server").child("ProffesionalUsers").child(CurrentUid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                dataSnapshot.getRef().child("phone").setValue(phoneToChange_et.getText().toString());
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(System.this, "שגיאת רשת נסה מאוחר יותר", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{//regular user change
                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myref;
                        myref = database.getReference();
                        myref.child("server").child("RegularUsers").child(CurrentUid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                dataSnapshot.getRef().child("phone").setValue(phoneToChange_et.getText().toString());
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(System.this, "שגיאת רשת נסה מאוחר יותר", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }




                    Toast.makeText(System.this, "השינויים נעשו תוכל לראות אותם בהתחברות הבאה", Toast.LENGTH_SHORT).show();
                }


            }
        });




















    }
    //-----------------------------------------------------------------------------------------------------
    private void ApplicationSettings(){
        //getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer,fragobj).commit();
        //getSupportFragmentManager().beginTransaction().remove(fragobj).commit();
        //remove all fragments from the Frame
        for (android.support.v4.app.Fragment fragment:getSupportFragmentManager().getFragments()) { //remove all fragments
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
        if(isAppSettingsOpen==false){
            AppSettingsLayout.setVisibility(View.VISIBLE);
            isAppSettingsOpen=true;
        }
                //makes theme blue
        BlueThemeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CurrntTheme == true) {
                    navigationView.setBackgroundColor((getResources().getColor(R.color.colorPrimaryDark))); //navigation color
                    toolbar.setBackgroundColor((getResources().getColor(R.color.colorPrimaryDark222))); //toolbar
                    SystemLayout.setBackgroundColor((getResources().getColor(R.color.colorPrimaryDark2232)));//background //colorPrimaryDark2232
                    CurrntTheme = false;
                }
            }
        });
        //makes theme black
        BlackThemeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CurrntTheme == false) {
                    navigationView.setBackgroundColor((getResources().getColor(R.color.BetterBlack))); //navigation color
                    toolbar.setBackgroundColor((getResources().getColor(R.color.BetterBlack))); //toolbar
                    SystemLayout.setBackgroundResource(R.drawable.system_wall2);
                    CurrntTheme = true;
                }

            }
        });

    }
    //-----------------------------------------------------------------------------------------------------


}



