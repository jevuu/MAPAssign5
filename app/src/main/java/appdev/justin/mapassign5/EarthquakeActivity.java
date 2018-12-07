package appdev.justin.mapassign5;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;

public class EarthquakeActivity extends AppCompatActivity {

    ListView linearLayoutListView;
    String stringUrl = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&minmagnitude=7";
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake);

        intent = getIntent();
        stringUrl += "&starttime=" + intent.getStringExtra("StartDate");
        stringUrl += "&orderby=" + intent.getStringExtra("OrderBy");
        if(intent.hasExtra("Limit")) {
            stringUrl += "&limit=" + intent.getStringExtra("Limit");
        }

        QuakeAsyncTask task = new QuakeAsyncTask();
        Log.d("String URL", stringUrl);
        task.execute(stringUrl);
    }

    class QuakeAsyncTask extends AsyncTask<String, Void, List<String>> {
        @Override
        protected List<String> doInBackground(String... stringurl) {
            return Utils.fetchEarthquakeData(stringurl[0]);
        }

        public void onPostExecute(List<String> postExecuteResult) {
            CustomListAdapter arrayAdapter = new CustomListAdapter
                    (EarthquakeActivity.this, postExecuteResult);
            linearLayoutListView = findViewById(R.id.listViewManyItemsID);
            linearLayoutListView.setAdapter(arrayAdapter);
        }
    }
}

class CustomListAdapter extends ArrayAdapter<String> {
    Activity context;
    List<String> itemname1;

    public CustomListAdapter(Activity activity, List<String> itemnameA) {
        super(activity, R.layout.one_quake, itemnameA);
        this.context = activity;
        this.itemname1 = itemnameA;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.one_quake, null, true);
        final String earthInfo[] = itemname1.get(position).split("@@");
        final String coords[] = earthInfo[4].substring(1, earthInfo[4].length() - 1).split(",");
        TextView textInfo = rowView.findViewById(R.id.textViewDate);
        //int mag = parseInt(earthInfo[3]);
        if(parseFloat(earthInfo[3]) >= 7.5) {
            textInfo.setBackgroundColor(0xFFFF0000);
        }else {
            textInfo.setBackgroundColor(0xFFFFBB33);
        }
        Date date = new Date(Long.parseLong(earthInfo[1]));
        SimpleDateFormat dateFormat = new SimpleDateFormat("E MMM dd kk:mm:ss zzz yyyy");
        String stringDate = dateFormat.format(date);
        textInfo.setText(earthInfo[0] + "\n" + stringDate);
        textInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.openstreetmap.org/?mlat=" + coords[1]
                                + "&mlon=" + coords[0]
                                + "#map=5/" + coords[1] + "/" + coords[0]));
                context.startActivity(browserIntent);
            }
        });
        return rowView;
    }
}
