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
import com.example.hp.hospitalondemand.HeartData.StomachWashDatas;
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


public class StomachWash extends AppCompatActivity {

    private ListView stomachwashlistView;
    private ProgressDialog stomachwash_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stomach_wash);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        stomachwash_dialog = new ProgressDialog(this);
        stomachwash_dialog.setIndeterminate(true);
        stomachwash_dialog.setCancelable(false);
        stomachwash_dialog.setMessage("Loading, Please wait...");


        // Create default options which will be used for every
//  displayImage(...) call if no options will be passed to this method
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);

        stomachwashlistView =(ListView)findViewById(R.id.stomach_wash_listView);
        new JSONTask().execute("https://hospitalondemands.000webhostapp.com/connection/json_get_data_stomachwash.php");
    }

    public class JSONTask extends AsyncTask<String, String, List<StomachWashDatas>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            stomachwash_dialog.show();
        }

        @Override
        protected List<StomachWashDatas> doInBackground(String... params) {

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
                JSONArray  parentArray = parentObject.getJSONArray("server_responce");

                List<StomachWashDatas> stomachWashDatasList= new ArrayList<>();

                Gson stomachwash_gson = new Gson();
                for (int j=0; j<parentArray.length(); j++){
                    JSONObject finalObject= parentArray.getJSONObject(j);
                    StomachWashDatas stomachWashDatas= stomachwash_gson.fromJson(finalObject.toString(),StomachWashDatas.class);
                    stomachWashDatasList.add(stomachWashDatas);
                }
                return stomachWashDatasList;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
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
        protected void onPostExecute(List<StomachWashDatas> stomachWashDatasresult) {
            super.onPostExecute(stomachWashDatasresult);

            stomachwash_dialog.dismiss();
            StomachwashAdapter stomachwashAdapter = new StomachwashAdapter(getApplicationContext(), R.layout.stomachwash, stomachWashDatasresult);
            stomachwashlistView.setAdapter(stomachwashAdapter);

        }
    }

    public class StomachwashAdapter extends ArrayAdapter{

        private List<StomachWashDatas> stomachWashDatasList;
        private int stomachwash_resource;
        private LayoutInflater stomachwash_inflater;

        public StomachwashAdapter(Context context, int resource, List<StomachWashDatas> objects) {
            super(context, resource, objects);
            stomachWashDatasList = objects;
            this.stomachwash_resource= resource;
            stomachwash_inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            stomachwash_ViewHolder stomachwash_holder;

            if (convertView == null){
                stomachwash_holder = new stomachwash_ViewHolder();
                convertView = stomachwash_inflater.inflate(stomachwash_resource, null);

                stomachwash_holder.stomachwash_imageView=(ImageView)convertView.findViewById(R.id.stomachwash_imageView);
                stomachwash_holder.stomachwash_textView1=(TextView)convertView.findViewById(R.id.stomachwash_textView2);
                stomachwash_holder.stomachwash_textView2=(TextView)convertView.findViewById(R.id.stomachwash_textView3);
                stomachwash_holder.stomachwash_textView3=(TextView)convertView.findViewById(R.id.stomachwash_textView4);
                stomachwash_holder.stomachwash_textView4=(TextView)convertView.findViewById(R.id.stomachwash_textView5);
                stomachwash_holder.stomachwash_textView5=(TextView)convertView.findViewById(R.id.stomachwash_textView6);
                convertView.setTag(stomachwash_holder);
            }else {
                stomachwash_holder =(stomachwash_ViewHolder) convertView.getTag();
            }

            final ProgressBar stomachwash_progressBar= (ProgressBar) convertView.findViewById(R.id.stomachwash_progressBar);
            ImageLoader.getInstance().displayImage(stomachWashDatasList.get(position).getHospital_stomachwash_images(), stomachwash_holder.stomachwash_imageView, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    stomachwash_progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    stomachwash_progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    stomachwash_progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    stomachwash_progressBar.setVisibility(View.GONE);
                }
            });

            stomachwash_holder.stomachwash_textView1.setText(stomachWashDatasList.get(position).getHospital_stomachwash_name());
            stomachwash_holder.stomachwash_textView2.setText(stomachWashDatasList.get(position).getHospital_stomachwash_description());
            stomachwash_holder.stomachwash_textView3.setText(stomachWashDatasList.get(position).getHospital_stomachwash_equipment());
            stomachwash_holder.stomachwash_textView4.setText(stomachWashDatasList.get(position).getHospital_stomachwash_address());
            stomachwash_holder.stomachwash_textView5.setText(stomachWashDatasList.get(position).getHospital_stomachwash_pnumber());
            return convertView;
        }
        class stomachwash_ViewHolder{
            private ImageView stomachwash_imageView;
            private TextView stomachwash_textView1;
            private TextView stomachwash_textView2;
            private TextView stomachwash_textView3;
            private TextView stomachwash_textView4;
            private TextView stomachwash_textView5;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.menu_stomach_wash,menu);
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
