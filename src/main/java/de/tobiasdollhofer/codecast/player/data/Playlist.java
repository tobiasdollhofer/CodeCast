package de.tobiasdollhofer.codecast.player.data;

import java.util.ArrayList;
import java.util.List;

public class Playlist {

    private List<Chapter> chapters;

    public Playlist() {
        this.chapters = new ArrayList<>();
    }

    public Playlist(List<Chapter> chapters) {
        this.chapters = chapters;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }

    public void addChapter(Chapter chapter){
        chapters.add(chapter);
    }

    public Chapter getChapter(int position){
        if(position < chapters.size()){
            return chapters.get(position);
        }

        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Playlist{chapters={");
        for(Chapter chapter : this.chapters){
            sb.append(chapter.toString());
            sb.append(", ");
        }
        sb.append("}}");
        return sb.toString();
    }
}
