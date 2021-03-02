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

import Classes.Review;

public class RecycleViewReviewsAdapter extends RecyclerView.Adapter<RecycleViewReviewsAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Review> reviewsList;
    public static final String TAG = "ReviewsAdapter";


    public RecycleViewReviewsAdapter(Context context, ArrayList<Review> reviewsList) {
        this.context = context;
        this.reviewsList = reviewsList;
    }

    @NonNull
    @Override
    public RecycleViewReviewsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.review_row, parent, false);
        return new RecycleViewReviewsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewReviewsAdapter.MyViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: ");
        Review temp = reviewsList.get(position);
        holder.command.setText(temp.getCommand());
        holder.type.setText(temp.getType());
        holder.date.setText(temp.getDate());
        holder.name.setText(temp.getUser().getName());

    }

    @Override
    public int getItemCount() {

        return reviewsList.size();
    }

    //An inner class to specify each row contents
    public class MyViewHolder extends RecyclerView.ViewHolder { // To hold each row

        TextView name,date,type,command;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            initViews();
        }

        /**
         * A method to initialize the widgets of the row
         */
        private void initViews() {
            name = itemView.findViewById(R.id.reviewRow_LBL_Name);
            date = itemView.findViewById(R.id.reviewRow_LBL_Date);
            type =itemView.findViewById(R.id.reviewRow_LBL_Type);
            command=itemView.findViewById(R.id.reviewRow_LBL_text);
        }

        public TextView getName() {
            return name;
        }

        public void setName(TextView name) {
            this.name = name;
        }

        public TextView getDate() {
            return date;
        }

        public void setDate(TextView date) {
            this.date = date;
        }

        public TextView getType() {
            return type;
        }

        public void setType(TextView type) {
            this.type = type;
        }

        public TextView getCommand() {
            return command;
        }

        public void setCommand(TextView command) {
            this.command = command;
        }
    }
}
