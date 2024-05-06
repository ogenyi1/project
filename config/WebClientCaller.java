package ng.optisoft.rosabon.config;

import ng.optisoft.rosabon.credit.annotations.LogMethod;
import ng.optisoft.rosabon.util.CrUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class WebClientCaller {

    private final WebClient webClient;
    private final WebClient webClientUnsafe;

    public WebClientCaller(@Qualifier("webClient") WebClient webClient,@Qualifier("webClientUnsafe") WebClient webClientUnsafe) {
        this.webClient = webClient;
        this.webClientUnsafe = webClientUnsafe;
    }

    @LogMethod
    public <T> ResponseEntity<String> makeGenericBlockingPostRequest (
            String url,
            Boolean addAuthorizationHeader,
            String accessToken,
            T requestBody) {
        WebClient.RequestHeadersSpec<?> requestSpec = webClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(CrUtil.convertObjectToJsonString(requestBody))
                .accept(MediaType.APPLICATION_JSON);

        if (Boolean.TRUE.equals(addAuthorizationHeader)) {
            requestSpec = requestSpec.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        }

        return requestSpec.retrieve()
                .toEntity(String.class)
                .onErrorResume(WebClientResponseException.class, this::handleWebClientError)
                .block(); // Blocking until the response is received

    }

    @LogMethod
    public <T> ResponseEntity<String> makeGenericBlockingPostRequestUnsafe (
            String url,
            Boolean addAuthorizationHeader,
            Map<String,Object> additionalHeaders,
            String accessToken,
            T requestBody,
            MediaType mediaType) {
        WebClient.RequestHeadersSpec<?> requestSpec = webClientUnsafe.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(CrUtil.convertObjectToJsonString(requestBody))
                .accept(mediaType);

        if (Boolean.TRUE.equals(addAuthorizationHeader)) {
            requestSpec = requestSpec.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        }

        // Add additional headers from the map
        if (null != additionalHeaders && !additionalHeaders.isEmpty()) {
            for (Map.Entry<String, Object> entry : additionalHeaders.entrySet()) {
                requestSpec = requestSpec.header(entry.getKey(), entry.getValue().toString());
            }
        }

        return requestSpec.retrieve()
                .toEntity(String.class)
                .onErrorResume(WebClientResponseException.class, this::handleWebClientError)
                .block(); // Blocking until the response is received

    }

    @LogMethod
    public <T> Mono<ResponseEntity<String>> makeGenericAsyncGetRequest (String url) {
        return webClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(String.class);
    }

    @LogMethod
    public <T> ResponseEntity<String> makeGenericBlockingGetRequest (String url) {

        return webClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(String.class)
                .onErrorResume(WebClientResponseException.class, this::handleWebClientError)
                .block();
    }

    @LogMethod
    public <T> ResponseEntity<String> makeGenericBlockingGetRequestUnsafe (String url,
                                                                           Boolean addAuthorizationHeader,
                                                                           Map<String,Object> additionalHeaders,
                                                                           String accessToken,
                                                                           MediaType mediaType) {

        WebClient.RequestHeadersSpec<?> requestSpec =  webClientUnsafe.get()
                .uri(url)
                .accept(mediaType);

        if (Boolean.TRUE.equals(addAuthorizationHeader)) {
            requestSpec = requestSpec.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        }

        // Add additional headers from the map
        if (null != additionalHeaders && !additionalHeaders.isEmpty()) {
            for (Map.Entry<String, Object> entry : additionalHeaders.entrySet()) {
                requestSpec = requestSpec.header(entry.getKey(), entry.getValue().toString());
            }
        }

        return requestSpec.retrieve()
                .toEntity(String.class)
                .onErrorResume(WebClientResponseException.class, this::handleWebClientError)
                .block(); // Blocking until the response is received
    }

    @LogMethod
    public <T> Mono<ResponseEntity<String>> makeGenericAsyncPostRequest (
            String url,
//            Boolean addHeaders,
            T requestBody) {
        return webClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(CrUtil.convertObjectToJsonString(requestBody))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(String.class);
    }

    private Mono<ResponseEntity<String>> handleWebClientError(WebClientResponseException ex) {
        HttpStatus statusCode = ex.getStatusCode();
        String errorResponse = ex.getResponseBodyAsString();

        // Return the custom response as a Mono
        return Mono.just(ResponseEntity.status(statusCode).body(errorResponse));
    }
}
