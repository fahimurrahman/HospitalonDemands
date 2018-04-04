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

import com.example.hp.hospitalondemand.HeartData.CancerDatas;
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

public class Cancer extends AppCompatActivity {

    private ListView cancerlistView;
    private ProgressDialog cancer_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        cancer_dialog= new ProgressDialog(this);
        cancer_dialog.setIndeterminate(true);
        cancer_dialog.setCancelable(false);
        cancer_dialog.setMessage("Loading, Please Wait...");

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);

        cancerlistView= (ListView)findViewById(R.id.cancer_listView);
        new JSONTask().execute("https://hospitalondemands.000webhostapp.com/connection/json_get_data_cancer.php");

    }

    public class JSONTask extends AsyncTask<String, String, List<CancerDatas>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cancer_dialog.show();
        }

        @Override
        protected List<CancerDatas> doInBackground(String... params) {
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
                List<CancerDatas> cancerDatasList= new ArrayList<>();

                Gson cancer_gson = new Gson();
                for (int n=0; n<parentArray.length(); n++){
                    JSONObject finalObject = parentArray.getJSONObject(n);
                    CancerDatas cancerDatas = cancer_gson.fromJson(finalObject.toString(),CancerDatas.class);
                    cancerDatasList.add(cancerDatas);
                }
                return cancerDatasList;

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
        protected void onPostExecute(List<CancerDatas> cancerresult) {
            super.onPostExecute(cancerresult);
            cancer_dialog.dismiss();
            CancerAdapter cancerAdapter = new CancerAdapter(getApplicationContext(), R.layout.cancer,cancerresult);
            cancerlistView.setAdapter(cancerAdapter);
        }
    }

    public class CancerAdapter extends ArrayAdapter{

        private List<CancerDatas> cancerDatasList;
        private int cancer_resource;
        private LayoutInflater cancer_inflater;

        public CancerAdapter(Context context, int resource, List<CancerDatas> objects) {
            super(context, resource, objects);
            cancerDatasList = objects;
            this.cancer_resource = resource;
            cancer_inflater= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            cancer_ViewHolder cancer_holder;

            if (convertView == null){
                cancer_holder = new cancer_ViewHolder();
                convertView = cancer_inflater.inflate(cancer_resource, null);
                cancer_holder.cancer_imageView=(ImageView)convertView.findViewById(R.id.cancer_imageView);
                cancer_holder.cancer_textView2=(TextView)convertView.findViewById(R.id.cancer_textView2);
                cancer_holder.cancer_textView3=(TextView)convertView.findViewById(R.id.cancer_textView3);
                cancer_holder.cancer_textView4=(TextView)convertView.findViewById(R.id.cancer_textView4);
                cancer_holder.cancer_textView5=(TextView)convertView.findViewById(R.id.cancer_textView5);
                cancer_holder.cancer_textView6=(TextView)convertView.findViewById(R.id.cancer_textView6);
                convertView.setTag(cancer_holder);
            } else {
                cancer_holder= (cancer_ViewHolder)convertView.getTag();
            }

            final ProgressBar cancer_progressBar =(ProgressBar)convertView.findViewById(R.id.cancer_progressBar);

            ImageLoader.getInstance().displayImage(cancerDatasList.get(position).getHospital_cancer_images(), cancer_holder.cancer_imageView, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    cancer_progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    cancer_progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    cancer_progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    cancer_progressBar.setVisibility(View.GONE);
                }
            });
            cancer_holder.cancer_textView2.setText(cancerDatasList.get(position).getHospital_cancer_name());
            cancer_holder.cancer_textView3.setText(cancerDatasList.get(position).getHospital_cancer_description());
            cancer_holder.cancer_textView4.setText(cancerDatasList.get(position).getHospital_cancer_equipment());
            cancer_holder.cancer_textView5.setText(cancerDatasList.get(position).getHospital_cancer_address());
            cancer_holder.cancer_textView6.setText(cancerDatasList.get(position).getHospital_cancer_pnumber());
            return convertView;
        }

        class cancer_ViewHolder{
            private ImageView cancer_imageView;
            private TextView cancer_textView2;
            private TextView cancer_textView3;
            private TextView cancer_textView4;
            private TextView cancer_textView5;
            private TextView cancer_textView6;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_cancer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id= item.getItemId();

        if (id == R.id.action_about){
            startActivity(new Intent(this,Us.class));
            return true;
        }
        else if (id == R.id.action_ambulance){
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
