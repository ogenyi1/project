package ng.optisoft.rosabon.config;

import lombok.RequiredArgsConstructor;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HeaderIterator;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;


/**
 * - Supports both HTTP and HTTPS
 * - Uses a connection pool to re-use connections and save overhead of creating connections.
 * - Has a custom connection keep-alive strategy (to apply a default keep-alive if one isn't specified)
 * - Starts an idle connection monitor to continuously clean up stale connections.
 */
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class HttpClientConfig {

	private final Logger logger = LoggerFactory.getLogger(HttpClientConfig.class);

    // Determines the timeout in milliseconds until a connection is established.
    @Value("${httpclient.connect-timeout}")
    private String CONNECT_TIMEOUT;

    // The timeout when requesting a connection from the connection manager.
    @Value("${httpclient.request-timeout}")
    private  String REQUEST_TIMEOUT;

    // The timeout for waiting for data
    @Value("${httpclient.socket-timeout}")
    private  String SOCKET_TIMEOUT;

    @Value("${httpclient.total-connections}")
    private String MAX_TOTAL_CONNECTIONS;

    @Value("${httpclient.keep-alive-time}")
    private String DEFAULT_KEEP_ALIVE_TIME;

    @Value("${httpclient.keep-alive-time-multiply}")
    private String DEFAULT_KEEP_ALIVE_TIME_M;

    @Value("${httpclient.idle-connectons}")
    private String CLOSE_IDLE_CONNECTION_WAIT_TIME_SECS;
    @Bean
    public PoolingHttpClientConnectionManager poolingConnectionManager() {
      PoolingHttpClientConnectionManager poolingConnectionManager = new PoolingHttpClientConnectionManager();

      SSLContextBuilder builder = new SSLContextBuilder();
      try {
          builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
      } catch (NoSuchAlgorithmException | KeyStoreException e) {
          logger.error("Pooling Connection Manager Initialisation failure because of " + e.getMessage(), e);
      }

      SSLConnectionSocketFactory sslsf = null;
      try {
          sslsf = new SSLConnectionSocketFactory(builder.build());
      } catch (KeyManagementException | NoSuchAlgorithmException e) {
          logger.error("Pooling Connection Manager Initialisation failure because of " + e.getMessage(), e);
      }

      @SuppressWarnings("unused")
	  Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
              .<ConnectionSocketFactory>create().register("https", sslsf)
              .register("http", new PlainConnectionSocketFactory())
              .build();

      poolingConnectionManager.setMaxTotal(Integer.parseInt(MAX_TOTAL_CONNECTIONS));
      return poolingConnectionManager;
    }

    @Bean
    public ConnectionKeepAliveStrategy connectionKeepAliveStrategy() {
      return (httpResponse, httpContext) -> {
        HeaderIterator headerIterator = httpResponse.headerIterator(HTTP.CONN_KEEP_ALIVE);
        HeaderElementIterator elementIterator = new BasicHeaderElementIterator(headerIterator);

        while (elementIterator.hasNext()) {
          HeaderElement element = elementIterator.nextElement();
          String param = element.getName();
          String value = element.getValue();
          if (value != null && param.equalsIgnoreCase("timeout")) {
            return Long.parseLong(value) * 1000; // convert to ms
          }
        }

        return (long) Integer.parseInt(DEFAULT_KEEP_ALIVE_TIME.trim()) * Integer.parseInt(DEFAULT_KEEP_ALIVE_TIME_M.trim());
      };
    }


    @Bean
    public Runnable idleConnectionMonitor(PoolingHttpClientConnectionManager pool) {
      return new Runnable() {
        @Override
        @Scheduled(fixedDelay = 20000)
        public void run() {
          // only if connection pool is initialised
          if (pool != null) {
            pool.closeExpiredConnections();
            pool.closeIdleConnections(Integer.parseInt(CLOSE_IDLE_CONNECTION_WAIT_TIME_SECS.trim()), TimeUnit.SECONDS);
          }
        }
      };
    }

    @Bean
    public CloseableHttpClient closeableHttpClient() {
      RequestConfig requestConfig = RequestConfig.custom()
              .setConnectTimeout(Integer.parseInt(CONNECT_TIMEOUT.trim()))
              .setConnectionRequestTimeout(Integer.parseInt(REQUEST_TIMEOUT.trim()))
              .setSocketTimeout(Integer.parseInt(SOCKET_TIMEOUT.trim()))
              .build();
      return HttpClients.custom()
              .setDefaultRequestConfig(requestConfig)
              .setConnectionManager(poolingConnectionManager())
              .setKeepAliveStrategy(connectionKeepAliveStrategy())
              .build();
    }

//    @Bean
//    public HttpClient httpClient() {
//
//        final HttpClientBuilder httpClient = create()
//                .setSSLHostnameVerifier((s, sslSession) -> true);
//
//        // if connection by proxy is enabled
//        if (Boolean.parseBoolean(isProxyEnabled)) {
//            logger.info("Connection by proxy is enabled: {}:{}", proxyIP, proxyPort);
//            httpClient.setProxy(new HttpHost(proxyIP, Integer.parseInt(proxyPort.trim())));
//        }
//        //httpClient.errorHandler(new CustomClientErrorHandler());
//        //httpClient.interceptors(new CustomClientHttpRequestInterceptor());
//
//        return httpClient.build();
//    }

}