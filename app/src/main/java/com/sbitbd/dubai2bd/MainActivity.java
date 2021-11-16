package com.sbitbd.dubai2bd;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.sbitbd.dubai2bd.Config.converter;
import com.sbitbd.dubai2bd.Config.sqliteDB;
import com.sbitbd.dubai2bd.ui.home.HomeViewModel;
import com.sbitbd.dubai2bd.ui.login_page.ui.login.LoginActivity;
import com.sbitbd.dubai2bd.ui.sh_cart.sh_cart;
import com.sbitbd.dubai2bd.ui.wish_list.wish;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private HomeViewModel homeViewModel;
    private boolean check = false;
    private MenuItem sign;
    private FloatingActionButton fab;
    Toolbar toolbar;
    private static int cart_count=0;
    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());

        toolbar.setLogo(R.drawable.finallogo);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,R.id.nav_product,R.id.nav_developer,R.id.nav_terms_cond)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        homeViewModel = new HomeViewModel();
        homeViewModel.resetSession(MainActivity.this);

        sign = navigationView.getMenu().findItem(R.id.nav_sign);
        user_check();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isOpen())
            drawer.close();
        else
            super.onBackPressed();
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem menuItem = menu.findItem(R.id.cart);
//        cart_count = getcart();
        menuItem.setIcon(converter.convertLayoutToImage(MainActivity.this,cart_count,R.drawable.ic_outline_shopping_cart_24));

        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(MainActivity.this, sh_cart.class));
                return true;
            }
        });
        menu.getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(MainActivity.this, wish.class));
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();
        user_check();

    }

    public void regularSnak(String msg){
        Snackbar snackbar = Snackbar.make(fab,msg, Snackbar.LENGTH_LONG);
        snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
        snackbar.show();
    }
    public void regularcondSnak(String msg){
        Snackbar snackbar = Snackbar.make(fab,msg, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }
    private void user_check(){
        try {
            homeViewModel = new HomeViewModel();
            check = homeViewModel.checkUser(MainActivity.this);
            if (check){
                sign.setTitle(R.string.sign_out);
                sign.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(MainActivity.this);
                        dialogBuilder.setTitle("Confirmation");
                        dialogBuilder.setMessage("Are you sure you want to Log out?");
                        dialogBuilder.setCancelable(false);
                        dialogBuilder.setNegativeButton("NO",(dialog, which) -> {
                            dialog.dismiss();
                        });
                        dialogBuilder.setPositiveButton("Yes",(dialog, which) -> {

                            homeViewModel.userDelete(MainActivity.this);
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finish();

                        });
                        dialogBuilder.show();

                        return true;
                    }
                });
            }else {

                sign.setOnMenuItemClickListener(item -> {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                    return true;
                });
            }
        }catch (Exception e){
        }
    }

    private int getcart(){
        int id = 0;
        sqliteDB sqliteDB = new sqliteDB(this);
        String session = homeViewModel.getSession(this);
        try {
            Cursor cursor = sqliteDB.getUerData("SELECT COUNT(id) AS 'id' FROM shopping_carts WHERE session_id = '" + session + "'");
            if(cursor.getCount() > 0){
                if(cursor.moveToNext()){
                    id = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("id")));
                }
            }
        }catch (Exception e){
        }
        finally {
            try {
                sqliteDB.close();
            }catch (Exception e){
            }
        }
        return id;
    }

}