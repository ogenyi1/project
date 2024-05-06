package ng.optisoft.rosabon.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import javax.net.ssl.SSLException;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfigUnsafe {

    private final Logger logger = LoggerFactory.getLogger(WebClientConfig.class);

    @Value("${webclient.connect-timeout}")
    private String CONNECT_TIMEOUT;

    @Value("${webclient.read-timeout}")
    private String READ_TIMEOUT;

    @Value("${webclient.write-timeout}")
    private String WRITE_TIMEOUT;

    @Bean(name = "webClientUnsafe")
    public WebClient webClientUnsafe() throws SSLException {
        try {
            // Install the all-trusting trust manager
            final SslContext sslContext = SslContextBuilder.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();

            // Set the SSL context globally for all HttpClient instances
            TcpClient tcpClient = TcpClient.create()
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, Integer.parseInt(CONNECT_TIMEOUT))
                    .doOnConnected(connection -> connection
                            .addHandlerLast(new ReadTimeoutHandler(Long.parseLong(READ_TIMEOUT), TimeUnit.MILLISECONDS))
                            .addHandlerLast(new WriteTimeoutHandler(Long.parseLong(WRITE_TIMEOUT), TimeUnit.MILLISECONDS)))
                    .secure(sslContextSpec -> sslContextSpec.sslContext(sslContext));

            WebClient.Builder builder = WebClient.builder()
                    .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
                    .exchangeStrategies(ExchangeStrategies.builder()
                            .codecs(configurer -> configurer.defaultCodecs()
                                    .maxInMemorySize(16 * 1024 * 1024)) // 16MB
                            .build());

            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
