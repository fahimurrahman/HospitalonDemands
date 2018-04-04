package com.example.hp.hospitalondemand;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hp.hospitalondemand.DatabaseHelper.Viewholder;
import com.firebase.client.Firebase;

public class DataInsert extends AppCompatActivity {
    Firebase ref;
    EditText et_name,et_hospitalName,et_latitude,et_longitude;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_insert);
        Firebase.setAndroidContext(this);
        ref=new Firebase("https://bd-hospital.firebaseio.com/");
        et_name=(EditText)findViewById(R.id.editText);
        et_hospitalName=(EditText)findViewById(R.id.editText2);
        final EditText et_description=(EditText)findViewById(R.id.editText5);
        et_latitude=(EditText)findViewById(R.id.editText3);
        et_longitude=(EditText)findViewById(R.id.editText4);
        button=(Button)findViewById(R.id.button111111);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=et_name.getText().toString();
                String hospitalName=et_hospitalName.getText().toString();
                String description=et_description.getText().toString();
                Double latitude=Double.parseDouble(et_latitude.getText().toString());
                Double longitude=Double.parseDouble(et_longitude.getText().toString());
                Viewholder viewholder=new Viewholder(hospitalName,description,latitude,longitude);
                Firebase newREf=ref.child(name).push();
                newREf.setValue(viewholder);
                Toast.makeText(DataInsert.this,"Data successfully uploded",Toast.LENGTH_LONG).show();
                et_hospitalName.setText("");
                et_latitude.setText("");
                et_longitude.setText("");
            }
        });


    }
}
