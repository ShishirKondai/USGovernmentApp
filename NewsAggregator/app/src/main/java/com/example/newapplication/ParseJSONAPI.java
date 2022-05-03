package com.example.newapplication;

import android.net.Uri;

import org.json.*;
import java.util.*;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class ParseJSONAPI {

    private static MainActivity mainActivity;

    private static final String API = "02a161cd7896437e8e0450936f7269b7";
    private static final String NEWSSOURCE_URL ="https://newsapi.org/v2/sources";
    private static final String SOURCEAPI_URL = "https://newsapi.org/v2/top-headlines";

    private static HashSet<String> menuitemsSet = new HashSet<String>();
    private static List<Source> sources;
    private static News newsData;
    private static List<Article> articleDetails;

    private static RequestQueue queue;
    private static RequestQueue articlesQueue;

    public static News getAPIData(MainActivity ma) {
        mainActivity = ma;
        queue = Volley.newRequestQueue(mainActivity);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, getURL(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        newsData = parseJSON(response);
                        ma.updateData(newsData);

                        menuitemsSet.add("all");
                        sources = newsData.getSources();

                        for (Source ele: sources){
                            menuitemsSet.add(ele.getCategory());
                        }
                        ma.updateMenuItem(menuitemsSet);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("User-Agent", "");
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
        return newsData;
    }

    public static List<Article> getArticles(String source, MainActivity ma){
        mainActivity = ma;
        articlesQueue = Volley.newRequestQueue(mainActivity);

        Uri.Builder buildURL = Uri.parse(SOURCEAPI_URL).buildUpon();
        buildURL.appendQueryParameter("sources", source);
        buildURL.appendQueryParameter("apiKey", API);

        JsonObjectRequest JSONRequest = new JsonObjectRequest
                (Request.Method.GET, buildURL.build().toString(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        articleDetails = parseDetails(response);
                        ma.updateViewPager(articleDetails);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("User-Agent", "");
                return headers;
            }
        };
        articlesQueue.add(JSONRequest);
        return articleDetails;
    }

    private static News parseJSON(JSONObject response) {
       String sourceID, sourceName, sourceDescription;
        try{
           List<Source> sources = new ArrayList<Source>();
           String status  = response.getString("status");

           if(response.getJSONArray("sources") != null){
               JSONArray jsonArray = response.getJSONArray("sources");
               int len = jsonArray.length();
               for (int i = 0; i < len; i++) {
                   JSONObject item = jsonArray.getJSONObject(i);
                   sourceID = item.getString("id");
                   sourceName = item.getString("name");
                   sourceDescription = item.getString("category");
                   sources.add(new Source(sourceID, sourceName, sourceDescription));
               }
           }
           newsData = new News(status, sources);
       }
       catch (Exception ex){
           ex.printStackTrace();
       }
        return newsData;
    }

    private static String getURL() {
        Uri.Builder buildURL = Uri.parse(NEWSSOURCE_URL).buildUpon();
        buildURL.appendQueryParameter("apiKey", API);
        String res = buildURL.build().toString();
        return res;
    }

    private static List<Article> parseDetails(JSONObject response) {
        List<Article> parsedList = new ArrayList<Article>();
        try{
            JSONArray jsonArray = response.getJSONArray("articles");
            for(int i = 0; i < jsonArray.length(); i++){
                Article object = new Article();
                JSONObject jsonItem = jsonArray.getJSONObject(i);

                String headline = jsonItem.getString("title");
                String author_name = jsonItem.getString("author");
                String URL = jsonItem.getString("url");
                String description = jsonItem.getString("description");
                String urlToImage = jsonItem.getString("urlToImage");
                String date = jsonItem.getString("publishedAt");

                object.setHeadline(headline);
                object.setAuthor(author_name);
                object.setUrl(URL);
                object.setDescription(description);
                object.setImageURL(urlToImage);
                object.setDate(date);

                parsedList.add(object);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return parsedList;
    }

    public static List<Source> updateDrawerLayoutData(String title) {
        List<Source> updateList = new ArrayList<Source>();
        int len = sources.size();
        for (int i = 0 ; i < len; i++){
            String matchCategory = sources.get(i).getCategory();
            if(matchCategory.equalsIgnoreCase(title)){
                updateList.add(sources.get(i));
            }
        }
        return updateList;
    }
}
