package edu.ucsb.cs.cs190i.derenlei.GauchoMap;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static boolean loggedIn = false;
    public static ListViewFragment listFragment;
    public static MapViewFragment mapFragment;
    public static MyEventsFragment myeventsFragment;
    public static SettingFragment settingFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if(!loggedIn) {
//            loggedIn = true;
//            Intent intent = new Intent(this, LoginActivity.class);
//            startActivity(intent);
//        }

//        if(!DatabaseFactory.is_empty()){
//
//        }


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);

        Bundle inBundle = getIntent().getExtras();
        String name = inBundle.get("name").toString();
        String surname = inBundle.get("surname").toString();
        String imageUrl = inBundle.get("imageUrl").toString();

        TextView user_name = (TextView) hView.findViewById(R.id.user_name);
        user_name.setText(name + " " + surname);

        ImageView prof_pic = (ImageView) hView.findViewById(R.id.profile_icon);
        prof_pic.setImageURI(Uri.parse(imageUrl));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        if(listFragment==null) {
            listFragment = new ListViewFragment();
        }
        if(mapFragment==null){
            mapFragment = new MapViewFragment();
        }
        if(myeventsFragment==null){
            myeventsFragment = new MyEventsFragment();
        }
        if(settingFragment==null){
            settingFragment = new SettingFragment();
        }


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, myeventsFragment);
        ft.commit();
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
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (id == R.id.map_view) {
            Intent myIntent = new Intent(MainActivity.this, MapsActivity.class);
            MainActivity.this.startActivity(myIntent);
//            ft.replace(R.id.fragment_container, mapFragment);
//            ft.commit();
        } else if (id == R.id.list_view) {
            ft.replace(R.id.fragment_container, listFragment);
            ft.commit();
        } else if (id == R.id.my_events) {
            ft.replace(R.id.fragment_container, myeventsFragment);
            ft.commit();
        } else if (id == R.id.setting) {
            ft.replace(R.id.fragment_container, settingFragment);
            ft.commit();
        } else if (id == R.id.logout) {
            LoginManager.getInstance().logOut();
            Intent login = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(login);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
