package de.tobiasdollhofer.codecast.player.service;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.fileEditor.impl.LoadTextUtil;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.java.stubs.index.JavaStubIndexKeys;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubIndexImpl;
import com.intellij.util.indexing.FileBasedIndex;
import de.tobiasdollhofer.codecast.player.data.AudioComment;
import de.tobiasdollhofer.codecast.player.data.AudioCommentType;
import de.tobiasdollhofer.codecast.player.data.Playlist;
import de.tobiasdollhofer.codecast.player.util.FilePathUtil;
import de.tobiasdollhofer.codecast.player.util.PlaylistLoader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PlaylistServiceImpl implements PlaylistService{

    private final Project project;
    private Playlist playlist;

    public PlaylistServiceImpl(Project project) {
        this.project = project;
        loadPlaylist();
    }

    @Override
    public Playlist getPlaylist() {
        return playlist;
    }

    @Override
    public void loadPlaylist() {
        playlist = PlaylistLoader.loadFromMetaFile(FilePathUtil.getCodeCastMetaPath(project));
        DumbService.getInstance(project).runWhenSmart(new Runnable() {
            @Override
            public void run() {
                loadPlaylistFromComments();
            }
        });

    }

    private void loadPlaylistFromComments() {
        System.out.println("Load Playlist From Comments");
        Collection<VirtualFile> files = FilenameIndex.getAllFilesByExt(project, "java", GlobalSearchScope.projectScope(project));
        ArrayList<VirtualFile> codecastFiles = getAllCodecastFiles(files);
        ArrayList<AudioComment> comments = new ArrayList<>();
        for(VirtualFile file : codecastFiles){
            comments.addAll(getCommetsFromFile(file));
        }
        Playlist playlist = createPlaylistFromComments(comments);
        System.out.println(playlist.toString());
    }

    private Playlist createPlaylistFromComments(ArrayList<AudioComment> comments) {
        return new Playlist();
    }

    private ArrayList<AudioComment> getCommetsFromFile(VirtualFile file) {
        ArrayList<AudioComment> comments = new ArrayList<>();
        String text = LoadTextUtil.loadText(file).toString();
        String[] commentSplit = text.split("@codecast");
        for(String comment : commentSplit){
            String commentLower = comment.toLowerCase(Locale.ROOT);
            if(commentLower.contains("@chapter") && commentLower.contains("@title") && commentLower.contains("@position")
                    && commentLower.contains("@url") && commentLower.contains("@type")){
                comments.add(getCommentFromTextBlock(comment));
            }
        }
        return comments;
    }

    private AudioComment getCommentFromTextBlock(String text){
        String chapter = getValueAfterAnnotation("@chapter", text);
        String title = getValueAfterAnnotation("@title", text);
        String position = getValueAfterAnnotation("@position", text);
        String url = getValueAfterAnnotation("@url", text);
        String type = getValueAfterAnnotation("@type", text);

        AudioCommentType commentType;
        if(type.equals("")){
            commentType = AudioCommentType.DEFAULT;
        }else{
            commentType = AudioCommentType.valueOf(type.toUpperCase(Locale.ROOT));
        }
        /*System.out.println(commentType);
        System.out.println("chapter: " + chapter);
        System.out.println("title: " + title);
        System.out.println("position: " + position);
        System.out.println("url: " + url);
        System.out.println("type: " + type);*/
        return new AudioComment("title", "path", 123456789, AudioCommentType.DEFAULT);
    }

    private String getValueAfterAnnotation(String annotation, String text){
        String value = "";

        String rawValue = text.substring(text.indexOf(annotation) + annotation.length());
        rawValue = rawValue.split("\\r?\\n")[0];
        value = rawValue.trim();
        System.out.println("annotation: " + annotation + ", value: " + value);
        return value;
    }

    private ArrayList<VirtualFile> getAllCodecastFiles(Collection<VirtualFile> javaFiles){
        ArrayList<VirtualFile> codecastFiles = new ArrayList<>();
        for(VirtualFile file : javaFiles){
            CharSequence text = LoadTextUtil.loadText(file);
            String textString = new String(text.toString());

            if(textString.contains("@codecast")){
                codecastFiles.add(file);
            }
        }
        return codecastFiles;
    }

    @Override
    public void emptyPlaylist() {
        playlist = null;
    }
}
