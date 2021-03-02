package Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.hairsalon.R;
import com.google.firebase.database.DatabaseReference;
import com.prolificinteractive.materialcalendarview.CalendarDay;


import CallBack.UserCallBack;
import Classes.MyData;
import Classes.Review;
import Classes.SpinnerType;
import Classes.User;

public class NewReviewDialog extends Dialog  {
    private static final String TAG = "DIALOG_EVENT";
    private TextView reviewName, reviewDate;
    private TextView cancel, save;
    private EditText reviewFreeText;
    private Button addPhotoBTN;
    private UserCallBack userCallBack;

    private Review newReview;
    private SpinnerType spinnerType;
    private User user;

    public NewReviewDialog(@NonNull Context context, UserCallBack userCallBack) {
        super(context);
        this.userCallBack = userCallBack;
        user= userCallBack.getUser();
    }
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: Creating new fidbek dialog");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_fidbek);
        initViews();
        Listeners();
    }

    private void Listeners() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dismiss();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNewReview();
            }
        });
    }


    private void initViews() {
        Log.d(TAG, "initViews: ");
        reviewDate= findViewById(R.id.newReview_LBL_Date);
        reviewDate = findViewById(R.id.newevent_LBL_name);
        reviewFreeText=findViewById(R.id.newReview_freeText);
        addPhotoBTN=findViewById(R.id.newReview_BTN_addPhoto);
        spinnerType=new SpinnerType(findViewById(R.id.newReview_LST_typeSpinner),this.getContext());
        cancel = findViewById(R.id.newReview_LBL_cancel);
        save = findViewById(R.id.newReview_LBL_save);
        spinnerType.initSpinner();
    }
    private void saveNewReview() {
        Log.d(TAG, "saveNewReview: ");
        DatabaseReference ReviewRef = MyData.getInstence().getReviewsRef();
        DatabaseReference newReviewRef = ReviewRef.push();
        Log.d(TAG, "saveNewReview: TYPE"+spinnerType.getType());
        newReview= new Review(user,reviewFreeText.getText().toString(),spinnerType.getType(),CalendarDay.today().toString());
        newReviewRef.setValue(newReview);
        Log.d(TAG, "saveNewReview: " + newReview.toString());
        Toast.makeText(getContext(),"Thanks!",Toast.LENGTH_LONG).show();
        dismiss();
    }





}
