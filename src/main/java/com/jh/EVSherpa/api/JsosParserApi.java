package com.jh.EVSherpa.api;

import com.jh.EVSherpa.global.config.KeyInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.URL;

public class JsosParserApi {
    public static void main(String[] args) throws IOException {
        KeyInfo keyInfo = new KeyInfo();

        String urlBuilder = "http://apis.data.go.kr/B552584/EvCharger/getChargerInfo" /*URL*/
        +"?serviceKey="+"WJo08u7NjS7h%2FNNZuvLvDZssjBLtqhGdpO939Mzlh9TERxC9Q7k%2BKrxh0MJfafGGlS8NrJLhDFxcy6kVcp5upA%3D%3D" /*Service Key*/
        +"&pageNo=1" /*페이지번호*/
        +"&numOfRows=1000" /*한 페이지 결과 수 (최소 10, 최대 9999)*/
        +"&dataType=JSON";
        try {
            URL url = new URL(urlBuilder);
            BufferedReader br = new BufferedReader (new InputStreamReader(url.openStream(), "UTF-8"));
            String result = br.readLine();

            JSONParser jsonParser = new JSONParser();
            JSONObject parse = (JSONObject) jsonParser.parse(result);
            long totalCount = (long)parse.get("totalCount");
            System.out.println(parse.get("totalCount"));
            JSONObject items = (JSONObject) parse.get("items");
            JSONArray item = (JSONArray) items.get("item");


            JSONObject tempItem = (JSONObject) item.get(0);
            System.out.println(tempItem.get("statNm"));

//            System.out.println(result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
