package com.onofre.salvador.donations;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ImageListActivity extends showImage {
    private static FirebaseUser currentUser;
    private DatabaseReference mDatabaseRef;
    private List<Donation> imgList;
    private ListView lv;
    private ImageListAdapter adapter;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        imgList = new ArrayList<>();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        lv = (ListView) findViewById(R.id.listViewImage);
        //Show progress dialog during list image loading
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait loading list image...");
        progressDialog.show();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference(CategoPictureTakenActivity.FB_DATABASE_PATH);
       // mDatabaseRef = FirebaseDatabase.getInstance().getReference(CategoPictureTakenActivity.FB_STORAGE_PATH + currentUser.getUid()).child("key");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                //String key = mDatabaseRef.getKey();
                //Fetch image data from firebase database
                //for (DataSnapshot snapshot : dataSnapshot.child("donation").getChildren()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //ImageUpload class require default constructor
                    Donation data = snapshot.getValue(Donation.class);
                    imgList.add(data);
                }


                //Init adapter
                adapter = new ImageListAdapter(ImageListActivity.this, R.layout.imageitems_activity, imgList);
                //Set adapter for listview
                lv.setAdapter(adapter);




                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View row, int position, long id) {
                        Intent intent = new Intent(ImageListActivity.this, showImage.class);
                        //String strImage = imgList.get(position).getUrl().toString();

                       Donation listado = imgList.get(position);
                        intent.putExtra("image", listado.getUrl());






                        

                        startActivity(intent);
                    }



                });

            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialog.dismiss();
            }
        });

    }
}
