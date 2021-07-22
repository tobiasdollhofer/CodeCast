package de.tobiasdollhofer.codecast.player.ui.playlist;

import com.intellij.openapi.project.Project;
import de.tobiasdollhofer.codecast.player.data.AudioComment;
import de.tobiasdollhofer.codecast.player.data.Chapter;
import de.tobiasdollhofer.codecast.player.data.Playlist;
import de.tobiasdollhofer.codecast.player.service.playermanager.PlayerManagerService;
import de.tobiasdollhofer.codecast.player.util.event.ui.UIEvent;
import de.tobiasdollhofer.codecast.player.util.event.ui.UIEventType;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * View to display playlist
 */
public class PlaylistView extends JPanel {

    private Playlist playlist;
    private Project project;
    private AudioComment current;

    public PlaylistView(Playlist playlist, Project project) {
        this.playlist = playlist;
        this.project = project;
        this.current = playlist.getFirstComment();
        buildView();
    }

    /**
     * sets layout and builds chapterviews
     */
    private void buildView() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        //create margin around view
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        buildChapterViews();
    }

    /**
     * creates and adds views for each chapter of the playlist
     */
    private void buildChapterViews() {
        for(Chapter chapter : playlist.getChapters()){
            ChapterView chapterView = new ChapterView(chapter, this);
            chapterView.add(Box.createVerticalStrut(20));
            add(chapterView);
        }
    }

    /**
     * notifies player manager service of click event
     * @param comment comment for which view was clicked
     */
    public void clicked(AudioComment comment){
        if(this.current.equals(comment)){
            // notify that same comment was clicked (play pause then)
            project.getService(PlayerManagerService.class).notify(new UIEvent(UIEventType.LIST_CLICKED_SAME, ""));
        }else{
            // notify comment was clicked (set comment as current)
            this.current = comment;
            project.getService(PlayerManagerService.class).notify(new UIEvent(UIEventType.LIST_CLICKED, ""));
        }
    }

    /**
     * notifies player manager service of play-pause-icon click event
     */
    public void playPauseClicked(){
        project.getService(PlayerManagerService.class).notify(new UIEvent(UIEventType.PLAY_PAUSE_CLICKED, ""));
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
        buildView();
    }

    public AudioComment getCurrent() {
        return current;
    }

    public void setCurrent(AudioComment current) {
        this.current = current;
        resetCommentViews();
    }

    public int getYOfCurrentCommentView() {
        int y = 0;
        ArrayList<ChapterView> chapterViews = getAllChapterViews();
        for(ChapterView chapterView : chapterViews){
            CommentView commentView = chapterView.findViewForComment(this.current);
            if(commentView != null){
                return y + commentView.getY();
            }
            y += chapterView.getHeight();
        }
        return 0;
    }

    /**
     * iterates through all chapterviews and searches for current comment to set to state "play"
     */
    public void playCurrent(){
        for(ChapterView view : getAllChapterViews()){
            view.playCurrent(current);
        }
    }

    /**
     * iterates through all chapterviews and searches for current comment to set to state "pause"
     */
    public void pauseCurrent(){
        for(ChapterView view : getAllChapterViews()){
            view.pauseCurrent(current);
        }
    }

    /**
     * extracts all chapter views from this view (without separators i.e.)
     * @return arraylist with all chapterviews
     */
    private ArrayList<ChapterView> getAllChapterViews(){
        ArrayList<ChapterView> views = new ArrayList<>();
        Component[] components = getComponents();
        for(Component component : components){
            if(component instanceof ChapterView){
                views.add((ChapterView) component);
            }
        }
        return views;
    }

    /**
     * resets all comment views in all chapters depending on current comment (which will be the only active after this)
     */
    private void resetCommentViews() {
        for(ChapterView view : getAllChapterViews()){
            view.resetCommentViews(current);
        }
    }

    public Project getProject() {
        return project;
    }
}
