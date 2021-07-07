package de.tobiasdollhofer.codecast.player.util.logging;

import com.opencsv.CSVWriter;
import de.tobiasdollhofer.codecast.player.util.event.ui.UIEventType;
import de.tobiasdollhofer.codecast.player.util.uuid.UuidHelper;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class CsvLogger {

    private static String CSV_FILENAME = "log_" + UuidHelper.getInstance().getUuid().toString() + ".csv";
    private static String CSV_PATH = System.getProperty("user.home") + "/" + "codecast" + "/" + CSV_FILENAME;

    private static String HOST = "tobiasdollhofer.de";
    private static String CODECAST_USER = "codecast";
    private static String CODECAST_ACCESS = "C0d3C4$t";
    /**
     * stores message with timestamp and uuid to csv
     * @param message
     */
    public static void log(Context context, UIEventType type, String message){
        try{
            File csv = new File(CSV_PATH);

            FileWriter fileWriter = new FileWriter(csv, true);

            String date = getCurrentDate();
            String time = getCurrentTime();
            CSVWriter csvWriter = new CSVWriter(fileWriter);
            String[] data = {date, time, context.toString(), type.toString(),message};
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

}
