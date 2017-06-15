package edu.ucsb.cs.cs190i.derenlei.GauchoMap;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import static edu.ucsb.cs.cs190i.derenlei.GauchoMap.R.id.get_button;
import static edu.ucsb.cs.cs190i.derenlei.GauchoMap.R.id.nameButton;
import static edu.ucsb.cs.cs190i.derenlei.GauchoMap.R.layout.event;

/**
 * Created by Zichen Sun on 2/6/2017.
 */

public class EventFragment extends android.support.v4.app.DialogFragment  {
    private EditText event_text;
    public static TextView event_time;
    private ImageView event_image;
    private EditText event_name;
    private Button editButton;
    private Button timeButton;
    private FragmentActivity fragmentActivity;
    private String previousName;
    private String previousInfo;
    private String previousTime;
    public static FragmentManager fragmentManager;

    public static EventFragment newInstance(Uri uri, String User, Double longitude, Double latitude) {
        EventFragment frag = new EventFragment();
        Bundle args = new Bundle();
        args.putString("title", uri.toString());
        args.putDouble("longitude", longitude);
        args.putDouble("latitude",latitude);
        args.putString("User",User);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentActivity = this.getActivity();
        fragmentManager = fragmentActivity.getFragmentManager();
        getDialog().setCanceledOnTouchOutside(false);
        return inflater.inflate(event, container);
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        window.getDecorView().setOnTouchListener(new SwipeDismissTouchListener(window.getDecorView(), null, new SwipeDismissTouchListener.DismissCallbacks() {
            @Override
            public boolean canDismiss(Object token) {
                return true;
            }

            @Override
            public void onDismiss(View view, Object token) {
                event_name = (EditText) view.findViewById(R.id.name_text);
                if (event_name.getText().toString().equals("")){
                    dismiss();

                }
                else {
                    if (previousName.equals("") || previousInfo.equals("") || previousTime.equals("")){
                        dismiss();
                    }
                    else {
                        event_name = (EditText) view.findViewById(R.id.name_text);
                        event_text = (EditText) view.findViewById(R.id.event_text);
                        event_time = (TextView) view.findViewById(R.id.time_text);
                        event_name.setText(previousName);
                        event_text.setText(previousInfo);
                        event_time.setText(previousTime);
                        Button getbutton = (Button) view.findViewById(get_button);
                        getbutton.callOnClick();
                    }
                }
            }
        }));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        event_name = (EditText) view.findViewById(R.id.name_text);
        event_text = (EditText) view.findViewById(R.id.event_text);
        event_image = (ImageView) view.findViewById(R.id.event_image);
        event_time = (TextView) view.findViewById(R.id.time_text);
        timeButton = (Button) view.findViewById(R.id.timeButton);
        timeButton.setEnabled(false);
        editButton = (Button) view.findViewById(R.id.editButton);
        event_name.setFocusable(false);
        event_text.setFocusable(false);
        final Button getbutton = (Button) view.findViewById(get_button);


        getbutton.setBackgroundColor(Color.GRAY);
        String title = getArguments().getString("title", "Enter Name");
        Uri uri = Uri.parse((String)getArguments().get("title"));
        Picasso.with(getActivity()).load(uri).resize(800,800).centerCrop().into(event_image);

//        if((String)getArguments().get("User") != DatabaseFactory.getUserByUri((String)getArguments().get("title"))){
//            LinearLayout ll = (LinearLayout) view.findViewById(R.id.event_ll);
//            ll.removeView(editButton);
//            ll.removeView(getbutton);
//        }

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(fragmentActivity.getFragmentManager(), "datePicker");
                //DialogFragment newFragment = new TimePickerFragment();
                //newFragment.show(fragmentActivity.getFragmentManager(), "timePicker");
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //previousName = event_name.getText().toString();
                //previousInfo = event_text.getText().toString();
                //previousTime = event_time.getText().toString();
                event_name.setFocusableInTouchMode(true);
                event_text.setFocusableInTouchMode(true);
                timeButton.setEnabled(true);
                getbutton.setText("Save");
                //DialogFragment newFragment = new TimePickerFragment();
                //newFragment.show(fragmentActivity.getFragmentManager(), "timePicker");
            }
        });


        //Picasso.with(getActivity()).load(uri).into(event_image);
        if(DatabaseFactory.is_present(String.valueOf(uri))) {
            event_name.setText(DatabaseFactory.getName(String.valueOf(uri)));
            event_text.setText(DatabaseFactory.getEvent(String.valueOf(uri)));
            event_time.setText(DatabaseFactory.getTime(String.valueOf(uri)));
            previousName = event_name.getText().toString();
            previousInfo = event_text.getText().toString();
            previousTime = event_time.getText().toString();
        }
        getDialog().setTitle(title);
        event_text.requestFocus();
        boolean present = DatabaseFactory.is_present(String.valueOf(uri));
        final String Uri = String.valueOf(uri);
        getbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (event_name.getText().toString().equals("") || event_text.getText().toString().equals("") ||
                        event_time.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Please fill the blank!", Toast.LENGTH_LONG).show();
                }
                else {
                    String name = String.valueOf(event_name.getText());
                    String event = String.valueOf(event_text.getText());
                    String time = String.valueOf(event_time.getText());
                    Double longitude = (Double) getArguments().get("longitude");
                    Double latitude = (Double) getArguments().get("latitude");
                    String User = (String) getArguments().get("User");
                    DatabaseFactory.saveandgetName(User, name, Uri, event, longitude, latitude, time);
                    DatabaseFactory.TEST(name);
                    DatabaseFactory.update();
                    LatLng position = new LatLng(latitude, longitude);
                    MapsActivity.addMarker(position);
                    getDialog().dismiss();
                }
            }
        });


    }
}
