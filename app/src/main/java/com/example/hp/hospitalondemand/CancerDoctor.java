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

import com.example.hp.hospitalondemand.HeartData.CancerDoctorDatas;
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

public class CancerDoctor extends AppCompatActivity {

    private ListView cancerdoctorlistView;
    private ProgressDialog cancerdoctor_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancer_doctor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        cancerdoctor_dialog= new ProgressDialog(this);
        cancerdoctor_dialog.setIndeterminate(true);
        cancerdoctor_dialog.setCancelable(false);
        cancerdoctor_dialog.setMessage("Loading, Please Wait...");

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);

        cancerdoctorlistView =(ListView)findViewById(R.id.cancer_doctor_listView);
        new JSONTask().execute("https://hospitalondemands.000webhostapp.com/connection/json_get_data_cancer_doctors.php");
    }

    public class JSONTask extends AsyncTask<String, String, List<CancerDoctorDatas>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cancerdoctor_dialog.show();
        }

        @Override
        protected List<CancerDoctorDatas> doInBackground(String... params) {
            HttpURLConnection connection =null;
            BufferedReader reader =null;
            try {
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

                List<CancerDoctorDatas>cancerDoctorDatasList = new ArrayList<>();
                Gson cancerdoctor_gson = new Gson();
                for (int n=0; n<parentArray.length(); n++) {
                    JSONObject finalObject = parentArray.getJSONObject(n);
                    CancerDoctorDatas cancerDoctorDatas = cancerdoctor_gson.fromJson(finalObject.toString(),CancerDoctorDatas.class);
                    cancerDoctorDatasList.add(cancerDoctorDatas);
                }
                return cancerDoctorDatasList;
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
        protected void onPostExecute(List<CancerDoctorDatas> cancerDoctorresult) {
            super.onPostExecute(cancerDoctorresult);
            cancerdoctor_dialog.dismiss();
            CancerDoctorAdapter cancerDoctorAdapter = new CancerDoctorAdapter(getApplicationContext(),R.layout.cancer_doctor, cancerDoctorresult);
            cancerdoctorlistView.setAdapter(cancerDoctorAdapter);
        }
    }
    public class CancerDoctorAdapter extends ArrayAdapter{

        private List<CancerDoctorDatas> cancerDoctorDatasList;
        private int cancerdoctor_resource;
        private LayoutInflater cancerdoctor_inflater;

        public CancerDoctorAdapter(Context context, int resource, List<CancerDoctorDatas> objects) {
            super(context, resource, objects);
            cancerDoctorDatasList = objects;
            this.cancerdoctor_resource = resource;
            cancerdoctor_inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            cancerdoctor_ViewHolder cancerdoctor_viewHolder ;

            if (convertView == null){
                cancerdoctor_viewHolder = new cancerdoctor_ViewHolder();
                convertView = cancerdoctor_inflater.inflate(cancerdoctor_resource,null);
                cancerdoctor_viewHolder.cancer_doctor_imageView=(ImageView)convertView.findViewById(R.id.cancer_doctor_imageView);
                cancerdoctor_viewHolder.cancer_doctor_textView2=(TextView)convertView.findViewById(R.id.cancer_doctor_textView2);
                cancerdoctor_viewHolder.cancer_doctor_textView3=(TextView)convertView.findViewById(R.id.cancer_doctor_textView3);
                cancerdoctor_viewHolder.cancer_doctor_textView4=(TextView)convertView.findViewById(R.id.cancer_doctor_textView4);
                convertView.setTag(cancerdoctor_viewHolder);
            }else {
                cancerdoctor_viewHolder=(cancerdoctor_ViewHolder)convertView.getTag();
            }

            final ProgressBar cancerdoctor_progressBar = (ProgressBar)convertView.findViewById(R.id.cancer_doctor_progressBar);
            ImageLoader.getInstance().displayImage(cancerDoctorDatasList.get(position).getCancer_doctors_images(), cancerdoctor_viewHolder.cancer_doctor_imageView, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    cancerdoctor_progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    cancerdoctor_progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    cancerdoctor_progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    cancerdoctor_progressBar.setVisibility(View.GONE);
                }
            });

            cancerdoctor_viewHolder.cancer_doctor_textView2.setText(cancerDoctorDatasList.get(position).getCancer_doctors_name());
            cancerdoctor_viewHolder.cancer_doctor_textView3.setText(cancerDoctorDatasList.get(position).getCancer_doctors_title());
            cancerdoctor_viewHolder.cancer_doctor_textView4.setText(cancerDoctorDatasList.get(position).getCancer_doctors_pnumber());
            return convertView;
        }

        class cancerdoctor_ViewHolder{
            private ImageView cancer_doctor_imageView;
            private TextView cancer_doctor_textView2;
            private TextView cancer_doctor_textView3;
            private TextView cancer_doctor_textView4;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_cancer_doctor, menu);
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
