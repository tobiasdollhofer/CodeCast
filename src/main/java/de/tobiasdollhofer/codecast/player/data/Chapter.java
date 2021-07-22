package de.tobiasdollhofer.codecast.player.data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Chapter entity stores some comments and a chapter title
 */
public class Chapter {

    private String title;
    private List<AudioComment> comments;

    public Chapter() {
        this.title = "";
        this.comments = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public String getTitleWithoutNumber(){
        return title.replaceFirst("^[0-9]+[.]", "");
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<AudioComment> getComments() {
        return comments;
    }

    /**
     *
     * @return first comment of chapter or null if list is empty
     */
    public AudioComment getFirstComment(){
        return this.getComment(0);
    }

    /**
     *
     * @return last comment of chapter or null if list is empty
     */
    public AudioComment getLastComment(){
        return getComment(comments.size() - 1);
    }

    /**
     * returns next comment or null if list is empty/current comment is last of chapter
     * @param comment current comment
     * @return next comment in chapter
     */
    public AudioComment getNextComment(AudioComment comment){
        int index = indexOfComment(comment);
        return getComment(index + 1);
    }

    /**
     * returns previous comment or null if list is empty/current comment is first of chapter
     * @param comment current comment
     * @return previous comment
     */
    public AudioComment getPreviousComment(AudioComment comment){
        int index = indexOfComment(comment);
        return getComment(index - 1);
    }

    /**
     * searches in list for comment and returns its index
     * @param comment comment to search for
     * @return index of comment or -1 if it won't be found
     */
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

    /**
     * adds comment to list and rearranges its order alphabetically
     * @param comment comment to add
     */
    public void addComment(AudioComment comment){
        this.comments.add(comment);
        // rearrange order depending on position of comment
        this.comments.sort(new Comparator<AudioComment>() {
            @Override
            public int compare(AudioComment c1, AudioComment c2) {
                return c1.getTitle().compareTo(c2.getTitle());
            }
        });
    }

    /**
     * returns comment on position or null if position is out of bounds
     * @param position index
     * @return comment or null
     */
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
