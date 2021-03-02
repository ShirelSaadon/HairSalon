package Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hairsalon.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import Adapters.RecycleViewAppointmentAdapter;
import Classes.Appointment;
import Classes.MyData;
import Dialogs.EventDialog;


public class DayListFragment extends Fragment {
    private static final String TAG = "DayListFRG";
    protected View view;
    private RecyclerView recyclerView;
    private HashMap<CalendarDay,ArrayList<Appointment>> allEvent=new HashMap<>();
    private FloatingActionButton floatingActionButton;
    private static DayListFragment instance = null;
    private ArrayList<Appointment> dailyList=new ArrayList<Appointment>();



    public DayListFragment() {
        allEvent=new HashMap<CalendarDay,ArrayList<Appointment>>();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        instance = this;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        if (view == null)
            view = inflater.inflate(R.layout.list_and_dialog_button, container, false);

        recyclerView = view.findViewById(R.id.RECYCLER_VIEW);
        initBtnAddEvent();
        return view;
    }

    private void initBtnAddEvent(){
        Log.d(TAG, "initBtnAddEvent: ");
        floatingActionButton=view.findViewById(R.id.BTN_addEventButton);
        floatingActionButton.setVisibility(View.INVISIBLE);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogEvent();
            }
        });
    }
    private void openDialogEvent() {
        Log.d(TAG, "openEventDialon: Opening new event dialog");
        EventDialog newEventDialog = new EventDialog(getContext());
        newEventDialog.show();
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.6);
        newEventDialog.getWindow().setLayout(width, height);
        newEventDialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        newEventDialog.getWindow().setDimAmount(0.9f);
    }

    public void appointmentsList(CalendarDay date) {
        Log.d(TAG, "appointmentsList: "+date.toString());
        Log.d(TAG, "appointmentsList: try yo print from  "+ MyData.getInstence().getEventsRef().child("CalendarDay{2021-1-19}").getClass());
        ArrayList<Appointment> dailyList=new ArrayList<Appointment>();
        MyData.getInstence().getEventsRef().child(date.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: ENTER");
                dailyList.removeAll(dailyList);
                if(dataSnapshot.getChildrenCount()==0){
                    Log.d(TAG, "onDataChange: 0 child" );
                    recyclerView.setVisibility(View.INVISIBLE);
                }else {
                    populatelist(dataSnapshot,date);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void updateDisableHours(CalendarDay date, Appointment app) {
        Log.d(TAG, "updateDisableHours: ");
        DatabaseReference dateRef = MyData.getInstence().getDisableHRef().child(date.toString());
        DatabaseReference hRef;
        int numOfHours = numOfHours(app.getType());
        int temp = Integer.parseInt(app.getTime().substring(0,2));
        for (int i =0 ;i<numOfHours;i++){
            Log.d(TAG, "updateDisableHours: saveHour "+temp);
            hRef=dateRef.push();
            hRef.setValue(String.format("%02d", temp));
            temp++;
        }
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
    public void populatelist(DataSnapshot dataSnapshot,CalendarDay date) {
        dailyList.removeAll(dailyList);
       MyData.getInstence().getDisableHRef().child(date.toString()).removeValue();
        for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
            Appointment app = eventSnapshot.getValue(Appointment.class);
            updateDisableHours(date,app);
            dailyList.add(app);
            Log.d(TAG, "populateList: " + dailyList.toString());

            if (dailyList.size() == dataSnapshot.getChildrenCount()) {
                Collections.sort(dailyList);
                recyclerView = view.findViewById(R.id.RECYCLER_VIEW);
                recyclerView.setVisibility(View.VISIBLE);
                RecycleViewAppointmentAdapter adapter = new RecycleViewAppointmentAdapter(getContext(), dailyList);
                recyclerView.setAdapter(adapter);
                Log.d(TAG, "populatelist: DONE");
            }
        }
    }


    public FloatingActionButton getFloatingActionButton() {
        return floatingActionButton;
    }

    public void setFloatingActionButton(FloatingActionButton floatingActionButton) {
        this.floatingActionButton = floatingActionButton;
    }

    public static DayListFragment getInstance() {
        return instance;
    }



    public static void setInstance(DayListFragment instance) {
        DayListFragment.instance = instance;
    }

    public HashMap<CalendarDay, ArrayList<Appointment>> getAllEvent() {
        return allEvent;
    }

    public void setAllEvent(HashMap<CalendarDay, ArrayList<Appointment>> allEvent) {
        this.allEvent = allEvent;
    }
}

