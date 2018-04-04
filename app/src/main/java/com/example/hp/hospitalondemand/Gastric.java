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

import com.example.hp.hospitalondemand.HeartData.GastricDatas;
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

public class Gastric extends AppCompatActivity {
    private ListView gastriclistView;
    private ProgressDialog gastric_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gastric);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        gastric_dialog = new ProgressDialog(this);
        gastric_dialog.setIndeterminate(true);
        gastric_dialog.setCancelable(false);
        gastric_dialog.setMessage("Loading, Please Wait...");

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);

        gastriclistView =(ListView)findViewById(R.id.gastric_listView);
        new JSONTask().execute("https://hospitalondemands.000webhostapp.com/connection/json_get_data_gastric.php");
    }

    public class JSONTask extends AsyncTask<String, String, List<GastricDatas>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            gastric_dialog.show();
        }

        @Override
        protected List<GastricDatas> doInBackground(String... params) {

            HttpURLConnection connection =null;
            BufferedReader reader = null;

            try {
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

                List<GastricDatas> gastricDatasList =new ArrayList<>();
                Gson gastric_gson = new Gson();
                for (int p=0; p<parentArray.length(); p++) {
                    JSONObject finalObject = parentArray.getJSONObject(p);
                    GastricDatas gastricDatas= gastric_gson.fromJson(finalObject.toString(),GastricDatas.class);
                    gastricDatasList.add(gastricDatas);
                }
                return gastricDatasList;
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
        protected void onPostExecute(List<GastricDatas> gastricresult) {
            super.onPostExecute(gastricresult);
            gastric_dialog.dismiss();
            GastricAdapter gastricAdapter =new GastricAdapter(getApplicationContext(),R.layout.gastric, gastricresult);
            gastriclistView.setAdapter(gastricAdapter);
        }
    }
    public class GastricAdapter extends ArrayAdapter{

        private List<GastricDatas> gastricDatasList;
        private int gastric_resource;
        private LayoutInflater gastric_inflater;

        public GastricAdapter(Context context, int resource, List<GastricDatas> objects) {
            super(context, resource, objects);
            gastricDatasList = objects;
            this.gastric_resource = resource;
            gastric_inflater =(LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            gastric_ViewHolder gastric_holder;

            if (convertView == null){
                gastric_holder = new gastric_ViewHolder();
                convertView = gastric_inflater.inflate(gastric_resource,null);

                gastric_holder.gastric_imageView =(ImageView)convertView.findViewById(R.id.gastric_imageView);
                gastric_holder.gastric_textView2 =(TextView)convertView.findViewById(R.id.gastric_textView2);
                gastric_holder.gastric_textView3 = (TextView)convertView.findViewById(R.id.gastric_textView3);
                gastric_holder.gastric_textView4 =(TextView)convertView.findViewById(R.id.gastric_textView4);
                gastric_holder.gastric_textView5 = (TextView)convertView.findViewById(R.id.gastric_textView5);
                gastric_holder.gastric_textView6 = (TextView)convertView.findViewById(R.id.gastric_textView6);
                convertView.setTag(gastric_holder);
            }else {
                gastric_holder = (gastric_ViewHolder) convertView.getTag();
            }

            final ProgressBar gastric_progressBar= (ProgressBar)convertView.findViewById(R.id.gastric_progressBar);
            ImageLoader.getInstance().displayImage(gastricDatasList.get(position).getHospital_gastric_images(), gastric_holder.gastric_imageView, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    gastric_progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    gastric_progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    gastric_progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    gastric_progressBar.setVisibility(View.GONE);
                }
            });
            gastric_holder.gastric_textView2.setText(gastricDatasList.get(position).getHospital_gastric_name());
            gastric_holder.gastric_textView3.setText(gastricDatasList.get(position).getHospital_gastric_description());
            gastric_holder.gastric_textView4.setText(gastricDatasList.get(position).getHospital_gastric_equipment());
            gastric_holder.gastric_textView5.setText(gastricDatasList.get(position).getHospital_gastric_address());
            gastric_holder.gastric_textView6.setText(gastricDatasList.get(position).getHospital_gastric_pnumber());
            return convertView;
        }
        class gastric_ViewHolder{
            private ImageView gastric_imageView;
            private TextView gastric_textView2;
            private TextView gastric_textView3;
            private TextView gastric_textView4;
            private TextView gastric_textView5;
            private TextView gastric_textView6;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.menu_gastric,menu);
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
