package com.example.hp.hospitalondemand;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Ambulances extends AppCompatActivity {
    Button button1, button2, button3, button4, button5, button6, button7, button8, button9,button10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulances);
        Toast.makeText(Ambulances.this, "Only for Dhaka City", Toast.LENGTH_LONG).show();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        button1 = (Button) findViewById(R.id.Abutton);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent acall = new Intent(Intent.ACTION_DIAL);
                acall.setData(Uri.parse("tel:+8801713205555"));
                if (ActivityCompat.checkSelfPermission(Ambulances.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(acall);
            }
        });
        button2 = (Button) findViewById(R.id.Abutton2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent acall1 = new Intent(Intent.ACTION_DIAL);
                acall1.setData(Uri.parse("tel:029127867"));
                if (ActivityCompat.checkSelfPermission(Ambulances.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                startActivity(acall1);
            }
        });
        button3 = (Button) findViewById(R.id.Abutton3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent acall11 = new Intent(Intent.ACTION_DIAL);
                acall11.setData(Uri.parse("tel:01720448666"));

                if (ActivityCompat.checkSelfPermission(Ambulances.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(acall11);
            }
        });
        button4 = (Button) findViewById(R.id.Abutton4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ab = new Intent(Intent.ACTION_DIAL);
                ab.setData(Uri.parse("tel:029661551"));
                if (ActivityCompat.checkSelfPermission(Ambulances.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                startActivity(ab);
            }
        });
        button5 = (Button) findViewById(R.id.Abutton5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ac= new Intent(Intent.ACTION_DIAL);
                ac.setData(Uri.parse("tel:01714360988"));
                if (ActivityCompat.checkSelfPermission(Ambulances.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                startActivity(ac);
            }
        });
        button6 = (Button) findViewById(R.id.Abutton6);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent am = new Intent(Intent.ACTION_DIAL);
                am.setData(Uri.parse("tel:+8801914001234"));
                if (ActivityCompat.checkSelfPermission(Ambulances.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                startActivity(am);
            }
        });
        button7 = (Button) findViewById(R.id.Abutton7);
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ama = new Intent(Intent.ACTION_DIAL);
                ama.setData(Uri.parse("tel:01713330088"));
                if (ActivityCompat.checkSelfPermission(Ambulances.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                startActivity(ama);
            }
        });
        button8 = (Button) findViewById(R.id.Abutton8);
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ama1 = new Intent(Intent.ACTION_DIAL);
                ama1.setData(Uri.parse("tel:+8801713377773"));
                if (ActivityCompat.checkSelfPermission(Ambulances.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                startActivity(ama1);
            }
        });
        button9 = (Button) findViewById(R.id.Abutton9);
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ama12 = new Intent(Intent.ACTION_DIAL);
                ama12.setData(Uri.parse("tel:+8801714090000"));
                if (ActivityCompat.checkSelfPermission(Ambulances.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                startActivity(ama12);
            }
        });
        button10 = (Button) findViewById(R.id.Abutton10);
        button10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ad = new Intent(Intent.ACTION_DIAL);
                ad.setData(Uri.parse("tel:01713488411"));
                if (ActivityCompat.checkSelfPermission(Ambulances.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                startActivity(ad);
            }
        });

    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item){
//        int id= item.getItemId();
//
//        if (id == R.id.action_about){
//            startActivity(new Intent(this,About.class));
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
