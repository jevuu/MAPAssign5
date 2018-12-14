package appdev.justin.mapassign5;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    EditText editNumberEarthquakes;
    Spinner spinOrderBy;
    TextView txtStartDate;
    Button btnSubmit;

    Date d = Calendar.getInstance().getTime();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    String stringStartDate;
    String stringOrderBy;
    String stringLimit;

    DialogFragment newFragment = new DatePickerFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set spinner
        spinOrderBy = findViewById(R.id.spinOrderBy);
        ArrayAdapter<CharSequence> spinOrderByAdapter = ArrayAdapter.createFromResource(this,
                R.array.arrayOrderBy, R.layout.spinner_item);
        spinOrderByAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinOrderBy.setAdapter(spinOrderByAdapter);

        //Set starting date to current date and display date
        stringStartDate = df.format(d);
        txtStartDate = findViewById(R.id.txtStartDate);
        txtStartDate.setText(stringStartDate);

        //Submit button. Upon pressing submit, get the user input for number of earthquakes to list
        //Then put all data into intent and start EarthquakeActivity
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get user input number of earthquakes to list
                editNumberEarthquakes = findViewById(R.id.editNumberEarthquakes);
                stringOrderBy = spinOrderBy.getSelectedItem().toString();
                stringLimit = editNumberEarthquakes.getText().toString();

                Intent intent = new Intent(getApplicationContext(), EarthquakeActivity.class);
                intent.putExtra("StartDate", txtStartDate.getText().toString());
                intent.putExtra("OrderBy", stringOrderBy);
                if(!stringLimit.isEmpty()) {
                    intent.putExtra("Limit", stringLimit);
                }
                startActivity(intent);
            }
        });

    }

    //Fragment for date selection pop-up
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

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
            Calendar c = Calendar.getInstance();
            c.set(year, month, day);
            Date d = c.getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String stringStartDate = df.format(d);
            TextView txtStartDate = getActivity().findViewById(R.id.txtStartDate);
            txtStartDate.setText(stringStartDate);
        }
    }

    public void showDatePickerDialog(View v) {
        //DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

}
