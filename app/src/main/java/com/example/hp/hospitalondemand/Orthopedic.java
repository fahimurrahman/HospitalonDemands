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

import com.example.hp.hospitalondemand.HeartData.OrthopedicDatas;
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

public class Orthopedic extends AppCompatActivity {

    private ListView orthopediclistView;
    private ProgressDialog orthopedic_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orthopedic);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        orthopedic_dialog =new ProgressDialog(this);
        orthopedic_dialog.setIndeterminate(true);
        orthopedic_dialog.setCancelable(false);
        orthopedic_dialog.setMessage("Loading, Please Wait...");

        // Image loader
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);

        orthopediclistView = (ListView) findViewById(R.id.orthopedic_listView);
        new JSONTask().execute("https://hospitalondemands.000webhostapp.com/connection/json_get_data_orthopedic.php");

    }

    public class JSONTask extends AsyncTask<String, String , List<OrthopedicDatas>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            orthopedic_dialog.show();
        }

        @Override
        protected List<OrthopedicDatas> doInBackground(String... params) {

            HttpURLConnection connection=null;
            BufferedReader reader= null;

            try{
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
                List<OrthopedicDatas> orthopedicDatasList = new ArrayList<>();

                Gson orthopedic_gson = new Gson();
                for (int k=0; k<parentArray.length(); k++){
                    JSONObject finalObject = parentArray.getJSONObject(k);
                    OrthopedicDatas orthopedicDatas= orthopedic_gson.fromJson(finalObject.toString(),OrthopedicDatas.class);
                    orthopedicDatasList.add(orthopedicDatas);
                }

                return orthopedicDatasList;

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
        protected void onPostExecute(List<OrthopedicDatas> orthopedicresult) {
            super.onPostExecute(orthopedicresult);
            orthopedic_dialog.dismiss();
            OrthopedicAdapter orthopedicAdapter= new OrthopedicAdapter(getApplicationContext(), R.layout.orthopedic, orthopedicresult);
            orthopediclistView.setAdapter(orthopedicAdapter);
        }
    }

    public class OrthopedicAdapter extends ArrayAdapter{

        private List<OrthopedicDatas> orthopedicsDatasList;
        private int orthopedic_resource;
        private LayoutInflater orthopedic_inflater;

        public OrthopedicAdapter(Context context, int resource, List<OrthopedicDatas> objects) {
            super(context, resource, objects);
            orthopedicsDatasList = objects;
            this.orthopedic_resource =resource;
            orthopedic_inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            orthopedic_ViewHolder orthipedic_holder;

            if (convertView == null){
                orthipedic_holder = new orthopedic_ViewHolder();
                convertView =orthopedic_inflater.inflate(orthopedic_resource, null);

                orthipedic_holder.orthopedic_imageView=(ImageView)convertView.findViewById(R.id.orthopedic_imageView);
                orthipedic_holder.orthopedic_textView2=(TextView)convertView.findViewById(R.id.orthopedic_textView2);
                orthipedic_holder.orthopedic_textView3=(TextView)convertView.findViewById(R.id.orthopedic_textView3);
                orthipedic_holder.orthopedic_textView4=(TextView)convertView.findViewById(R.id.orthopedic_textView4);
                orthipedic_holder.orthopedic_textView5=(TextView)convertView.findViewById(R.id.orthopedic_textView5);
                orthipedic_holder.orthopedic_textView6=(TextView)convertView.findViewById(R.id.orthopedic_textView6);
                convertView.setTag(orthipedic_holder);
            }else{
                orthipedic_holder = (orthopedic_ViewHolder) convertView.getTag();
            }
            //Image load start
            final ProgressBar orthopedic_progressBar = (ProgressBar) convertView.findViewById(R.id.orthopedic_progressBar);

            ImageLoader.getInstance().displayImage(orthopedicsDatasList.get(position).getHospital_orthopedic_images(), orthipedic_holder.orthopedic_imageView, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    orthopedic_progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    orthopedic_progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    orthopedic_progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    orthopedic_progressBar.setVisibility(View.GONE);
                }
            });
            orthipedic_holder.orthopedic_textView2.setText(orthopedicsDatasList.get(position).getHospital_orthopedic_name());
            orthipedic_holder.orthopedic_textView3.setText(orthopedicsDatasList.get(position).getHospital_orthopedic_description());
            orthipedic_holder.orthopedic_textView4.setText(orthopedicsDatasList.get(position).getHospital_orthopedic_equipment());
            orthipedic_holder.orthopedic_textView5.setText(orthopedicsDatasList.get(position).getHospital_orthopedic_address());
            orthipedic_holder.orthopedic_textView6.setText(orthopedicsDatasList.get(position).getHospital_orthopedic_pnumber());

            return convertView;
        }

        class orthopedic_ViewHolder{
            private ImageView orthopedic_imageView;
            private TextView orthopedic_textView2;
            private TextView orthopedic_textView3;
            private TextView orthopedic_textView4;
            private TextView orthopedic_textView5;
            private TextView orthopedic_textView6;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.menu_orthopedic,menu);
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
