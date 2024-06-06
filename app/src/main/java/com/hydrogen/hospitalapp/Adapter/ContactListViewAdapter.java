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

public class ContactListViewAdapter extends RecyclerView.Adapter<ContactListViewAdapter.ViewHolder> {
    List<JsonObject> contactList;

    ContactViewListClick contactViewListClick;

    public ContactListViewAdapter(List<JsonObject> contactList, ContactViewListClick contactViewListClick){
        this.contactList = contactList;
        this.contactViewListClick = contactViewListClick;
    }

    @NonNull
    @Override
    public ContactListViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_view_box,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactListViewAdapter.ViewHolder holder, int position) {
        holder.contactNameView.setText(contactList.get(position).get("name").getAsString());
        holder.contactView.setText(contactList.get(position).get("contact").getAsString());

        holder.callButton.setOnClickListener(view -> {
            contactViewListClick.onItemClick(contactList.get(position));
        });


    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView contactNameView, contactView, callButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            contactNameView = itemView.findViewById(R.id.nameContactListView);
            contactView = itemView.findViewById(R.id.contactContactListView);
            callButton = itemView.findViewById(R.id.callContactListView);


        }
    }

    public interface ContactViewListClick{
        void onItemClick(JsonObject object);
    }
}
