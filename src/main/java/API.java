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
            String s = ","+r.makeRequest();
            if (s.length() <= 50) {
                a.log(false, startTime);
            } else {
                a.log(true, startTime);
            }
            System.out.println(s);
        }
    }

    public void log (boolean success, long startTime) {
        String succeeded = "        \"Success\": \"false\",\n        \"Error\": \"Invalid input/No output for given search terms\"";
        if (success) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Long endTime = System.currentTimeMillis();
            succeeded = "        \"Success\": \"true\",\n        \"Start Time\": \"" + sdf.format(startTime) + "\",\n        \"End Time\": \"" + sdf.format(endTime) + "\",\n        \"Elapsed Time\": \"" + (endTime-startTime) + " ms\"";
        }
        String file = "{\n    \"Log\": {\n        \"Developer\": \"It's Gif Not Gif\",\n        \"Version\": \"2.0\",\n        \"Parameters\": {\n            \"Start Date\": \"" + startDate + "\",\n            \"End Date\": \"" + endDate + "\",\n            \"Instrument IDs\": \"" + instrumentIDs + "\",\n            \"Topic Codes\": \"" + topicCodes + "\"\n        }\n" + succeeded + ",\n        \"Log File Name (if using JAR)\": \"log.txt\"\n    }\n}";
        System.out.println(file);
        try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get("./log.txt"));
            writer.write(file);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}