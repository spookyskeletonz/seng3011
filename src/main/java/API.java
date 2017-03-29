import java.io.FileNotFoundException;
import java.io.FileReader;
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

        Scanner sc = null;
        try
        {
            sc = new Scanner(new FileReader(args[0]));
        }
        catch (FileNotFoundException e) {}

        while (sc.hasNext()) {
            String line = sc.nextLine();
            String[] temp1 = line.split("\\?", 2);
            String[] temp2 = temp1[1].split("&");

            String[] input = new String[5];
            int x = 0;
            for (String s : temp2) {
                input[x] = s.split("=")[1];
                if (input[x].contains(",")) {
                    a.error();
                }
                x++;
            }
            a.method = input[0];
            a.startDate = input[1];
            a.endDate = input[2];
            a.instrumentID = input[3];
            a.topicCodes = input[4];
        }

        //Request r = new Request(m.startDate, m.endDate, m.instrumentID, m.topicCodes);
    }

    void error() {
        System.out.println("Too many Instrument IDs or Topic Codes, max is 1");
        System.exit(0);
    }

}