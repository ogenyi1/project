package ng.optisoft.rosabon.credit.clients;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ng.optisoft.rosabon.credit.annotations.LogMethod;
import ng.optisoft.rosabon.exception.GenericException;
import ng.optisoft.rosabon.util.CrUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class HttpClientCaller{

    public static String MONO_SECRET;
    public static String MONO_APP_ID;
    public static Integer CLIENT_ID;
    public static String CLIENT_SECRET;
    public static String MBS_CLIENT_USERNAME;

    private final HttpClient httpClient;

    @Value("${mono.sec.key}")
    private String monoSecret;
    @Value("${mono.app.id}")
    private String monoAppId;
    @Value("${paystack.test.key}")
    private String paystackToken;
    @Value("${mybankstatement.client.secret}")
    private String myBankStatementSecret;
    @Value("${mybankstatement.client.id}")
    private Integer myBankStatementClientId;
    @Value("${mybankstatement.client.username}")
    private String myBankStatementClientUsername;
    @Value("${wema.nip-service.vendorID}")
    private String wemaNIPServiceVendorID;


    @Value("${mono.app.id}")
    public void setMonoAppId(String monoAppId) {
        MONO_APP_ID = monoAppId;
    }

    @Value("${mono.sec.key}")
    public void setMonoSecret(String monoSecret) {
        MONO_SECRET = monoSecret;
    }

    @Value("${mybankstatement.client.id}")
    public void setMyBankStatementClientId(Integer myBankStatementClientId) {
        CLIENT_ID = myBankStatementClientId;
    }

    @Value("${mybankstatement.client.secret}")
    public void setMyBankStatementSecret(String myBankStatementSecret) {
        CLIENT_SECRET = myBankStatementSecret;
    }

    @Value("${mybankstatement.client.username}")
    public void setMyBankStatementUsername(String myBankStatementSecret) {
        MBS_CLIENT_USERNAME = myBankStatementClientUsername;
    }

    @LogMethod
    public <T> String httpMyBankPostRequest(String url, Boolean addHeader, T requestBody) {

        try {

            HttpPost httpPost = new HttpPost(url);
            if (addHeader) {

                httpPost.setHeader("Client-ID", CLIENT_ID.toString());
                httpPost.setHeader("Client-Secret", CLIENT_SECRET);
            }
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity(CrUtil.convertObjectToJsonString(requestBody)));

            HttpResponse execute = postRequest(httpPost);

            return EntityUtils.toString(execute.getEntity());

        } catch (IOException e) {
            throw new GenericException(e.getMessage());
        }

    }

    @LogMethod
    public <T> String makeWemaNIPServicePostRequest(String url,
                                                    Boolean addVendorIdHeader,
                                                    Boolean addAuthorizationHeader,
                                                    String accessToken,
                                                    T requestBody) {

        try {

            HttpPost httpPost = new HttpPost(url);
            if (addVendorIdHeader) {
                httpPost.setHeader("VendorID", wemaNIPServiceVendorID);
            }
            if (addAuthorizationHeader) {
                httpPost.setHeader("Authorization", "Bearer " + accessToken);
            }
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity(CrUtil.convertObjectToJsonString(requestBody)));

            HttpResponse execute = postRequest(httpPost);

            return EntityUtils.toString(execute.getEntity());

        } catch (IOException e) {
            throw new GenericException(e.getMessage());
        }

    }

    @LogMethod
    public <T> String makeWemaNIPServiceGetRequest(String url,
                                                   Boolean addVendorIdHeader,
                                                    Boolean addAuthorizationHeader,
                                                   String accessToken) {

        try {

            HttpGet httpGet = new HttpGet(url);

            if (addVendorIdHeader) {
                httpGet.setHeader("VendorID", wemaNIPServiceVendorID);
            }
            if (addAuthorizationHeader) {
                httpGet.setHeader("Authorization", "Bearer " + accessToken);
            }

            httpGet.setHeader("Content-Type", "application/json");
            HttpResponse execute = getRequest(httpGet);

            return EntityUtils.toString(execute.getEntity());

        } catch (IOException e) {
            throw new GenericException(e.getMessage());
        }

    }

    @LogMethod
    public <T> String httpErpPostRequest(String url, Boolean addHeader, T requestBody, String token) {

        try {

            HttpPost httpPost = new HttpPost(url);
            if (addHeader) {

                httpPost.setHeader("Authorization", "Bearer " + token);

            }
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity(CrUtil.convertObjectToJsonString(requestBody)));

            HttpResponse execute = postRequest(httpPost);

            return EntityUtils.toString(execute.getEntity());

        } catch (IOException e) {
            throw new GenericException(e.getMessage());
        }

    }

    @LogMethod
    public <T> String httpErpGetRequest(String url, Boolean addHeader, String token) {

        try {

            HttpGet httpGet = new HttpGet(url);

            if (addHeader) {
                httpGet.setHeader("Authorization", "Bearer " + token);

            }

            httpGet.setHeader("Content-Type", "application/json");
            HttpResponse execute = getRequest(httpGet);

            return EntityUtils.toString(execute.getEntity());

        } catch (IOException e) {
            throw new GenericException(e.getMessage());
        }

    }

    @LogMethod
    public <T> String httpMyBankGetRequest(String url, Boolean addHeader) {

        try {

            HttpGet httpGet = new HttpGet(url);

            if (addHeader) {
                httpGet.setHeader("Client_ID", CLIENT_ID.toString());
                httpGet.setHeader("Client-Secret", CLIENT_SECRET);
            }

            httpGet.setHeader("Content-Type", "application/json");
            HttpResponse execute = getRequest(httpGet);

            return EntityUtils.toString(execute.getEntity());

        } catch (IOException e) {
            throw new GenericException(e.getMessage());
        }

    }

    @LogMethod
    public <T> String httpMonoPostRequest(String url, Boolean addHeader, String sessionId, T requestBody) {

        try {

            HttpPost httpPost = new HttpPost(url);
            if (addHeader) {
                httpPost.setHeader("x-session-id", sessionId);
            }
            httpPost.setHeader("mono-sec-key", MONO_SECRET);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity(CrUtil.convertObjectToJsonString(requestBody)));

            HttpResponse execute = postRequest(httpPost);

            return EntityUtils.toString(execute.getEntity());

        } catch (IOException e) {
            throw new GenericException(e.getMessage());
        }

    }

    @LogMethod
    public <T> String httpMonoGetRequest(String url, Boolean addHeader) {

        try {

            HttpGet httpGet = new HttpGet(url);

            if (addHeader) {
                httpGet.setHeader("mono-sec-key", MONO_SECRET);
            }

            httpGet.setHeader("Content-Type", "application/json");
            HttpResponse execute = getRequest(httpGet);

            return EntityUtils.toString(execute.getEntity());

        } catch (IOException e) {
            throw new GenericException(e.getMessage());
        }

    }

    @LogMethod
    public <T> String httpPostRequestERP(String url, String token, T requestBody) {

        try {

            HttpPost httpPost = new HttpPost(url);

            httpPost.setHeader("Authorization", "Bearer " + token);

            httpPost.setEntity(new StringEntity(CrUtil.convertObjectToJsonString(requestBody)));
            httpPost.setHeader("Content-Type", "application/json");
            HttpResponse execute = postRequest(httpPost);

            return EntityUtils.toString(execute.getEntity());

        } catch (IOException e) {
            throw new GenericException(e.getMessage());
        }

    }

    @LogMethod
    public <T> String httpPostRequestERP2(String url, String token, String requestBody) {

        try {

            HttpPost httpPost = new HttpPost(url);

            httpPost.setHeader("Authorization", "Bearer " + token);

            httpPost.setEntity(new StringEntity(requestBody));
            httpPost.setHeader("Content-Type", "application/json");
            HttpResponse execute = postRequest(httpPost);

            return EntityUtils.toString(execute.getEntity());

        } catch (IOException e) {

            throw new GenericException(e.getMessage());
        }
        catch (Exception e){
            log.info("Error when making post request {}", e.getMessage());
            throw new GenericException(e.getMessage());

        }

    }

    @LogMethod
    public <T> String httpPostRequest(String url, Boolean addHeader, T requestBody) {

        try {

            //String moduleId = serverletRequest.getHeader("Module_Id");
            //String authorization = serverletRequest.getHeader("Authorization");

            HttpPost httpPost = new HttpPost(url);
            if (addHeader)
                httpPost.setHeader("Authorization", paystackToken);

            httpPost.setEntity(new StringEntity(CrUtil.convertObjectToJsonString(requestBody)));
            httpPost.setHeader("Content-Type", "application/json");
            HttpResponse execute = postRequest(httpPost);

            //return IOUtils.toString(execute.getEntity().getContent());
            return EntityUtils.toString(execute.getEntity());

        } catch (IOException e) {
            throw new GenericException(e.getMessage());
        }

    }

    @LogMethod
    public <T> String httpGetRequest(String url, Boolean addHeader) {

        try {

            HttpGet httpGet = new HttpGet(url);

            if (addHeader)
                httpGet.setHeader("Authorization", paystackToken);

            httpGet.setHeader("Content-Type", "application/json");
            HttpResponse execute = getRequest(httpGet);

            return EntityUtils.toString(execute.getEntity());
            //return IOUtils.toString(execute.getEntity().getContent());

        } catch (IOException e) {
            throw new GenericException(e.getMessage());
        }

    }


    @LogMethod
    public <T> String loginToErpPostRequest(String url, Boolean addHeader, T requestBody) {

        try {

            //String moduleId = serverletRequest.getHeader("Module_Id");
            //String authorization = serverletRequest.getHeader("Authorization");

            HttpPost httpPost = new HttpPost(url);
            if (addHeader)
                httpPost.setHeader("Authorization", paystackToken);

            httpPost.setEntity(new StringEntity(CrUtil.convertObjectToJsonString(requestBody)));
            httpPost.setHeader("Content-Type", "application/json");
            HttpResponse execute = postRequest(httpPost);

            //return IOUtils.toString(execute.getEntity().getContent());
            return EntityUtils.toString(execute.getEntity());

        } catch (IOException e) {
            throw new GenericException(e.getMessage());
        }

    }

    @LogMethod
    public <T> String fetchErpCustomerId(String url, String token, String bvn, Boolean addHeader) {

        try {

            HttpGet httpGet = new HttpGet(url + bvn);

            if (addHeader)
                httpGet.setHeader("Authorization", "Bearer " + token);

            httpGet.setHeader("Content-Type", "application/json");
            HttpResponse execute = getRequest(httpGet);

            return EntityUtils.toString(execute.getEntity());
            //return IOUtils.toString(execute.getEntity().getContent());

        } catch (IOException e) {
            throw new GenericException(e.getMessage());
        }

    }

    @LogMethod
    public <T> String makeGenericPostRequest(String url, T requestBody) {
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(CrUtil.convertObjectToJsonString(requestBody)));
            httpPost.setHeader("Content-Type", "application/json");
            HttpResponse execute = postRequest(httpPost);
            return EntityUtils.toString(execute.getEntity());
        } catch (IOException e) {
            throw new GenericException(e.getMessage());
        }

    }

    @LogMethod
    public <T> String makeGenericGetRequest(String url) {
        try {

            HttpGet httpGet = new HttpGet(url);

            httpGet.setHeader("Content-Type", "application/json");
            HttpResponse execute = getRequest(httpGet);

            return EntityUtils.toString(execute.getEntity());

        } catch (IOException e) {
            throw new GenericException(e.getMessage());
        }
    }

    @LogMethod
    public <T> String getBase64StringFromExternalPDFResource(String pdfUrl) {
        try {
            // download PDF from URL
            HttpGet httpGet = new HttpGet(pdfUrl);
            HttpResponse execute = getRequest(httpGet);
            HttpEntity entity = execute.getEntity();
            byte[] pdfBytes = IOUtils.toByteArray(entity.getContent());
            EntityUtils.consume(entity);
            // convert PDF to base64 string
            String base64String = Base64.getEncoder().encodeToString(pdfBytes);
            return base64String;
        } catch (IOException e) {
            throw new GenericException(e.getMessage());
        }
    }

    private HttpResponse getRequest(HttpGet httpGet) {

        try {
            return httpClient.execute(httpGet);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new GenericException("Request Timeout " + ex.getMessage());

        }
    }

    private HttpResponse postRequest(HttpPost httpPost) {

        try {
            return httpClient.execute(httpPost);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new GenericException("Request Timeout " + ex.getMessage());

        }
    }


}
