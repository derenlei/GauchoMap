package edu.ucsb.cs.cs190i.derenlei.GauchoMap;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyEventsFragment extends Fragment {


    public MyEventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_my_events, container, false);
        TextView text = (TextView) v.findViewById(R.id.list_textvw);
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.rView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (!DatabaseFactory.is_empty()) {
            ArrayList<String> nameList = DatabaseFactory.getNamelist();
            RVAdapter mAdapter = new RVAdapter(nameList);
            recyclerView.setAdapter(mAdapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setHasFixedSize(true);
        }
        else{
            text.setText("You have not created any events");
        }
//        // Inflate the layout for this fragment
        return v;
    }

}
