package com.onofre.salvador.donations;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
public class AutenticationMenu extends AppCompatActivity {


    private static final String TAG = "Mensage";
    private FirebaseAuth auth;
    private static final int REQUEST_CODE = 101;
    final FirebaseUser currentUser =
            FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autentication_menu);

        auth = FirebaseAuth.getInstance();




        if (auth.getCurrentUser() != null ) {
            startActivity(new Intent(this, SignedInActivity.class));
            finish();
        } else {
            authenticateUser();
        }

    }



        private void authenticateUser () {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setTheme(R.style.CustomTheme)
                        .setLogo(R.drawable.dona)
                        .setAvailableProviders(getProviderList())
                        .setIsSmartLockEnabled(false)



                        .build(),
                REQUEST_CODE);





    }



        private List<AuthUI.IdpConfig> getProviderList () {

        List<AuthUI.IdpConfig> providers = new ArrayList<>();

        providers.add(
                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build());

        providers.add(new
                AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());

       /* providers.add(new
                AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build());

        providers.add(new
                AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build());

        /*providers.add(new
                AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build());
*/
        return providers;
    }


        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);



        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == REQUEST_CODE) {

            if (resultCode == ResultCodes.OK ) {
                // set the activity you want after signed in
                startActivity(new Intent(this, SignedInActivity.class));
                return;
            }
        } else {
            if (response == null) {
                // User cancelled Sign-in
                return;
            }

            if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                // Device has no network connection
                return;
            }

            if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                // Unknown error occurred
                return;
            }
        }

    }
    }


