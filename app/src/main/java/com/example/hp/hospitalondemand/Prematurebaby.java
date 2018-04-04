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

import com.example.hp.hospitalondemand.HeartData.PrematurebabyDatas;
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

public class Prematurebaby extends AppCompatActivity {

    private ListView prematurebabylistView;
    private ProgressDialog prematurebaby_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prematurebaby);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!= null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        prematurebaby_dialog =new ProgressDialog(this);
        prematurebaby_dialog.setIndeterminate(true);
        prematurebaby_dialog.setCancelable(false);
        prematurebaby_dialog.setMessage("Loading, Please Wait...");

        // Image loader
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);

        prematurebabylistView =(ListView) findViewById(R.id.prematurebaby_listView);
        new JSONTask().execute("https://hospitalondemands.000webhostapp.com/connection/json_get_data_prematurebaby.php");

    }

    public class JSONTask extends AsyncTask<String, String, List<PrematurebabyDatas>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prematurebaby_dialog.show();
        }

        @Override
        protected List<PrematurebabyDatas> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader=null;

            try{
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
                List<PrematurebabyDatas> prematurebabyDatasList =new ArrayList<>();

                Gson prematurebaby_gson = new Gson();
                for (int l=0; l<parentArray.length(); l++){
                    JSONObject finalObject= parentArray.getJSONObject(l);
                    PrematurebabyDatas prematurebabyDatas= prematurebaby_gson.fromJson(finalObject.toString(),PrematurebabyDatas.class);
                    prematurebabyDatasList.add(prematurebabyDatas);
                }
                return prematurebabyDatasList;

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
        protected void onPostExecute(List<PrematurebabyDatas> prematurebabyresult) {
            super.onPostExecute(prematurebabyresult);
            prematurebaby_dialog.dismiss();
            PrematurebabyAdapter prematurebabyAdapter = new PrematurebabyAdapter(getApplicationContext(), R.layout.prematurebaby, prematurebabyresult);
            prematurebabylistView.setAdapter(prematurebabyAdapter);
        }
    }

    public class PrematurebabyAdapter extends ArrayAdapter{

        private List<PrematurebabyDatas> prematurebabysDatasList;
        private int prematurebaby_resource;
        private LayoutInflater prematurebaby_inflater;

        public PrematurebabyAdapter(Context context, int resource, List<PrematurebabyDatas> objects) {
            super(context, resource, objects);
            prematurebabysDatasList = objects;
            this.prematurebaby_resource =resource;
            prematurebaby_inflater=(LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            prematurebaby_ViewHolder prematurebaby_holder;

            if (convertView == null){
                prematurebaby_holder = new prematurebaby_ViewHolder();
                convertView = prematurebaby_inflater.inflate(prematurebaby_resource, null);

                prematurebaby_holder.prematurebaby_imageView =(ImageView)convertView.findViewById(R.id.prematurebaby_imageView);
                prematurebaby_holder.prematurebaby_textView2=(TextView)convertView.findViewById(R.id.prematurebaby_textView2);
                prematurebaby_holder.prematurebaby_textView3=(TextView)convertView.findViewById(R.id.prematurebaby_textView3);
                prematurebaby_holder.prematurebaby_textView4=(TextView)convertView.findViewById(R.id.prematurebaby_textView4);
                prematurebaby_holder.prematurebaby_textView5=(TextView)convertView.findViewById(R.id.prematurebaby_textView5);
                prematurebaby_holder.prematurebaby_textView6=(TextView)convertView.findViewById(R.id.prematurebaby_textView6);
                convertView.setTag(prematurebaby_holder);
            }else{
                prematurebaby_holder =(prematurebaby_ViewHolder) convertView.getTag();
            }

            final ProgressBar prematurebaby_progressBar=(ProgressBar)convertView.findViewById(R.id.prematurebaby_progressBar);

            ImageLoader.getInstance().displayImage(prematurebabysDatasList.get(position).getHospital_prematurebaby_images(), prematurebaby_holder.prematurebaby_imageView, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    prematurebaby_progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    prematurebaby_progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    prematurebaby_progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    prematurebaby_progressBar.setVisibility(View.GONE);
                }
            });

            prematurebaby_holder.prematurebaby_textView2.setText(prematurebabysDatasList.get(position).getHospital_prematurebaby_name());
            prematurebaby_holder.prematurebaby_textView3.setText(prematurebabysDatasList.get(position).getHospital_prematurebaby_description());
            prematurebaby_holder.prematurebaby_textView4.setText(prematurebabysDatasList.get(position).getHospital_prematurebaby_equipment());
            prematurebaby_holder.prematurebaby_textView5.setText(prematurebabysDatasList.get(position).getHospital_prematurebaby_address());
            prematurebaby_holder.prematurebaby_textView6.setText(prematurebabysDatasList.get(position).getHospital_prematurebaby_pnumber());

            return convertView;
        }

        class prematurebaby_ViewHolder{
            private ImageView prematurebaby_imageView;
            private TextView prematurebaby_textView2;
            private TextView prematurebaby_textView3;
            private TextView prematurebaby_textView4;
            private TextView prematurebaby_textView5;
            private TextView prematurebaby_textView6;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater =getMenuInflater();
        menuInflater.inflate(R.menu.menu_prematurebaby,menu);
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
