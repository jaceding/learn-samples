package per.jaceding.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.lang.NonNull;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;

/**
 * RestTemplate 配置类
 *
 * @author jaceding
 * @date 2020/7/21
 */
@Configuration
public class RestTemplateConfiguration {

    /**
     * 默认连接超时时间
     */
    public static final int DEFAULT_CONNECT_TIMEOUT = 30000;

    /**
     * 默认读超时时间
     */
    public static final int DEFAULT_READ_TIMEOUT = 30000;

    @Bean
    public RestTemplate httpRestTemplate() {
        return getBaseRestTemplate();
    }

    @Bean
    public RestTemplate httpsRestTemplate() {
        RestTemplate restTemplate = getBaseRestTemplate();
        restTemplate.setRequestFactory(new HttpsClientRequestFactory());
        return restTemplate;
    }

    private RestTemplate getBaseRestTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT);
        requestFactory.setReadTimeout(DEFAULT_READ_TIMEOUT);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
        return restTemplate;
    }

    private static class DefaultResponseErrorHandler implements ResponseErrorHandler {
        /**
         * 对response进行判断，如果是异常情况，返回true
         */
        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            return response.getStatusCode().value() != HttpServletResponse.SC_OK;
        }

        /**
         * 异常情况时的处理方法
         */
        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            BufferedReader br = new BufferedReader(new InputStreamReader(response.getBody()));
            StringBuilder sb = new StringBuilder();
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            try {
                throw new Exception(sb.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static class HttpsClientRequestFactory extends SimpleClientHttpRequestFactory {

        @Override
        protected void prepareConnection(@NonNull HttpURLConnection connection, @NonNull String httpMethod) {
            try {
                if (!(connection instanceof HttpsURLConnection)) {
                    throw new RuntimeException("An instance of HttpsURLConnection is expected");
                }

                HttpsURLConnection httpsConnection = (HttpsURLConnection) connection;

                TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            @Override
                            public X509Certificate[] getAcceptedIssuers() {
                                return null;
                            }

                            @Override
                            public void checkClientTrusted(X509Certificate[] certs, String authType) {
                            }

                            @Override
                            public void checkServerTrusted(X509Certificate[] certs, String authType) {
                            }

                        }
                };
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                httpsConnection.setSSLSocketFactory(new MyCustomSslSocketFactory(sslContext.getSocketFactory()));

                httpsConnection.setHostnameVerifier((s, sslSession) -> true);

                super.prepareConnection(httpsConnection, httpMethod);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // SSLSocketFactory用于创建 SSLSockets
        private static class MyCustomSslSocketFactory extends SSLSocketFactory {

            private final SSLSocketFactory delegate;

            MyCustomSslSocketFactory(SSLSocketFactory delegate) {
                this.delegate = delegate;
            }

            // 返回默认启用的密码套件。除非一个列表启用，对SSL连接的握手会使用这些密码套件。
            // 这些默认的服务的最低质量要求保密保护和服务器身份验证
            @Override
            public String[] getDefaultCipherSuites() {
                return delegate.getDefaultCipherSuites();
            }

            // 返回的密码套件可用于SSL连接启用的名字
            @Override
            public String[] getSupportedCipherSuites() {
                return delegate.getSupportedCipherSuites();
            }

            @Override
            public Socket createSocket(final Socket socket, final String host, final int port,
                                       final boolean autoClose) throws IOException {
                final Socket underlyingSocket = delegate.createSocket(socket, host, port, autoClose);
                return overrideProtocol(underlyingSocket);
            }

            @Override
            public Socket createSocket(final String host, final int port) throws IOException {
                final Socket underlyingSocket = delegate.createSocket(host, port);
                return overrideProtocol(underlyingSocket);
            }

            @Override
            public Socket createSocket(final String host, final int port, final InetAddress localAddress,
                                       final int localPort) throws
                    IOException {
                final Socket underlyingSocket = delegate.createSocket(host, port, localAddress, localPort);
                return overrideProtocol(underlyingSocket);
            }

            @Override
            public Socket createSocket(final InetAddress host, final int port) throws IOException {
                final Socket underlyingSocket = delegate.createSocket(host, port);
                return overrideProtocol(underlyingSocket);
            }

            @Override
            public Socket createSocket(final InetAddress host, final int port, final InetAddress localAddress,
                                       final int localPort) throws
                    IOException {
                final Socket underlyingSocket = delegate.createSocket(host, port, localAddress, localPort);
                return overrideProtocol(underlyingSocket);
            }

            private Socket overrideProtocol(final Socket socket) {
                if (!(socket instanceof SSLSocket)) {
                    throw new RuntimeException("An instance of SSLSocket is expected");
                }
                ((SSLSocket) socket).setEnabledProtocols(new String[]{"TLSv1"});
                return socket;
            }
        }
    }
}
