package com.onofre.salvador.donations;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.onofre.salvador.*;

public class SignedInActivity extends AppCompatActivity {

    private static final String TAG = "mensage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed_in);




        final FirebaseUser currentUser =
                FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser.isEmailVerified()!= true) {

            currentUser.sendEmailVerification()
                    .addOnCompleteListener(this, new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            // Re-enable button
                            // findViewById(R.id.verify_email_button).setEnabled(true);

                            if (task.isSuccessful()) {
                                Log.v(TAG, "sendEmailVerification", task.getException());
                                Toast.makeText(SignedInActivity.this,
                                        "Verification email sent to " + currentUser.getEmail(),
                                        Toast.LENGTH_SHORT).show();


                            } else {
                                Log.e(TAG, "sendEmailVerification", task.getException());
                                Toast.makeText(SignedInActivity.this,
                                        "Failed to send verification email.",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

        }
         if(currentUser.isEmailVerified()== false){

             AuthUI.getInstance()
                     .signOut(this)
                     .addOnCompleteListener(new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task) {
                             if (task.isSuccessful()) {
                                 startActivity(new Intent(
                                         SignedInActivity.this, AutenticationMenu.class));
                                 finish();
                             } else {
                                 // Report error to user
                             }
                         }
                     });
         }

         

        if (currentUser == null) {
            startActivity(new Intent(this, AutenticationMenu.class));
            finish();
            return;
        }

        //TextView email = (TextView) findViewById(R.id.email);
        TextView displayname = (TextView) findViewById(R.id.displayname);

       // email.setText(currentUser.getEmail());
        displayname.setText(currentUser.getDisplayName());
/*
        if (currentUser.getPhotoUrl() != null) {
            displayImage(currentUser.getPhotoUrl());
        }

*/


    }
/*
    void displayImage(Uri imageUrl) {

        // show The Image in a ImageView
        new DownloadImageTask((ImageView) findViewById(R.id.imageView))
                .execute(imageUrl.toString());
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }


        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
*/
    public void signOut(View view) {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(
                                    SignedInActivity.this, AutenticationMenu.class));
                            finish();
                        } else {
                            // Report error to user
                        }
                    }
                });
    }

    public void deleteAccount(View view) {
        AuthUI.getInstance()
                .delete(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(SignedInActivity.this,
                                    AutenticationMenu.class));
                            finish();
                        } else {
                            // Notify user of error
                        }
                    }
                });
    }
    public void donationMethod(View view) {
        Intent intent = new Intent(SignedInActivity.this,CategoPictureTakenActivity.class);
        startActivity(intent);


    }
    public void donationListButton(View view) {
        Intent intent = new Intent(SignedInActivity.this,ImageListActivity.class);
        startActivity(intent);

    }
}


