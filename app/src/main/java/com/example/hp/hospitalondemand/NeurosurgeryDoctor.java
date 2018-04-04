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

import com.example.hp.hospitalondemand.HeartData.NeurosurgeryDoctorDatas;
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

public class NeurosurgeryDoctor extends AppCompatActivity {

    private ListView neurosurgerydoctorlistView;
    private ProgressDialog neurosurgerydoctor_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neurosurgery_doctor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() !=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        neurosurgerydoctor_dialog= new ProgressDialog(this);
        neurosurgerydoctor_dialog.setIndeterminate(true);
        neurosurgerydoctor_dialog.setCancelable(false);
        neurosurgerydoctor_dialog.setMessage("Loading, Please Wait...");

        // Image loader
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);

        neurosurgerydoctorlistView =(ListView)findViewById(R.id.neurosurgery_doctor_listView);
        new JSONTask().execute("https://hospitalondemands.000webhostapp.com/connection/json_get_data_neurosurgery_doctors.php");

    }

    public class JSONTask extends AsyncTask<String, String, List<NeurosurgeryDoctorDatas>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            neurosurgerydoctor_dialog.show();
        }

        @Override
        protected List<NeurosurgeryDoctorDatas> doInBackground(String... params) {
            HttpURLConnection connection =null;
            BufferedReader reader =null;

            try {
                URL url = new URL(params[0]);
                connection= (HttpURLConnection)url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader= new BufferedReader(new InputStreamReader(stream));
                StringBuilder buffer = new StringBuilder();

                String line;
                while ((line= reader.readLine()) != null){
                    buffer.append(line);
                }
                String finalJson = buffer.toString();

                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("server_responce");
                List<NeurosurgeryDoctorDatas> neurosurgeryDoctorDatasList= new ArrayList<>();

                Gson neurosurgerydoctor_gson = new Gson();
                for (int m=0; m<parentArray.length(); m++) {
                    JSONObject finalObject = parentArray.getJSONObject(m);
                    NeurosurgeryDoctorDatas neurosurgeryDoctorDatas = neurosurgerydoctor_gson.fromJson(finalObject.toString(),NeurosurgeryDoctorDatas.class);
                    neurosurgeryDoctorDatasList.add(neurosurgeryDoctorDatas);
                }
                return neurosurgeryDoctorDatasList;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }finally {
                if (connection != null){
                    connection.disconnect();
                    try {
                        if (reader != null) {
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<NeurosurgeryDoctorDatas> neurosurgeryDoctorresult) {
            super.onPostExecute(neurosurgeryDoctorresult);
            neurosurgerydoctor_dialog.dismiss();
            NeurosurgeryDoctorAdapter neurosurgeryDoctorAdapter = new NeurosurgeryDoctorAdapter(getApplicationContext(),R.layout.neurosurgery_doctor,neurosurgeryDoctorresult);
            neurosurgerydoctorlistView.setAdapter(neurosurgeryDoctorAdapter);
        }
    }

    public class NeurosurgeryDoctorAdapter extends ArrayAdapter{

        private List<NeurosurgeryDoctorDatas> neurosurgeryDoctorDatasList;
        private int neurosurgeryDoctor_resource;
        private LayoutInflater neurosurgeryDoctor_inflater;

        public NeurosurgeryDoctorAdapter(Context context, int resource, List<NeurosurgeryDoctorDatas> objects) {
            super(context, resource, objects);
            neurosurgeryDoctorDatasList = objects;
            this.neurosurgeryDoctor_resource= resource;
            neurosurgeryDoctor_inflater =(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            neurosurgeryDoctor_ViewHolder neurosurgeryDoctor_viewHolder;

            if (convertView== null){
                neurosurgeryDoctor_viewHolder= new neurosurgeryDoctor_ViewHolder();
                convertView= neurosurgeryDoctor_inflater.inflate(neurosurgeryDoctor_resource, null);

                neurosurgeryDoctor_viewHolder.neurosurgery_doctor_imageView=(ImageView)convertView.findViewById(R.id.neurosurgery_doctor_imageView);
                neurosurgeryDoctor_viewHolder.neurosurgery_doctor_textView2=(TextView)convertView.findViewById(R.id.neurosurgery_doctor_textView2);
                neurosurgeryDoctor_viewHolder.neurosurgery_doctor_textView3=(TextView)convertView.findViewById(R.id.neurosurgery_doctor_textView3);
                neurosurgeryDoctor_viewHolder.neurosurgery_doctor_textView4=(TextView)convertView.findViewById(R.id.neurosurgery_doctor_textView4);
                convertView.setTag(neurosurgeryDoctor_viewHolder);
            }else {
                neurosurgeryDoctor_viewHolder= (neurosurgeryDoctor_ViewHolder)convertView.getTag();
            }
            final ProgressBar neurosurgeryDoctor_progressBar = (ProgressBar)convertView.findViewById(R.id.neurosurgery_doctor_progressBar);
            ImageLoader.getInstance().displayImage(neurosurgeryDoctorDatasList.get(position).getNeurosurgery_doctors_images(), neurosurgeryDoctor_viewHolder.neurosurgery_doctor_imageView, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    neurosurgeryDoctor_progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    neurosurgeryDoctor_progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    neurosurgeryDoctor_progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    neurosurgeryDoctor_progressBar.setVisibility(View.GONE);
                }
            });

            neurosurgeryDoctor_viewHolder.neurosurgery_doctor_textView2.setText(neurosurgeryDoctorDatasList.get(position).getNeurosurgery_doctors_name());
            neurosurgeryDoctor_viewHolder.neurosurgery_doctor_textView3.setText(neurosurgeryDoctorDatasList.get(position).getNeurosurgery_doctors_title());
            neurosurgeryDoctor_viewHolder.neurosurgery_doctor_textView4.setText(neurosurgeryDoctorDatasList.get(position).getNeurosurgery_doctors_pnumber());
            return convertView;
        }

        class neurosurgeryDoctor_ViewHolder{
            private ImageView neurosurgery_doctor_imageView;
            private TextView neurosurgery_doctor_textView2;
            private TextView neurosurgery_doctor_textView3;
            private TextView neurosurgery_doctor_textView4;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_neurosurgery_doctor,menu);
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
