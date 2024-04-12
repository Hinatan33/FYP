package my.edu.utar.fyp1;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    RecyclerView rv;
    SVAdapter adapter;
    List<SportsVideo> videolist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the title of the ActionBar
        getSupportActionBar().setTitle("Sports Video");

        // Initialize video list
        videolist = new ArrayList<>();
        // Set up the RecyclerView
        rv = findViewById(R.id.videoList);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SVAdapter(this,videolist);
        rv.setAdapter(adapter);
        // Fetch JSON data from server
        getJsonData();
    }
    private void getJsonData() {
        // URL of the JSON data to fetch
        String URL= "https://raw.githubusercontent.com/Hinatan33/Fyp1/main/json.data";
        // Initialize Volley RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        // Send JSON object request to the server
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    // Get the "categories" array from the JSON response
                    JSONArray categories = response.getJSONArray("categories");
                    // Get the first object from the "categories" array
                    JSONObject categoriesData = categories.getJSONObject(0);
                    // Get the "videos" array from the first object
                    JSONArray videos = categoriesData.getJSONArray("videos");
                    // Loop through all the videos in the "videos" array
                    for( int i=0;i< videos.length();i++){
                        JSONObject video = videos.getJSONObject(i);
                        // Create a new Video object and set its properties using the JSON data
                        SportsVideo v = new SportsVideo();
                        v.setTitle(video.getString("title"));
                        v.setDescription(video.getString("description"));
                        v.setImageUrl(video.getString("thumb"));
                        JSONArray videoUrl = video.getJSONArray("sources");
                        v.setVideoUrl(videoUrl.getString(0));
                        // Add the Video object to the list of all videos
                        videolist.add(v);
                        // Notify the adapter that data set has been changed
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> Log.d(TAG, "onErrorResponse: " + error.getMessage()));
        // Add the request to the RequestQueue
        requestQueue.add(objectRequest);
    }
}