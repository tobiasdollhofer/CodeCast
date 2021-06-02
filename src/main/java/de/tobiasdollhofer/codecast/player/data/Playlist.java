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

    public Chapter getFirstChapter(){
        if(chapters.size() > 0){
            return chapters.get(0);
        }
        return null;
    }

    public Chapter getLastChapter(){
        if(chapters.size() > 0){
            return chapters.get(chapters.size() - 1);
        }
        return null;
    }

    public AudioComment getFirstComment(){
        Chapter chapter = getFirstChapter();
        if(chapter != null){
            return chapter.getFirstComment();
        }
        return null;
    }

    public  AudioComment getLastComment(){
        Chapter chapter = getLastChapter();
        if(chapter != null){
            return chapter.getLastComment();
        }
        return null;
    }

    public int indexOfChapter(Chapter chapter){
        for(int i = 0; i < chapters.size(); i++){
            if(chapters.get(i).equals(chapter)){
                return i;
            }
        }
        return -1;
    }

    public Chapter getNextChapter(Chapter chapter){
        int index = indexOfChapter(chapter);
        return getChapter(index + 1);
    }

    public Chapter getPreviousChapter(Chapter chapter){
        int index = indexOfChapter(chapter);
        return getChapter(index - 1);
    }

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

    public void addChapter(Chapter chapter){
        chapters.add(chapter);
    }

    public Chapter getChapter(int position){
        if(position >= 0 && position < chapters.size()){
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
