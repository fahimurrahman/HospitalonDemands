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

import com.example.hp.hospitalondemand.HeartData.HeartsDoctorDatas;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HeartDoctor extends AppCompatActivity {

    private ListView heartdoctorlistView;
    private ProgressDialog heartdoctor_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_doctor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        heartdoctor_dialog= new ProgressDialog(this);
        heartdoctor_dialog.setIndeterminate(true);
        heartdoctor_dialog.setCancelable(false);
        heartdoctor_dialog.setMessage("Loading, Please wait...");

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);

        heartdoctorlistView= (ListView) findViewById(R.id.heart_doctor_listView);
        new JSONTask().execute("https://hospitalondemands.000webhostapp.com/connection/json_get_data_heart_doctors.php");
    }

    public class JSONTask extends AsyncTask<String, String, List<HeartsDoctorDatas>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            heartdoctor_dialog.show();
        }

        @Override
        protected List<HeartsDoctorDatas> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try{
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder buffer = new StringBuilder();

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String finalJson = buffer.toString();
                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("server_responce");

                List<HeartsDoctorDatas> heartsDoctorDatasList = new ArrayList<>();
                Gson heartdoctors_gson = new Gson();

                for (int i = 0; i < parentArray.length(); i++){
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    HeartsDoctorDatas heartsDoctorDatas = heartdoctors_gson.fromJson(finalObject.toString(), HeartsDoctorDatas.class);
                    heartsDoctorDatasList.add(heartsDoctorDatas);
                }
                return heartsDoctorDatasList;


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
        protected void onPostExecute(List<HeartsDoctorDatas> heartDoctorresult) {
            super.onPostExecute(heartDoctorresult);
            heartdoctor_dialog.dismiss();
            HeartDoctorAdapter heartDoctorAdapter = new HeartDoctorAdapter(getApplicationContext(), R.layout.heart_doctors, heartDoctorresult);
            heartdoctorlistView.setAdapter(heartDoctorAdapter);
        }
    }
    public class HeartDoctorAdapter extends ArrayAdapter{

        private List<HeartsDoctorDatas> heartsDoctorDatasList;
        private int heartDoctor_resource;
        private LayoutInflater heartDoctor_inflater;
        public HeartDoctorAdapter(Context context, int resource, List<HeartsDoctorDatas> objects) {
            super(context, resource, objects);
            heartsDoctorDatasList = objects;
            this.heartDoctor_resource = resource;
            heartDoctor_inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            heartdoctor_ViewHolder heartdoctor_viewHolder;
            if (convertView == null){
                heartdoctor_viewHolder = new heartdoctor_ViewHolder();
                convertView = heartDoctor_inflater.inflate(heartDoctor_resource, null);
                heartdoctor_viewHolder.heart_doctor_imageView=(ImageView)convertView.findViewById(R.id.heart_doctor_imageView);
                heartdoctor_viewHolder.heart_doctor_textView2=(TextView)convertView.findViewById(R.id.heart_doctor_textView2);
                heartdoctor_viewHolder.heart_doctor_textView3=(TextView)convertView.findViewById(R.id.heart_doctor_textView3);
                heartdoctor_viewHolder.heart_doctor_textView4=(TextView)convertView.findViewById(R.id.heart_doctor_textView4);
                convertView.setTag(heartdoctor_viewHolder);
            }else {
                heartdoctor_viewHolder= (heartdoctor_ViewHolder)convertView.getTag();
            }
            final ProgressBar heartdoctor_progressBar=(ProgressBar)convertView.findViewById(R.id.heart_doctor_progressBar);
            ImageLoader.getInstance().displayImage(heartsDoctorDatasList.get(position).getHeart_doctors_images(), heartdoctor_viewHolder.heart_doctor_imageView, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    heartdoctor_progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    heartdoctor_progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    heartdoctor_progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    heartdoctor_progressBar.setVisibility(View.GONE);

                }
            });
            heartdoctor_viewHolder.heart_doctor_textView2.setText(heartsDoctorDatasList.get(position).getHeart_doctors_name());
            heartdoctor_viewHolder.heart_doctor_textView3.setText(heartsDoctorDatasList.get(position).getHeart_doctors_title());
            heartdoctor_viewHolder.heart_doctor_textView4.setText(heartsDoctorDatasList.get(position).getHeart_doctors_pnumber());
            return convertView;
        }

        class heartdoctor_ViewHolder{
            private ImageView heart_doctor_imageView;
            private TextView heart_doctor_textView2;
            private TextView heart_doctor_textView3;
            private TextView heart_doctor_textView4;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_heart_doctor,menu);
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
