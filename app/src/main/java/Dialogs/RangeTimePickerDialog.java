package Dialogs;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import CallBack.nextValidCallBack;
import Classes.Appointment;
import Classes.MyData;
import Fragments.CalendarFragment;

public class RangeTimePickerDialog extends TimePickerDialog {
    /**final var**/
    private final static String TAG = "timePickerDIALOG";
    private final int MIN_HOUR = 8;
    private final int MAX_HOUR = 21;
    private final int MIN_MINUTE = -1;
    private final int MAX_MINUTE = 25;
    private final int TIME_PICKER_INTERVAL=60;

    private static RangeTimePickerDialog instance;

    /**views **/
    private TimePicker mTimePicker;
    private Calendar calendar = Calendar.getInstance();
    private DateFormat dateFormat;
    /**list var**/
    private List<String> disableHour =new ArrayList<>();
    private List<String> ableHour =new ArrayList<>();

    /**Integer var**/
    private int currentHour = 0;
    private int currentMinute = 0;
    private int typeHours;
    public nextValidCallBack nextValidCallBack;


    public RangeTimePickerDialog(nextValidCallBack nextValidCallBack,int typeHours,Context context, int theme, OnTimeSetListener callBack, int hourOfDay, int minute, boolean is24HourView) {
        super(context, theme,callBack, hourOfDay, minute, is24HourView);
        this.nextValidCallBack=nextValidCallBack;
        currentHour = hourOfDay;
        currentMinute = minute;
        this.typeHours = typeHours;
        Log.d(TAG, "RangeTimePickerDialog: "+this.typeHours);
        dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
        instance=this;
        try {
            Class<?> superclass = getClass().getSuperclass();
            Field mTimePickerField = superclass.getDeclaredField("mTimePicker");
            mTimePickerField.setAccessible(true);
            mTimePicker = (TimePicker) mTimePickerField.get(this);
            mTimePicker.setOnTimeChangedListener(this);
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
    }

    /**this function read from firebase "disableHours" reference the hours that are not available
     * also calls the functions - initAbleHours & initIntervalMinutes**/
    public void initDisableHours(){
        MyData.getInstence().getDisableHRef().child(CalendarFragment.getInstance().getSelectedDay().toString()).addListenerForSingleValueEvent(
                new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: ENTER");
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    disableHour.add(eventSnapshot.getValue(String.class));
                    Log.d(TAG, "onDataChange: "+disableHour.toString());
                }
                initAbleHours();
                initIntervalMinutes();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }





    private void initAbleHours(){
        for(int i=MIN_HOUR;i<MAX_HOUR;i++)
            ableHour.add(String.format("%02d", i));
        ableHour.removeAll(disableHour);
    }
    private void updateDialogTitle(TimePicker timePicker, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        String title = dateFormat.format(calendar.getTime());
        setTitle(title);
    }

    /**this function set to spinner minutes time picker a interval of minutes**/
    public void initIntervalMinutes(){
        try {
            Class<?> classForid = Class.forName("com.android.internal.R$id");
            Field timePickerField = classForid.getField("timePicker");
            mTimePicker = (TimePicker) findViewById(timePickerField.getInt(null));
            Field field = classForid.getField("minute");
            NumberPicker minuteSpinner = (NumberPicker) mTimePicker.findViewById(field.getInt(null));
            NumberPicker hourPicker = (NumberPicker) mTimePicker.findViewById(Resources.getSystem().getIdentifier(
                    "hour", "id", "android"));

            List<String> displayedValues = new ArrayList<>();
            minuteSpinner.setMinValue(0);
            minuteSpinner.setMaxValue((60 / TIME_PICKER_INTERVAL) - 1);
            for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
                displayedValues.add(String.format("%02d", i));
            }
            minuteSpinner.setDisplayedValues(displayedValues.toArray(new String[displayedValues.size()]));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        initDisableHours();
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        Log.d(TAG, "onTimeChanged: ENTER EBALE"+ ableHour.toString());
        boolean validTime = true;
        //check if the client want event today
        if (calendar.get(Calendar.DATE)== CalendarFragment.getInstance().getSelectedDay().getDay())
            updateTime(hourOfDay,minute);

        Log.d(TAG, "onTimeChanged:   "+Integer.toString(hourOfDay)+
                " LIST DISABLE " +disableHour.toString()+"typeHours "+Integer.toString(hourOfDay+typeHours)+"IN "+ typeHours);
        //check if the hour available-including treatment length-not the time client want or need
        if(!ableHour.contains(Integer.toString(hourOfDay))||
                !ableHour.contains(Integer.toString(hourOfDay+typeHours-1))) {
            Log.d(TAG, "onTimeChanged: "+hourOfDay+" type "+ typeHours);
            validTime =false;
        }

        if (validTime) {
            Log.d(TAG, "onTimeChanged: VALID TIME");
            currentHour = hourOfDay;
            currentMinute = minute;
        }else{
            Log.d(TAG, "onTimeChanged: NOTVALID "+ currentHour);
            currentHour = nextValidCallBack.nextValidHour(hourOfDay);

            if(currentHour==-1) {
                Toast.makeText(getContext(), "There is no more valid option! try another date or event type!", Toast.LENGTH_LONG).show();
                dismiss();
            }

        }
        Log.d(TAG, "onTimeChanged: CURH  "+currentHour+ "  CURMIN  " +currentMinute );
        updateTime(currentHour, currentMinute);
        updateDialogTitle(view, currentHour, currentMinute);
    }

    /**set the next valid hour to hour spinner
     * return -1 if there is no valid hour for the specific event type**/
//    @Override
//    public int nextValidHour(int hourOfDay) {
//        Log.d(TAG, "nextValidHour: ");
//        for (int i=0 ;i<ableHour.size();i++){
//            int temp=Integer.parseInt(ableHour.get(i))+typeHours-1;
//            Log.d(TAG, "nextValidHour: ABLE_HOURE"+ ableHour.toString());
//            Log.d(TAG, "nextValidHour: TEMP"+ temp+" STRtemp "+String.format("%02d", temp)+"GET(i) "+ableHour.get(i));
//
//            if(Integer.parseInt(ableHour.get(i)) >hourOfDay && ableHour.contains(String.format("%02d", temp))) {
//                Log.d(TAG, "nextValidHour: in IF ");
//                return (Integer.parseInt(ableHour.get(i)));
//            }
//        }
//        return -1;
//    }

    public RangeTimePickerDialog getInstance(){
        return instance;
    }
}

