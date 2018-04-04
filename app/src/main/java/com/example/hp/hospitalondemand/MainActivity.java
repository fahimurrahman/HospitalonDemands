package com.example.hp.hospitalondemand;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public ImageButton button,button1,button2,button3,button4,button5,button6,button8,location_button;
    public Button button7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button=(ImageButton)findViewById(R.id.heart_button);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent heart= new Intent(MainActivity.this,HeartOption.class);
                startActivity(heart);
            }
        });

        button1= (ImageButton) findViewById(R.id.orthopedic_button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,OthopedicOption.class));
            }
        });

        button2=(ImageButton) findViewById(R.id.neurosurgery_button);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,NeurosurgeryOption.class));
            }
        });


        button3 =(ImageButton) findViewById(R.id.premature_button);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent prematurebaby =new Intent(MainActivity.this,PrematurebabyOption.class);
                startActivity(prematurebaby);
            }
        });


        button4=(ImageButton) findViewById(R.id.stomach_button);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent stomachwash = new Intent(MainActivity.this,StomachwashOption.class);
                startActivity(stomachwash);
            }
        });

        button5=(ImageButton) findViewById(R.id.cancer_button);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CancerOption.class));
            }
        });
        button6= (ImageButton) findViewById(R.id.eye_button);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,EyeOption.class));
            }
        });

        button7=(Button)findViewById(R.id.about_button);
        button7.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent about=new Intent(MainActivity.this,Us.class);
                startActivity(about);
            }
        });
        button8 = (ImageButton) findViewById(R.id.gastric_button);
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,GastricOption.class));
            }
        });

        location_button = (ImageButton) findViewById(R.id.mapimageButton);
        location_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,NearbyHospital.class));
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            startActivity(new Intent(this,Us.class));
            return true;
        }
        else if (id == R.id.action_ambulance){
            startActivity(new Intent(this,Ambulances.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_ambulance) {
            startActivity(new Intent(MainActivity.this,Ambulances.class));

        } else if (id == R.id.nav_about) {
            startActivity(new Intent(MainActivity.this,Us.class));

        }
        else if (id == R.id.nav_develop) {
            startActivity(new Intent(MainActivity.this, About.class));
        }

//        else if (id == R.id.nav_manage) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
