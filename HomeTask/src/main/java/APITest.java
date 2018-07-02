
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ofiry on 7/2/2018.
 */
public class APITest {

    private static final String SITE = "https://api.bcqa.bc2.customers.biocatch.com/api/v6/score";
    private static final String CUSTOMER_ID = "customerID=bcqa";
    private static final String ACTION = "action=getScore";
    private static final String UUID = "uuid=bioTest";
    private static final String VALID_CUSTOMER_SESSION_ID = "customerSessionID=d0gg5hamj5sz7zwcbv8wmy0qxpt30a86";
    private static final String INVALID_CUSTOMER_SESSION_ID = "customerSessionID=InValidValue";
    private static final String ACTIVITY_TYPE = "activityType=MAKE_PAYMENT";
    private static final String BRAND = "brand=testQa";

    private static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {

        String validUrl = getUrlWithParam(SITE, CUSTOMER_ID, ACTION, UUID, VALID_CUSTOMER_SESSION_ID, ACTIVITY_TYPE, BRAND);
        String inValidUrl = getUrlWithParam(SITE, CUSTOMER_ID, ACTION, UUID, INVALID_CUSTOMER_SESSION_ID, ACTIVITY_TYPE, BRAND);
        System.out.println("******** valid response test ***********");
        responseTest(validUrl);
        System.out.println("******** invalid response test ***********");
        responseTest(inValidUrl);
    }

    private static String sendGET(String url) throws IOException {
        String responseAsString = "GET request failed";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            responseAsString = response.toString();
            System.out.println(responseAsString);
        } else {
            System.out.println("GET request failed");
        }
        return responseAsString;
    }

    /**
     * get Url With Param
     *
     * @param site
     * @param customerId
     * @param action
     * @param uuid
     * @param customerSessionId
     * @param activityType
     * @param brand
     * @return url with parameters
     */
    private static String getUrlWithParam(String site, String customerId, String action, String uuid, String customerSessionId, String activityType, String brand) {
        return site + "?" + customerId + "&" + action + "&" + uuid + "&" + customerSessionId + "&" + activityType + "&" + brand;
    }

    /**
     * verify Response Score Bigger Than limitScore
     *
     * @param apiResponseScore String from api response
     * @param limitScore       score to compare with
     */
    private static void verifyResponseScoreBiggerThan(String apiResponseScore, int limitScore) {
        if (apiResponseScore == null) {
            System.out.println("Cant find any 'score' parameter in response");
        } else {
            if (Integer.parseInt(apiResponseScore) > limitScore) {
                System.out.println("score parameter is more than " + limitScore + " : " + apiResponseScore);
            } else
                System.out.println("score parameter is less than " + limitScore + " : " + apiResponseScore);
        }
    }

    /**
     * api Validation
     *
     * @param bcStatus status from response
     */
    private static void apiValidation(String bcStatus) {
        if ("tested".equals(bcStatus)) {
            System.out.println("Valid response !!!");
        } else
            System.out.println("Invalid response !!!");
    }

    /**
     * response Test
     *
     * @param url url of Get request
     * @throws IOException
     */
    private static void responseTest(String url) throws IOException {
        ApiResponse apiResponse = mapper.readValue(sendGET(url), ApiResponse.class);
        apiValidation(apiResponse.bcStatus);
        verifyResponseScoreBiggerThan(apiResponse.score, 800);
    }
}
