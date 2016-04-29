package product.presisco.yourdrivers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import product.presisco.yourdrivers.ContentFrames.HeadlinesFragment;
import product.presisco.yourdrivers.ContentFrames.ViewPointsFragment;
import product.presisco.yourdrivers.DataModel.Category;
import product.presisco.yourdrivers.Network.Constants;
import product.presisco.yourdrivers.Preference.SettingsActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ViewPointsFragment mViewPointsFragment;
    HeadlinesFragment mHeadlinesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        //drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setTitle(ViewPointsFragment.TITLE_TEXT);

        mViewPointsFragment = ViewPointsFragment.newInstance();
        mHeadlinesFragment = HeadlinesFragment.newInstance(new Category("all", Constants.NEWS_CATEGORY, "0"));

        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.contentFrame, mViewPointsFragment);
        trans.commit();
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
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //    return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        switch (id) {
            case R.id.nav_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.nav_viewpoint:
                getSupportActionBar().setTitle(ViewPointsFragment.TITLE_TEXT);
                trans.replace(R.id.contentFrame, mViewPointsFragment);
                trans.commit();
                break;
            case R.id.nav_topics:
                getSupportActionBar().setTitle(HeadlinesFragment.TITLE_TEXT);
                trans.replace(R.id.contentFrame, mHeadlinesFragment);
                trans.commit();
                break;
            case R.id.nav_about:
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
