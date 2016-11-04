package forex_livedata;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.sf.json.*;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author John Filipowicz
 */
public class Data {
    private static final String acessKey = "?access_key=cdf87f37ab7d61045f9e62e788693407";
    private static final String baseURL = "http://apilayer.net/api";
    private String endPoint;
    private JSONObject jsonObj;
    private String reflectURL;
    private int historyRange;
    
    /**
     * Initializes a data object with the desired end point
     * @param _endPoint 
     */
    public Data (String _endPoint) {
        historyRange = 10;
        
        if(_endPoint.equalsIgnoreCase(""))
            endPoint = "/live";
        else
            endPoint = _endPoint;
    }
    
    /**
     * Sets the request type (e.g. live || historical)
     * @param end
     * @throws Exception 
     */
    public void setEndPoint(String end) throws Exception{
        end = end.toLowerCase();
        
        // Using a switch statement due to possibility of expanding the application to do more
        switch(end){
            case "live":
                endPoint = "/live";
                break;
            case "historical":
                endPoint = "/historical";
                break;
        }
    }
    
    /**
     * Gets Json source currency code
     * @return source
     */
    public Object getJSonSource(){
        return jsonObj.get("source");
    }
    
    /**
     * Gets Json quote currency codes
     * @return quotes
     */
    public Object getJSonQuotes() {
        return jsonObj.get("quotes");
    }
    
    /**
     * Sets number of ratios to request from history
     * @param _historyRange 
     */
    public void setHistRange(int _historyRange) {
        historyRange = _historyRange;
    }
    
    /**
     * Gets current number of ratios to be requested from history
     * @return historyRange
     */
    public int getHistRange() {
        return historyRange;
    }
    
    /**
     * Initialize the json object field with the specifications of the parameters
     * @param currencies currencies to compare the source to
     * @param source currency to compare to others
     * @param format boolean for formatting vs one line output
     * @return quotes json tag
     */
    public String GetRequest(String source, String currencies, boolean format) {
        String output = "";
        String url = baseURL + endPoint + acessKey;
        url += currencies + source;
        if(format){
            url += "&format=1";
        }
        
    System.out.println(url);
        
        String jsonStr = webUrl(url);
        jsonObj = JSONObject.fromObject(jsonStr);
        
        output = jsonObj.getString("quotes");
        
        return output;
    }
    
    public String GetJsonHistoric (String source, String currency) {
        String url = "", output = "", dateStr = "";
        int tempDay = 0;
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        
        dateStr = dateFormat.format(date);
        
        for(int i = 0; i < dateStr.length(); i++){
            if(dateStr.charAt(i) == '/')
                dateStr = dateStr.substring(0, i) + "-" + dateStr.substring(i + 1);
        }
        
        
        url = baseURL + "/historical" + acessKey + "&date=" + dateStr + "&source=" + source + "&currencies=" + currency + "&format=1";
        url = webUrl(url);
        JSONObject json = JSONObject.fromObject(url);
        output = json.get("quotes").toString();
        
        return output;
    }
    
    /**
     * Produce the full list of supported currencies as a string
     * @return formatted output of the list
     */
    public String GetJsonApprovedList() {
        String url = baseURL + "/list" + acessKey + "&format=1";
        String formatted = "";
        url = webUrl(url);
        JSONObject json = JSONObject.fromObject(url);
        formatted = json.get("currencies").toString();
        
        for (int i = 0; i < formatted.length(); i++){
            if(formatted.charAt(i) == ',')
                formatted = formatted.substring(0, i - 1) + "\n" + formatted.substring(i + 1);
        }
        
        return formatted;
    }
    
    /**
     * Helper method to format the api request url
     * @param url
     * @return formatted url as a string
     */
    private static String webUrl(String url) {
        String result = "";
        URL service;
        
        try{
            service = new URL(url);
            result = IOUtils.toString(service, "UTF-8");
        
        }
        catch(MalformedURLException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return result;
    }
}
