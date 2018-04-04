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

import com.example.hp.hospitalondemand.HeartData.PrematurebabyDoctorDatas;
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

public class PrematurebabyDoctor extends AppCompatActivity {

    private ListView prematurebabydoctorlistView;
    private ProgressDialog prematurebabydoctor_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prematurebaby_doctor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar()!= null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        prematurebabydoctor_dialog=new ProgressDialog(this);
        prematurebabydoctor_dialog.setIndeterminate(true);
        prematurebabydoctor_dialog.setCancelable(false);
        prematurebabydoctor_dialog.setMessage("Loading, Please Wait...");

        // Image loader
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);

        prematurebabydoctorlistView =(ListView)findViewById(R.id.prematurebaby_doctor_listView);
        new JSONTask().execute("https://hospitalondemands.000webhostapp.com/connection/json_get_data_prematurebaby_doctors.php");
    }

    public class JSONTask extends AsyncTask<String, String, List<PrematurebabyDoctorDatas>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prematurebabydoctor_dialog.show();
        }

        @Override
        protected List<PrematurebabyDoctorDatas> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader=null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder buffer = new StringBuilder();

                String line;
                while((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                String finalJson = buffer.toString();

                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("server_responce");
                List<PrematurebabyDoctorDatas> prematurebabyDoctorDatasList= new ArrayList<>();

                Gson prematurebabyDoctor_gson= new Gson();
                for (int l=0; l<parentArray.length(); l++){
                    JSONObject finalObject= parentArray.getJSONObject(l);
                    PrematurebabyDoctorDatas prematurebabyDoctorDatas = prematurebabyDoctor_gson.fromJson(finalObject.toString(),PrematurebabyDoctorDatas.class);
                    prematurebabyDoctorDatasList.add(prematurebabyDoctorDatas);
                }
                return prematurebabyDoctorDatasList;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }finally {
                if (connection != null){
                    connection.disconnect();
                }
                try{
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
        protected void onPostExecute(List<PrematurebabyDoctorDatas> prematurebabyDoctorresult) {
            super.onPostExecute(prematurebabyDoctorresult);
            prematurebabydoctor_dialog.dismiss();
            PrematurebabyDoctorAdapter prematurebabyDoctorAdapter= new PrematurebabyDoctorAdapter(getApplicationContext(),R.layout.prematurebaby_doctor, prematurebabyDoctorresult);
            prematurebabydoctorlistView.setAdapter(prematurebabyDoctorAdapter);
        }
    }

    public class PrematurebabyDoctorAdapter extends ArrayAdapter{

        private List<PrematurebabyDoctorDatas> prematurebabyDoctorDatasList;
        private int prematurebabyDoctor_resource;
        private LayoutInflater prematurebabyDoctor_inflater;

        public PrematurebabyDoctorAdapter(Context context, int resource, List<PrematurebabyDoctorDatas> objects) {
            super(context, resource, objects);
            prematurebabyDoctorDatasList =objects;
            this.prematurebabyDoctor_resource =resource;
            prematurebabyDoctor_inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            prematurebabyDoctor_ViewHolder prematurebabyDoctor_viewHolder;
            if (convertView == null){
                prematurebabyDoctor_viewHolder =new prematurebabyDoctor_ViewHolder();
                convertView = prematurebabyDoctor_inflater.inflate(prematurebabyDoctor_resource, null);

                prematurebabyDoctor_viewHolder.prematurebaby_doctor_imageView=(ImageView)convertView.findViewById(R.id.prematurebaby_doctor_imageView);
                prematurebabyDoctor_viewHolder.prematurebaby_doctor_textView2=(TextView)convertView.findViewById(R.id.prematurebaby_doctor_textView2);
                prematurebabyDoctor_viewHolder.prematurebaby_doctor_textView3=(TextView)convertView.findViewById(R.id.prematurebaby_doctor_textView3);
                prematurebabyDoctor_viewHolder.prematurebaby_doctor_textView4=(TextView)convertView.findViewById(R.id.prematurebaby_doctor_textView4);
                convertView.setTag(prematurebabyDoctor_viewHolder);
            } else {
                prematurebabyDoctor_viewHolder= (prematurebabyDoctor_ViewHolder) convertView.getTag();
            }
            final ProgressBar prematurebabyDoctor_progressBar =(ProgressBar)convertView.findViewById(R.id.prematurebaby_doctor_progressBar);
            ImageLoader.getInstance().displayImage(prematurebabyDoctorDatasList.get(position).getPrematurebaby_doctors_images(), prematurebabyDoctor_viewHolder.prematurebaby_doctor_imageView, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    prematurebabyDoctor_progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    prematurebabyDoctor_progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    prematurebabyDoctor_progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    prematurebabyDoctor_progressBar.setVisibility(View.GONE);
                }
            });
            prematurebabyDoctor_viewHolder.prematurebaby_doctor_textView2.setText(prematurebabyDoctorDatasList.get(position).getPrematurebaby_doctors_name());
            prematurebabyDoctor_viewHolder.prematurebaby_doctor_textView3.setText(prematurebabyDoctorDatasList.get(position).getPrematurebaby_doctors_title());
            prematurebabyDoctor_viewHolder.prematurebaby_doctor_textView4.setText(prematurebabyDoctorDatasList.get(position).getPrematurebaby_doctors_pnumber());
            return convertView;
        }

        class prematurebabyDoctor_ViewHolder{
            private ImageView prematurebaby_doctor_imageView;
            private TextView prematurebaby_doctor_textView2;
            private TextView prematurebaby_doctor_textView3;
            private TextView prematurebaby_doctor_textView4;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater =getMenuInflater();
        menuInflater.inflate(R.menu.menu_prematurebaby_doctor,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

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
