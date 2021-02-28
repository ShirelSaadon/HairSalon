package Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.hairsalon.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class ActivityLogin extends AppCompatActivity {
    private static final String TAG ="LOGIN_AC";
    private static final int RC_SIGN_IN=1234;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            openHomeActivity();
        } else {
            startLoginMethod();
        }
    }

    private void openHomeActivity() {
        Log.d(TAG, "openHomeActivity: ");
        Intent myIntent = new Intent(this, HomeActivity.class);
        startActivity(myIntent);
        finish();
    }

    private void startLoginMethod() {
        Log.d(TAG, "startLoginMethod: ");
        startActivityForResult(AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.PhoneBuilder().build()
                        ))
                        //.setLogo(R.drawable.ic_logo_s)
                        //.setTosAndPrivacyPolicyUrls(
                          //      "https://example.com/terms.html",
                            //    "https://example.com/privacy.html"
                        .build(),
                RC_SIGN_IN);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                openHomeActivity();
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    showSnackbar(R.string.sign_in_cancelled);
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackbar(R.string.no_internet_connection);
                    return;
                }

                showSnackbar(R.string.unknown_error);
                Log.e("pttt", "Sign-in error: ", response.getError());
            }
        }
    }


    private void showSnackbar(int id) {
        Toast.makeText(this, getText(id), Toast.LENGTH_SHORT).show();
    }

}