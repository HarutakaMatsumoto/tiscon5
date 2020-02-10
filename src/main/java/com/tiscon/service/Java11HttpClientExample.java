package com.mkyong.http;

import java.io.StringReader;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.w3c.dom.Element;

public class Java11HttpClientExample {
    String appid;

    {
        appid = "dj00aiZpPVNNWXcwSkdjWndmTiZzPWNvbnN1bWVyc2VjcmV0Jng9YjM";
    }

    // one instance, reuse
    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    public Document sendGet(String uri) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uri))
                .setHeader("User-Agent", "dj00aiZpPVNNWXcwSkdjWndmTiZzPWNvbnN1bWVyc2VjcmV0Jng9YjM-")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        //API to obtain DOM Document instance
        DocumentBuilder builder = null;
        try
        {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();

            //Parse the content to Document object
            Document doc = builder.parse(new InputSource(new StringReader(response.body())));
            return doc;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public double getDistance(Properties propertyFrom, Properties propertyTo) throws Exception {

        String baseURL = "https://map.yahooapis.jp/dist/V1/distance";

        String params = "";
        params += "coordinates=";
        params += propertyFrom.getProperty("lon") + "," + propertyFrom.getProperty("lat") +
                "%20" + propertyTo.getProperty("lon") + "," + propertyTo.getProperty("lat");
        params += "%20&appid=dj00aiZpPVNNWXcwSkdjWndmTiZzPWNvbnN1bWVyc2VjcmV0Jng9YjM-";

        String url = baseURL + "?" + params;

        System.out.println(url);

        Document document = sendGet(url);
        Element list = document.getDocumentElement();
        System.out.println(list.getChildNodes().item(0).getNodeName());
        String distance = list.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getChildNodes().item(2).getNodeValue();

        System.out.println("あ");
        return Double.parseDouble(distance);
    }

    private void sendPost() throws Exception {

        // form parameters
        Map<Object, Object> data = new HashMap<>();
        data.put("username", "abc");
        data.put("password", "123");
        data.put("custom", "secret");
        data.put("ts", System.currentTimeMillis());

        HttpRequest request = HttpRequest.newBuilder()
                .POST(buildFormDataFromMap(data))
                .uri(URI.create("https://httpbin.org/post"))
                .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // print status code
        System.out.println(response.statusCode());

        // print response body
        System.out.println(response.body());

    }

    private static HttpRequest.BodyPublisher buildFormDataFromMap(Map<Object, Object> data) {
        var builder = new StringBuilder();
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
        }
        System.out.println(builder.toString());
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }

}
