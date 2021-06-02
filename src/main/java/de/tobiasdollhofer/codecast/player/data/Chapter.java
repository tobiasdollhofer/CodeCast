package de.tobiasdollhofer.codecast.player.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public AudioComment getFirstComment(){
        return this.getComment(0);
    }

    public AudioComment getLastComment(){
        if(comments.size() > 0){
            return comments.get(comments.size() - 1);
        }
        return null;
    }

    public AudioComment getNextComment(AudioComment comment){
        int index = indexOfComment(comment);
        return getComment(index + 1);
    }

    public AudioComment getPreviousComment(AudioComment comment){
        int index = indexOfComment(comment);
        return getComment(index - 1);
    }

    public int indexOfComment(AudioComment comment){
        for(int i = 0; i < comments.size(); i++){
            if(comments.get(i).equals(comment)){
                return i;
            }
        }
        return -1;
    }

    public void setComments(List<AudioComment> comments) {
        this.comments = comments;
    }

    public void addComment(AudioComment comment){
        this.comments.add(comment);
    }

    public AudioComment getComment(int position){
        if(position >= 0 && position < this.comments.size()){
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chapter chapter = (Chapter) o;
        return title.equals(chapter.title) && comments.equals(chapter.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, comments);
    }
}
