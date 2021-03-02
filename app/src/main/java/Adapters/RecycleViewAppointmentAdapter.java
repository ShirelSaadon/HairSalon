package Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hairsalon.R;

import java.util.ArrayList;

import Classes.Appointment;

public class RecycleViewAppointmentAdapter extends RecyclerView.Adapter<RecycleViewAppointmentAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Appointment> dailyList;
    public static final String TAG = "AppointmentsAdapter";


    public RecycleViewAppointmentAdapter(Context context, ArrayList<Appointment> dailyList) {
        this.context = context;
        this.dailyList = dailyList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.appointments_recycle_view_row, parent, false);
        return new RecycleViewAppointmentAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: ");
        Appointment temp = dailyList.get(position);
        Log.d(TAG, "onBindViewHolder: "+temp.toString());
        String type = temp.getType();
        String time = temp.getTime();
        Log.d(TAG, "onBindViewHolder: "+type);
        holder.name.setText(type);
        holder.hour.setText(time);
    }

    @Override
    public int getItemCount() {

        return dailyList.size();
    }

    //An inner class to specify each row contents
    public class MyViewHolder extends RecyclerView.ViewHolder { // To hold each row

        TextView name, hour;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            initViews();
        }

        /**
         * A method to initialize the widgets of the row
         */
        private void initViews() {
            name = itemView.findViewById(R.id.appointmentRow_LBL_Name);
            hour = itemView.findViewById(R.id.appointmentRow_LBL_Time);
        }

        public TextView getName() {
            return name;
        }

        public void setName(TextView name) {
            this.name = name;
        }

        public TextView getHour() {
            return hour;
        }

        public void setHour(TextView hour) {
            this.hour = hour;
        }
    }

}
