package com.example.newapplication;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import android.view.*;
import android.widget.*;
import java.util.*;

public class MainActivity extends AppCompatActivity {

    private Menu menu;
    News news;
    private static List<Article> articleDetails = new ArrayList<>();
    NewsAdapter articlesAdapter;
    private ArrayAdapter<Source> arrAdapter;

    private ActionBarDrawerToggle drawerToggle;
    List<Source> sourceList;
    List<Source> list;
    private ViewPager2 viewPager2;
    private DrawerLayout drawer;
    private ListView drawerList;

    TextView menuItem;

    HashMap<String, String> color;
    private static HashMap<String, String> hashMap = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String app_tile = "News Gateway";
        setTitle(app_tile);

        viewPager2 = findViewById(R.id.viewPager2_id);
        drawer = findViewById(R.id.drawer);
        drawerList = findViewById(R.id.drawer_layout);

        boolean isNetworkConnected = checkNetworkConnection();
        if(isNetworkConnected){
            getNewsData();
        }
        else {

        }
        drawerList.setOnItemClickListener(
                (parent, view, position, id) -> selectItem(position)
        );

        drawerToggle = new ActionBarDrawerToggle(this,
                drawer, R.string.drawer_open,R.string.drawer_close);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    public static Boolean isNetworkConnected(Activity activity){
        ConnectivityManager connectivityManager = activity.getSystemService(ConnectivityManager.class);
        NetworkInfo network = connectivityManager.getActiveNetworkInfo();
        if(network != null && network.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        return true;
    }

    private void getNewsData() {
        news = ParseJSONAPI.getAPIData(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        if(item.getTitle().equals("all")){
            updateDrawerItems(sourceList, item.getTitle().toString());
        }
        else if(item.getTitle() != null){
           List<Source> updatedList =  ParseJSONAPI.updateDrawerLayoutData(item.getTitle().toString());
           String str = item.getTitle().toString();
           updateDrawerItems(updatedList, str);
        }
        else {

        }
        return super.onOptionsItemSelected(item);
    }

    private void updateDrawerItems(List<Source> updateDrawerItems, String title) {
        list = updateDrawerItems;
        arrAdapter = new ArrayAdapter<Source>(this, android.R.layout.simple_list_item_1, updateDrawerItems){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                Source newsSource = updateDrawerItems.get(position);
                View view = super.getView(position, convertView, parent);
                menuItem = (TextView) view.findViewById(android.R.id.text1);
                menuItem.setText(newsSource.getName());
                if(title.equals("all")){
                    int allListLength = updateDrawerItems.size();
                    String title = "News Gateway";
                    setTitle(title + " " + "(" + allListLength + ")" );
                    menuItem.setTextColor(Color.parseColor(color.get(newsSource.getCategory())));
                }
                else {
                    int len = updateDrawerItems.size();
                    setTitle(updateDrawerItems.get(0).getCategory() + " (" + len + ")");
                }
                menuItem.setTextColor(Color.parseColor(color.get(newsSource.getCategory())));
                return view;
            }
        };
        drawerList.setAdapter(arrAdapter);
    }

    private void selectItem(int position) {
        String setSourceTitle = list.get(position).getName();
        setTitle(setSourceTitle);
        articleDetails = ParseJSONAPI.getArticles(list.get(position).getId(), this);
        viewPager2.setCurrentItem(position);
        drawer.setBackgroundResource(0);
        viewPager2.setBackground(null);
        drawer.closeDrawer(drawerList);
    }

    private boolean checkNetworkConnection() {
        if(isNetworkConnected(this)){
            return true;
        }
        return false;
    }

    public void updateData(News news) {
        this.news = news;
        sourceList = news.getSources();
        list = sourceList;

        arrAdapter = new ArrayAdapter<Source>(this, android.R.layout.simple_list_item_1, this.sourceList){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                Source source = sourceList.get(position);

                View view = super.getView(position, convertView, parent);
                menuItem = (TextView) view.findViewById(android.R.id.text1);

                menuItem.setText(source.getName());

                int len = sourceList.size();
                String title = "News Gateway ";
                setTitle(title + "(" + len + ")");

                menuItem.setTextColor(Color.parseColor(color.get(source.getCategory())));
                return view;
            }
        };
        drawerList.setAdapter(arrAdapter);
    }

    public void updateMenuItem(HashSet<String> dynamicMenu) {
        color = sourceColors(dynamicMenu);
        for (String menu: dynamicMenu) {
            if(!menu.equals("all")){
                SpannableString sstr = new SpannableString(menu);
                sstr.setSpan(new ForegroundColorSpan(Color.parseColor(color.get(menu))), 0, sstr.length(), 0);
                this.menu.add(sstr);
            }
            else {
                this.menu.add(menu);
            }
        }
    }

    public void updateViewPager(List<Article> articleDetails) {
        articlesAdapter = new NewsAdapter(this, articleDetails);
        viewPager2.setAdapter(articlesAdapter);
    }

    public static HashMap<String, String> sourceColors(HashSet<String> dynamicMenu){

        List<String> list = new ArrayList<String>();
        list.add("#B50000FF");
        list.add("#DDA52A2A");
        list.add("#DC8A2BE2");
        list.add("#F2008B8B");
        list.add("#DC6495ED");
        list.add("#EBDC143C");
        list.add("#D8FF7F50");
        list.add("#E97FFF00");
        list.add("#EBDEB887");
        list.add("#F5D2691E");
        list.add("#EB5F9EA0");

        int index = 0;
        for(String str : dynamicMenu){
            hashMap.put(str, list.get(index));
            index++;
        }
        return hashMap;
    }
}
