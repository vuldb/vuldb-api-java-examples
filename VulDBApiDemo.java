/*
    vuldb_api_demo - Java VulDB API Demo

    License: GPL-3.0
    Required Dependencies: 
        * java.io.BufferedReader
        * java.io.DataOutputStream
        * java.io.IOException
        * java.io.InputStreamReader
        * java.net.MalformedURLException
        * java.net.URL
        * javax.net.ssl.HttpsURLConnection
    Optional Dependencies: None
*/

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class VulDBApiDemo {

    public static void main(String[] args) {
        try {
            // API-URL
            URL url = new URL("https://vuldb.com/?api");

            // Put Headers together, the headers are used for authentication
            String personalApiKey = "YOUR_API-KEY_HERE"; // Enter your personal API key here. They can be found here: https://vuldb.com/?my.apihistory
            String userAgent = "VulDB API Advanced Java Demo Agent"; // You can adjust this to your liking.       

            // Put body together, the body tells that API what should be read, adjust the arguments below
            /*
                The body should look something like this: 
                "recent=5&details=2" 
                
                Make sure to only search for either a specific ID or CVE, or for recent data.
            */

            String recent = "5";        // Default value is "5"
            String details = "0";       // Default value is "0"         
            String id = null;           // Example: "290848"            Enter a specific VulDB id to search for (Default value is null)
            String cve = null;          // Example: "CVE-2024-1234"     Enter a CVE to search for (Default Value is null)

            // Put together the Request Body
            String requestBody;
            if (id == null && cve == null) {
                requestBody = "recent=" + recent + "&" + details;
            } else if (cve != null) {
                requestBody = "search=" + cve + "&" + details;
            } else {
                requestBody = "id=" + id + "&" + details;
            }
            
            // Make Request
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("User-Agent", userAgent);
            conn.setRequestProperty("X-VulDB-ApiKey", personalApiKey);

            try (DataOutputStream dos = new DataOutputStream(conn.getOutputStream())) {
                dos.write(requestBody.getBytes("UTF-8"));
                dos.close();
            }          

            int responseCode = conn.getResponseCode();

            if (responseCode == 200) {
                try (BufferedReader bf = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    String line;
                    while ((line = bf.readLine()) != null) {

                        // The output is delivered to the terminal by the following line, you can grab and transform it here
                        System.out.println(line);
                    }
                }
            } else {
                /*
                    For troubleshooting: 
                        * See the https://vuldb.com/?my.apihistory page and check if your request appears there. 
                        * If it does, click on the ID of the request to further check what VulDB received and sent.
                        * If it doesn't, check your request and your internet connection 

                */
                System.out.println("Request failed with response code: " + responseCode);
            }
        } catch (MalformedURLException e) {
            System.err.println("MalformedURLException: Invalid URL format. " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IOException: Error in communication. " + e.getMessage());
        } catch (Exception e) {
            System.err.println("General Exception: " + e.getMessage());
        }
    }
}
