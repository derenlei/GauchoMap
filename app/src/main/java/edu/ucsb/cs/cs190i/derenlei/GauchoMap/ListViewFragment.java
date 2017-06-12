package edu.ucsb.cs.cs190i.derenlei.GauchoMap;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
//        ArrayList<String> namelist = getNamelist;
//        Boolean b = !d.is_empty();
//        ArrayList<CardView> cards = new ArrayList();
        LinearLayout ll = new LinearLayout(getActivity());
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
//
//        if (!DatabaseFactory.is_empty()) {
//            System.out.print("hi");
//        }
//            ArrayList<String> namelist = DatabaseFactory.getNamelist();
//            View aCard = inflater.inflate(R.layout.card, container, false);
            for (int i = 0; i < 1; i++) {
//                String name = namelist.get(i);
                View aCard = inflater.inflate(R.layout.card, container, false);
                aCard.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                String name = "UCEN";
                String desc = "Melvin Nguyen";
                String date = "6/12/2017";
//                int interest = 0;
//                String uri = DatabaseFactory.getUri(name);
//                String time = DatabaseFactory.getTime(name);
//                int interest = DatabaseFactory.getInterest(uri);
                CardView card = (CardView) aCard.findViewById(R.id.cv);
                TextView n = (TextView) card.findViewById(R.id.event_name);
                TextView datevw = (TextView) card.findViewById(R.id.event_date);
                TextView descvw = (TextView) card.findViewById(R.id.event_desc);
                n.setText(name);
                descvw.setText(desc);
                datevw.setText(date);

                ll.addView(card);
            }
//        }else{

//        text.setText("No events nearby");
//        }
//
//
        v = ll;
//        // Inflate the layout for this fragment
        return v;
    }

}
