import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

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
        API a = new API();
        ArrayList<Request> requests = new ArrayList<Request>();
        a.startDate = args[0];
        a.endDate = args[1];
        if(a.startDate == "NULL" || a.endDate == "NULL"){
            System.out.println("Please enter start/end date");
            return;
        }
        a.instrumentIDs = args[2];
        a.topicCodes = args[3];
        if(a.instrumentIDs == "-") a.instrumentIDs = null;
        if(a.topicCodes == "-") a.topicCodes = null;
        requests.add(new Request(a.startDate, a.endDate, a.instrumentIDs, a.topicCodes));
        for (Request r : requests) {
                System.out.println(r.makeRequest());
            }
    }

}