package com.example.vivek.musiccolors;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.example.vivek.musiccolors.menu.DrawerAdapter;
import com.example.vivek.musiccolors.menu.DrawerItem;
import com.example.vivek.musiccolors.menu.SimpleItem;
import com.example.vivek.musiccolors.menu.SpaceItem;

public class MainActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener{


    private ArrayList<SongInfo> song = new ArrayList<SongInfo>();
    RecyclerView recyclerView;
    SongAdapter songAdapter;
    MediaPlayer mediaPlayer;
    private Handler myHandler = new Handler();
    int i=0;




    private SlidingRootNav slidingRootNav;
    private static final int NAV1 = 0;
    private static final int NAV2 = 1;
    private static final int NAV3 = 2;
    private static final int NAV4 = 3;
    private static final int EXIT = 5;

    private String[] screenTitles;
    private Drawable[] screenIcons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //song
        recyclerView  = findViewById(R.id.RecyclerView);
        songAdapter = new SongAdapter(this,song);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(songAdapter);
        mediaPlayer = new MediaPlayer();
        songAdapter.setOnItemClickListener(new SongAdapter.OnItemClickListener(){

            @Override
            public void onItemClick(final CardView b, View v, SongInfo obj, int position) {
             try{
                 if( i==1)
                 {  i=0;
                     mediaPlayer.stop();
                     mediaPlayer.reset();
                     mediaPlayer.release();
                     mediaPlayer = null;
                 }else {
                     mediaPlayer = new MediaPlayer();
                     mediaPlayer.setDataSource(obj.getUri());
                     mediaPlayer.prepareAsync();
                     mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                         @Override
                         public void onPrepared(MediaPlayer mp) {
                             mp.start();
                             i=1;

                         }
                     });
                 }
             }catch (IOException e){

             }
            }
        });
        checkUserPermission();


        //NAVIGATION


        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(true)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();

        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        DrawerAdapter drawadapter = new DrawerAdapter(Arrays.asList(
                createItemFor(NAV1).setChecked(true),
                createItemFor(NAV2),
                createItemFor(NAV3),
                createItemFor(NAV4),
                new SpaceItem(48),
                createItemFor(EXIT)));
        drawadapter.setListener(this);

        RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(drawadapter);
        drawadapter.setSelected(NAV1);


    }


    @Override
    public void onItemSelected(int position) {
        if (position == EXIT) {
            finish();
        }
        if (position == NAV2

                ) {
            Toast.makeText(getApplicationContext(),"NAV2 is Selected", Toast.LENGTH_SHORT).show();
        }

        if (position == NAV3

                ) {
            Toast.makeText(getApplicationContext(),"NAV3 is Selected", Toast.LENGTH_SHORT).show();
        }

        if (position == NAV4

                ) {
            Toast.makeText(getApplicationContext(),"NAV4 is Selected", Toast.LENGTH_SHORT).show();

        }

        slidingRootNav.closeMenu();
        // Fragment selectedScreen = CenteredTextFragment.createFor(screenTitles[position]);
        // showFragment(selectedScreen);
    }



    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.textColorSecondary))
                .withTextTint(color(R.color.textColorSecondary))
                .withSelectedIconTint(color(R.color.navfun))
                .withSelectedTextTint(color(R.color.navfun));
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }

        ta.recycle();
        return icons;
    }
    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }

    private void checkUserPermission(){
        if(Build.VERSION.SDK_INT>=23){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},123);
                return;
            }
        }
        loadSongs();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 123:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    loadSongs();
                }else{
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    checkUserPermission();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        }

    }

    private void loadSongs(){
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC+"!=0";
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                do{
                    String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));


                    SongInfo s = new SongInfo(name,artist,url);
                    song.add(s);

                }while (cursor.moveToNext());
            }

            cursor.close();
            songAdapter = new SongAdapter(MainActivity.this,song);

        }
    }
}






