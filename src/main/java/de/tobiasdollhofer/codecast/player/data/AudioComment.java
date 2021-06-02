package de.tobiasdollhofer.codecast.player.data;

import de.tobiasdollhofer.codecast.player.util.FilePathUtil;

import java.io.File;
import java.util.Objects;

public class AudioComment {

    private String title;
    private String path;
    private long id;
    private AudioCommentType type;

    public AudioComment(String title, String path, long id, AudioCommentType type) {
        this.title = title;
        this.path = path;
        this.id = id;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AudioCommentType getType() {
        return type;
    }

    public void setType(AudioCommentType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "AudioComment{" +
                "title='" + title + '\'' +
                ", path='" + path + '\'' +
                ", id=" + id +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AudioComment comment = (AudioComment) o;
        return id == comment.id && title.equals(comment.title) && Objects.equals(path, comment.path) && type == comment.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, path, id, type);
    }
}
