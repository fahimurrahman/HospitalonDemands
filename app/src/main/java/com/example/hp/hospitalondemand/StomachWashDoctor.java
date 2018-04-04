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

import com.example.hp.hospitalondemand.HeartData.StomachWashDoctorDatas;
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

public class StomachWashDoctor extends AppCompatActivity {
    private ListView stomachwashdoctorlistView;
    private ProgressDialog stomachwashdoctor_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stomach_wash_doctor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        stomachwashdoctor_dialog= new ProgressDialog(this);
        stomachwashdoctor_dialog.setIndeterminate(true);
        stomachwashdoctor_dialog.setCancelable(false);
        stomachwashdoctor_dialog.setMessage("Loading, Please wait...");


        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);

        stomachwashdoctorlistView = (ListView) findViewById(R.id.stomach_wash_doctor_listView);
        new JSONTask().execute("https://hospitalondemands.000webhostapp.com/connection/json_get_data_stomachwash_doctors.php");
    }


    public class JSONTask extends AsyncTask<String, String, List<StomachWashDoctorDatas>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            stomachwashdoctor_dialog.show();
        }

        @Override
        protected List<StomachWashDoctorDatas> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader =null;
            try {
                URL url = new URL(params[0]);
                connection= (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream= connection.getInputStream();
                reader= new BufferedReader(new InputStreamReader(stream));
                StringBuilder buffer=new StringBuilder();

                String line;
                while((line= reader.readLine())!=null){
                    buffer.append(line);
                }

                String finalJson = buffer.toString();

                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("server_responce");
                List<StomachWashDoctorDatas> stomachWashDoctorDatasList = new ArrayList<>();

                Gson stomachWashDoctor_gson = new Gson();
                for (int j=0; j<parentArray.length(); j++) {
                    JSONObject finalObject = parentArray.getJSONObject(j);
                    StomachWashDoctorDatas stomachWashDoctorDatas = stomachWashDoctor_gson.fromJson(finalObject.toString(),StomachWashDoctorDatas.class);
                    stomachWashDoctorDatasList.add(stomachWashDoctorDatas);
                }
                return stomachWashDoctorDatasList;
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
        protected void onPostExecute(List<StomachWashDoctorDatas> stomachWashDoctorresult) {
            super.onPostExecute(stomachWashDoctorresult);
            stomachwashdoctor_dialog.dismiss();
            StomachWashDoctorAdapter stomachWashDoctorAdapter = new StomachWashDoctorAdapter(getApplicationContext(), R.layout.stomachwash_doctor, stomachWashDoctorresult);
            stomachwashdoctorlistView.setAdapter(stomachWashDoctorAdapter);
        }
    }

    public class StomachWashDoctorAdapter extends ArrayAdapter{
        private List<StomachWashDoctorDatas> stomachWashDoctorDatasList;
        private int stomachWashDoctor_resource;
        private LayoutInflater stomachWashDoctor_inflater;

        public StomachWashDoctorAdapter(Context context, int resource, List<StomachWashDoctorDatas> objects) {
            super(context, resource, objects);
            stomachWashDoctorDatasList = objects;
            this.stomachWashDoctor_resource= resource;
            stomachWashDoctor_inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            stomachWashDoctor_Viewholder stomachWashDoctor_viewholder;

            if (convertView== null){
                stomachWashDoctor_viewholder = new stomachWashDoctor_Viewholder();
                convertView = stomachWashDoctor_inflater.inflate(stomachWashDoctor_resource, null);

                stomachWashDoctor_viewholder.stomachwash_doctor_imageView=(ImageView)convertView.findViewById(R.id.stomach_wash_doctor_imageView);
                stomachWashDoctor_viewholder.stomachwash_doctor_textView2=(TextView)convertView.findViewById(R.id.stomach_wash_doctor_textView2);
                stomachWashDoctor_viewholder.stomachwash_doctor_textView3=(TextView)convertView.findViewById(R.id.stomach_wash_doctor_textView3);
                stomachWashDoctor_viewholder.stomachwash_doctor_textView4=(TextView)convertView.findViewById(R.id.stomach_wash_doctor_textView4);
                convertView.setTag(stomachWashDoctor_viewholder);
            }else {
                stomachWashDoctor_viewholder = (stomachWashDoctor_Viewholder)convertView.getTag();
            }

            final ProgressBar stomachWashDoctor_progressBar =(ProgressBar) convertView.findViewById(R.id.stomach_wash_doctor_progressBar);
            ImageLoader.getInstance().displayImage(stomachWashDoctorDatasList.get(position).getStomachwash_doctors_images(), stomachWashDoctor_viewholder.stomachwash_doctor_imageView, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    stomachWashDoctor_progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    stomachWashDoctor_progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    stomachWashDoctor_progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    stomachWashDoctor_progressBar.setVisibility(View.GONE);
                }
            });
            stomachWashDoctor_viewholder.stomachwash_doctor_textView2.setText(stomachWashDoctorDatasList.get(position).getStomachwash_doctors_name());
            stomachWashDoctor_viewholder.stomachwash_doctor_textView3.setText(stomachWashDoctorDatasList.get(position).getStomachwash_doctors_title());
            stomachWashDoctor_viewholder.stomachwash_doctor_textView4.setText(stomachWashDoctorDatasList.get(position).getStomachwash_doctors_pnumber());
            return convertView;
        }
        class stomachWashDoctor_Viewholder{
            private ImageView stomachwash_doctor_imageView;
            private TextView stomachwash_doctor_textView2;
            private TextView stomachwash_doctor_textView3;
            private TextView stomachwash_doctor_textView4;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.menu_stomach_wash_doctor,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
