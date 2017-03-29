/**
 * Created by arunavsarkar on 29/3/17.
 */

import java.net.MalformedURLException;
import java.util.*;
import java.io.*;
import java.net.*;

public class Request {

    private String startDate;
    private String endDate;
    private String instrumentID;
    private String topicCode;

    public Request(String startDate, String endDate, String instrumentID, String topicCode){
        this.startDate = startDate;
        this.endDate = endDate;
        this.instrumentID = instrumentID;
        this.topicCode = topicCode;
    }

    public String makeRequest(){
        StringBuilder result = new StringBuilder();
        String query = "PREFIX\tw3:\t<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX\tfe:\t<http://adage.cse.unsw.edu.au/ontology/financial-events#>\n" +
                "PREFIX\tins:\t<http://adage.cse.unsw.edu.au/resource/financial-events#>\n" +
                "PREFIX\txs:\t<http://www.w3.org/2001/XMLSchema#>\n" +
                "SELECT\t?s\t?time\t?headline\t?newsBody\t\n" +
                "WHERE\t{\n" +
                "?s\tw3:type\tfe:TRTHNewsEvent.\n" +
                "?s\tfe:relatedRIC\t?ric.\n" +
                "?s\tfe:timeStamp\t?t.\n" +
                "?t\tfe:startTime\t?time.\n" +
                "?s\tfe:newsText\t?newsBody.\n" +
                "?s\tfe:headLine\t?headline.\n" +
                "?s\tfe:topicCode\t?topicCode.\n" +
                "?s\tfe:languageOfNews\t\"en\".\n" +
                "\t\tFILTER\t(?ric\t=\t"+instrumentID+")\n" +
                "\t\tFILTER\t(?topicCode\t=\t\""+topicCode+"\")\n" +
                "\t\tFILTER(xs:dateTime(?time)\t>\t\""+startDate+"\"^^xs:dateTime\t&&\txs:dateTime(?time)\t<=\t\n" +
                "\""+endDate+"\"^^xs:dateTime)\n" +
                "}";

        try {
            String encodedQuery = URLEncoder.encode(query, "UTF-8");
            String url = "http://adage.cse.unsw.edu.au:8005/v1/graphs/sparql?query=”"+encodedQuery+"”";
            URL obj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR: check exception stack trace";
        }
    }

}
