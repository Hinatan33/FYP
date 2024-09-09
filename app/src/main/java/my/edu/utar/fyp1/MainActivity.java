package my.edu.utar.fyp1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
    SearchView searchView;
    FirebaseAuth auth;
    Button button;
    FirebaseUser user;
    FloatingActionButton addVideosBtn,showvideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addVideosBtn = findViewById(R.id.addVideosBtn);

        addVideosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             startActivity(new Intent(MainActivity.this, AddVideos.class));
            }
        });

        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logout);
        user = auth.getCurrentUser();

        if (user == null){
            Intent intent = new Intent(getApplicationContext(), LoginPage.class);
            startActivity(intent);
            finish();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginPage.class);
                startActivity(intent);
                finish();
            }
        });

    //Search bar
    searchView = findViewById(R.id.searchView);
    searchView.clearFocus();
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }
        @Override
        public boolean onQueryTextChange(String newText) {
            filterList(newText);
            return true;
        }
    });

    // Set custom toolbar
    getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
    getSupportActionBar().setCustomView(R.layout.toolbar_title_layout);
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

    private void filterList(String Text) {
    List<SportsVideo> filteredList = new ArrayList<>();
    for (SportsVideo item : videolist){
        if (item.getTitle().toLowerCase().contains(Text.toLowerCase())){
            filteredList.add(item);
        }
    }

    if (filteredList.isEmpty()){
        Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
    }else {
        adapter.setFilteredList(filteredList);
    }
}
private void getJsonData() {
    // URL of the JSON data to fetch
    String URL= "https://raw.githubusercontent.com/Hinatan33/Fyp1/main/json.data";
    // Initialize Volley RequestQueue
    RequestQueue requestQueue = Volley.newRequestQueue(this);
    // Send JSON object request to the server
    JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET,
            URL, null, new Response.Listener<JSONObject>() {
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
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d(TAG, "onErrorResponse: " + error.getMessage());
        }
    });
    // Add the request to the RequestQueue
    requestQueue.add(objectRequest);
}
}