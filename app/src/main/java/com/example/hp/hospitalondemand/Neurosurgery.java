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
import com.example.hp.hospitalondemand.HeartData.NeurosurgeryDatas;
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

public class Neurosurgery extends AppCompatActivity {
    private ListView neurosurgerylistView;
    private ProgressDialog neurosurgery_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neurosurgery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() !=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        neurosurgery_dialog = new ProgressDialog(this);
        neurosurgery_dialog.setIndeterminate(true);
        neurosurgery_dialog.setCancelable(false);
        neurosurgery_dialog.setMessage("Loading, Please Wait...");

        // Image loader
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);

        neurosurgerylistView =(ListView)findViewById(R.id.neurosurgery_listView);
        new JSONTask().execute("https://hospitalondemands.000webhostapp.com/connection/json_get_data_neurosurgery.php");
    }

    public class JSONTask extends AsyncTask<String,String,List<NeurosurgeryDatas>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            neurosurgery_dialog.show();
        }

        @Override
        protected List<NeurosurgeryDatas> doInBackground(String... params) {
            HttpURLConnection connection =null;
            BufferedReader reader =null;

            try {
                URL url = new URL(params[0]);
                connection= (HttpURLConnection)url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader= new BufferedReader(new InputStreamReader(stream));
                StringBuilder buffer = new StringBuilder(); //TODO Change StringBuilder to StringBuffer

                String line; //TODO line = "";
                while ((line= reader.readLine()) != null){
                    buffer.append(line);
                }
                String finalJson = buffer.toString();

                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("server_responce");

                List<NeurosurgeryDatas> neurosurgeryDatasList =new ArrayList<>();
                Gson neurosurgery_gson = new Gson();

                for (int m=0; m<parentArray.length(); m++){
                    JSONObject finalObject = parentArray.getJSONObject(m);
                    NeurosurgeryDatas neurosurgeryDatas = neurosurgery_gson.fromJson(finalObject.toString(),NeurosurgeryDatas.class);
                    neurosurgeryDatasList.add(neurosurgeryDatas);
                }
                return neurosurgeryDatasList;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
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
        protected void onPostExecute(List<NeurosurgeryDatas> neurosurgeryresults) {
            super.onPostExecute(neurosurgeryresults);
            neurosurgery_dialog.dismiss();
            NeurosurgeryAdapter neurosurgeryAdapter= new NeurosurgeryAdapter(getApplicationContext(), R.layout.neurosurgery, neurosurgeryresults);
            neurosurgerylistView.setAdapter(neurosurgeryAdapter);
        }
    }

    public class NeurosurgeryAdapter extends ArrayAdapter{
        private List<NeurosurgeryDatas> neurosurgeryDatasList;
        private int neurosurgery_resource;
        private LayoutInflater neurosurgery_inflater;

        public NeurosurgeryAdapter(Context context, int resource, List<NeurosurgeryDatas> objects) {
            super(context, resource, objects);
            neurosurgeryDatasList = objects;
            neurosurgery_resource =resource;
            neurosurgery_inflater =(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            neurosurgery_ViewHolder neurosurgery_holder;
            if (convertView == null){
                neurosurgery_holder = new neurosurgery_ViewHolder();
                convertView = neurosurgery_inflater.inflate(neurosurgery_resource,null);

                neurosurgery_holder.neurosurgery_imageView=(ImageView)convertView.findViewById(R.id.neurosurgery_imageView);
                neurosurgery_holder.neurosurgery_textView2=(TextView)convertView.findViewById(R.id.neurosurgery_textView2);
                neurosurgery_holder.neurosurgery_textView3=(TextView)convertView.findViewById(R.id.neurosurgery_textView3);
                neurosurgery_holder.neurosurgery_textView4=(TextView)convertView.findViewById(R.id.neurosurgery_textView4);
                neurosurgery_holder.neurosurgery_textView5=(TextView)convertView.findViewById(R.id.neurosurgery_textView5);
                neurosurgery_holder.neurosurgery_textView6=(TextView)convertView.findViewById(R.id.neurosurgery_textView6);
                convertView.setTag(neurosurgery_holder);
            }else {
                neurosurgery_holder =(neurosurgery_ViewHolder) convertView.getTag();
            }

            final ProgressBar neurosurgery_progressBar =(ProgressBar)convertView.findViewById(R.id.neurosurgery_progressBar);

            ImageLoader.getInstance().displayImage(neurosurgeryDatasList.get(position).getHospital_neurosurgery_images(), neurosurgery_holder.neurosurgery_imageView, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    neurosurgery_progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    neurosurgery_progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    neurosurgery_progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    neurosurgery_progressBar.setVisibility(View.GONE);
                }
            });

            neurosurgery_holder.neurosurgery_textView2.setText(neurosurgeryDatasList.get(position).getHospital_neurosurgery_name());
            neurosurgery_holder.neurosurgery_textView3.setText(neurosurgeryDatasList.get(position).getHospital_neurosurgery_description());
            neurosurgery_holder.neurosurgery_textView4.setText(neurosurgeryDatasList.get(position).getHospital_neurosurgery_equipment());
            neurosurgery_holder.neurosurgery_textView5.setText(neurosurgeryDatasList.get(position).getHospital_neurosurgery_address());
            neurosurgery_holder.neurosurgery_textView6.setText(neurosurgeryDatasList.get(position).getHospital_neurosurgery_pnumber());
            return convertView;
        }
        class neurosurgery_ViewHolder{
            private ImageView neurosurgery_imageView;
            private TextView neurosurgery_textView2;
            private TextView neurosurgery_textView3;
            private TextView neurosurgery_textView4;
            private TextView neurosurgery_textView5;
            private TextView neurosurgery_textView6;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_neurosurgery,menu);
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
