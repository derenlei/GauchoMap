package edu.ucsb.cs.cs190i.derenlei.GauchoMap;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
//        Boolean b = !d.is_empty();
//        ArrayList<CardView> cards = new ArrayList();
//        LinearLayout ll = new LinearLayout(getActivity());
//
//        if(!DatabaseFactory.is_empty()) {
//            ArrayList<String> namelist = DatabaseFactory.getNamelist();
//            View aCard = inflater.inflate(R.layout.card, container, false);
//            for (int i = 0; i < namelist.size(); i++) {
//                String name = namelist.get(i);
//                String uri = DatabaseFactory.getUri(name);
//                String time = DatabaseFactory.getTime(name);
//                int interest = DatabaseFactory.getInterest(uri);
//                CardView card = (CardView) aCard.findViewById(R.id.cv);
//                TextView n = (TextView) card.findViewById(R.id.event_name);
//                n.setText(name);
//
//                ll.addView(card);
//            }
//        }else{

        text.setText("You created no events");
//        }
//
//
//        v = ll;
//        // Inflate the layout for this fragment
        return v;
    }

}
