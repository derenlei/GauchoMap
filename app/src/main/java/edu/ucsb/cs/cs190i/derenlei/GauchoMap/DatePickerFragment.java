package edu.ucsb.cs.cs190i.derenlei.GauchoMap;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.widget.DatePicker;
import android.app.DialogFragment;
import android.icu.util.Calendar;



/**
 * Created by Deren Lei on 6/9/2017.
 */

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    public static int pickedYear;
    public static int pickedMonth;
    public static int pickedDay;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        pickedDay = day;
        pickedMonth = month;
        pickedYear = year;
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(EventFragment.fragmentManager, "timePicker");
    }
}