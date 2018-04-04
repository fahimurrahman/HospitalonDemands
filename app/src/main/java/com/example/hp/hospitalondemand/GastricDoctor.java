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

import com.example.hp.hospitalondemand.HeartData.GastricDoctorDatas;
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

public class GastricDoctor extends AppCompatActivity {
    private ListView gastricdoctorlistView;
    private ProgressDialog gastricdoctor_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gastric_doctor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        gastricdoctor_dialog= new ProgressDialog(this);
        gastricdoctor_dialog.setIndeterminate(true);
        gastricdoctor_dialog.setCancelable(false);
        gastricdoctor_dialog.setMessage("Loading, Please Wait...");

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);

        gastricdoctorlistView = (ListView)findViewById(R.id.gastric_doctor_listView);
        new JSONTask().execute("https://hospitalondemands.000webhostapp.com/connection/json_get_data_gastric_doctors.php");
    }

    public class JSONTask extends AsyncTask<String, String, List<GastricDoctorDatas>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            gastricdoctor_dialog.show();
        }

        @Override
        protected List<GastricDoctorDatas> doInBackground(String... params) {
            HttpURLConnection connection=null;
            BufferedReader reader= null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
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
                List<GastricDoctorDatas> gastricDoctorDatasList = new ArrayList<>();

                Gson gastricdoctor_gson= new Gson();
                for (int p=0; p<parentArray.length(); p++) {
                    JSONObject finalObject = parentArray.getJSONObject(p);
                    GastricDoctorDatas gastricDoctorDatas = gastricdoctor_gson.fromJson(finalObject.toString(), GastricDoctorDatas.class);
                    gastricDoctorDatasList.add(gastricDoctorDatas);
                }
                return gastricDoctorDatasList;
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
        protected void onPostExecute(List<GastricDoctorDatas> gastricDoctorresult) {
            super.onPostExecute(gastricDoctorresult);
            gastricdoctor_dialog.dismiss();
            GastricDoctorAdapter gastricDoctorAdapter = new GastricDoctorAdapter(getApplicationContext(),R.layout.gastric_doctor,gastricDoctorresult);
            gastricdoctorlistView.setAdapter(gastricDoctorAdapter);

        }
    }

    public class GastricDoctorAdapter extends ArrayAdapter{

        private List<GastricDoctorDatas> gastricDoctorDatasList;
        private int gastricdoctor_resource;
        private LayoutInflater gastricdoctor_inflater;

        public GastricDoctorAdapter(Context context, int resource, List<GastricDoctorDatas> objects) {
            super(context, resource, objects);
            gastricDoctorDatasList = objects;
            this.gastricdoctor_resource= resource;
            gastricdoctor_inflater =(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            gastricdoctor_ViewHolder gastricdoctor_viewHolder;

            if (convertView == null){
                gastricdoctor_viewHolder= new gastricdoctor_ViewHolder();
                convertView = gastricdoctor_inflater.inflate(gastricdoctor_resource,null);

                gastricdoctor_viewHolder.gastric_doctor_imageView=(ImageView)convertView.findViewById(R.id.gastric_doctor_imageView);
                gastricdoctor_viewHolder.gastric_doctor_textView2=(TextView)convertView.findViewById(R.id.gastric_doctor_textView2);
                gastricdoctor_viewHolder.gastric_doctor_textView3=(TextView)convertView.findViewById(R.id.gastric_doctor_textView3);
                gastricdoctor_viewHolder.gastric_doctor_textView4=(TextView)convertView.findViewById(R.id.gastric_doctor_textView4);
                convertView.setTag(gastricdoctor_viewHolder);
            }else {
                gastricdoctor_viewHolder = (gastricdoctor_ViewHolder)convertView.getTag();
            }

            final ProgressBar gastricdoctor_progressBar = (ProgressBar)convertView.findViewById(R.id.gastric_doctor_progressBar);
            ImageLoader.getInstance().displayImage(gastricDoctorDatasList.get(position).getGastric_doctors_images(), gastricdoctor_viewHolder.gastric_doctor_imageView, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    gastricdoctor_progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    gastricdoctor_progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    gastricdoctor_progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    gastricdoctor_progressBar.setVisibility(View.GONE);
                }
            });

            gastricdoctor_viewHolder.gastric_doctor_textView2.setText(gastricDoctorDatasList.get(position).getGastric_doctors_name());
            gastricdoctor_viewHolder.gastric_doctor_textView3.setText(gastricDoctorDatasList.get(position).getGastric_doctors_title());
            gastricdoctor_viewHolder.gastric_doctor_textView4.setText(gastricDoctorDatasList.get(position).getGastric_doctors_pnumber());
            return convertView;
        }
        class gastricdoctor_ViewHolder{
            private ImageView gastric_doctor_imageView;
            private TextView gastric_doctor_textView2;
            private TextView gastric_doctor_textView3;
            private TextView gastric_doctor_textView4;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.menu_gastric_doctor,menu);
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
