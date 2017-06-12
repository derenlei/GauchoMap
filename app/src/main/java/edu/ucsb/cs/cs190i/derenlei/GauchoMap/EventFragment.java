package edu.ucsb.cs.cs190i.derenlei.GauchoMap;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import static android.R.attr.fragment;

/**
 * Created by Zichen Sun on 2/6/2017.
 */

public class EventFragment extends android.support.v4.app.DialogFragment  {
    private EditText event_text;
    public static TextView event_time;
    private ImageView event_image;
    private EditText event_name;
    private Button timeButton;
    private FragmentActivity fragmentActivity;
    public static FragmentManager fragmentManager;

    public static EventFragment newInstance(Uri uri, int User, Double longitude, Double latitude) {
        EventFragment frag = new EventFragment();
        Bundle args = new Bundle();
        args.putString("title", uri.toString());
        args.putDouble("longitude", longitude);
        args.putDouble("latitude",latitude);
        args.putInt("User",User);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentActivity = this.getActivity();
        fragmentManager = fragmentActivity.getFragmentManager();
        getDialog().setCanceledOnTouchOutside(false);
        return inflater.inflate(R.layout.event, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        event_name = (EditText) view.findViewById(R.id.name_text);
        event_text = (EditText) view.findViewById(R.id.event_text);
        event_image = (ImageView) view.findViewById(R.id.event_image);
        event_time = (TextView) view.findViewById(R.id.time_text);
        timeButton = (Button) view.findViewById(R.id.timeButton);
        Button getbutton = (Button) view.findViewById(R.id.get_button);
        getbutton.setBackgroundColor(Color.GRAY);
        String title = getArguments().getString("title", "Enter Name");
        Uri uri = Uri.parse((String)getArguments().get("title"));
        Picasso.with(getActivity()).load(uri).resize(800,800).centerCrop().into(event_image);

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(fragmentActivity.getFragmentManager(), "datePicker");
                //DialogFragment newFragment = new TimePickerFragment();
                //newFragment.show(fragmentActivity.getFragmentManager(), "timePicker");
            }
        });

        //Picasso.with(getActivity()).load(uri).into(event_image);
        if(DatabaseFactory.is_present(String.valueOf(uri))) {
            event_name.setText(DatabaseFactory.getName(String.valueOf(uri)));
            event_text.setText(DatabaseFactory.getEvent(String.valueOf(uri)));
            event_time.setText(DatabaseFactory.getTime(String.valueOf(uri)));
        }
        getDialog().setTitle(title);
        event_text.requestFocus();
        boolean present = DatabaseFactory.is_present(String.valueOf(uri));
        final String Uri = String.valueOf(uri);
        getbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = String.valueOf(event_name.getText());
                String event = String.valueOf(event_text.getText());
                String time = String.valueOf(event_time.getText());
                Double longitude = (Double) getArguments().get("longitude");
                Double latitude = (Double) getArguments().get("latitude");
                int User = (int)getArguments().get("User");
                DatabaseFactory.saveandgetName(User,name,Uri,event,longitude,latitude,time);
                DatabaseFactory.TEST(name);
                DatabaseFactory.update();
                LatLng position = new LatLng(latitude, longitude);
                MapsActivity.addMarker(position);
                getDialog().dismiss();
            }
        });
    }
}
