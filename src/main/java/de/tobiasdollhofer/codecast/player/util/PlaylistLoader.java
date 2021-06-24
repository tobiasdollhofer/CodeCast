package de.tobiasdollhofer.codecast.player.util;

import com.intellij.openapi.fileEditor.impl.LoadTextUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import de.tobiasdollhofer.codecast.player.CommentPlayer;
import de.tobiasdollhofer.codecast.player.data.AudioComment;
import de.tobiasdollhofer.codecast.player.data.AudioCommentType;
import de.tobiasdollhofer.codecast.player.data.Chapter;
import de.tobiasdollhofer.codecast.player.data.Playlist;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

public class PlaylistLoader {

    /**
     * TODO cleanup
     * Method loads xml and provides the complete playlist
     * @param path String value of path to meta file
     * @return loaded Playlist-Object
     */
    public static Playlist loadFromMetaFile(String path){
        Playlist playlist = new Playlist();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

       try(InputStream is = new FileInputStream(path)){
           DocumentBuilder db = dbf.newDocumentBuilder();

           Document document = db.parse(is);
           Element root = document.getDocumentElement();
           NodeList chapters = root.getElementsByTagName("chapter");

           // iterate through all "chapter"-nodes inside the root node of the xml and load them
           for(int i = 0; i < chapters.getLength(); i++){
               loadChapter(chapters.item(i), playlist);
           }


       }catch(ParserConfigurationException | SAXException | IOException e){
            e.printStackTrace();
       }
       System.out.println(playlist.toString());
       return playlist;
    }

    /**
     * Method loads a single chapter from the xml
     * @param chapterItem Node from one single chapter
     * @param playlist existing playlist object where chapter will be added
     */
    private static void loadChapter(Node chapterItem, Playlist playlist) {
        Chapter chapter = new Chapter();

        // create list with all nodes inside the chapter node and iterate through it.
        // Setting title or load comment depending on node type
        NodeList nodeList = chapterItem.getChildNodes();
        for(int i = 0; i < nodeList.getLength(); i++){
            String nodeName = nodeList.item(i).getNodeName().toLowerCase(Locale.ROOT);
            if(nodeName.equals("title")){
                chapter.setTitle(nodeList.item(i).getTextContent());
            }
            if(nodeName.equals("comment")){
                loadComment(nodeList.item(i), chapter);
            }
        }

        playlist.addChapter(chapter);
    }

    /**
     * Method loads one single comment and adds it to chapter
     * @param commentItem Node with one single comment
     * @param chapter Existing chapter object where comment will be added
     */
    private static void loadComment(Node commentItem, Chapter chapter) {

        // Cast node to element for better selection
        if(commentItem.getNodeType() == Node.ELEMENT_NODE){
            Element element = (Element) commentItem;

            // init values for comment
            AudioCommentType type = AudioCommentType.DEFAULT;
            String title = "";
            String path = "";
            long id = 0;

            // get comment type from xml. If no value was added, it will be DEFAULT comment type
            String commentType = element.getAttribute("type").toUpperCase(Locale.ROOT);
            if(commentType != null && !commentType.equals("")){
                type = AudioCommentType.valueOf(commentType);
            }

            // get title from the comment
            NodeList nodeList = element.getElementsByTagName("title");
            if(nodeList.getLength() > 0){
                title = nodeList.item(0).getTextContent();
            }

            // get path from the comment
            nodeList = element.getElementsByTagName("url");
            if(nodeList.getLength() > 0){
                path = nodeList.item(0).getTextContent();
            }

            // get id from the comment
            String idValue = element.getAttribute("id");
            if(idValue != null && !idValue.equals("")){
                id = Long.parseLong(element.getAttribute("id"));
            }

            // only create a comment and add it to the chapter object if there are no default values except comment type
            if(!title.equals("") && !path.equals("") && id != 0) {
                AudioComment comment = new AudioComment(title, type);
                chapter.addComment(comment);
            }
        }


    }

    /**
     * Method generates Playlist from project comment annotations
     * @param project current project
     * @return Playlist
     */
    public static Playlist loadPlaylistFromComments(Project project) {
        System.out.println("Load Playlist From Comments");

        // find all java project files
        Collection<VirtualFile> files = FilenameIndex.getAllFilesByExt(project, "java", GlobalSearchScope.projectScope(project));

        // find all files with @codecast annotation
        ArrayList<VirtualFile> codecastFiles = getAllCodecastFiles(files);

        // extract all comments from codecast files
        ArrayList<AudioComment> comments = new ArrayList<>();
        for(VirtualFile file : codecastFiles){
            comments.addAll(getCommetsFromFile(file, project));
        }

        // build playlist on using all comments
        return createPlaylistFromComments(project, comments);
    }

