package com.tiscon.service;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.xml.parsers.*;
import javax.xml.xpath.*;
import org.w3c.dom.*;

public class LocalSearch {

    private final String appid;

    public LocalSearch(String appid) {
        this.appid = appid;
    }

    private Document get(String url, Map<String, String> headers) throws Exception {

        URL urlObj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

        // timeout
        con.setConnectTimeout(3 * 1000);
        con.setReadTimeout(3 * 1000);

        // HTTP method
        con.setRequestMethod("GET");

        // HTTP request headers
        for (Map.Entry<String, String> header : headers.entrySet()) {
            con.addRequestProperty(header.getKey(), header.getValue());
        }

        con.connect();

        InputStream is = con.getInputStream();
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
        is.close();

        return doc;
    }

    private List<Properties> xml2pois(Document doc) throws XPathExpressionException {

        List<Properties> result = new ArrayList<Properties>();

        XPath xpath = XPathFactory.newInstance().newXPath();
        NodeList features = (NodeList) xpath.evaluate("YDF/Feature", doc, XPathConstants.NODESET);
        for (int i = 0; i < features.getLength(); i++) {
            Node feature = features.item(i);
            String type = xpath.evaluate("Geometry/Type", feature);
            if (type.equals("point")) {
                String coordinates = xpath.evaluate("Geometry/Coordinates", feature);
                String[] ll = coordinates.split(",");
                Properties p = new Properties();
                p.setProperty("name", xpath.evaluate("Name", feature));
                p.setProperty("lat", ll[1]);
                p.setProperty("lon", ll[0]);
                result.add(p);
            }
        }
        return result;
    }

    private String encode(Map<String, String> params) throws UnsupportedEncodingException {
        String encoding = "UTF-8";
        List<String> list = new ArrayList<String>();
        for (Map.Entry<String, String> header : params.entrySet()) {
            String key = URLEncoder.encode(header.getKey(), encoding);
            String val = URLEncoder.encode(header.getValue(), encoding);
            list.add(key + "=" + val);
        }
        return String.join("&", list);
    }

    public List<Properties> search(String query) throws Exception {

        String baseURL = "https://map.yahooapis.jp/geocode/V1/geoCoder";

        Map<String, String> params = new LinkedHashMap<String, String>();//,や が入ると文字化け
        params.put("query", query);
        params.put("output", "xml");
        params.put("results", "3");
        params.put("sort", "score");

        String url = baseURL + "?" + encode(params);

        Map<String, String> headers = new LinkedHashMap<String, String>();
        headers.put("User-Agent", "Yahoo AppID: " + appid);

        Document doc = get(url, headers);
        return xml2pois(doc);
    }

}
