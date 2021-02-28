package Dialogs;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.hairsalon.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import CallBack.nextValidCallBack;
import Classes.Appointment;
import Classes.MyData;
import Classes.User;
import Fragments.CalendarFragment;

public class EventDialog extends Dialog implements nextValidCallBack {

    private static final String TAG = "DIALOG_EVENT";
    private TextView eventName, eventTime, eventPhone;
    private TextView cancel, save;
    private Appointment newEvent;
    private Spinner eventType;
    private String type = " ", time = " ";
    public int numOfHours;
    User user;

    public RangeTimePickerDialog rangeTimePickerDialog;
    private List<String> disableHour = new ArrayList<>();
    private List<String> ableHour = new ArrayList<>();

    public void initDisableHours(){
        MyData.getInstence().getDisableHRef().child(CalendarFragment.getInstance().getSelectedDay().toString()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d(TAG, "onDataChange: ENTER");
                        for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                            disableHour.add(eventSnapshot.getValue(String.class));
                            Log.d(TAG, "onDataChange: DISABLE HOURES "+disableHour.toString());
                        }
                        initAbleHours();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });
    }

    private void initAbleHours() {
        Log.d(TAG, "initAbleHours: ");
        for(int i=8;i<21;i++)
            ableHour.add(String.format("%02d", i));
        ableHour.removeAll(disableHour);

        Log.d(TAG, "initAbleHours: "+ableHour.toString()+" DIS"+disableHour.toString());
        if (ableHour.isEmpty()) {
            Toast.makeText(getContext(), "THIS DAY IS FULL ,PLEASE TRY ANOTHER DATE", Toast.LENGTH_LONG).show();
            this.dismiss();
        }
    }


    public EventDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        initDisableHours();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_event);
        Log.d(TAG, "onCreate: Creating new event dialog");
        initViews();
        Listeners();
    }
    private int currentHour( ){
        int temp ;
        int currentHour;
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.DATE) == CalendarFragment.getInstance().getSelectedDay().getDay())
            currentHour = nextValidHour(calendar.get(Calendar.HOUR_OF_DAY));
        else
            currentHour=Integer.parseInt(ableHour.get(0));
        for(int i =0 ;i<numOfHours;i++) {
            temp=Integer.parseInt(ableHour.get(0))+i;
            Log.d(TAG, "initTimePicker: LOOP "+temp);
            if (!ableHour.contains(String.format("%02d", temp))) {
                currentHour = nextValidHour(Integer.parseInt(ableHour.get(0)));
                break;
            }
        }
        return currentHour;
    }

    private void initTimePicker(int currentHour,int currentMinute) {
        Log.d(TAG, "initTimePicker: initing timepicker");
        // Get Current Time
        Log.d(TAG, "initTimePicker: " + ableHour.toString());
        Log.d(TAG, "initTimePicker: CUR"+currentHour);

        rangeTimePickerDialog = new RangeTimePickerDialog(this,numOfHours, getContext(),
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        eventTime.setText(String.format("%02d:%02d", hourOfDay, minutes));
                        time = eventTime.getText().toString();
                        Log.d(TAG, "onTimeSet: " + time);
                    }
                }, currentHour, currentMinute, true);
        rangeTimePickerDialog.getWindow().setBackgroundDrawableResource(R.color.purple_200);
        rangeTimePickerDialog.show();
    }

    private void initSpinner() {
        Log.d(TAG, "initSpinner: initing spinner");
        ArrayList<String> types = new ArrayList<>();

        types.add("type");
        types.add("hair color");
        types.add("haircut");
        types.add("hairdo");
        types.add("hair straightenin");
        Log.d(TAG, "initSpinner: " + types);
        Log.d(TAG, "initSpinner: " + this.getContext());

        //create an ArrayAdapter from the String Array
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, types);
        Log.d(TAG, "initSpinner: " + dataAdapter.toString());
        //set the view for the Drop down list
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //set the ArrayAdapter to the spinner
        eventType.setAdapter(dataAdapter);
        //attach the listener to the spinner


        eventType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemSelected: Selected: " + types.get(i));
                type = types.get(i);
                numOfHours=numOfHours(type);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initViews() {
        Log.d(TAG, "initViews: ");
        eventPhone = findViewById(R.id.newevent_LBL_phone);
        eventName = findViewById(R.id.newevent_LBL_name);
        eventTime = findViewById(R.id.newevent_EDT_time);
        eventType = findViewById(R.id.newevent_LST_typeSpinner);
        cancel = findViewById(R.id.LBL_cancel);
        save = findViewById(R.id.LBL_save);
        initTextToLay();
        initSpinner();
    }

    public void initTextToLay() {
        MyData.getInstence().getUsersRef().child(MyData.getInstence().getFirebaseUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User value = dataSnapshot.getValue(User.class);
                user = value;
                eventName.setText("Hi " + user.getName());
                eventPhone.setText("phone number: " + user.getPhone());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void Listeners() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dismiss();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNewEvent();
            }
        });

        eventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (type.equals(" ") || type.equals("type"))
                    Toast.makeText(getContext(), "YOU HAVE TO CHOOSE EVENT TYPE FIRST!!", Toast.LENGTH_LONG).show();

                int currentHour=currentHour();
                int currentMinute =currentMinute();
                if (currentHour<0)
                    Toast.makeText(getContext(), "THERE IS NO VALID TIME FOR "+type+" ,PLEASE TRY ANOTHER DATE", Toast.LENGTH_LONG).show();
                else
                    initTimePicker(currentHour,currentMinute);
            }
        });
    }

    private int currentMinute() {
        return  Calendar.getInstance().get(Calendar.MINUTE);
    }

    private void saveNewEvent() {
        Log.d(TAG, "saveNewEvent: ");
        CalendarDay date = CalendarFragment.getInstance().getSelectedDay();
        DatabaseReference dateRef = MyData.getInstence().getEventsRef().child(date.toString());
        DatabaseReference newEventRef = dateRef.push();
        newEvent = new Appointment(MyData.getInstence().getFirebaseUser().getUid(), time, type, newEventRef.getKey(), date.toString());
        newEventRef.setValue(newEvent);

        Log.d(TAG, "saveNewEvent: " + newEvent.toString());


        Toast.makeText(getContext(),"Thanks!, we will see you son",Toast.LENGTH_LONG).show();
        dismiss();
    }

    private int numOfHours(String type){
        Log.d(TAG, "numOfHours: ");
        switch (type){
            case "hair color":
                return 2;
            case  "haircut":
                return  1;
            case "hairdo":
                return 1;
            case "hair straightenin":
                return 3;
            default:
                return 0;
        }
    }

    @Override
    public int nextValidHour(int hourOfDay) {
        Log.d(TAG, "nextValidHour: ");
        for (int i=0 ;i<ableHour.size();i++){
            int temp=Integer.parseInt(ableHour.get(i))+numOfHours-1;
            Log.d(TAG, "nextValidHour: ABLE_HOURE"+ ableHour.toString());
            Log.d(TAG, "nextValidHour: TEMP"+ temp+" STRtemp "+String.format("%02d", temp)+"GET(i) "+ableHour.get(i));

            if(Integer.parseInt(ableHour.get(i)) >hourOfDay && ableHour.contains(String.format("%02d", temp))) {
                Log.d(TAG, "nextValidHour: in IF ");
                return (Integer.parseInt(ableHour.get(i)));
            }
        }
        return -1;
    }
}
