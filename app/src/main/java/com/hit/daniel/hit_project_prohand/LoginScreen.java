package com.hit.daniel.hit_project_prohand;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.nsd.NsdManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebHistoryItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static android.view.View.INVISIBLE;

public class LoginScreen extends AppCompatActivity {

    private static final String TAG = "DanielTests  :  ";

    public List<RegularUser> regularUsersFin = new ArrayList<RegularUser>();
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    View view;
    FrameLayout f;
    boolean isRegisterOpen = false,firstTimeOpeningRegister=true,isProfesionalUserCheckedArea = false;
    LinearLayout l1,l2;
    MediaPlayer splashSound;
    Animation uptodown,downtoup,baunce,fade_in,fade_out;
    Button btn_registration;
    //for the user register details
    boolean isProffesional = false;
    String UidToSend;
    String  Email,Password,Name,Phone,Area,ConnectEmail,ConnectPassword;
    EditText Etinput_email,Etinput_password,Etinput_fullname,Etinput_phone,EtConnectEmail,EtConnectPassword;






/*onStart Run override*/
    @Override
    protected void onStart() {//on start the activity check for user connect or not
        super.onStart();


    }


    /*OnBackPressed Overide*/
    @Override
    public void onBackPressed() {
        if(isRegisterOpen = true){
            f.removeView(view);
            l2.setVisibility(View.VISIBLE);
            isRegisterOpen = false;

        }else {
            //todo open dialog box "Are u sure you want to quit?" and then close the app

            ((LoginScreen)this).finish();
        }


    }


    //check if the username and password is correct and act
    private void startSignIn(){
        //alertDialog Progress
        final ProgressDialog progressDialog = new ProgressDialog(LoginScreen.this);
        progressDialog.setTitle("מאמת משתמש מול השרת");
        progressDialog.setMessage("אנא המתן בזמן בדיקת הנתונים...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ConnectEmail = EtConnectEmail.getText().toString();
        ConnectPassword = EtConnectPassword.getText().toString();
        if(ConnectEmail.isEmpty() || ConnectPassword.isEmpty()){
            Toast.makeText(this, "אנא מלא מייל וסיסמא", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }else{

            mAuth.signInWithEmailAndPassword(ConnectEmail,ConnectPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(!task.isSuccessful()){
                        progressDialog.dismiss();
                        Toast.makeText(LoginScreen.this, "שגיאת פרטי התחברות", Toast.LENGTH_LONG).show();
                    }
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        Toast.makeText(LoginScreen.this, "התחברת בהצלחה מועבר למערכת", Toast.LENGTH_LONG).show();
                        //moving to the System Intent
                        UidToSend = mAuth.getUid().toString();
                        Intent mainIntent = new Intent(LoginScreen.this, System.class);
                        mainIntent.putExtra("UidToSend",UidToSend);
                        LoginScreen.this.startActivity(mainIntent);
                        LoginScreen.this.finish();
                    }



                }
            });
        }






    }


















    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);





        //test

        regularUsersFin = new ArrayList<RegularUser>();
        final List<RegularUser> regularUsers = new ArrayList<RegularUser>(); //create new list of Regular users
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("server").child("RegularUsers");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,Object> users = (Map<String,Object>) dataSnapshot.getValue(); //get Map of Regular users from dataBase
                String email,password,phone,name;

                for (Map.Entry<String,Object> entry : users.entrySet()){

                    //Get user map
                    Map singleUser = (Map)entry.getValue();
                    //Get ALL field to strings and append to list
                    email = singleUser.get("email").toString();
                    password = singleUser.get("password").toString();
                    name = singleUser.get("name").toString();
                    phone = singleUser.get("phone").toString();
                    //adding the regular user to the list from the strings we achived
                    regularUsers.add(new RegularUser(email,password,name,phone));
                    regularUsersFin.add(new RegularUser(email,password,name,phone));
                }
                //here my list is ready


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //todo Error reading excaption

            }
        });


        //



        new Handler().postDelayed(new Runnable(){//need to wait untill the Array come from server
            @Override
            public void run() {

                //Create an Intent that will start the Menu-Activity.
                //checks if user allready registerd
                mAuth.addAuthStateListener(mAuthListener);




                //todo remove print
                //System.out.println(TAG + regularUsersFin.get(0).getName().toString());
                //System.out.println(TAG + regularUsersFin.get(1).getName().toString());
                //System.out.println(TAG + regularUsersFin.get(2).getName().toString());
                //System.out.println(TAG + regularUsersFin.get(3).getName().toString());




            }
        }, 3000);



















        /*****************************************Mauth***********************************************************/
        //todo authntication after click Connect button
        mAuth = FirebaseAuth.getInstance();
        Button btnConnect = (Button)findViewById(R.id.btnConnect);
        EtConnectEmail = (EditText)findViewById(R.id.EtConnectEmail);
        EtConnectPassword = (EditText)findViewById(R.id.EtConnectPassword);

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignIn();//methode check if user and password is correct
            }
        });



        //this check if the user state is loged in or not
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final ProgressDialog progressDialog = new ProgressDialog(LoginScreen.this);
                progressDialog.setTitle("מאמת פרטים להתחברות אוטומטית");
                progressDialog.setMessage("אנא המתן בזמן בדיקת הנתונים מול השרת...");
                progressDialog.setCancelable(false);
                progressDialog.show();

             if(firebaseAuth.getCurrentUser()== null){
                 progressDialog.dismiss();

             }
             else if(firebaseAuth.getCurrentUser()!= null){
                 //its mean that the user connect in the past and we have his email and password
                 //todo move to the System intent
                 Toast.makeText(LoginScreen.this, "אתה מועבר למערכת בהתחברות אוטומטית", Toast.LENGTH_LONG).show();
                 progressDialog.dismiss();
                 UidToSend = mAuth.getUid();
                 Intent mainIntent = new Intent(LoginScreen.this, System.class);
                 mainIntent.putExtra("UidToSend",UidToSend);
                 LoginScreen.this.startActivity(mainIntent);
                 LoginScreen.this.finish();



             }
            }
        };


