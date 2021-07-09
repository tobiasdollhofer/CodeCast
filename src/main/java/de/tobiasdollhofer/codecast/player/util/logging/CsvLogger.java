package de.tobiasdollhofer.codecast.player.util.logging;

import com.opencsv.CSVWriter;
import de.tobiasdollhofer.codecast.player.util.event.ui.UIEventType;
import de.tobiasdollhofer.codecast.player.util.uuid.UuidHelper;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class CsvLogger {

    private static String CSV_FILENAME = UuidHelper.getInstance().getUuid().toString() + "_player.csv";
    private static String CSV_PATH = System.getProperty("user.home") + "/" + "codecast" + "/" + CSV_FILENAME;

    /**
     * stores message with timestamp and uuid to csv
     * @param message
     */
    public static void log(Context context, UIEventType type, String message){
        try{
            File csv = new File(CSV_PATH);

            FileWriter fileWriter = new FileWriter(csv, true);
            String[] data;
            if(csv.exists()){
                String uuid = UuidHelper.getInstance().getUuid().toString();
                String sessionId = UuidHelper.getInstance().getSessionId().toString();
                String date = getCurrentDate();
                String time = getCurrentTime();
                data = new String[]{uuid, sessionId, date, time, context.toString(), type.toString(), message};
            }else{
                data = new String[]{"uuid", "sessionId", "date", "time", "context", "eventtype", "message"};
            }
            CSVWriter csvWriter = new CSVWriter(fileWriter);
            System.out.println(Arrays.toString(data));
            csvWriter.writeNext(data);
            csvWriter.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }


    private static String getCurrentDate() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(new Date());
    }

    private static String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss:SSS");
        return format.format(new Date());
    }

    public static void logStartup(){
        log(Context.EDITOR, UIEventType.STARTUP, "New PLAYER-SESSION-ID: " + UuidHelper.getInstance().getSessionId());
    }
}