    /**
     * Method generates playlist from provided comments list
     * @param comments extracted comments list
     * @return playlist
     */
    private static Playlist createPlaylistFromComments(Project project, ArrayList<AudioComment> comments) {
        Playlist playlist = new Playlist();

        // add all comments to playlist (sorting will be handled by playlist and their chapters)
        for(AudioComment comment : comments){
            playlist.addComment(comment);
        }

        DownloadUtil.downloadComments(project, playlist);
        System.out.println(playlist);
        return playlist;
    }

    /**
     * Method creates list with all comments found in this file
     * @param file virtual file to look at
     * @return arraylist with comments
     */
    private static ArrayList<AudioComment> getCommetsFromFile(VirtualFile file, Project project) {
        ArrayList<AudioComment> comments = new ArrayList<>();
        String text = LoadTextUtil.loadText(file).toString();
        PsiFile psi = PsiManager.getInstance(project).findFile(file);
        @NotNull Collection<PsiComment> psiComments = PsiTreeUtil.findChildrenOfType(psi, PsiComment.class);
        // split text comment of file on every @codecast-annotation
        //String[] commentSplit = text.split("@codecast");
        for(PsiComment comment : psiComments){
            //String commentLower = comment.toLowerCase(Locale.ROOT);
            // check if comment is complete
            /*if(commentLower.contains("@chapter") && commentLower.contains("@title") && commentLower.contains("@position")
                    && commentLower.contains("@url") && commentLower.contains("@type")){
                PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
                comments.add(getCommentFromTextBlock(project, comment, psiFile));
            }*/
            if(comment.getText().contains("@codecast") && comment.getText().contains("@url")){
                addCommentFromTextBlock(project, comment.getText(), psi, comments);
            }
        }
        return comments;
    }

    private static void addCommentFromTextBlock(Project project, String textBlock, PsiFile file, ArrayList<AudioComment> list){
        AudioComment comment = getCommentFromTextBlock(project, textBlock, file);

        if(comment != null){
            list.add(comment);
        }
    }

    /**
     * Method creates single content from a textblock which codecast-comment completeness was already checked
     * @param text textblock which codecast-comment completeness was already checked
     * @return single audio comment
     */
    private static AudioComment getCommentFromTextBlock(Project project, String text, PsiFile file){
        AudioComment comment = getCommentFromTextBlock(text);
        comment.setFile(file);
        return comment;
    }

    /**
     * Method creates single content from a textblock which codecast-comment completeness was already checked
     * @param textBlock textblock which codecast-comment completeness was already checked
     * @return single audio comment
     */
    public static AudioComment getCommentFromTextBlock(String textBlock){
        String rawInfos = getValueAfterAnnotation("@codecast", textBlock);
        String url = getValueAfterAnnotation("@url", textBlock);

        // return if no values for annotations were found
        if(rawInfos.equals("") || url.equals("")) return null;

        // split codecast-infos on pipe -> ["X. Chapter title", " X. comment title (type)"]
        String[] rawInfoSplit = rawInfos.split("\\|");

        // check if info is complete
        if(rawInfoSplit.length != 2) return null;

        String chapter = rawInfoSplit[0].trim();

        // split before comment type -> ["X. comment title", "type)"]
        String[] rawTitleSplit = rawInfoSplit[1].split("\\(");
        String title = rawTitleSplit[0].trim();

        AudioCommentType type;
        try{
            // take everything from after the opening bracket
            String rawCommentValue = rawTitleSplit[1];
            // remove closing bracket and get depending comment type: "type)" -> AudioCommentType.TYPE
            type = AudioCommentType.valueOf(rawCommentValue.replace("\\)", "").trim().toUpperCase(Locale.ROOT));
        }catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e){
            // use default type if annotated type was not found or no type was annotated
            type = AudioCommentType.DEFAULT;
        }

        AudioComment comment = new AudioComment(title, type);
        comment.setChapter(chapter);
        comment.setUrl(url);
        return comment;
    }

    /**
     * Method extracts value after annotation in a string
     * @param annotation Annotation which value has to be extracted
     * @param text text to search for value of annotation
     * @return string value
     */
    private static String getValueAfterAnnotation(String annotation, String text){
        String value = "";

        // cut off whole text before the actual value of the annotation
        String rawValue = text.substring(text.indexOf(annotation) + annotation.length());

        // cut off whole text after linebreak
        rawValue = rawValue.split("\\r?\\n")[0];

        // remove leading whitespaces
        value = rawValue.trim();
        return value;
    }

    /**
     * Method searches for files with codecast annotations
     * @param javaFiles collection of virtual javafiles
     * @return arraylist with virtualfiles with codecast annotations
     */
    private static ArrayList<VirtualFile> getAllCodecastFiles(Collection<VirtualFile> javaFiles){
        ArrayList<VirtualFile> codecastFiles = new ArrayList<>();

        // check for each file if there is a codecast annotation in there
        for(VirtualFile file : javaFiles){
            CharSequence text = LoadTextUtil.loadText(file);
            String textString = text.toString();

            if(textString.contains("@codecast")){
                codecastFiles.add(file);
            }
        }
        return codecastFiles;
    }
}
