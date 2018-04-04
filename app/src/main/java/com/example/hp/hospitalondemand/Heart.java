package com.example.hp.hospitalondemand;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.hp.hospitalondemand.HeartData.HeartsDatas;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
//TODO import java.net.MalformedURLException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Heart extends AppCompatActivity {

    private ListView heartlistView;
    private ProgressDialog heart_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        heart_dialog = new ProgressDialog(this);
        heart_dialog.setIndeterminate(true);
        heart_dialog.setCancelable(false);
        heart_dialog.setMessage("Loading, Please wait...");


        // Data fetch started from here

        // Create default options which will be used for every
//  displayImage(...) call if no options will be passed to this method
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);
        //List view
        heartlistView = (ListView) findViewById(R.id.heart_listView);
        new JSONTask().execute("https://hospitalondemands.000webhostapp.com/connection/json_get_data.php");
    }

    public class JSONTask extends AsyncTask<String, String, List<HeartsDatas>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            heart_dialog.show();
        }


        @Override
        protected List<HeartsDatas> doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder buffer = new StringBuilder(); // TODO (StringBuffer buffer = new StringBuffer();)

                String line;                   //TODO (line = "";)
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String finalJson = buffer.toString();

                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("server_responce");

                List<HeartsDatas> heartsDatasList =new ArrayList<>();

                Gson heart_gson= new Gson();
                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    HeartsDatas heartsDatas = heart_gson.fromJson(finalObject.toString(), HeartsDatas.class);
                    // adding the final object of the list
                    heartsDatasList.add(heartsDatas);
                }
                return heartsDatasList;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null){
                    connection.disconnect();
                }
                try {
                    if (reader != null){
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<HeartsDatas> heartresult) {
            super.onPostExecute(heartresult);

            heart_dialog.dismiss();
            HeartAdapter heartAdapter= new HeartAdapter(getApplicationContext(), R.layout.heart, heartresult);
            heartlistView.setAdapter(heartAdapter);
        }
    }

    public class HeartAdapter extends ArrayAdapter{

        private List<HeartsDatas> heartsDatasList;
        private int heart_resource;
        private LayoutInflater heart_inflater;

        public HeartAdapter(Context context, int resource, List<HeartsDatas> objects) {
            super(context, resource, objects);
            heartsDatasList = objects;
            this.heart_resource = resource;
            heart_inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            heart_ViewHolder heart_holder;

            if (convertView == null){
                heart_holder = new heart_ViewHolder();
                convertView = heart_inflater.inflate(heart_resource, null);
                heart_holder.heart_imageView=(ImageView)convertView.findViewById(R.id.heart_imageView);
                heart_holder.heart_textView21= (TextView)convertView.findViewById(R.id.heart_textView2);
                heart_holder.heart_textView31= (TextView)convertView.findViewById(R.id.heart_textView3);
                heart_holder.heart_textView41= (TextView)convertView.findViewById(R.id.heart_textView4);
                heart_holder.heart_textView51= (TextView)convertView.findViewById(R.id.heart_textView5);
                heart_holder.heart_textView61= (TextView)convertView.findViewById(R.id.heart_textView6);
                convertView.setTag(heart_holder);
            } else {
                heart_holder = (heart_ViewHolder) convertView.getTag();
            }

            final ProgressBar heart_progressBar = (ProgressBar)convertView.findViewById(R.id.heart_progressBar);

            ImageLoader.getInstance().displayImage(heartsDatasList.get(position).getHospital_heart_images(), heart_holder.heart_imageView, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    heart_progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    heart_progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    heart_progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    heart_progressBar.setVisibility(View.GONE);
                }
            });

            heart_holder.heart_textView21.setText(heartsDatasList.get(position).getHospital_heart_name());
            heart_holder.heart_textView31.setText(heartsDatasList.get(position).getHospital_heart_discription());
            heart_holder.heart_textView41.setText(heartsDatasList.get(position).getHospital_heart_equipment());
            heart_holder.heart_textView51.setText(heartsDatasList.get(position).getHospital_heart_adress());
            heart_holder.heart_textView61.setText(heartsDatasList.get(position).getHospital_heart_pnumber());
            return convertView;
        }

        class heart_ViewHolder{
            private ImageView heart_imageView;
            private TextView heart_textView21;
            private TextView heart_textView31;
            private TextView heart_textView41;
            private TextView heart_textView51;
            private TextView heart_textView61;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_heart,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id= item.getItemId();

        if (id == R.id.action_about){
            startActivity(new Intent(this,Us.class));
            return true;
        }else if (id == R.id.action_ambulance){
            startActivity(new Intent(this,Ambulances.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
