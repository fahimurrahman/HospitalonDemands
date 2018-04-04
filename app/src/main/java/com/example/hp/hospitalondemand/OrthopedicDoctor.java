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

import com.example.hp.hospitalondemand.HeartData.OrthopedicDoctorDatas;
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

public class OrthopedicDoctor extends AppCompatActivity {

    private ListView orthopedicdoctorlistView;
    private ProgressDialog orthopedicdoctor_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orthopedic_doctor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        orthopedicdoctor_dialog=new ProgressDialog(this);
        orthopedicdoctor_dialog.setIndeterminate(true);
        orthopedicdoctor_dialog.setCancelable(false);
        orthopedicdoctor_dialog.setMessage("Loading, Please Wait...");

        // Image loader
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);

        orthopedicdoctorlistView= (ListView)findViewById(R.id.orthopedic_doctor_listView);
        new JSONTask().execute("https://hospitalondemands.000webhostapp.com/connection/json_get_data_orthopedic_doctors.php");

    }

    public class JSONTask extends AsyncTask<String, String,List<OrthopedicDoctorDatas>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            orthopedicdoctor_dialog.show();
        }

        @Override
        protected List<OrthopedicDoctorDatas> doInBackground(String... params) {
            HttpURLConnection connection=null;
            BufferedReader reader= null;

            try {
                URL url = new URL(params[0]);
                connection= (HttpURLConnection) url.openConnection();
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
                List<OrthopedicDoctorDatas> orthopedicDoctorDatasList = new ArrayList<>();

                Gson orthopedicDoctor_gson = new Gson();
                for (int k=0; k<parentArray.length(); k++) {
                    JSONObject finalObject = parentArray.getJSONObject(k);
                    OrthopedicDoctorDatas orthopedicDoctorDatas= orthopedicDoctor_gson.fromJson(finalObject.toString(),OrthopedicDoctorDatas.class);
                    orthopedicDoctorDatasList.add(orthopedicDoctorDatas);
                }
                return orthopedicDoctorDatasList;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }finally {
                if(connection != null){
                    connection.disconnect();
                }try{
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
        protected void onPostExecute(List<OrthopedicDoctorDatas> orthopedicDoctorresult) {
            super.onPostExecute(orthopedicDoctorresult);
            orthopedicdoctor_dialog.dismiss();
            OrthopedicDoctorAdapter orthopedicDoctorAdapter = new OrthopedicDoctorAdapter(getApplicationContext(),R.layout.orthopedic_doctor, orthopedicDoctorresult);
            orthopedicdoctorlistView.setAdapter(orthopedicDoctorAdapter);
        }
    }

    public class OrthopedicDoctorAdapter extends ArrayAdapter{

        private List<OrthopedicDoctorDatas> orthopedicDoctorDatasList;
        private int orthopedicDoctor_resource;
        private LayoutInflater orthopedicDoctor_inflater;

        public OrthopedicDoctorAdapter(Context context, int resource, List<OrthopedicDoctorDatas> objects) {
            super(context, resource, objects);
            orthopedicDoctorDatasList =objects;
            this.orthopedicDoctor_resource=resource;
            orthopedicDoctor_inflater=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            orthopedicDoctor_ViewHolder orthopedicDoctor_viewHolder;
            if (convertView==null){
                orthopedicDoctor_viewHolder= new orthopedicDoctor_ViewHolder();
                convertView = orthopedicDoctor_inflater.inflate(orthopedicDoctor_resource, null);

                orthopedicDoctor_viewHolder.orthopedic_doctor_imageView=(ImageView)convertView.findViewById(R.id.orthopedic_doctor_imageView);
                orthopedicDoctor_viewHolder.orthopedic_doctor_textView2=(TextView)convertView.findViewById(R.id.orthopedic_doctor_textView2);
                orthopedicDoctor_viewHolder.orthopedic_doctor_textView3=(TextView)convertView.findViewById(R.id.orthopedic_doctor_textView3);
                orthopedicDoctor_viewHolder.orthopedic_doctor_textView4=(TextView)convertView.findViewById(R.id.orthopedic_doctor_textView4);
                convertView.setTag(orthopedicDoctor_viewHolder);
            }else {
                orthopedicDoctor_viewHolder = (orthopedicDoctor_ViewHolder)convertView.getTag();
            }
            final ProgressBar orthopedicDoctor_progressBar= (ProgressBar)convertView.findViewById(R.id.orthopedic_doctor_progressBar);
            ImageLoader.getInstance().displayImage(orthopedicDoctorDatasList.get(position).getOrthopedic_doctors_images(), orthopedicDoctor_viewHolder.orthopedic_doctor_imageView, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    orthopedicDoctor_progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    orthopedicDoctor_progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    orthopedicDoctor_progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    orthopedicDoctor_progressBar.setVisibility(View.GONE);
                }
            });
            orthopedicDoctor_viewHolder.orthopedic_doctor_textView2.setText(orthopedicDoctorDatasList.get(position).getOrthopedic_doctors_name());
            orthopedicDoctor_viewHolder.orthopedic_doctor_textView3.setText(orthopedicDoctorDatasList.get(position).getOrthopedic_doctors_title());
            orthopedicDoctor_viewHolder.orthopedic_doctor_textView4.setText(orthopedicDoctorDatasList.get(position).getOrthopedic_doctors_pnumber());

            return convertView;
        }

        class orthopedicDoctor_ViewHolder{
            private ImageView orthopedic_doctor_imageView;
            private TextView orthopedic_doctor_textView2;
            private TextView orthopedic_doctor_textView3;
            private TextView orthopedic_doctor_textView4;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.menu_orthopedic_doctor,menu);
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
