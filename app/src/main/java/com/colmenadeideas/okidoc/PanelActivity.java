package com.colmenadeideas.okidoc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.colmenadeideas.okidoc.appointments.AppointmentsNextFragment;
import com.colmenadeideas.okidoc.includes.config.libs.User;
import com.colmenadeideas.okidoc.practices.PracticeListFragment;


public class PanelActivity extends AppCompatActivity
        implements ArticulosListaFragmento.EscuchaFragmento{

    private DrawerLayout drawerLayout;
    private String drawerTitle;
    private boolean dosPaneles;

    TextView doctorEmail;
    User session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.panel_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Fragment fragment = new AppointmentsNextFragment();
        loadFirstFragment(fragment, "Próximas Citas");


        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        session = new User(PanelActivity.this);

       // NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
       // navigationView.setNavigationItemSelectedListener(this);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }


    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // Marcar item presionado
                        menuItem.setChecked(true);
                        // Crear nuevo fragmento
                        String title = menuItem.getTitle().toString();
                        selectItem(title);

                        return true;
                    }
                }
        );
    }

    //private void selectItem(String title) {
    private void selectItem(String title) {

        Fragment fragment = null;

        switch (title){
            case "Import":
                fragment = new ArticulosListaFragmento();

                break;
            case "Prácticas y Horarios":
                fragment = new PracticeListFragment();
                break;
            default:
                //fragment = PlaceholderFragment.newInstance(title);
                fragment = new AppointmentsNextFragment();
                break;
        }

        loadFirstFragment(fragment, title);

    }

    private Fragment loadFirstFragment(Fragment fragmentSelected, String title){

        Bundle args = new Bundle();
        args.putString(PlaceholderFragment.ARG_SECTION_TITLE, title);

        Fragment fragment = fragmentSelected;

        fragment.setArguments(args);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.mainArea, fragment)
                .commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        setTitle(title);
        return null;
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
        //getMenuInflater().inflate(R.menu.panel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void alSeleccionarItem(String idArticulo) {
        //Tuve que duplicar el Evento

        if (dosPaneles) {
            //cargarFragmentoDetalle(idArticulo);
        } else {
            Log.d("ISPANEL","Nonoi");
            Intent intent = new Intent(this, ArticuloDetalleActividad.class);
            intent.putExtra(ArticuloDetalleFragmento.ID_ARTICULO, idArticulo);

            startActivity(intent);
        }
    }


}
