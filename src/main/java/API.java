import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by geokinetic on 29/03/17.
 */
public class API {
    private String startDate;
    private String endDate;
    private String instrumentIDs;
    private String topicCodes;

    public API() {}

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        API a = new API();
        ArrayList<Request> requests = new ArrayList<Request>();
        a.startDate = args[0];
        a.endDate = args[1];
        if(a.startDate.equals("NULL") || a.endDate.equals("NULL")){
            System.out.println("Please enter start/end date");
            return;
        }
        a.instrumentIDs = args[2];
        a.topicCodes = args[3];
        if(a.instrumentIDs.equals("-")) a.instrumentIDs = null;
        if(a.topicCodes.equals("-")) a.topicCodes = null;
        requests.add(new Request(a.startDate, a.endDate, a.instrumentIDs, a.topicCodes));
        for (Request r : requests) {
                System.out.println(r.makeRequest());
                a.log(true, startTime);
            }
    }

    public void log (boolean success, long startTime) {
        String succeeded = "Error";
        if (success) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy, HH:mm");
            Long endTime = System.currentTimeMillis();
            succeeded = "Success\nStart Time = " + sdf.format(startTime) + "\nEnd Time:" + sdf.format(endTime) + "\nElapsed Time:" + (endTime-startTime) + "\nOutput file: log.txt";
        }
        String file = "It's Gif Not Gif\nVersion 2.0\n" + succeeded;

        try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get("./log.txt"));
            writer.write(file);
        } catch (Exception e) {}
    }

}