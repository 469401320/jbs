package me.hao0.jbs.common.http;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.common.base.Strings;
import me.hao0.jbs.common.exception.HttpException;
import java.io.*;
import java.util.Collections;
import java.util.Map;


public class Http {

    private String url;

    private HttpMethod method = HttpMethod.GET;

    private Map<String, String> headers = Collections.emptyMap();

    private Map<String, ?> params = Collections.emptyMap();


    private String body;

    private Boolean ssl = Boolean.FALSE;

    private Integer connectTimeout = 1000 * 5;

    private Integer readTimeout = 1000 * 5;


    private Boolean encode = Boolean.TRUE;


    private String contentType = "";


    private String charset = HttpRequest.CHARSET_UTF8;


    private String accept = "";

    private Http(String url){
        this.url = url;
    }

    private Http method(HttpMethod method){
        this.method = method;
        return this;
    }

    public Http ssl(){
        this.ssl = Boolean.TRUE;
        return this;
    }

    public Http headers(Map<String, String> headers){
        this.headers = headers;
        return this;
    }

    public Http params(Map<String, ?> params){
        this.params = params;
        return this;
    }


    public Http body(String body){
        this.body = body;
        return this;
    }

    public Http encode(Boolean encode){
        this.encode = encode;
        return this;
    }

    public Http contentType(String contentType){
        this.contentType = contentType;
        return this;
    }

    public Http charset(String charset){
        this.charset = charset;
        return this;
    }

    public Http accept(String accept){
        this.accept = accept;
        return this;
    }


    public Http connTimeout(Integer connectTimeout){
        this.connectTimeout = connectTimeout * 1000;
        return this;
    }


    public Http readTimeout(Integer readTimeout){
        this.readTimeout = readTimeout * 1000;
        return this;
    }

    public String request(){
        switch (method){
            case GET:
                return doGet();
            case POST:
                return doPost();
            default:
                break;
        }
        return null;
    }


    private String doPost() {
        HttpRequest post = HttpRequest.post(url, params, encode)
                .headers(headers)
                .connectTimeout(connectTimeout)
                .readTimeout(readTimeout)
                .acceptGzipEncoding()
                .uncompress(true);
        setOptionalHeaders(post);

        if (!Strings.isNullOrEmpty(body)){
            post.send(body);
        }

        if (ssl){
            trustHttps(post);
        }

        return post.body();
    }

    private String doGet() {
        HttpRequest get = HttpRequest.get(url, params, encode)
                .headers(headers)
                .connectTimeout(connectTimeout)
                .readTimeout(readTimeout)
                .acceptGzipEncoding()
                .uncompress(true);
        if (ssl){
            trustHttps(get);
        }
        setOptionalHeaders(get);
        return get.body();
    }

    private void setOptionalHeaders(HttpRequest request) {
        if (!Strings.isNullOrEmpty(contentType)){
            request.contentType(contentType, charset);
        }
        if (!Strings.isNullOrEmpty(accept)){
            request.accept(accept);
        }
    }

    private void trustHttps(HttpRequest request) {
        request.trustAllCerts().trustAllHosts();
    }

    public static Http get(String url){
        return new Http(url);
    }

    public static Http post(String url){
        return new Http(url).method(HttpMethod.POST);
    }

    public static Http put(String url){
        return new Http(url).method(HttpMethod.PUT);
    }

    public static Http delete(String url){
        return new Http(url).method(HttpMethod.DELETE);
    }


    public static String upload(String url, String fieldName, String fileName, byte[] data, Map<String, String> params){
        return upload(url, fieldName, fileName, new ByteArrayInputStream(data), params);
    }


    public static String upload(String url, String fieldName, String fileName, InputStream in, Map<String, String> params){
        try {
            HttpRequest request = HttpRequest.post(url);
            if (!params.isEmpty()){
                for (Map.Entry<String, String> param : params.entrySet()){
                    request.part(param.getKey(), param.getValue());
                }
            }
            request.part(fieldName , fileName, null, in);
            return request.body();
        } catch (Exception e){
            throw new HttpException(e);
        }
    }


    public static void download(String url, File into){
        try {
            download(url, new FileOutputStream(into));
        } catch (FileNotFoundException e) {
            throw new HttpException(e);
        }
    }


    public static void download(String url, OutputStream output){
        try {
            HttpRequest request =  HttpRequest.get(url);
            if (request.ok()){
                request.receive(output);
            } else {
                throw new HttpException("request isn't ok: " + request.body());
            }
        } catch (Exception e){
            throw new HttpException(e);
        }
    }
}
