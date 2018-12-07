package appdev.justin.mapassign5;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView linearLayoutListView;
    String stringUrl = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2000-01-01&minmagnitude=6&limit=20&orderby=time";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        QuakeAsyncTask task = new QuakeAsyncTask();
        task.execute(stringUrl);
    }

    class QuakeAsyncTask extends AsyncTask<String, Void, List<String>> {
        @Override
        protected List<String> doInBackground(String... stringurl) {
            return Utils.fetchEarthquakeData(stringurl[0]);
        }

        public void onPostExecute(List<String> postExecuteResult) {
            CustomListAdapter arrayAdapter = new CustomListAdapter
                    (MainActivity.this, postExecuteResult);
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
        TextView textInfo = rowView.findViewById(R.id.textViewDate);
        if(position % 2 == 0) {
            textInfo.setBackgroundColor(0xFF33B5E5);
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
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(earthInfo[2]));
                context.startActivity(browserIntent);
            }
        });
        return rowView;
    }
}
