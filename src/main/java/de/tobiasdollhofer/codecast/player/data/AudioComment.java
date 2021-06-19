package de.tobiasdollhofer.codecast.player.data;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import org.apache.commons.io.FilenameUtils;

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
    private String position;
    private PsiFile file;

    public AudioComment(String title, AudioCommentType type) {
        this.title = title;
        this.url = "";
        this.chapter = "";
        this.position = "";
        this.type = type;
        this.downloaded = false;
    }

    public String getTitle() {
        return title;
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

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public PsiFile getFile() {
        return file;
    }

    public void setFile(PsiFile file) {
        this.file = file;
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
        return title.equals(comment.title) && Objects.equals(url, comment.url) && type == comment.type && Objects.equals(chapter, comment.chapter) && Objects.equals(position, comment.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, downloaded, url, type, chapter, position);
    }

    @Override
    public String toString() {
        return "AudioComment{" +
                "title='" + title + '\'' +
                ", downloaded=" + downloaded +
                ", url='" + url + '\'' +
                ", type=" + type +
                ", chapter='" + chapter + '\'' +
                ", position='" + position + '\'' +
                '}';
    }
}
