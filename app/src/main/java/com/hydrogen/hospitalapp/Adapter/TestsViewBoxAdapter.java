package com.hydrogen.hospitalapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.hydrogen.hospitalapp.R;

import java.util.List;

public class TestsViewBoxAdapter extends RecyclerView.Adapter<TestsViewBoxAdapter.ViewHolder> {

    List<JsonObject> testList;
    TestViewBoxItemClick testViewBoxItemClick;

    public TestsViewBoxAdapter(List<JsonObject> testList, TestViewBoxItemClick testViewBoxItemClick) {
        this.testList = testList;
        this.testViewBoxItemClick = testViewBoxItemClick;
    }

    @NonNull
    @Override
    public TestsViewBoxAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_view_box, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestsViewBoxAdapter.ViewHolder holder, int position) {
        holder.testNameView.setText(testList.get(position).get("test_name").getAsString());
        holder.createdDateView.setText(testList.get(position).get("created_date").getAsString().split("T")[0]);

        holder.itemView.setOnClickListener(view -> {
            testViewBoxItemClick.onItemClick(testList.get(position));
        });

    }

    @Override
    public int getItemCount() {
        return testList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView testNameView, createdDateView, distance;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            testNameView = itemView.findViewById(R.id.titleView);
            createdDateView = itemView.findViewById(R.id.otherView);
            distance = itemView.findViewById(R.id.distanceView);
            distance.setVisibility(View.GONE);
        }
    }

    public interface TestViewBoxItemClick{
        void onItemClick(JsonObject object);
    }
}
