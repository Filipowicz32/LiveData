package forex_livedata;

/**
 * 
 * @author John Filipowicz
 */
public class Controller {
    private String rawQuotes, finalQuotes;
    private Data data;
    
    public Controller(){
        data = new Data("");
    }
    
    /**
     * Sets request type
     * @param endpoint
     * @throws Exception 
     */
    public void setEndPoint (String endpoint) throws Exception{
        data.setEndPoint(endpoint);
    }
    
    public Double CreateHistoricComparison(String source, String quote){
        String histStr = data.GetJsonHistoric(source, quote);
        double histDouble = Double.parseDouble(histStr.substring(10, histStr.length() - 1));
        return histDouble;
    }
    
    /**
     * Retrieve json allowed currency list from data class
     * @return list
     */
    public String CreateAllowedCurrenciesList() {
        return data.GetJsonApprovedList();
    }
    
    /**
     * Create Raw Quotes from the json request
     * @param source source currency
     * @param quotes desired compared currencies
     */
    public void CreateRawQuotes(String source, String quotes) {
        rawQuotes = data.GetRequest(source, quotes, true);
    }
    
    /**
     * Forms a formatted output from the raw output parameter
     * @param raw raw output
     */
    public void FormatOutput(String raw) {
        int last = 0;
        finalQuotes = "";
        
        for(int i = 0; i < raw.length(); i++){
            if(raw.charAt(i) == ','){
                finalQuotes += raw.substring(last + 1, i - 1) + "\n";
                last = i;
            }
            else if (i == raw.length() - 1){
                finalQuotes += raw.substring(last + 1, i - 1);
            }
        }
    }
    
    /**
     * Returns field finalQuotes
     * @return final quotes
     */
    public String getFinalQuotes() {
        return finalQuotes;
    }
    
    /**
     * Returns field rawQuotes
     * @return raw quotes
     */
    public String getRawQuotes() {
        return rawQuotes;
    }
    
    /**
     * Determines if the string is under proper formatting for the api call (/[a-z]{3}/i)
     * @param str
     * @throws Exception 
     */
    public void Validate(String str) throws Exception{
        boolean valid = true;
        Exception error = null;
        str = str.toUpperCase();
        
        if(str.charAt(0) < 65 || str.charAt(0) > 90)
            valid = false;
        if(str.charAt(str.length() - 1) < 65 || str.charAt(str.length() - 1) > 90)
            valid = false;
        
        for(int i = 0; i < str.length(); i++){
            if((i + 1) % 4 == 0 && str.charAt(i) != ',')
                valid = false;
            if((i + 1) % 4 != 0){
                if(str.charAt(i) < 65 || str.charAt(i) > 90)
                    valid = false;
            }
        }
        
        if(!valid)
            throw error;
    }
}
