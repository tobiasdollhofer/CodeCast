package de.tobiasdollhofer.codecast.player.service;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.fileEditor.impl.LoadTextUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.impl.java.stubs.index.JavaStubIndexKeys;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubIndexImpl;
import de.tobiasdollhofer.codecast.player.data.Playlist;
import de.tobiasdollhofer.codecast.player.util.FilePathUtil;
import de.tobiasdollhofer.codecast.player.util.PlaylistLoader;

import java.util.ArrayList;
import java.util.Collection;
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
        loadPlaylistFromComments();
    }

    private void loadPlaylistFromComments() {
        System.out.println("Load Playlist From Comments");


        Collection<VirtualFile> files = FilenameIndex.getAllFilesByExt(project, "java", GlobalSearchScope.projectScope(project));
        ArrayList<VirtualFile> codecastFiles = getAllCodecastFiles(files);

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
