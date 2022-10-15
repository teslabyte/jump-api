package teslabyte.gamesapi.line;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class LineAuthentication {
    private static final String lineUrl = "hap-us.line-apps.com/auth/v3.2/token/refresh";
    private static final String appId = "LGYDS";
    private static final String deviceId = "";
    private static final String mcc = "310";
    private static final String mnc = "410";
    private static final String sdkversion = "2.5.4.56";
    private String userToken = "";

    public LineAuthentication() {

    }

    public LineAuthentication(String userToken) {
        this.userToken = userToken;
    }

    public void authenticate(){
        String timestamp = generateTimestamp();
        String response = getRequest(timestamp);
        System.out.println(response);
    }

    public String generateTimestamp(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.000+0900");
        ZoneId zoneId = ZoneId.of("Asia/Tokyo");
        LocalDateTime nowTokyo = LocalDateTime.now(zoneId);
        String timestamp = dtf.format(nowTokyo).replace(" ","T");
        return timestamp;
    }

    public String getRequest(String timestamp) {
        try {
            URL url = new URL("https://" + lineUrl);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("GET");

            // HEADERS HERE
            httpURLConnection.setRequestProperty("x-linegame-appid", appId);
            httpURLConnection.setRequestProperty("x-linegame-deviceid", deviceId);
            httpURLConnection.setRequestProperty("x-linegame-mcc", mcc);
            httpURLConnection.setRequestProperty("x-linegame-mnc", mnc);
            httpURLConnection.setRequestProperty("x-linegame-sdkversion", sdkversion);
            httpURLConnection.setRequestProperty("x-linegame-timestamp", timestamp);
            httpURLConnection.setRequestProperty("x-linegame-usertoken", userToken);

            httpURLConnection.connect();
            return readResponse(httpURLConnection);
        } catch (Exception e){
            System.out.println(e.getMessage());  //TODO exception handling
            return "null";
        }
    }

    public String readResponse(HttpURLConnection httpURLConnection) {
        try {
            String line, info = "";
            int responseCode = httpURLConnection.getResponseCode();
            boolean isError = false;
            BufferedReader reader;
            if (200 <= responseCode && responseCode <= 299) {
                reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
            }
            while ((line = reader.readLine()) != null) {
                info += line;
            }
            reader.close();

            return info + "\n";
        } catch (Exception e){
            System.out.println(e.getMessage()); //TODO exception handling
            return "null";
        }
    }
}
