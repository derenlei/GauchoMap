package edu.ucsb.cs.cs190i.derenlei.GauchoMap;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListViewFragment extends Fragment {

    public ListViewFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_list_view, container, false);
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
            text.setText("No events nearby");
        }






//        TextView text = (TextView) v.findViewById(R.id.list_textvw);
//
//        LinearLayout ll = new LinearLayout(getActivity());
//        ll.setOrientation(LinearLayout.VERTICAL);
//        ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
//
//        ArrayList<String> nameList = DatabaseFactory.getNamelist();
//
//        if (!DatabaseFactory.is_empty()) {
//            for (int i = 0; i < nameList.size(); i++) {
//                text.setText("");
//                String name = nameList.get(i);
//                String uri = DatabaseFactory.getUri(name);
//                String desc = "Melvin Nguyen";
//                String date = DatabaseFactory.getTime(uri);
//
//                View aCard = inflater.inflate(R.layout.card, container, false);
//                CardView card = (CardView) aCard.findViewById(R.id.cv);
//
//                ImageView imgView = (ImageView) card.findViewById(R.id.event_photo);
//                TextView name_vw = (TextView) card.findViewById(R.id.event_name);
//                TextView date_vw = (TextView) card.findViewById(R.id.event_date);
//                TextView desc_vw = (TextView) card.findViewById(R.id.event_desc);
//
//                imgView.setImageURI(Uri.parse(uri));
//                name_vw.setText(name);
//                date_vw.setText(date);
//                desc_vw.setText(desc);
//                ll.addView(card);
//            }
//        }
//        else{
//            text.setText("No events nearby");
//        }
//
//        v = ll;
//        // Inflate the layout for this fragment
        return v;
    }

}
