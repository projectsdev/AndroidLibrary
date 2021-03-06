package domain.project1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

public class controlActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    NavigationView navigationView;
    ActionBar actionBar;
    Toolbar toolbar;
    Context context;
    TabLayout tabLayout;
    SharedPreferences preferences;
    String student_name;
    String branch;
    @Override
    public void onBackPressed() {
        if(navigationView.isShown()){
            drawerLayout.closeDrawers();
        }
        else
            super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        context = this;
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        preferences = getSharedPreferences("UserDetails",Context.MODE_PRIVATE);
        TextView text =  (navigationView.getHeaderView(0)).findViewById(R.id.nav_header_text);
        student_name = preferences.getString("name","Welcome");
        branch = preferences.getString("course","Course") + '/' + preferences.getString("department","Dept");
        text.setText("Logged as "+student_name+'\n'+branch);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                int itemId = item.getItemId();
                if (itemId == R.id.books) {
                    Toast.makeText(getApplicationContext(), "Books selected", Toast.LENGTH_SHORT).show();
                    Intent Booking = new Intent(context,Booking.class);
                    startActivity(Booking);

                }
                else if(itemId == R.id.nrbooks){
                    Intent Booking = new Intent(context,GetNonRenewablebooks.class);
                    startActivity(Booking);
                }
                else  if (itemId == R.id.logout) {
                    Intent obj = new Intent(controlActivity.this,Login.class);
                    obj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(obj);
                    getSharedPreferences("UserDetails",Context.MODE_PRIVATE).edit().putBoolean("autologin",false).commit();
                    finish();
                }
                return false;
            }
        });

        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Library");
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_dehaze_black_24dp);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        String[] titles = new String[]{"Home","Recently Added","My Bookings"};
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
           Fragment fragment = null;
            switch (position){
                case 0:
                     fragment = new Tab1();
                     break;
                case 1:
                    fragment = new Tab2();
                    break;
                case 2:
                    fragment = new Tab3();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

    }
}
