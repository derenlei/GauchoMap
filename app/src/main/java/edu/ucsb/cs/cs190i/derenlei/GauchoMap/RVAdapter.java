package edu.ucsb.cs.cs190i.derenlei.GauchoMap;

import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Melvine on 6/14/17.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.EventViewHolder> {

    public static class EventViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView event_name;
        TextView event_desc;
        TextView event_date;
        ImageView event_photo;

        EventViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            event_name = (TextView)itemView.findViewById(R.id.event_name);
            event_desc = (TextView)itemView.findViewById(R.id.event_desc);
            event_date = (TextView)itemView.findViewById(R.id.event_date);
            event_photo = (ImageView)itemView.findViewById(R.id.event_photo);
        }
    }

    ArrayList<String> nameList;

    RVAdapter(ArrayList<String> nameList){
        this.nameList = nameList;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card, viewGroup, false);
        EventViewHolder evh = new EventViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(EventViewHolder eventViewHolder, int i) {
        String uri = DatabaseFactory.getUri(nameList.get(i));

        eventViewHolder.cv.setCardElevation(10);
        eventViewHolder.event_name.setText(nameList.get(i));
        eventViewHolder.event_desc.setText(DatabaseFactory.getEvent(uri));
        eventViewHolder.event_date.setText(DatabaseFactory.getTime(uri));
        eventViewHolder.event_photo.setImageURI(Uri.parse(uri));
    }

    @Override
    public int getItemCount() {
        return nameList.size();
    }
}
