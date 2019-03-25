package com.harmonycloud.service;

import com.harmonycloud.enums.ErrorMsgEnum;
import com.harmonycloud.exception.OrderException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.net.Proxy;

@Component
public final class RestProxyTemplate {

    @Value("${proxy.host:}")
    private String host;

    @Value("${proxy.port:}")
    private String port;

    @Autowired
    RestTemplate restTemplate;

    @Bean
    public RestTemplate template() {
        return new RestTemplate();
    }


    @PostConstruct
    public void init() {
        if (host.isEmpty() || port.isEmpty()) {
            return;
        }
        int portNr = -1;
        try {
            portNr = Integer.parseInt(port);
        } catch (NumberFormatException e) {
            throw new OrderException(ErrorMsgEnum.FORMAT_ERROR.getMessage());
        }
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        InetSocketAddress address = new InetSocketAddress(host, portNr);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, address);
        factory.setProxy(proxy);

        restTemplate.setRequestFactory(factory);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }
}
