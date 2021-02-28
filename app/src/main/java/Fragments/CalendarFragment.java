package Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.hairsalon.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

import Classes.Appointment;
import Decorators.DayEnableDecorator;


public class CalendarFragment extends Fragment {
    private String TAG="CalendarFRG";
    private  View view;
    private MaterialCalendarView calendarView;
    private List<CalendarDay> eventsDates;
    private ArrayList<Appointment> events;
    private CalendarDay selectedDay;
    ArrayList<CalendarDay> enabledDates = new ArrayList<>();
    private static CalendarFragment instance = null;

    public CalendarFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        instance = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        view=inflater.inflate(R.layout.fragment_calendar, container, false);
        initCalendar();
        return view;
    }
    private void initEnableDays(){

    }

    private void initCalendar() {
        Log.d(TAG, "initCalendar: Initing calendar");
        calendarView = view.findViewById(R.id.calendarView);
        calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_SINGLE);
        calendarView.setDynamicHeightEnabled(true); // 5 rows instead of 6
        calendarView.getSelectedDates();
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                Log.d(TAG, "onDateSelected: Selected date: " + date.toString() + " boolean: " + selected);
                selectedDay = date;
                Log.d(TAG, "onDateSelected:"+selectedDay);
                if (DayListFragment.getInstance().getFloatingActionButton().getVisibility() == View.INVISIBLE)
                    DayListFragment.getInstance().getFloatingActionButton().setVisibility(View.VISIBLE);

                DayListFragment.getInstance().appointmentsList(date);
            }
        });
        //calendarView.addDecorator(new DayEnableDecorator(enabledDates));
        calendarView.state().edit()
                .setFirstDayOfWeek(DayOfWeek.SUNDAY)
                .setMinimumDate(CalendarDay.from(LocalDate.now()))
                .setMaximumDate(CalendarDay.from(LocalDate.now().getYear() + 1, 5, 12))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
//        if (tripDates != null) { // Paint trip days if available
//            paintTripDates();
//        }
//        if (tripEventsDates != null) {
//            paintTripPlans();
//        }
    }



    public CalendarDay getSelectedDay() {
        return selectedDay;
    }

    public static CalendarFragment getInstance() {
        return instance;
    }


}
