package my.edu.utar.fyp1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
public class VideoActivity extends AppCompatActivity {
    FloatingActionButton addVideo;
    private RecyclerView videosRv;
    private ArrayList<ModelVideo> videoArrayList;
    private AdapterVideo adapterVideo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        videosRv = findViewById(R.id.videosRv);
        addVideo = findViewById(R.id.addVideos);
        loadVideosFromFirebase();
        addVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VideoActivity.this, AddVideos.class));
            }
        });
    }
    private void loadVideosFromFirebase(){
        videoArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Videos");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    ModelVideo modelVideo = ds.getValue(ModelVideo.class);
                    videoArrayList.add(modelVideo);
                }
                adapterVideo = new AdapterVideo(VideoActivity.this, videoArrayList);
                videosRv.setAdapter(adapterVideo);
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }
}