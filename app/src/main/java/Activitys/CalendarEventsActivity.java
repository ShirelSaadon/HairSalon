package Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.example.hairsalon.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Classes.Appointment;
import Classes.MyData;
import Fragments.CalendarFragment;
import Fragments.DayListFragment;


public class CalendarEventsActivity extends AppCompatActivity {

    private static final String TAG = "Clanedar_Ac";
    private MaterialCalendarView calendarView;
    private FloatingActionButton floatingActionButton;
    private List<CalendarDay> eventsDates;
    private ArrayList<Appointment> events;

    private CalendarDay selectedDay;

    //fragments
    private CalendarFragment calendarFragment;
    private DayListFragment dayListFragment;

        
  

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        AndroidThreeTen.init(this);
        setContentView(R.layout.activity_appointments);
        //initBtnAddEvent();
        initCalenderFragment();
        initListFragment();
    }



    private void initCalenderFragment(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        calendarFragment=new CalendarFragment();
        ft.add(R.id.placeHolder_CALENDAR, calendarFragment).commit();
    }
    private void initListFragment() {
        dayListFragment = new DayListFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.placeHolder_LST,dayListFragment).commit();
    }


    private void addEventToEventsArray(Appointment event) {
        Log.d(TAG, "addEventToEventsArray: Adding event to events array: " + event.toString());
        CalendarDay tempDay = selectedDay;
        //Add to myEvents array
        //Sort by date
        events.add(event);
        //Collections.sort(events);
        Log.d(TAG, "addEventToEventsArray: Added to events");
        if (eventsDates == null) {
            eventsDates = new ArrayList<>();
        }
        eventsDates.add(tempDay);
        Log.d(TAG, "addEventToEventsArray: Events array: " + events.toString());
        //paintTripPlans();
    }
    //@Override
    public void getNewEvent(Appointment event) {
//        Log.d(TAG, "getNewEvent: Got event start: " + event.toString());
//        Instant instant = selectedDay.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant();
//        long timeInMillis = instant.toEpochMilli() / 1000;
//        Log.d(TAG, "getNewEvent: Time in epoch: " + timeInMillis);
//        event.setDate(timeInMillis);
//        Log.d(TAG, "getNewEvent: Got event: " + event.toString());
//
//        addEventToEventsArray(event);
//        displayDailyEvents(selectedDay);
    }




}