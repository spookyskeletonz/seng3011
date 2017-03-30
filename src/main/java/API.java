import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by geokinetic on 29/03/17.
 */
public class API {
    private String method;
    private String startDate;
    private String endDate;
    private String instrumentID;
    private String topicCodes;

    public API() {}

    public static void main(String[] args) {
        API a = new API();
        ArrayList<Request> requests = new ArrayList<Request>();

        Scanner sc = null;
        try
        {
            sc = new Scanner(new FileReader(args[0]));
        }
        catch (FileNotFoundException e) {}

        while (sc.hasNext()) {
            String line = sc.nextLine();
            //String[] temp1 = line.split("\\?", 2);
            String[] temp2 = line.split("&");

            String[] input = new String[4];
            int x = 0;
            for (String s : temp2) {
                input[x] = s.split("=")[1];
                x++;
            }
            a.startDate = input[0];
            a.endDate = input[1];
            a.instrumentID = input[2];
            a.topicCodes = input[3];
            requests.add(new Request(a.startDate, a.endDate, a.instrumentID, a.topicCodes));
        }

        try {
            PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
            for (Request r : requests) {
                writer.println(r.makeRequest());
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error with document writer");
        }
    }

    void error() {
        System.out.println("Too many Instrument IDs or Topic Codes, max is 1");
        System.exit(0);
    }

}