/*******************************************Mauth***********************************************************/








































/*
        Map<String,RegularUser> users = new HashMap<>();
        users.put("daniel",new RegularUser("test1@testos.com","test","Robot Cohen","054888888"))
*/






        /*activity Login References*/
        l1 = (LinearLayout) findViewById(R.id.l1);
        l2 = (LinearLayout) findViewById(R.id.l2);
        btn_registration = (Button)findViewById(R.id.btnregistration);

        /*Animation References*/
        uptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        downtoup = AnimationUtils.loadAnimation(this,R.anim.downtoup);
        baunce = AnimationUtils.loadAnimation(this,R.anim.bounce);
        fade_in = AnimationUtils.loadAnimation(this,R.anim.fade_in);
        fade_out = AnimationUtils.loadAnimation(this,R.anim.fade_out);

        /*Activity registration (Inflated Layout) References*/
        //Button btnCancelRegisteration = (Button) findViewById(R.id.btnCancelRegisteration);





        l1.setAnimation(uptodown);
        l2.setAnimation(downtoup);









        /*Layout Inflater CODE*/ /*activity_registration*/
        LayoutInflater i = getLayoutInflater();
        f = (FrameLayout) findViewById(R.id.ROOTloginFrameLayout);
        view = i.inflate(R.layout.activity_registration,null);


        /*Refference Layout Inflater*/
        final LinearLayout Register_Lay = (LinearLayout)view.findViewById(R.id.Register_Lay);
        final LinearLayout profesional_details_extra_layout = (LinearLayout)view.findViewById(R.id.profesional_details_extra_layout);
        ImageButton closeInflate = (ImageButton)view.findViewById(R.id.close_regisration_btn);
        Button RegisterMeBTN = (Button)view.findViewById(R.id.btn_OKRegister);
        final Button RegularUserTypeBTN = (Button)view.findViewById(R.id.RegularUser);
        final Button ProffesionalUserTypeBTN = (Button)view.findViewById(R.id.ProffesionalUser);
        //proffesional buttons area select
        final Button jerusalem_area = (Button)view.findViewById(R.id.jerusalem_area);
        final Button center_area = (Button)view.findViewById(R.id.center_area);
        final Button south_area = (Button)view.findViewById(R.id.south_area);
        final Button north_area = (Button)view.findViewById(R.id.north_area);

        final LinearLayout ReisterTypeLayout =(LinearLayout)view.findViewById(R.id.register_type_layout);


        /*textBOXEs*/
        //EditText Etinput_email,Etinput_password,Etinput_fullname,Etinput_phone;
        Etinput_email = (EditText)view.findViewById(R.id.input_email);
        Etinput_password = (EditText)view.findViewById(R.id.input_password);
        Etinput_fullname = (EditText)view.findViewById(R.id.input_fullname);
        Etinput_phone = (EditText)view.findViewById(R.id.input_phone);














        RegisterMeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //todo progress bar dialog
                final ProgressDialog progressDialog = new ProgressDialog(LoginScreen.this);
                progressDialog.setTitle("בודק נתונים מול שרת");
                progressDialog.setMessage("אנא המתן בזמן בדיקת הנתונים...");
                progressDialog.setCancelable(false);
                progressDialog.show();


                if(Etinput_email.getText().toString().isEmpty()||Etinput_password.getText().toString().isEmpty()||
                        Etinput_fullname.getText().toString().isEmpty()||Etinput_phone.getText().toString().isEmpty()){//check  user enter all filds
                    progressDialog.dismiss();
                    Toast.makeText(LoginScreen.this, "עלייך למלא את כל הפרטים", Toast.LENGTH_SHORT).show();
                }
                else if (!Etinput_email.getText().toString().contains("@")){//check if the Email contains '@'
                    //Toast.makeText(LoginScreen.this, "אנא הכנס מייל תקין", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Etinput_email.setError("אנא הכנס מייל תקין");
                    Etinput_email.requestFocus();
                }
                else if(Etinput_password.getText().toString().length()<6){//check the password lengh is bigger then 6 charcters
                    //Toast.makeText(LoginScreen.this, "הסיסמא חייבת להיות גדולה מ-6 תוים", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Etinput_password.setError("הסיסמא חייבת להיות גדולה מ-6 תוים");
                    Etinput_password.requestFocus();
                }
                else if(Etinput_phone.getText().toString().length() <9){
                    //todo fix the problem its can contain '#' '@' '!' '^' atsedra symbols and its shut the server key value
                    progressDialog.dismiss();
                    Etinput_phone.setError("אנא הכנס מספר טלפון תיקני");
                    Etinput_phone.requestFocus();
                }
                else if(isProffesional == true &&  isProfesionalUserCheckedArea==false) {//check if proffesional user chooses Area of work
                    progressDialog.dismiss();
                    Toast.makeText(LoginScreen.this, "בחר איזור שירות", Toast.LENGTH_SHORT).show();

                }
                //if we get here so we can Write the user to the server
                else{
                    //todo add acount to users auth
                    Email = Etinput_email.getText().toString();
                    Password = Etinput_password.getText().toString();
                    mAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful()){
                           //if we get here we can create the user in the auth and in the database


                           //if the user type is proffesional user
                           if(isProffesional == true){
                               //get all EditText Data to Local Strings

                               Email = Etinput_email.getText().toString();
                               Password = Etinput_password.getText().toString();
                               Name = Etinput_fullname.getText().toString();
                               Phone = Etinput_phone.getText().toString();


                               //todo check if user allready registerd with the email

                               //creating new proffesional user Object inside the server
                               ProffesionalUser puser = new ProffesionalUser(Email,Password,Name,Phone,Area);
                               puser.setUid(mAuth.getUid());
                               puser.SaveToDatabase();

                               //clean all filds
                               Etinput_email.setText("");
                               Etinput_password.setText("");
                               Etinput_fullname.setText("");
                               Etinput_phone.setText("");
                               center_area.setBackgroundResource(R.drawable.buttonstyle);
                               south_area.setBackgroundResource(R.drawable.buttonstyle);
                               north_area.setBackgroundResource(R.drawable.buttonstyle);
                               jerusalem_area.setBackgroundResource(R.drawable.buttonstyle);
                               center_area.setTextColor(Color.BLACK);
                               south_area.setTextColor(Color.BLACK);
                               north_area.setTextColor(Color.BLACK);
                               jerusalem_area.setTextColor(Color.BLACK);



                               Toast.makeText(LoginScreen.this, "המידע נשמר בשרת אתה יכול להתחבר", Toast.LENGTH_SHORT).show();
                               f.removeView(view);
                               l2.setVisibility(View.VISIBLE);
                               isRegisterOpen = false;
                               progressDialog.dismiss();


                           }
                           //if the user type is Regular user
                           else if(isProffesional == false){
                               //get all EditText Data to Local Strings
                               Email = Etinput_email.getText().toString();
                               Password = Etinput_password.getText().toString();
                               Name = Etinput_fullname.getText().toString();
                               Phone = Etinput_phone.getText().toString();


                               //todo check if user allready registerd with the email

                               //creating new regular user Object
                               RegularUser ruser = new RegularUser(Email,Password,Name,Phone); //create the user
                               ruser.setUid(mAuth.getUid()); //takeing the UID from Auth and assume it in the object
                               ruser.SaveToDatabase(); //saving the user to database


                               //clean all filds
                               Etinput_email.setText("");
                               Etinput_password.setText("");
                               Etinput_fullname.setText("");
                               Etinput_phone.setText("");


                               Toast.makeText(LoginScreen.this, "המידע נשמר בשרת אתה יכול להתחבר", Toast.LENGTH_SHORT).show();
                               f.removeView(view);
                               l2.setVisibility(View.VISIBLE);
                               isRegisterOpen = false;
                               progressDialog.dismiss();




                           }







                       }else{
                           progressDialog.dismiss();
                           Toast.makeText(LoginScreen.this, "אימייל שגוי", Toast.LENGTH_SHORT).show();


                       }



                        }
                    });




                }




            }
        });










        //Register Button ROOT LAYOUT
        btn_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRegisterOpen = true;
                f.addView(view);
                l2.setVisibility(INVISIBLE);
                Register_Lay.startAnimation(fade_in);
                ReisterTypeLayout.startAnimation(baunce);

                //alert dialog for the first time opening registration//its dialog inside dialog
                if(firstTimeOpeningRegister == true) {
                    final AlertDialog.Builder WelcomeRegisterDialog = new AlertDialog.Builder(LoginScreen.this);
                    WelcomeRegisterDialog.setMessage("הפרטים האישיים של בעלי המקצוע יהיו מפורסמים באפליקציה לכולם, " +
                            "הפרטים האישיים השייכים למשתמשים רגילים יהיו זמינים אך ורק לאחר הזמנת שירות ויהיו זמינים אך ורק לבעל המקצוע המבצע את השירות. תודה." ).setCancelable(false)
                            .setPositiveButton("הבנתי", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();//close the dialog
                                    WelcomeRegisterDialog.setTitle("בתור התחלה");
                                    WelcomeRegisterDialog.setMessage("עליך לבחור את סוג המשתמש בכפתורים העליונים ולאחר מכן למלא את כל הפרטים").setPositiveButton("אין בעיה", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                                    AlertDialog alert = WelcomeRegisterDialog.create();
                                    alert.show();

                                }
                            });
                    WelcomeRegisterDialog.setTitle("חשוב לדעת");
                    AlertDialog alert = WelcomeRegisterDialog.create();
                    alert.show();
                    firstTimeOpeningRegister = false;
                }







            }
        });

        //close registeration Button thx X btn
        closeInflate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f.removeView(view);
                l2.setVisibility(View.VISIBLE);
                isRegisterOpen = false;


            }
        });


        RegularUserTypeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isProffesional = false;
                RegularUserTypeBTN.setBackgroundResource(R.drawable.buttonstyle4);
                RegularUserTypeBTN.setTextColor(Color.WHITE);

                /*To do -> Hide the Buttons for the proffesionals*/
                profesional_details_extra_layout.setVisibility(View.GONE);


                ProffesionalUserTypeBTN.setBackgroundResource(R.drawable.buttonstyle);
                ProffesionalUserTypeBTN.setTextColor(Color.BLACK);

                RegularUserTypeBTN.startAnimation(baunce);




            }
        });



        ProffesionalUserTypeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isProffesional = true;
                ProffesionalUserTypeBTN.setBackgroundResource(R.drawable.buttonstyle4);
                ProffesionalUserTypeBTN.setTextColor(Color.WHITE);

                /*To do -> Hide the Buttons for the proffesionals*/
                profesional_details_extra_layout.setVisibility(View.VISIBLE);


                RegularUserTypeBTN.setBackgroundResource(R.drawable.buttonstyle);
                RegularUserTypeBTN.setTextColor(Color.BLACK);
                ProffesionalUserTypeBTN.startAnimation(baunce);




            }
        });





        jerusalem_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //just for justify again
                isProffesional = true;
                isProfesionalUserCheckedArea = true;
                center_area.setBackgroundResource(R.drawable.buttonstyle);
                south_area.setBackgroundResource(R.drawable.buttonstyle);
                north_area.setBackgroundResource(R.drawable.buttonstyle);

                center_area.setTextColor(Color.BLACK);
                south_area.setTextColor(Color.BLACK);
                north_area.setTextColor(Color.BLACK);

                jerusalem_area.setBackgroundResource(R.drawable.buttonstyle4);
                jerusalem_area.setTextColor(Color.WHITE);
                Area = "Jerusalem";
                Toast.makeText(LoginScreen.this, "איזור נבחר: ירושלים והסביבה", Toast.LENGTH_SHORT).show();




            }
        });


        center_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //just for justify again
                isProffesional = true;
                isProfesionalUserCheckedArea = true;
                jerusalem_area.setBackgroundResource(R.drawable.buttonstyle);
                south_area.setBackgroundResource(R.drawable.buttonstyle);
                north_area.setBackgroundResource(R.drawable.buttonstyle);

                jerusalem_area.setTextColor(Color.BLACK);
                south_area.setTextColor(Color.BLACK);
                north_area.setTextColor(Color.BLACK);

                center_area.setBackgroundResource(R.drawable.buttonstyle4);
                center_area.setTextColor(Color.WHITE);
                Area = "center";
                Toast.makeText(LoginScreen.this, "איזור נבחר: המרכז", Toast.LENGTH_SHORT).show();




            }
        });

        south_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //just for justify again
                isProffesional = true;
                isProfesionalUserCheckedArea = true;
                jerusalem_area.setBackgroundResource(R.drawable.buttonstyle);
                center_area.setBackgroundResource(R.drawable.buttonstyle);
                north_area.setBackgroundResource(R.drawable.buttonstyle);

                jerusalem_area.setTextColor(Color.BLACK);
                center_area.setTextColor(Color.BLACK);
                north_area.setTextColor(Color.BLACK);

                south_area.setBackgroundResource(R.drawable.buttonstyle4);
                south_area.setTextColor(Color.WHITE);
                Area = "south";
                Toast.makeText(LoginScreen.this, "איזור נבחר: הדרום", Toast.LENGTH_SHORT).show();




            }
        });

        north_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //just for justify again
                isProffesional = true;
                isProfesionalUserCheckedArea = true;
                jerusalem_area.setBackgroundResource(R.drawable.buttonstyle);
                center_area.setBackgroundResource(R.drawable.buttonstyle);
                south_area.setBackgroundResource(R.drawable.buttonstyle);

                jerusalem_area.setTextColor(Color.BLACK);
                center_area.setTextColor(Color.BLACK);
                south_area.setTextColor(Color.BLACK);

                north_area.setBackgroundResource(R.drawable.buttonstyle4);
                north_area.setTextColor(Color.WHITE);
                Area = "north";
                Toast.makeText(LoginScreen.this, "איזור נבחר: הצפון", Toast.LENGTH_SHORT).show();




            }
        });



























    }
}
