package Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hairsalon.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import java.util.ArrayList;
import Adapters.RecycleViewReviewsAdapter;
import CallBack.UserCallBack;
import Classes.MyData;
import Classes.Review;
import Classes.User;
import Dialogs.NewReviewDialog;

public class CustomerTellsActivity extends AppCompatActivity implements UserCallBack {
    private static final String TAG = "CustomerTells";
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private String userGson ;
    private User user;
    private ArrayList<Review> reviewList=new ArrayList<Review>();
////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_and_dialog_button);
        Bundle bundle = getIntent().getExtras();
        userGson = bundle.getString("USER_EXTRA");
        user=new Gson().fromJson(userGson,User.class);
        Log.d(TAG, "onCreate: "+user.getName());
        recyclerView = findViewById(R.id.RECYCLER_VIEW);
        reviewsList();
    }

    private void initBtnAddEvent(){
        Log.d(TAG, "initBtnAddEvent: ");
        floatingActionButton=findViewById(R.id.BTN_addEventButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewReviewEvent();
            }
        });
    }

    private void openNewReviewEvent() {
        Log.d(TAG, "openDialog: Opening new  dialog");
        NewReviewDialog newReviewDialog = new NewReviewDialog(this,this);
        newReviewDialog.show();
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.6);
        newReviewDialog.getWindow().setLayout(width, height);
        newReviewDialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        newReviewDialog.getWindow().setDimAmount(0.9f);
    }

    public void reviewsList() {
        Log.d(TAG, "reviewsList: ");
        initBtnAddEvent();
        ArrayList<Review> reviewsList=new ArrayList<Review>();
        MyData.getInstence().getReviewsRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: ENTER");
                reviewsList.removeAll(reviewsList);
                if(dataSnapshot.getChildrenCount()==0){
                    Log.d(TAG, "onDataChange: 0 child" );
                }else {
                    populatelist(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void populatelist(DataSnapshot dataSnapshot) {
        reviewList.removeAll(reviewList);
        // MyData.getInstence().getDisableHRef().child(date.toString()).removeValue();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            Log.d(TAG, "populatelist: "+snapshot.getClass()+"  "+snapshot.getValue());
            Review review = snapshot.getValue(Review.class);
            reviewList.add(review);
            Log.d(TAG, "populateList: " + review.toString());

            if (reviewList.size() == dataSnapshot.getChildrenCount()) {
                recyclerView.setVisibility(View.VISIBLE);
                RecycleViewReviewsAdapter adapter =new RecycleViewReviewsAdapter(this,reviewList);
                recyclerView.setAdapter(adapter);
                Log.d(TAG, "populatelist: DONE");
            }
        }
    }

    @Override
    public User getUser() {
        return user;
    }
}