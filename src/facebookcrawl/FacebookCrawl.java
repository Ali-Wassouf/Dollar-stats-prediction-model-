/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facebookcrawl;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Ehab Bshara
 */


public class FacebookCrawl {

    /**
     * @param args the command line arguments
     */
    private String accessToken = "EAACEdEose0cBALYS0LY6O51hj6dekScgsGxs3KDoGAWM9qOxlAWT7DZCqYqNU64WCaZCQdqxiEPwbK0IfEw5ZCJlzEB30Js7eatOMg4yWFRwfP947iDjHvBrP1xZCdf3VtgGl4ncIIdM6OHX1yoypzE3MbZAlZA8FzP0Cd4UcUdQZDZD";
    private String GraphURL = "https://graph.facebook.com/";
    private String pageId = "dollar.damas";
    private String postString;
    private String postDate;
    private List<Map<String, Object>> posts = null;

    public FacebookCrawl() throws IOException {
        posts = new ArrayList<>();
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;

        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }

        return sb.toString();
    }

    public List<Map<String, Object>> getPosts() {
        return posts;
    }

    private static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {

        InputStream is = new URL(url).openStream();

        try {

            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;

        } finally {
            is.close();
        }

    }

    private JSONObject crawl() throws IOException, JSONException {
        String accessTokenAppend = "?access_token=" + accessToken;
        String pageURL = this.GraphURL;
        pageURL = pageURL.concat(pageId + "/posts");
        pageURL = pageURL.concat(accessTokenAppend);
        JSONObject pageObject = new JSONObject();
        pageObject.put("id", pageId);
        pageObject = readJsonFromUrl(pageURL);
        return pageObject;

    }

    public void fetchData(JSONObject jObject) throws IOException, JSONException {
              // JSONArray members = jObject.names();

        JSONArray fields = jObject.getJSONArray("data");
        List<JSONObject> data = new ArrayList<>();
        String normalizedResult;
        for (int i = 0; i < fields.length(); i++) {
            data.add(fields.getJSONObject(i));
        }
        for (JSONObject entry : data) {
            JSONArray members = entry.names();
            postString = null;
            postDate = null;
            Map<String, Object> post = new HashMap<>();
            for (int i = 0; i < members.length(); i++) {
                String check = members.getString(i);
                if (check.equalsIgnoreCase("message")) {
                    postString = (entry.getString(members.getString(i)));
                      //normalizedResult = Normalize.nomalize(post);
                   
                }
                if (check.equalsIgnoreCase("created_time")) {
                    postDate = (entry.getString(members.getString(i)));
                }
            }
            post = Normalize.nomalize(postString);
            if (post != null) {
                post.put("date", postDate);
                posts.add(post);
            }

        }
        write();

        JSONObject paging = jObject.getJSONObject("paging");
        JSONArray pmembers = paging.names();
        String nextURL = null;
        for (int i = 0; i < pmembers.length(); i++) {

            String check = pmembers.getString(i);
            if (StringUtils.startsWith(check, "next")) {
                nextURL = paging.getString(pmembers.getString(i));
                //System.out.println(nextURL);
            }
        }
        if (nextURL != null) {
            System.out.println("next url :" + nextURL);
            posts.clear();
                    // jObject = null ;
            // jObject = readJsonFromUrl(nextURL);
            // fetchData(jObject);
        }

    }

    private void write() throws IOException {
        
        FileWriter writer = new FileWriter("D://d.txt",true);
        for (Map m : posts) {
            for (Object key : m.keySet()) {
                 writer.append(key+" "+m.get(key)+"\n");

            }
            writer.write("\n");
        }
        writer.close();
    }
public static void main(String[] args) {
        // TODO code application logic here
        try{
        FacebookCrawl crawl = new FacebookCrawl();
        JSONObject ob = crawl.crawl();
        crawl.fetchData(ob);
        }catch (Exception e )
        {
            e.printStackTrace();
        }
    }
    
}
