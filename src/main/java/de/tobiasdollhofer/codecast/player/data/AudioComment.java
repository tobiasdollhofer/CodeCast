package de.tobiasdollhofer.codecast.player.data;

import com.intellij.openapi.project.Project;
import de.tobiasdollhofer.codecast.player.util.FilePathUtil;
import javafx.util.Duration;
import org.apache.commons.io.FilenameUtils;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import java.io.File;
import java.util.Objects;

/**
 * Single audio comment entity
 */
public class AudioComment {

    private String title;
    private boolean downloaded;
    private String url;
    private AudioCommentType type;
    private String chapter;
    private Duration duration;

    public AudioComment(String title, AudioCommentType type) {
        this.title = title;
        this.url = "";
        this.chapter = "";
        this.type = type;
        this.downloaded = false;
    }

    public String getTitle() {
        return title;
    }

    public String getTitleWithoutNumbers(){
        return title.replaceFirst("^[0-9]+[.]", "");
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public AudioCommentType getType() {
        return type;
    }

    public void setType(AudioCommentType type) {
        this.type = type;
    }

    public boolean isDownloaded() {
        return downloaded;
    }

    public void setDownloaded(boolean downloaded) {
        this.downloaded = downloaded;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getChapter() {
        return chapter;
    }

    public String getChapterWithoutNumbers(){
        return chapter.replaceFirst("^[0-9]+[.]", "");
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public Duration getDuration() {
        return duration;
    }

    /**
     * calculates and stores duration
     * will be executed after download of file is finished
     * @param project depending project used to get filepath where stored
     */
    public void calculateDuration(Project project){
        try{
            AudioFile audioFile = AudioFileIO.read(new File(FilePathUtil.getFilePathForComment(project, this)));
            this.duration = new Duration(audioFile.getAudioHeader().getTrackLength() * 1000);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Extracts the filename from a path
     * @return filename for the path
     */
    public String getFileName(){
        if(!url.equals("")){
            return FilenameUtils.getName(url);
        }
        return "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AudioComment comment = (AudioComment) o;
        return title.equals(comment.title) && Objects.equals(url, comment.url) && type == comment.type && Objects.equals(chapter, comment.chapter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, downloaded, url, type, chapter);
    }

    @Override
    public String toString() {
        return "AudioComment{" +
                "title='" + title + '\'' +
                ", downloaded=" + downloaded +
                ", url='" + url + '\'' +
                ", type=" + type +
                ", chapter='" + chapter + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }
}
