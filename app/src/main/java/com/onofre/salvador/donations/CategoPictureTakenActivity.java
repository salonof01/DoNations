package com.onofre.salvador.donations;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.Manifest;
import android.text.TextUtils;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;
import android.widget.Spinner;

import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import android.widget.Toast;
import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CategoPictureTakenActivity extends AppCompatActivity{
    Spinner spinnerDonationPlace;
    private StorageReference mStorageRef;
    Button buttonMakeDonation;

    Spinner spinnerCategory;
    Button buttonPicture;
    DatabaseReference databaseDonation;
    private static FirebaseUser currentUser;
    private static final String TAG = "RealtimeDB";
    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    EditText editTextAddress;
    EditText editTextPhone;
    EditText description;
    public static final int REQUEST_CODE = 1234;
    public static final int READ_EXTERNAL_STORAGE = 0;
    private static final int GALLERY_INTENT = 2;
    private ProgressDialog mProgressDialog;
    private Uri photoUri;
    private File photoFile;
    public Bitmap image;
    UploadTask.TaskSnapshot taskSnapshot;

    private Uri mImageUri = null;
    private StorageReference mStorage;
    String mCurrentPhotoPath;
    ImageView imageViewPicture;
    Intent intent ;
    public  static final int RequestPermissionCode  = 1 ;
    public static final String FB_STORAGE_PATH = "image/";
    public static final String FB_DATABASE_PATH = "UserInfo";
    int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catego_picture_taken);


        mStorageRef = FirebaseStorage.getInstance().getReference();
        databaseDonation = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH);
        spinnerCategory = (Spinner)findViewById(R.id.spinnerCategory);
        description=(EditText)findViewById(R.id.donationdescript);
        spinnerDonationPlace = (Spinner) findViewById(R.id.spinnerDonationPlace);
        buttonPicture=(Button)findViewById(R.id.takePictureButton);
        imageViewPicture =(ImageView)findViewById(R.id.pictureTaken);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        buttonMakeDonation = (Button) findViewById(R.id.buttonMakeDonation);
        buttonMakeDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddDonation();
            }
        });
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            startActivity(new Intent(this, AutenticationMenu.class));
            finish();
            return;
        }

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference(FB_DATABASE_PATH);
        //dbRef.addValueEventListener(changeListener);



        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
        EnableRuntimePermission();




    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;

    }
    public void dispatchTakePictureIntent(View v ) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this,
                        "com.onofre.salvador.fileproviderOno",
                        photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_CODE);
            }
        }
    }



    private void AddDonation(){

        count= count + 1;

        if (photoUri != null) {
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("Uploading image");
            dialog.show();
/*
             StorageReference ref = mStorageRef.child(FB_STORAGE_PATH +  photoUri);
*/
            StorageReference ref = mStorageRef.child(FB_STORAGE_PATH + currentUser.getUid() + "." + photoUri);
            ref.putFile(photoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    //Dimiss dialog when success
                    dialog.dismiss();

                    String name = currentUser.getDisplayName();
                    String desc = description.getText().toString().trim();
                    String address = editTextAddress.getText().toString().trim();
                    String email = currentUser.getEmail();
                    String category = spinnerCategory.getSelectedItem().toString();
                    String phone = editTextPhone.getText().toString().trim();
                    String donateTo = spinnerDonationPlace.getSelectedItem().toString();
                    String url = taskSnapshot.getDownloadUrl().toString();

                    if (!TextUtils.isEmpty(name)) {
                        //String id = databaseDonation.
                        //String id = currentUser.getUid();
                        // Donation donar= new Donation(null,name,null,null,null,null,null);
                        //String key = dbRef.push().getKey();

                        //String key =  "donation" ;
                        //String key =  "donation" + System.currentTimeMillis();

                        Donation user = new Donation(address, name, donateTo, desc, email, phone, category, url);
                        dbRef.child(currentUser.getUid()).setValue(user);
                        //dbRef.child(currentUser.getUid()).child(key).setValue(user);
                        // databaseDonation.child(id).setValue(user);
                        Toast.makeText(getApplicationContext(), "Donation added", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter a name", Toast.LENGTH_LONG).show();
                    }

                    //Display success toast msg
                    Toast.makeText(getApplicationContext(), "Image uploaded", Toast.LENGTH_SHORT).show();
                    /*ImageUpload imageUpload = new ImageUpload(txtImageName.getText().toString(), taskSnapshot.getDownloadUrl().toString());*/

                    //Save image info in to firebase database
                    //String uploadId= currentUser.getUid();



                    /*String uploadId = mDatabaseRef.push().getKey();
                    mDatabaseRef.child(uploadId).setValue(imageUpload);
*/
                }
            }) .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    //Dimiss dialog when error
                    dialog.dismiss();
                    //Display err toast msg
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            //Show upload progress

                            double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            dialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });


        } else {
            Toast.makeText(getApplicationContext(), "Please take a picture", Toast.LENGTH_SHORT).show();
        }


    }




    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK   ) {

             image = BitmapFactory.decodeFile(mCurrentPhotoPath);

            imageViewPicture.setImageBitmap(image);



        }

        galleryAddPic();
    }



    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(CategoPictureTakenActivity.this,
                Manifest.permission.CAMERA))
        {

            Toast.makeText(CategoPictureTakenActivity.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(CategoPictureTakenActivity.this,new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);

        }
    }


    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }







/*





    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(DetailActivity.this,"Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(DetailActivity.this,"Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }


*/




}

