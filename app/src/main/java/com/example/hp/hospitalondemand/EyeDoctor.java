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

import com.example.hp.hospitalondemand.HeartData.EyeDoctorDatas;
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

public class EyeDoctor extends AppCompatActivity {
    private ListView eyedoctorlistView;
    private ProgressDialog eyedoctor_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eye_doctor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        eyedoctor_dialog= new ProgressDialog(this);
        eyedoctor_dialog.setIndeterminate(true);
        eyedoctor_dialog.setCancelable(false);
        eyedoctor_dialog.setMessage("Loading, Please Wait...");

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);

        eyedoctorlistView =(ListView)findViewById(R.id.eye_doctor_listView);
        new JSONTask().execute("https://hospitalondemands.000webhostapp.com/connection/json_get_data_eye_doctors.php");
    }

    public class JSONTask extends AsyncTask<String, String, List<EyeDoctorDatas>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            eyedoctor_dialog.show();
        }

        @Override
        protected List<EyeDoctorDatas> doInBackground(String... params) {
            HttpURLConnection connection =null;
            BufferedReader reader = null;

            try{
                URL url = new URL(params[0]);
                connection =(HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream =connection.getInputStream();
                reader =new BufferedReader(new InputStreamReader(stream));
                StringBuilder buffer= new StringBuilder();

                String line;

                while((line = reader.readLine()) != null){
                    buffer.append(line);
                }
                String finalJson = buffer.toString();

                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("server_responce");
                List<EyeDoctorDatas> eyeDoctorDatasList = new ArrayList<>();

                Gson eyedoctor_gson= new Gson();
                for (int o=0; o<parentArray.length(); o++) {
                    JSONObject finalObject = parentArray.getJSONObject(o);
                    EyeDoctorDatas  eyeDoctorDatas= eyedoctor_gson.fromJson(finalObject.toString(),EyeDoctorDatas.class);
                    eyeDoctorDatasList.add(eyeDoctorDatas);
                }
                return eyeDoctorDatasList;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<EyeDoctorDatas> eyeDoctorresult) {
            super.onPostExecute(eyeDoctorresult);
            eyedoctor_dialog.dismiss();
            EyeDoctorAdapter eyeDoctorAdapter = new EyeDoctorAdapter(getApplicationContext(),R.layout.eye_doctor,eyeDoctorresult);
            eyedoctorlistView.setAdapter(eyeDoctorAdapter);
        }
    }

    public class EyeDoctorAdapter extends ArrayAdapter{

        private List<EyeDoctorDatas> eyeDoctorDatasList;
        private int eyedoctor_resource;
        private LayoutInflater eyedoctor_inflater;

        public EyeDoctorAdapter(Context context, int resource, List<EyeDoctorDatas> objects) {
            super(context, resource, objects);
            eyeDoctorDatasList= objects;
            this.eyedoctor_resource = resource;
            eyedoctor_inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final eyedoctor_ViewHolder eyedoctor_viewHolder;
            if (convertView == null){
                eyedoctor_viewHolder = new eyedoctor_ViewHolder();
                convertView = eyedoctor_inflater.inflate(eyedoctor_resource,null);

                eyedoctor_viewHolder.eye_doctor_imageView=(ImageView)convertView.findViewById(R.id.eye_doctor_imageView);
                eyedoctor_viewHolder.eye_doctor_textView2=(TextView)convertView.findViewById(R.id.eye_doctor_textView2);
                eyedoctor_viewHolder.eye_doctor_textView3=(TextView)convertView.findViewById(R.id.eye_doctor_textView3);
                eyedoctor_viewHolder.eye_doctor_textView4=(TextView)convertView.findViewById(R.id.eye_doctor_textView4);
                convertView.setTag(eyedoctor_viewHolder);
            }else {
                eyedoctor_viewHolder= (eyedoctor_ViewHolder)convertView.getTag();
            }

            final ProgressBar eyedoctor_progressBar = (ProgressBar)convertView.findViewById(R.id.eye_doctor_progressBar);
            ImageLoader.getInstance().displayImage(eyeDoctorDatasList.get(position).getEye_doctors_images(), eyedoctor_viewHolder.eye_doctor_imageView, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    eyedoctor_progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    eyedoctor_progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    eyedoctor_progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    eyedoctor_progressBar.setVisibility(View.GONE);
                }
            });

            eyedoctor_viewHolder.eye_doctor_textView2.setText(eyeDoctorDatasList.get(position).getEye_doctors_name());
            eyedoctor_viewHolder.eye_doctor_textView3.setText(eyeDoctorDatasList.get(position).getEye_doctors_title());
            eyedoctor_viewHolder.eye_doctor_textView4.setText(eyeDoctorDatasList.get(position).getEye_doctors_pnumber());
            return convertView;
        }

        class eyedoctor_ViewHolder{
            private ImageView eye_doctor_imageView;
            private TextView eye_doctor_textView2;
            private TextView eye_doctor_textView3;
            private TextView eye_doctor_textView4;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater =getMenuInflater();
        menuInflater.inflate(R.menu.menu_eye_doctor,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id== R.id.action_about){
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
