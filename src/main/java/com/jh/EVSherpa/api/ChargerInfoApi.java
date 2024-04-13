package com.jh.EVSherpa.api;

import com.jh.EVSherpa.global.config.KeyInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URLEncoder;

public class ChargerInfoApi {
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        KeyInfo keyInfo = new KeyInfo();
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552584/EvCharger/getChargerInfo"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + keyInfo.getServerKey()); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("7", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수 (최소 10, 최대 9999)*/
//        urlBuilder.append("&" + URLEncoder.encode("zcode","UTF-8") + "=" + URLEncoder.encode("11", "UTF-8")); /*시도 코드 (행정구역코드 앞 2자리)*/

        String[] TAG_LIST = {"statNm", "statId", "chgerId", "chgerType", "addr", "lat", "lng", "useTime", "busiId", "bnm",
                "busiNm", "busiCall", "stat", "statUpdDt", "lastTsdt", "lastTedt", "nowTsdt", "output", "method", "zcode", "zscode", "kind", "kindDetail", "parkingFree", "note",
                "limitYn", "limitDetail", "delYn", "delDetail", "trafficYn"};

        String urlStr = urlBuilder.toString();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document parse = dBuilder.parse(urlStr);

        parse.getDocumentElement().normalize();
        NodeList nList = parse.getElementsByTagName("item");

        for (int i = 0; i < nList.getLength(); i++) {
            Node item = nList.item(i);
            if (item.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) item;
                for (String tag : TAG_LIST) {
                    Node node = e.getElementsByTagName(tag).item(0);         // 빈 칸은 empty
                    System.out.println(tag + " : " + node.getTextContent());        //TODO: list로 반환
                }
            }
        }
    }
}
