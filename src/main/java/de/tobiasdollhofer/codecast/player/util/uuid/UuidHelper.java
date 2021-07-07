package de.tobiasdollhofer.codecast.player.util.uuid;


import java.io.*;
import java.util.UUID;

/**
 * helper class to get a uuid for csv logging file
 */
public class UuidHelper {

    private static String CODECAST_ROOT = System.getProperty("user.home") + "/" + "codecast" + "/";
    private static String UUID_FILE_PATH = CODECAST_ROOT + "uuid.txt";


    private UUID uuid;

    private static UuidHelper instance;

    private UuidHelper(){
        uuid = loadUuidFromUserHome();
        if(uuid == null){
            createNewUuid();
        }
    }

    /**
     *
     * @return uuid of this user
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     *
     * @return uuid.UuidHelper instance
     */
    public static UuidHelper getInstance(){
        if(UuidHelper.instance == null){
            UuidHelper.instance = new UuidHelper();
        }
        return UuidHelper.instance;
    }


    /**
     * creates new uuid and saves it to codecast directory as uuid.txt
     */
    private void createNewUuid() {
        this.uuid = UUID.randomUUID();
        try {
            File uuidFile = new File(UUID_FILE_PATH);

            BufferedWriter writer = new BufferedWriter(new FileWriter(uuidFile));
            writer.write(uuid.toString());

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * checks if there plugin was already initialized with uuid and loads it from uuid.txt file
     * @return uuid of user or null of not already initialized
     */
    private UUID loadUuidFromUserHome() {
        // check if there is a codecast directory
        File codecastRoot = new File(CODECAST_ROOT);
        if(!codecastRoot.exists()) {
            // create codecast directory for new uuid creation
            codecastRoot.mkdir();
            return null;
        }

        // check if there is a uuid file to extract
        File uuidFile = new File(UUID_FILE_PATH);
        if(!uuidFile.exists()) return null;

        return readUuid();
    }

    /**
     * reads uuid from uuid.txt file and returns it
     * @return uuid or null if not already initialized
     */
    private UUID readUuid() {
        File uuidFile = new File(UUID_FILE_PATH);
        try{
            BufferedReader reader = new BufferedReader(new FileReader(uuidFile));
            String line = "";
            if((line = reader.readLine()) != null){
                UUID uuid = UUID.fromString(line);
                return uuid;
            }
        }catch (IOException | IllegalArgumentException e){
            e.printStackTrace();
        }

        return null;
    }

}
