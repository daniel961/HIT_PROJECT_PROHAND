package com.hit.daniel.hit_project_prohand;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static android.support.v4.content.ContextCompat.startActivity;
import static android.webkit.WebSettings.PluginState.ON;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<ProffesionalUser>proffesionalUsersToShow = new ArrayList<>();
    boolean isClicked = false;
    /*
    private ArrayList<String>ProNames = new ArrayList<>();
    private ArrayList<String>ProPhones = new ArrayList<>();
    private ArrayList<String>ProCategory = new ArrayList<>();
    */
    private Context mContext;

    public RecyclerViewAdapter(ArrayList<ProffesionalUser> proffesionalUsers, Context mContext) {
        this.proffesionalUsersToShow = proffesionalUsers;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_items,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");

        holder.CallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //call the number //proffesionalUsersToShow.get(position).getPhone()
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+proffesionalUsersToShow.get(position).getPhone()));
                startActivity(mContext,intent,null);
            }
        });

        holder.AddMedal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the current medals of pro user and parse int
                //show to the user the change on screen
                int medals = Integer.parseInt(proffesionalUsersToShow.get(position).getMedals());
                medals++;
                holder.medals_counter_tv.setTextColor(Color.BLUE);//change color to blue
                holder.medals_counter_tv.setTextSize(25);//make it bigger size text
                holder.AddMedal.setVisibility(View.INVISIBLE);//make the add medal button invisble
                holder.medals_counter_tv.setText(Integer.toString(medals));//show the new value after add one


                //save the change in database
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myref;
                myref = database.getReference();
                myref.child("server").child("ProffesionalUsers").child(proffesionalUsersToShow.get(position).getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("medals").setValue(holder.medals_counter_tv.getText().toString());
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(mContext, "שגיאת רשת נסה מאוחר יותר", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        holder.tv_name.setText(proffesionalUsersToShow.get(position).getName());
        holder.tv_phone.setText(proffesionalUsersToShow.get(position).getPhone());
        holder.EmailTV.setText(proffesionalUsersToShow.get(position).getEmail());
        holder.medals_counter_tv.setText(proffesionalUsersToShow.get(position).getMedals());

        String fixCategory;
        switch (proffesionalUsersToShow.get(position).getCategory().toString()){
            case "1":  fixCategory = "טכנאי מזגנים";
                break;
            case "2":  fixCategory = "טכנאי מקררים";
                break;
            case "3":  fixCategory = "טכנאי מכונות כביסה";
                break;
            case "4":  fixCategory = "טכנאי תריסים חשמליים";
                break;

            case "5":  fixCategory = "טכנאי מחשבים";
                break;
            case "6":  fixCategory = "טכנאי מצלמות";
                break;
            case "7":  fixCategory = "טכנאי מדיח כלים";
                break;
            case "8":  fixCategory = "טכנאי מוצרי חשמל כללי";
                break;

            case "9":  fixCategory = "טכנאי סלולר";
                break;
            case "10":  fixCategory = "טכנאי תנורים";
                break;
            case "11":  fixCategory = "טכנאי טלוויזיות";
                break;
            default:
                fixCategory = "בעל מקצוע כלשהו";
                break;

        }
        holder.tv_category.setText(fixCategory);

        /*
        holder.tv_name.setText(ProNames.get(position));
        holder.tv_category.setText(ProCategory.get(position));
        holder.tv_phone.setText(ProPhones.get(position));
        */
        //onClick on view holder

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isClicked == true){
                    holder.ShowMoreDetailsLayout.setVisibility(View.GONE);
                    isClicked = false;
                }else{
                    holder.ShowMoreDetailsLayout.setVisibility(View.VISIBLE);
                    isClicked = true;
                }



                Log.d(TAG, "onClick: clicked" + proffesionalUsersToShow.get(position).getName());
                //holder.ShowMoreDetailsLayout.setVisibility(View.VISIBLE);
                //Toast.makeText(mContext, "מתקשר ל- " + proffesionalUsersToShow.get(position).getPhone() , Toast.LENGTH_SHORT).show();

            }
        });



    }

    @Override
    public int getItemCount() {
        return proffesionalUsersToShow.size();//tells How many Objects we have in the list
    }






    public class ViewHolder extends RecyclerView.ViewHolder{//view holder inner class
        ImageView imageView;
        TextView tv_name,tv_category,tv_phone,medals_counter_tv,EmailTV;
        RelativeLayout relativeLayout,ShowMoreDetailsLayout;
        Button CallBtn,AddMedal;

        //add two things

        public ViewHolder(View itemView) {//super defult constructor
            super(itemView);
            imageView = itemView.findViewById(R.id.UserPhoto);
            tv_name = itemView.findViewById(R.id.ProName);
            tv_category = itemView.findViewById(R.id.ProCategory);
            tv_phone = itemView.findViewById(R.id.ProNumber);
            relativeLayout = itemView.findViewById(R.id.ItemParentLayout);
            medals_counter_tv = itemView.findViewById(R.id.medals_counter_tv);//medals counter TV
            /*under this is iside*/
            ShowMoreDetailsLayout = itemView.findViewById(R.id.ShowMoreDetailsLayout);
            EmailTV = itemView.findViewById(R.id.EmailTV);
            CallBtn = itemView.findViewById(R.id.CallBtn);
            AddMedal = itemView.findViewById(R.id.AddMedal);




        }




    }


}
