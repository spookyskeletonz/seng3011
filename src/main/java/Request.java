/**
 * Created by arunavsarkar on 29/3/17.
 */


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;

public class Request {

    private String startDate;
    private String endDate;
    private String instrumentIDs;
    private String topicCodes;

    public Request(String startDate, String endDate, String instrumentIDs, String topicCodes){
        this.startDate = startDate;
        this.endDate = endDate;
        this.instrumentIDs = instrumentIDs;
        this.topicCodes = topicCodes;
    }

    public String makeRequest(){
        String[] instrumentID = {null};
        if(instrumentIDs != null) {
            instrumentID = instrumentIDs.split(",");
        }
        String[] topicCode = {null};
        if(topicCodes != null){
            topicCode = topicCodes.split(",");
        }

        StringBuilder result = new StringBuilder();

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
                "?s fe:languageOfNews \"en\".\n";

        //this next logic will add the filters into the codes based on what instrument ID's and topic codes there are.
        //if there are no instrument ID's or topic codes it will not add any filter
        if(instrumentID[0] == null){
            if(topicCode[0] != null) {
                query += "FILTER (";
                for (int topicCount = 0; topicCount <= topicCode.length - 1; topicCount++) {
                    if(topicCount != 0) query += " || ";
                    query += "?topicCode = \"N2:"+topicCode[topicCount]+"\"";
                    if(topicCount == topicCode.length-1) query += ")\n";
                }
            }
        } else {
            query += "FILTER (";
            for (int instCount = 0; instCount <= instrumentID.length - 1; instCount++) {
                if(instCount != 0) query += " || ";
                query += "?ric = ins:RIC_"+instrumentID[instCount];
                if(instCount == instrumentID.length-1) query += ")\n";
            }
            if(topicCode[0] != null) {
                query += "FILTER (";
                for (int topicCount = 0; topicCount <= topicCode.length - 1; topicCount++) {
                    if(topicCount != 0) query += " || ";
                    query += "?topicCode = \"N2:"+topicCode[topicCount]+"\"";
                    if(topicCount == topicCode.length-1) query += ")\n";
                }
            }
        }

        query += "FILTER(xs:dateTime(?time) > \""+startDate+"\"^^xs:dateTime && xs:dateTime(?time) <= \""+endDate+"\"^^xs:dateTime)\n}";

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

            String strResult =  result.toString();

            strResult = strResult.replaceFirst("\\{\"head\":\\{\"vars\":\\[\"s\",\"id\",\"time\",\"headline\",\"newsBody\"\\]\\},\"results\":\\{\"bindings\":\\[", "{\n\"NewsDataSet\": [");
            strResult = strResult.replaceAll("\\\\n", "\n");
            strResult = strResult.replaceAll("\"type\":.*?:","");
            strResult = strResult.replaceAll("\\{\"s\":\\{.*?\"\\},","{" );
            strResult = strResult.replaceAll("\\{\"id\":\\{.*?\"\\}", "\n\\{\n\"InstrumentIDs\": \""+instrumentIDs +"\", \n\"Topic Codes\": \""+ topicCodes + "\"");
            strResult = strResult.replaceAll(",\"time\":\\{\"datatype\":\".*?\",\"",",\n\"TimeStamp\": \"");
            strResult = strResult.replaceAll("\\},\"headline\":\\{",",\n\"Headline\":");
            strResult = strResult.replaceAll("\\},\"newsBody\":\\{",",\n\"NewsText\": ");
            strResult = strResult.replaceAll("\\}\\},\n\\{\n\"InstrumentIDs\":","\n}\n{\n\"InstrumentIDs\":");
            strResult = strResult.replaceAll("\"\\}\\}\\]\\}\\}","\n]}");

            return strResult;
        } catch (Exception e) {
            return Arrays.toString(e.getStackTrace());
            //return "ERROR: check exception stack trace";
        }
    }

}
