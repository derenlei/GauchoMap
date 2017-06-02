package edu.ucsb.cs.cs190i.derenlei.GauchoMap;

import android.app.DialogFragment;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Deren Lei on 6/2/2017.
 */

public class TagEntryFragment extends android.support.v4.app.DialogFragment {
    private EditText event_text;
    private EditText event_time;
    private ImageView event_image;
    private EditText event_name;

    public static TagEntryFragment newInstance(Uri uri, Double longitude, Double latitude) {
        TagEntryFragment frag = new TagEntryFragment();
        Bundle args = new Bundle();
        args.putString("title", uri.toString());
        args.putDouble("longitude", longitude);
        args.putDouble("latitude",latitude);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tag, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        event_name = (EditText) view.findViewById(R.id.name_text);
        event_text = (EditText) view.findViewById(R.id.event_text);
        event_image = (ImageView) view.findViewById(R.id.event_image);
        event_time = (EditText) view.findViewById(R.id.time_text);
        Button getbutton = (Button) view.findViewById(R.id.get_button);
        getbutton.setBackgroundColor(Color.GRAY);
        String title = getArguments().getString("title", "Enter Name");
        Uri uri = Uri.parse((String)getArguments().get("title"));
        Picasso.with(getActivity()).load(uri).resize(800,800).centerCrop().into(event_image);
        //Picasso.with(getActivity()).load(uri).into(event_image);
        if(DatabaseFactory.is_present(String.valueOf(uri))) {
            event_name.setText(DatabaseFactory.getName(String.valueOf(uri)));
            event_text.setText(DatabaseFactory.getEvent(String.valueOf(uri)));
            event_time.setText(DatabaseFactory.getTime(String.valueOf(uri)));
        }
        getDialog().setTitle(title);
        event_text.requestFocus();
        boolean present = DatabaseFactory.is_present(String.valueOf(uri));
        if(DatabaseFactory.is_present(String.valueOf(uri))){
            final String Uri = String.valueOf(uri);
            getbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = String.valueOf(event_name.getText());
                    String event = String.valueOf(event_text.getText());
                    String time = String.valueOf(event_time.getText());
                    Double longitude = (Double) getArguments().get("longitude");
                    Double latitude = (Double) getArguments().get("latitude");
                    DatabaseFactory.saveandgetName(name,Uri,event,longitude,latitude,time);
                    DatabaseFactory.TEST(name);
                    DatabaseFactory.update();
                }
            });
        }
        else{
            final String Uri = String.valueOf(uri);
            getbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = String.valueOf(event_name.getText());
                    String event = String.valueOf(event_text.getText());
                    String time = String.valueOf(event_time.getText());
                    Double longitude = (Double) getArguments().get("longitude");
                    Double latitude = (Double) getArguments().get("latitude");
                    DatabaseFactory.saveandgetName(name,Uri,event,longitude,latitude,time);
                    DatabaseFactory.TEST(name);
                    DatabaseFactory.update();
                }
            });
        }

    }
}
