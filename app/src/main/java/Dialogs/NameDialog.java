package Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;

import com.example.hairsalon.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import CallBack.ButtonClickCallBack;

public class NameDialog extends Dialog {
    private MaterialButton materialButton;
    private TextInputEditText editText;
    private String userName="an";
    private static final String TAG = "DIALOG_NAME";
    public ButtonClickCallBack myListener;
    private MaterialButton okButton;

    public NameDialog(@NonNull Context context, ButtonClickCallBack myListener) {
        super(context);
        this.myListener=myListener;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_user_name);

        editText = findViewById(R.id.TIET_name_input);
        //okButton =  findViewById(R.id.BTN_name_ok);

        okButton.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                userName = editText.getText().toString();
                Log.d(TAG, "onClick: "+userName);
                myListener.onButtonClick();
            }
        });

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
