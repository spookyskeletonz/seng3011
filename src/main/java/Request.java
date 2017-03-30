/**
 * Created by arunavsarkar on 29/3/17.
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

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
        System.out.println(instrumentID+"|"+topicCode+"|"+startDate+"|"+endDate);
        String query = "PREFIX w3: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX fe: <http://adage.cse.unsw.edu.au/ontology/financial-events#>\n" +
                "PREFIX ins: <http://adage.cse.unsw.edu.au/resource/financial-events#>\n" +
                "PREFIX xs: <http://www.w3.org/2001/XMLSchema#>\n" +
                "\n" +
                "SELECT ?s ?id ?time ?headline ?newsBody\n" +
                "WHERE {\n" +
                "?s w3:type fe:TRTHNewsEvent.\n" +
                "?s fe:messageId ?id.\n" +
                "?s fe:relatedRIC ?ric.\n" +
                "?s fe:timeStamp ?t.\n" +
                "?t fe:startTime ?time.\n" +
                "?s fe:newsText ?newsBody.\n" +
                "?s fe:headLine ?headline.\n" +
                "?s fe:topicCode ?topicCode.\n" +
                "?s fe:languageOfNews ?lang.\n" +
                "?s fe:languageOfNews \"en\".\n" +
                "FILTER (?ric = ins:RIC_"+instrumentID+")\n" +
                "FILTER (?topicCode = \"N2:"+topicCode+"\")\n" +
                "FILTER(xs:dateTime(?time) > "+startDate+"^^xs:dateTime && xs:dateTime(?time) <= "+endDate+"^^xs:dateTime)\n" +
                "}";

        /*Authenticator.setDefault (new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication ("student", "studentML".toCharArray());
            }
        });*/

        try {
            String encodedQuery = URLEncoder.encode(query, "UTF-8");
            String url = "http://adage.cse.unsw.edu.au:8005/v1/graphs/sparql?query="+encodedQuery;
            URL obj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            String userpass = "student:studentML";
            String encoded = new sun.misc.BASE64Encoder().encode(userpass.getBytes());
            conn.setRequestProperty("Authorization", "Basic " + encoded);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/sparql-results+json");

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
