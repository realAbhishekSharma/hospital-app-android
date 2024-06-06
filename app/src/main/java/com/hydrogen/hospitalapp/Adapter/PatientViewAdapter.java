package com.hydrogen.hospitalapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.hydrogen.hospitalapp.R;

import java.util.List;

public class PatientViewAdapter extends RecyclerView.Adapter<PatientViewAdapter.ViewHolder> {
    private List<JsonObject> patientList;
    private PatientItemClick patientItemClick;
    private Context context;

    public PatientViewAdapter(Context context, List<JsonObject> patientList, PatientItemClick patientItemClick) {
        this.context = context;
        this.patientList = patientList;
        this.patientItemClick = patientItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_view_box, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientViewAdapter.ViewHolder holder, int position) {

        holder.titleView.setText(patientList.get(position).get("title").getAsString());
        holder.descriptionView.setText(patientList.get(position).get("description").getAsString());
        holder.hospitalNameView.setText(patientList.get(position).get("hospital_name").getAsString());
        holder.admitDateView.setText(patientList.get(position).get("admit_date").getAsString());

        if (patientList.get(position).get("discharge_date") == null){
            holder.statusCircle.setBackgroundTintList(context.getApplicationContext().getResources().getColorStateList(R.color.green));
        }else {
            holder.statusCircle.setBackgroundTintList(context.getApplicationContext().getResources().getColorStateList(R.color.red));
        }


        holder.itemView.setOnClickListener(view -> {
            patientItemClick.onItemClick(patientList.get(position));
        });

    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }

    public interface PatientItemClick{
        void onItemClick(JsonObject object);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleView, descriptionView, hospitalNameView, admitDateView, statusCircle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleView = itemView.findViewById(R.id.titleName);
            descriptionView = itemView.findViewById(R.id.descriptionView);
            hospitalNameView = itemView.findViewById(R.id.hospitalNameView);
            admitDateView = itemView.findViewById(R.id.admitDateView);
            statusCircle = itemView.findViewById(R.id.statusCircle);
        }
    }
}
