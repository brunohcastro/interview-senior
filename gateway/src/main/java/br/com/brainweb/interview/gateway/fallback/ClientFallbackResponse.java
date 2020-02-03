package br.com.brainweb.interview.gateway.fallback;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ClientFallbackResponse implements ClientHttpResponse {
    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.OK;
    }

    @Override
    public int getRawStatusCode() {
        return HttpStatus.OK.value();
    }

    @Override
    public String getStatusText() {
        return HttpStatus.OK.toString();
    }

    @Override
    public void close() {
    }

    @Override
    public InputStream getBody() {
        return new ByteArrayInputStream("{\"error\":\"Sorry, Service is Down!\"}".getBytes());
    }

    @Override
    public HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccessControlAllowCredentials(true);
        headers.setAccessControlAllowOrigin("*");
        return headers;
    }
}
