package de.tobiasdollhofer.codecast.player.util;

import com.intellij.openapi.fileEditor.impl.LoadTextUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import de.tobiasdollhofer.codecast.player.data.AudioComment;
import de.tobiasdollhofer.codecast.player.data.AudioCommentType;
import de.tobiasdollhofer.codecast.player.data.Chapter;
import de.tobiasdollhofer.codecast.player.data.Playlist;
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

    public static Playlist loadPlaylistFromComments(Project project) {
        System.out.println("Load Playlist From Comments");
        Collection<VirtualFile> files = FilenameIndex.getAllFilesByExt(project, "java", GlobalSearchScope.projectScope(project));
        ArrayList<VirtualFile> codecastFiles = getAllCodecastFiles(files);
        ArrayList<AudioComment> comments = new ArrayList<>();
        for(VirtualFile file : codecastFiles){
            comments.addAll(getCommetsFromFile(file));
        }
        Playlist playlist = createPlaylistFromComments(comments);
        System.out.println(playlist.toString());
        return playlist;
    }

    private static Playlist createPlaylistFromComments(ArrayList<AudioComment> comments) {
        return new Playlist();
    }

    private  static ArrayList<AudioComment> getCommetsFromFile(VirtualFile file) {
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

    private static AudioComment getCommentFromTextBlock(String text){
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
        AudioComment comment = new AudioComment(title, commentType);
        comment.setUrl(url);
        comment.setPosition(position);
        comment.setChapter(chapter);
        System.out.println(comment);
        return comment;
    }

    private static String getValueAfterAnnotation(String annotation, String text){
        String value = "";

        String rawValue = text.substring(text.indexOf(annotation) + annotation.length());
        rawValue = rawValue.split("\\r?\\n")[0];
        value = rawValue.trim();
        //System.out.println("annotation: " + annotation + ", value: " + value);
        return value;
    }

    private static ArrayList<VirtualFile> getAllCodecastFiles(Collection<VirtualFile> javaFiles){
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
}
