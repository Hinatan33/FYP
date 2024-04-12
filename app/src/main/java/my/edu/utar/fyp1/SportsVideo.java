package my.edu.utar.fyp1;

import java.io.Serializable;
public class SportsVideo implements Serializable {
    private String title;
    private String desc;
    private String video;
    private String thumbnail;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return desc;
    }
    public void setDescription(String description) {
        this.desc = description;
    }
    public String getVideoUrl() {
        return video;
    }
    public void setVideoUrl(String videoUrl) {
        this.video = videoUrl;
    }
    public String getImageUrl() {
        return thumbnail;
    }
    public void setImageUrl(String imageUrl) {
        this.thumbnail = imageUrl;
    }
}