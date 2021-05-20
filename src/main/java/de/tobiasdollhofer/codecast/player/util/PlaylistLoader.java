package de.tobiasdollhofer.codecast.player.util;

import de.tobiasdollhofer.codecast.player.data.Playlist;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PlaylistLoader {

    public static Playlist loadFromMetaFile(String path){
        Playlist playlist = new Playlist();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

       try(InputStream is = new FileInputStream(path)){
           DocumentBuilder db = dbf.newDocumentBuilder();

           Document document = db.parse(is);
           System.out.println("Root Element :" + document.getDocumentElement().getNodeName());

        }catch(ParserConfigurationException | SAXException | IOException e){
            e.printStackTrace();
        }

        return playlist;
    }
}
