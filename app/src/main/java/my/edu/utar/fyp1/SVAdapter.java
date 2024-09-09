package my.edu.utar.fyp1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;
public class SVAdapter extends RecyclerView.Adapter<SVAdapter.ViewHolder> {

    private List<SportsVideo> allSportsVideos;
    private Context context;
    /**
     * Constructor for VideoAdapter class
     * @param context Context of the activity or fragment that uses this adapter
     * @param sportsVideos List of Video objects to be displayed in the RecyclerView
     */
    public SVAdapter(Context context, List<SportsVideo> sportsVideos){
        this.allSportsVideos = sportsVideos;
        this.context = context;
    }
    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to
     represent an item.
     * @param parent The ViewGroup into which the new View will be added after it
    is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_svadapter,parent,false);

        return new ViewHolder(v);
    }
    /**
     * Called by RecyclerView to display the data at the specified position.
     * @param holder The ViewHolder which should be updated to represent the
    contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.title.setText(allSportsVideos.get(position).getTitle());

        Picasso.get().load(allSportsVideos.get(position).getImageUrl()).into(holder.thumbnail);
        holder.view.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when the user clicks on an item in the RecyclerView
             * @param v The View that was clicked.
             */
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putSerializable("videoData", allSportsVideos.get(position));
                Intent i = new Intent(context, SVPlayer.class);
                i.putExtras(b);
                v.getContext().startActivity(i);
            }
        });
    }
    /**
     * Returns the total number of items in the data set held by the adapter.
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return allSportsVideos.size();
    }

    public void setFilteredList(List<SportsVideo> filteredList) {
        this.allSportsVideos = filteredList;
        notifyDataSetChanged();
    }

    /**
     * This inner class represents a ViewHolder that holds references to the views in the
     layout "video_view".
     */
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView thumbnail;
        TextView title;
        View view;
        /**
         * Constructor for the ViewHolder class
         * @param itemView The View that holds the layout for each item in the
        RecyclerView
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.videoThumbnail);
            title = itemView.findViewById(R.id.videoTitle);
            view = itemView;
        }
    }
}