package my.edu.utar.fyp1;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
public class SVPlayer extends AppCompatActivity {
    // Initialize variables
    public static final String TAG = "TAG";
    ProgressBar progressbar;
    ImageView fullScreen;
    FrameLayout frameLayout;
    VideoView videoPlayer;
    Boolean isFullScreen = false;
    Uri videoUrl; // Add this line to store the video URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the layout
        setContentView(R.layout.activity_svplayer);
        // Enable up button for navigating to parent activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Find and initialize progress bar and full screen option image view
        progressbar = findViewById(R.id.progressBar);
        fullScreen = findViewById(R.id.fullScreenOp);
        fullScreen.setVisibility(View.VISIBLE);
        // Get the intent and bundle that contains video data
        Intent i = getIntent();
        Bundle data = i.getExtras();
        SportsVideo v = (SportsVideo) data.getSerializable("videoData");
        // Set the title and description of the video
        getSupportActionBar().setTitle(v.getTitle());
        frameLayout = findViewById(R.id.frameLayout);
        TextView title = findViewById(R.id.videoTitle);
        TextView desc = findViewById(R.id.videoDesc);
        videoPlayer = findViewById(R.id.videoView);
        title.setText(v.getTitle());
        desc.setText(v.getDescription());// Set the video URL and attach a media controller
        videoUrl = Uri.parse(v.getVideoUrl()); // Store the video URL
        videoPlayer.setVideoURI(videoUrl);
        MediaController mc = new MediaController(this);
        videoPlayer.setMediaController(mc);
        // Start playing the video and hide the progress bar once it is prepared
        videoPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoPlayer.start();
                progressbar.setVisibility(View.GONE);
            }
        });
        // Add listener for full screen option button
        fullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFullScreen) {
                    fullScreen.setImageDrawable(ContextCompat.getDrawable(SVPlayer.this, R.drawable.fullscreen));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    if(getSupportActionBar() != null){
                        getSupportActionBar().show();
                    }
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) videoPlayer.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    params.height = (int) ( 200 * getApplicationContext().getResources().getDisplayMetrics().density);
                    videoPlayer.setLayoutParams(params);
                    isFullScreen = false;
                }else{
                    fullScreen.setImageDrawable(ContextCompat.getDrawable(SVPlayer.this, R.drawable.exitfullscreen));
                    fullScreen.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
                    fullScreen.getLayoutParams().width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
                    fullScreen.requestLayout();
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                            |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                    if(getSupportActionBar() != null){
                        getSupportActionBar().hide();
                    }
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) videoPlayer.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    params.height = params.MATCH_PARENT;
                    videoPlayer.setLayoutParams(params);
                    isFullScreen = true;
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (videoPlayer != null) {
            videoPlayer.stopPlayback();
        }
    }

    // Handle up button press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //handle back button
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isFullScreen) {
            fullScreen.setImageDrawable(ContextCompat.getDrawable(SVPlayer.this, R.drawable.fullscreen));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            if (getSupportActionBar() != null) {
                getSupportActionBar().show();
            }
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) videoPlayer.getLayoutParams();
            params.width = params.MATCH_PARENT;
            params.height = (int) (200 * getApplicationContext().getResources().getDisplayMetrics().density);
            videoPlayer.setLayoutParams(params);
            isFullScreen = false;
        } else {
            // Navigate back to the main activity
            Intent intent = new Intent(SVPlayer.this, MainActivity.class);
            startActivity(intent);
        }
    }
}