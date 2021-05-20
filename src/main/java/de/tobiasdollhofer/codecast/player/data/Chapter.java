package de.tobiasdollhofer.codecast.player.data;

import java.util.ArrayList;
import java.util.List;

public class Chapter {

    private String title;
    private List<AudioComment> comments;

    public Chapter() {
        this.title = "";
        this.comments = new ArrayList<>();
    }

    public Chapter(String title, List<AudioComment> comments) {
        this.title = title;
        this.comments = comments;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<AudioComment> getComments() {
        return comments;
    }

    public void setComments(List<AudioComment> comments) {
        this.comments = comments;
    }

    public void addComment(AudioComment comment){
        this.comments.add(comment);
    }

    public AudioComment getComment(int position){
        if(position < this.comments.size()){
            return comments.get(position);
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Chapter{");
        sb.append("title='" + title + '\'');
        sb.append(", comments={");

        for(AudioComment comment : comments){
            sb.append(comment.toString());
            sb.append(", ");
        }

        sb.append("}}");
        return sb.toString();
    }
}
