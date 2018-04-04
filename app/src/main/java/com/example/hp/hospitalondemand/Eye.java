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

import com.example.hp.hospitalondemand.HeartData.EyeDatas;
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

public class Eye extends AppCompatActivity {
    private ListView eyelistView;
    private ProgressDialog eye_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eye);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        eye_dialog = new ProgressDialog(this);
        eye_dialog.setIndeterminate(true);
        eye_dialog.setCancelable(false);
        eye_dialog.setMessage("Loading, Please Wait...");

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);

        eyelistView =(ListView)findViewById(R.id.eye_listView);
        new JSONTask().execute("https://hospitalondemands.000webhostapp.com/connection/json_get_data_eye.php");

    }

    public class JSONTask extends AsyncTask<String, String, List<EyeDatas>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            eye_dialog.show();
        }

        @Override
        protected List<EyeDatas> doInBackground(String... params) {
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
                List<EyeDatas> eyeDatasList = new ArrayList<>();

                Gson eye_gson = new Gson();
                for (int o=0; o<parentArray.length(); o++){
                    JSONObject finalObject = parentArray.getJSONObject(o);
                    EyeDatas eyeDatas = eye_gson.fromJson(finalObject.toString(),EyeDatas.class);
                    eyeDatasList.add(eyeDatas);
                }
                return eyeDatasList;
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
        protected void onPostExecute(List<EyeDatas> eyeresult) {
            super.onPostExecute(eyeresult);
            eye_dialog.dismiss();
            EyeAdapter eyeAdapter = new EyeAdapter(getApplicationContext(),R.layout.eye,eyeresult);
            eyelistView.setAdapter(eyeAdapter);
        }
    }

    public class EyeAdapter extends ArrayAdapter{

        private List<EyeDatas> eyeDatasList;
        private int eye_resource;
        private LayoutInflater eye_inflater;

        public EyeAdapter(Context context, int resource, List<EyeDatas> objects) {
            super(context, resource, objects);
            eyeDatasList = objects;
            this.eye_resource= resource;
            eye_inflater= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            eye_ViewHolder eye_holder;

            if (convertView ==null){
                eye_holder = new eye_ViewHolder();
                convertView = eye_inflater.inflate(eye_resource,null);

                eye_holder.eye_imageView=(ImageView)convertView.findViewById(R.id.eye_imageView);
                eye_holder.eye_textView2 = (TextView)convertView.findViewById(R.id.eye_textView2);
                eye_holder.eye_textView3 = (TextView)convertView.findViewById(R.id.eye_textView3);
                eye_holder.eye_textView4 = (TextView)convertView.findViewById(R.id.eye_textView4);
                eye_holder.eye_textView5 = (TextView)convertView.findViewById(R.id.eye_textView5);
                eye_holder.eye_textView6 = (TextView)convertView.findViewById(R.id.eye_textView6);
                convertView.setTag(eye_holder);
            }else {
                eye_holder = (eye_ViewHolder) convertView.getTag();
            }
            final ProgressBar eye_progressBar = (ProgressBar) convertView.findViewById(R.id.eye_progressBar);

            ImageLoader.getInstance().displayImage(eyeDatasList.get(position).getHospital_eye_images(), eye_holder.eye_imageView, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    eye_progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    eye_progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    eye_progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    eye_progressBar.setVisibility(View.GONE);
                }
            });
            eye_holder.eye_textView2.setText(eyeDatasList.get(position).getHospital_eye_name());
            eye_holder.eye_textView3.setText(eyeDatasList.get(position).getHospital_eye_description());
            eye_holder.eye_textView4.setText(eyeDatasList.get(position).getHospital_eye_equipment());
            eye_holder.eye_textView5.setText(eyeDatasList.get(position).getHospital_eye_address());
            eye_holder.eye_textView6.setText(eyeDatasList.get(position).getHospital_eye_pnumber());
            return convertView;
        }

        class eye_ViewHolder{
            private ImageView eye_imageView;
            private TextView eye_textView2;
            private TextView eye_textView3;
            private TextView eye_textView4;
            private TextView eye_textView5;
            private TextView eye_textView6;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater =getMenuInflater();
        menuInflater.inflate(R.menu.menu_eye,menu);
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
