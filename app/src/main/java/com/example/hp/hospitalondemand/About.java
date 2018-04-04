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

import com.example.hp.hospitalondemand.HeartData.AboutDatas;
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

public class About extends AppCompatActivity {

    private ListView aboutlistView;
    private ProgressDialog about_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        about_dialog = new ProgressDialog(this);
        about_dialog.setIndeterminate(true);
        about_dialog.setCancelable(true);
        about_dialog.setMessage("Loading, Please wait...");

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);

        aboutlistView =(ListView)findViewById(R.id.about_listView);
        new JSONTask().execute("https://hospitalondemands.000webhostapp.com/connection/json_get_data_about.php");
    }

    public class JSONTask extends AsyncTask<String, String, List<AboutDatas>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            about_dialog.show();
        }

        @Override
        protected List<AboutDatas> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

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
                List<AboutDatas> aboutDatasList =new ArrayList<>();

                Gson about_gson = new Gson();
                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    AboutDatas aboutDatas = about_gson.fromJson(finalObject.toString(),AboutDatas.class);
                    aboutDatasList.add(aboutDatas);
                }
                return aboutDatasList;
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
                try {
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
        protected void onPostExecute(List<AboutDatas> aboutDatasresult) {
            super.onPostExecute(aboutDatasresult);
            about_dialog.dismiss();
            AboutAdapter aboutAdapter = new AboutAdapter(getApplicationContext(), R.layout.about,aboutDatasresult);
            aboutlistView.setAdapter(aboutAdapter);
        }
    }

    public class AboutAdapter extends ArrayAdapter{

        private List<AboutDatas> aboutDatasList;
        private int about_resource;
        private LayoutInflater about_inflater;

        public AboutAdapter(Context context, int resource, List<AboutDatas> objects) {
            super(context, resource, objects);
            aboutDatasList = objects;
            this.about_resource= resource;
            about_inflater=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            about_ViewHolder about_viewHolder;
            if (convertView==null){
                about_viewHolder = new about_ViewHolder();
                convertView = about_inflater.inflate(about_resource, null);

                about_viewHolder.about_imageView=(ImageView)convertView.findViewById(R.id.about_imageView);
                about_viewHolder.about_textView=(TextView)convertView.findViewById(R.id.about_textView);
                about_viewHolder.about_textView2=(TextView)convertView.findViewById(R.id.about_textView2);
                about_viewHolder.about_textView3=(TextView)convertView.findViewById(R.id.about_textView3);
                about_viewHolder.about_textView4=(TextView)convertView.findViewById(R.id.about_textView4);
                about_viewHolder.about_textView5=(TextView)convertView.findViewById(R.id.about_textView5);
                convertView.setTag(about_viewHolder);
            }else {
                about_viewHolder = (about_ViewHolder)convertView.getTag();
            }

            final ProgressBar about_progressBar = (ProgressBar)convertView.findViewById(R.id.about_progressBar);
            ImageLoader.getInstance().displayImage(aboutDatasList.get(position).getAbout_images(), about_viewHolder.about_imageView, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    about_progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    about_progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    about_progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    about_progressBar.setVisibility(View.GONE);
                }
            });

            about_viewHolder.about_textView.setText(aboutDatasList.get(position).getAbout_position());
            about_viewHolder.about_textView2.setText(aboutDatasList.get(position).getAbout_name());
            about_viewHolder.about_textView3.setText(aboutDatasList.get(position).getAbout_title());
            about_viewHolder.about_textView4.setText(aboutDatasList.get(position).getAbout_dept());
            about_viewHolder.about_textView5.setText(aboutDatasList.get(position).getAbout_university());
            return convertView;
        }

        class about_ViewHolder{
            private ImageView about_imageView;
            private TextView about_textView;
            private TextView about_textView2;
            private TextView about_textView3;
            private TextView about_textView4;
            private TextView about_textView5;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_about,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id= item.getItemId();

        if (id == R.id.action_about){
            startActivity(new Intent(this,Us.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
