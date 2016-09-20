package com.colmenadeideas.okidoc.models.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.colmenadeideas.okidoc.R;
import com.colmenadeideas.okidoc.models.Appointment;
import com.colmenadeideas.okidoc.models.PatientSimple;


import java.util.ArrayList;


public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {
    private final ArrayList<Appointment> listData;
    private final OnItemClickListener listener;

    public AppointmentAdapter(ArrayList<Appointment> listData, OnItemClickListener listener) {
        this.listData = listData;
        this.listener = listener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.patient_item_list_horizontal, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.item = listData.get(position);
//        holder.viewName.setText(holder.item.getPatients().get(0).getName());
        holder.viewName.setText("Name");

       /* Glide.with(holder.itemView.getContext())
                    .load(holder.item.getPatients().get(0).getAvatar())
                    .thumbnail(0.1f)
                    .centerCrop()
                    .into(holder.viewAvatar);*/
        holder.bind(listData.get(position), listener);
        System.out.println(holder.toString());
        /*
        * DynamicPlayUpTextView dynamicPlayUpTextView = new DynamicPlayUpTextView(ctx, temp);
            holder.dynamictextViewArrayList.add(i, dynamicPlayUpTextView);

            holder.dynamicLL.addView(holder.dynamictextViewArrayList.get(i).buildDynamicPlayUpTextView());

        * */
    }
    @Override
    public int getItemCount() {
        if (listData != null) {
            return listData.size() > 0 ? listData.size() : 0;
        } else {
            return 0;
        }
    }


    private String getPracticeId(int posicion) {
        if (posicion != RecyclerView.NO_POSITION) {
            return listData.get(posicion).appointmentCode; //Id
        } else {
            return null;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public Appointment item;

        public final TextView viewName;
        public final ImageView viewAvatar;

        public ViewHolder(View view) {
            super(view);
            view.setClickable(true);
            viewName    = (TextView) view.findViewById(R.id.patientName);
            viewAvatar  = (ImageView) view.findViewById(R.id.avatar);
        }
        public void bind(final Appointment appointment, final OnItemClickListener listener) {

            ArrayList<PatientSimple> patient = item.getPatients();

            for (int a = 0; a < patient.size(); a++){
                viewName.setText(patient.get(a).getName()+ " "+ patient.get(a).getLastname());
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(appointment, getPosition());
                }
            });
        }
    }
    public interface OnItemClickListener {
        void onItemClick(Appointment item, int position);
        /*onItemClick(AdapterView<?> parent, View view, int position, long id)*/
        // public void onClick(ViewHolder viewHolder, String idPractice);
    }
}
