package com.hydrogen.hospitalapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.hydrogen.hospitalapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EnrollPreviewAdapter extends RecyclerView.Adapter<EnrollPreviewAdapter.ViewHolder> {

    private List<JsonObject> enrollDataList;
    private EnrollItemClick enrollItemClick;
    private Context context;

    public EnrollPreviewAdapter(Context context, List<JsonObject> enrollDataList, EnrollItemClick enrollItemClick) {
        this.context = context;
        this.enrollDataList = enrollDataList;
        this.enrollItemClick = enrollItemClick;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.enroll_preview_box, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull EnrollPreviewAdapter.ViewHolder holder, int position) {

        holder.departmentName.setText(enrollDataList.get(position).get("department_name").getAsString());
        holder.serviceName.setText(enrollDataList.get(position).get("service_name").getAsString());
        holder.doctorName.setText(enrollDataList.get(position).get("doctor_name").getAsString());
        holder.enrollDate.setText(enrollDataList.get(position).get("enroll_open_date").getAsString());


        if (enrollDataList.get(position).get("enroll_status").getAsInt() == 1){
            holder.statusCircle.setBackgroundTintList(context.getApplicationContext().getResources().getColorStateList(R.color.green));
        }


        holder.itemView.setOnClickListener(view -> {
            enrollItemClick.onItemClick(enrollDataList.get(position));
        });

    }

    @Override
    public int getItemCount() {
        return enrollDataList.size();
    }

    public interface EnrollItemClick{
        void onItemClick(JsonObject object);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView departmentName, statusCircle, serviceName, doctorName, enrollDate, locateDepartment;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            departmentName = itemView.findViewById(R.id.departmentName);
            statusCircle = itemView.findViewById(R.id.statusCircle);
            serviceName = itemView.findViewById(R.id.serviceName);
            doctorName = itemView.findViewById(R.id.doctorName);
            enrollDate = itemView.findViewById(R.id.enrollDate);
            locateDepartment = itemView.findViewById(R.id.locateDepartment);


        }
    }
}
