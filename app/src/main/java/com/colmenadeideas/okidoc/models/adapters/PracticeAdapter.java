package com.colmenadeideas.okidoc.models.adapters;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.colmenadeideas.okidoc.R;
import com.colmenadeideas.okidoc.models.Practice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PracticeAdapter extends RecyclerView.Adapter<PracticeAdapter.ViewHolder> {

    private final ArrayList<Practice> listData;
    private final OnItemClickListener listener;

    public PracticeAdapter(ArrayList<Practice> listData, OnItemClickListener listener) {
        this.listData = listData;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.practice_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.item = listData.get(position);
        holder.viewName.setText(listData.get(position).name);

        //holder.viewName.setVisibility(View.GONE);

        //holder.viewAddress.setText(listData.get(position).address);
            /*Glide.with(holder.itemView.getContext())
                    .load(holder.item.urlMiniatura)
                    .thumbnail(0.1f)
                    .centerCrop()
                    .into(holder.viewMiniatura);*/
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
            return listData.get(posicion).id;
        } else {
            return null;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // public final TextView viewId;
        public final TextView viewName;
        public final TextView viewDay;     public final TextView ini_schedule;    public final TextView end_schedule; public final TextView quota;
        public final TextView viewDay2;    public final TextView ini_schedule2;   public final TextView end_schedule2; public final TextView quota2;
        public final TextView viewDay3;    public final TextView ini_schedule3;   public final TextView end_schedule3; public final TextView quota3;
        public final TextView viewDay4;    public final TextView ini_schedule4;   public final TextView end_schedule4; public final TextView quota4;
        public final TextView viewDay5;    public final TextView ini_schedule5;   public final TextView end_schedule5; public final TextView quota5;
        public final TextView viewDay6;    public final TextView ini_schedule6;   public final TextView end_schedule6; public final TextView quota6;
        public final TextView viewDay7;    public final TextView ini_schedule7;   public final TextView end_schedule7; public final TextView quota7;

        public final RelativeLayout horario1;      public final RelativeLayout horario2;     public final RelativeLayout horario3;
        public final RelativeLayout horario5;      public final RelativeLayout horario6;      public final RelativeLayout horario4;
        public final RelativeLayout horario7;
        public Practice item;

        public ViewHolder(View view) {
            super(view);
            view.setClickable(true);
            viewName = (TextView) view.findViewById(R.id.name);
            viewDay = (TextView) view.findViewById(R.id.day);   ini_schedule = (TextView) view.findViewById(R.id.ini_schedule);    end_schedule = (TextView) view.findViewById(R.id.end_schedule);
            viewDay2 = (TextView) view.findViewById(R.id.day2); ini_schedule2 = (TextView) view.findViewById(R.id.ini_schedule2);   end_schedule2 = (TextView) view.findViewById(R.id.end_schedule2);
            viewDay3 = (TextView) view.findViewById(R.id.day3); ini_schedule3 = (TextView) view.findViewById(R.id.ini_schedule3);   end_schedule3 = (TextView) view.findViewById(R.id.end_schedule3);
            viewDay4 = (TextView) view.findViewById(R.id.day4); ini_schedule4 = (TextView) view.findViewById(R.id.ini_schedule4);   end_schedule4 = (TextView) view.findViewById(R.id.end_schedule4);
            viewDay5 = (TextView) view.findViewById(R.id.day5); ini_schedule5 = (TextView) view.findViewById(R.id.ini_schedule5);   end_schedule5 = (TextView) view.findViewById(R.id.end_schedule5);
            viewDay6 = (TextView) view.findViewById(R.id.day6); ini_schedule6 = (TextView) view.findViewById(R.id.ini_schedule6);   end_schedule6 = (TextView) view.findViewById(R.id.end_schedule6);
            viewDay7 = (TextView) view.findViewById(R.id.day7); ini_schedule7 = (TextView) view.findViewById(R.id.ini_schedule7);   end_schedule7 = (TextView) view.findViewById(R.id.end_schedule7);

            quota = (TextView) view.findViewById(R.id.quota);   quota2 = (TextView) view.findViewById(R.id.quota2);   quota3 = (TextView) view.findViewById(R.id.quota3);
            quota4 = (TextView) view.findViewById(R.id.quota4); quota5 = (TextView) view.findViewById(R.id.quota5);  quota6 = (TextView) view.findViewById(R.id.quota6);
            quota7 = (TextView) view.findViewById(R.id.quota7);

            horario1 = (RelativeLayout) view.findViewById(R.id.horario1);
            horario2 = (RelativeLayout) view.findViewById(R.id.horario2);
            horario3 = (RelativeLayout) view.findViewById(R.id.horario3);
            horario4 = (RelativeLayout) view.findViewById(R.id.horario4);
            horario5 = (RelativeLayout) view.findViewById(R.id.horario5);
            horario6 = (RelativeLayout) view.findViewById(R.id.horario6);
            horario7 = (RelativeLayout) view.findViewById(R.id.horario7);
        }

        public void bind(final Practice practice, final OnItemClickListener listener) {
            viewName.setText(item.name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(practice);
                }
            });
            JSONArray scheduleArray = item.getSchedule();
            for (int a = 0; a < scheduleArray.length(); a++){
                try {
                    JSONObject schedule = scheduleArray.getJSONObject(a);
                    switch (a){
                        case 0:
                            if (schedule.has("day")){
                                horario1.setVisibility(View.VISIBLE);
                                viewDay.setText(schedule.getString("day"));
                                ini_schedule.setText(schedule.getString("ini_schedule"));
                                end_schedule.setText(schedule.getString("end_schedule"));
                                quota.setText(schedule.getString("quota"));
                            }
                            break;
                        case 1:
                            if (schedule.has("day")){
                                horario2.setVisibility(View.VISIBLE);
                                viewDay2.setText(schedule.getString("day"));
                                ini_schedule2.setText(schedule.getString("ini_schedule"));
                                end_schedule2.setText(schedule.getString("end_schedule"));
                                quota2.setText(schedule.getString("quota"));
                            }
                            break;
                        case 2:
                            if (schedule.has("day")){
                                horario3.setVisibility(View.VISIBLE);
                                viewDay3.setText(schedule.getString("day"));
                                ini_schedule3.setText(schedule.getString("ini_schedule"));
                                end_schedule3.setText(schedule.getString("end_schedule"));
                                quota3.setText(schedule.getString("quota"));
                            }
                            break;
                        case 3:
                            if (schedule.has("day")){
                                horario4.setVisibility(View.VISIBLE);
                                viewDay4.setText(schedule.getString("day"));
                                ini_schedule4.setText(schedule.getString("ini_schedule"));
                                end_schedule4.setText(schedule.getString("end_schedule"));
                                quota4.setText(schedule.getString("quota"));
                            }
                            break;
                        case 4:
                            if (schedule.has("day")){
                                horario5.setVisibility(View.VISIBLE);
                                viewDay5.setText(schedule.getString("day"));
                                ini_schedule5.setText(schedule.getString("ini_schedule"));
                                end_schedule5.setText(schedule.getString("end_schedule"));
                                quota5.setText(schedule.getString("quota"));
                            }
                            break;
                        case 5:
                            if (schedule.has("day")){
                                horario6.setVisibility(View.VISIBLE);
                                viewDay6.setText(schedule.getString("day"));
                                ini_schedule6.setText(schedule.getString("ini_schedule"));
                                end_schedule6.setText(schedule.getString("end_schedule"));
                                quota6.setText(schedule.getString("quota"));
                            }
                            break;
                        case 6:
                            if (schedule.has("day")){
                                horario7.setVisibility(View.VISIBLE);
                                viewDay7.setText(schedule.getString("day"));
                                ini_schedule7.setText(schedule.getString("ini_schedule"));
                                end_schedule7.setText(schedule.getString("end_schedule"));
                                quota7.setText(schedule.getString("quota"));
                            }
                            break;
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }



   public interface OnItemClickListener {
       void onItemClick(Practice item);
       // public void onClick(ViewHolder viewHolder, String idPractice);
   }
}