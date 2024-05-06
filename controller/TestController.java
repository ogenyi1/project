package ng.optisoft.rosabon.controller;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
public class TestController {

    @GetMapping
    public String testing() {
        log.info("Got into the test controller ");

        return "Hello word";
    }

    private static final String SECRET_KEY = "your_secret_key_here";

//    public static String generateXAuthSignature(HttpHeaders headers, Map<String, String> requestParams) {
public static String generateXAuthSignature() {

    // Combine headers and request params into a single map
//        Map<String, String> allParams = headers.toSingleValueMap();
//        allParams.putAll(requestParams);
//
//        // Sort the keys and values alphabetically
//        String sortedParams = allParams.entrySet().stream()
//                .sorted(Map.Entry.comparingByKey())
//                .map(entry -> entry.getKey() + "=" + entry.getValue())
//                .collect(Collectors.joining("&"));

        // Append the secret key to the end of the sorted params
//        String message = sortedParams + SECRET_KEY;
        String message = "dGVzdF9Qcm92aWR1cw==:29A492021F4B709A8D1152C3EF4D32DC5A7092723ECAC4C511781003584B48873CCBFEBDEAE89CF22ED1CB1A836213549BC6638A3B563CA7FC009BEB3BC30CF8";

        String message2 = "Um9zYW5GYm8g:19074D09E0889D2D592B9A0886A7AD7478F2318C6B6CA429230A5DFA01713088";

        // Hash the message using SHA512
        String digest = DigestUtils.sha512Hex(message2);

        // Return the digest as the x-auth-signature
        return digest;
    }

    public static void main(String[] args) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("ClientId", "29A492021F4B709A8D1152C3EF4D32DC5A7092723ECAC4C511781003584B48873CCBFEBDEAE89CF22ED1CB1A836213549BC6638A3B563CA7FC009BEB3BC30CF8\n" +
//                "(");
//
//        // Set up the request params for the API request
//        Map<String, String> requestParams = new HashMap<>();
//        requestParams.put("param1", param1);
//        requestParams.put("param2", param2);
//
//        // Generate the x-auth-signature for the API request
//        String xAuthSignature = generateXAuthSignature(headers, requestParams);

        String result = generateXAuthSignature().toUpperCase();
        System.out.println(result);


    }
}
