package Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hairsalon.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Classes.MyData;
import Classes.User;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HOME_AC";
    private static Context context;
    private User user ;
    private String name;
    private TextView nameLBL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        HomeActivity.context = getApplicationContext();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser.isAnonymous())
            showNameDialog();
        else {
            Log.d(TAG, "onCreate: UID"+firebaseUser.getUid());
            MyData.getInstence().getUsersRef().child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   user= dataSnapshot.getValue(User.class);
                   nameLBL.setText("Hi "+user.getName());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }


            });
        }
        Log.d(TAG, "onCreate:NAME "+name);
        nameLBL=findViewById(R.id.user_name_LBL);
        nameLBL.setText("Hi "+name);
    }



    public void showNameDialog(){
        Log.d(TAG, "showNameDialog: ");

        LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
        final View namedialogView = inflater.inflate(R.layout.dialog_user_name, null);
        final TextView etName = (EditText) namedialogView.findViewById(R.id.TIET_name_input);
        AlertDialog dialog = new AlertDialog.Builder(HomeActivity.this)
                .setView(namedialogView)
                .setTitle(R.string.input_name_msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        name = etName.getText().toString();
                        Log.d(TAG, "onClick: "+name);
                        updateUser();
                    }
                }).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: ");
        //Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu_lay, menu);
        return true;
    }

    private void updateUser() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = firebaseUser.getUid();
        String phone = firebaseUser.getPhoneNumber();
        User user = new User()
                .setName(name)
                .setPhone(phone)
                .setUid(uid);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        myRef.child(user.getUid()).setValue(user);

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: ");
        switch (item.getItemId()){
            case R.id.item1_services:
                servicesActivity();
            case R.id.item2_customersTell:
                customersTellActivity();
            case R.id.item3_makeAnAppointment:
                makeAnAppointmentActivity();
            case R.id.item4_contactUs:
                contactUsActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    private void contactUsActivity() {
    }

    private void makeAnAppointmentActivity() {
        Log.d(TAG, "makeAnAppointmentActivity: ");
        Intent myIntent = new Intent(HomeActivity.this, CalendarEventsActivity.class);
        startActivity(myIntent);
    }

    private void customersTellActivity() {

    }

    private void servicesActivity() {
        Log.d(TAG, "servicesActivity: ");
        Intent myIntent = new Intent(HomeActivity.this, ServicesActivity.class);
        startActivity(myIntent);
    }

    public static Context getAppContext() {
        return HomeActivity.context;
    }
}
