package de.tobiasdollhofer.codecast.player.data;



import javax.swing.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Playlist entity to store chapters
 */
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

    /**
     *
     * @return first chapter of list or null if list is empty
     */
    public Chapter getFirstChapter(){
        return getChapter(0);
    }

    /**
     *
     * @return last chapter of list or null if list is empty
     */
    public Chapter getLastChapter(){
        return getChapter(chapters.size() - 1);
    }

    /**
     *
     * @return first comment of first chapter or null if there are no comments
     */
    public AudioComment getFirstComment(){
        Chapter chapter = getFirstChapter();
        if(chapter != null){
            return chapter.getFirstComment();
        }
        return null;
    }

    /**
     *
     * @return last comment of last chapter or null if there are no comments
     */
    public  AudioComment getLastComment(){
        Chapter chapter = getLastChapter();
        if(chapter != null){
            return chapter.getLastComment();
        }
        return null;
    }

    /**
     *
     * @param chapter chapter to search for
     * @return index of chapter or -1 if chapter won't be found
     */
    public int indexOfChapter(Chapter chapter){
        for(int i = 0; i < chapters.size(); i++){
            if(chapters.get(i).equals(chapter)){
                return i;
            }
        }
        return -1;
    }

    /**
     *
     * @param chapter current chapter
     * @return next chapter or null if current is last chapter
     */
    public Chapter getNextChapter(Chapter chapter){
        int index = indexOfChapter(chapter);
        return getChapter(index + 1);
    }

    /**
     *
     * @param chapter current chapter
     * @return chapter before current chapter or null if current is first chapter
     */
    public Chapter getPreviousChapter(Chapter chapter){
        int index = indexOfChapter(chapter);
        return getChapter(index - 1);
    }

    /**
     *
     * @param comment current comment
     * @return next comment of current chapter OR first comment of next chapter OR null if current is the last comment of last chapter
     */
    public AudioComment getNextComment(AudioComment comment){
        Chapter chapter = findChapterForComment(comment);
        if(chapter != null){
            AudioComment nextComment = chapter.getNextComment(comment);
            if(nextComment != null){
                return nextComment;
            }

            chapter = getNextChapter(chapter);
            if(chapter != null){
                return chapter.getFirstComment();
            }
        }
        return null;
    }

    /**
     *
     * @param comment current comment
     * @return previous comment of current chapter OR last comment of previous chapter OR null if current is the first comment of first chapter
     */
    public AudioComment getPreviousComment(AudioComment comment){
        Chapter chapter = findChapterForComment(comment);
        if(chapter != null){
            AudioComment previousComment = chapter.getPreviousComment(comment);
            if(previousComment != null){
                return previousComment;
            }

            chapter = getPreviousChapter(chapter);
            if(chapter != null){
                return chapter.getLastComment();
            }
        }
        return null;
    }

    /**
     *
     * @param comment comment to search for
     * @return chapter in which the current comment is stored or null if no chapter can be found
     */
    public Chapter findChapterForComment(AudioComment comment){
        for(int i = 0; i < chapters.size(); i++){
            if(chapters.get(i).indexOfComment(comment) != -1){
                return getChapter(i);
            }
        }
        return null;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }

    /**
     * adds a chapter to the playlist and rearranges the order of the chapters
     * @param chapter chapter to add
     */
    public void addChapter(Chapter chapter){
        chapters.add(chapter);
        // rearrange order depending on title of the chapter
        chapters.sort(new Comparator<Chapter>() {
            @Override
            public int compare(Chapter c1, Chapter c2) {
                return c1.getTitle().compareTo(c2.getTitle());
            }
        });
    }

    /**
     *
     * @param position position of the chapter
     * @return chapter on position or null of position is out of bounds
     */
    public Chapter getChapter(int position){
        if(position >= 0 && position < chapters.size()){
            return chapters.get(position);
        }

        return null;
    }

    /**
     * adds comment to existing chapter if its chapter title exists or creates a new chapter and adds it to playlist
     * @param comment comment to add
     */
    public void addComment(AudioComment comment){
        // add comment to chapter if fitting chapter already exists
        for(Chapter c : chapters){
            if(c.getTitle().equals(comment.getChapter())){
                c.addComment(comment);
                return;
            }
        }

        // otherwise create new chapter and add it to playlist
        Chapter chapter = new Chapter();
        chapter.setTitle(comment.getChapter());
        chapter.addComment(comment);
        addChapter(chapter);
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

    /**
     *
     * @return all comments of all chapters
     */
    public ArrayList<AudioComment> getAllComments() {
        ArrayList<AudioComment> comments = new ArrayList<>();

        for(Chapter chapter : chapters){
            comments.addAll(chapter.getComments());
        }
        return comments;
    }

    /**
     *
     * @return if playlist has no comments in it
     */
    public boolean isEmpty() {
        return getAllComments().size() == 0;
    }
}
