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

public class ChargerStatusApi {
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        KeyInfo keyInfo = new KeyInfo();
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552584/EvCharger/getChargerStatus"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + keyInfo.getServerKey()); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지 번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수 (최소 10, 최대 9999)*/
        urlBuilder.append("&" + URLEncoder.encode("period", "UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*상태갱신 조회 범위(분) (기본값 5, 최소 1, 최대 10)*/
//        urlBuilder.append("&" + URLEncoder.encode("zcode","UTF-8") + "=" + URLEncoder.encode("11", "UTF-8")); /*시도 코드 (행정구역코드 앞 2자리)*/

        String[] TAG_LIST = {"busiId", "statId", "chgerId", "stat", "statUpdDt", "lastTsdt", "lastTedt", "nowTsdt"};
        String url = urlBuilder.toString();

        DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dbuilder = dbfactory.newDocumentBuilder();
        Document parse = dbuilder.parse(url);

        parse.getDocumentElement().normalize();
        NodeList nList = parse.getElementsByTagName("item");

        for (int i = 0; i < nList.getLength(); i++) {
            Node item = nList.item(i);
            if (item.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) item;
                for (String tag : TAG_LIST) {
                    System.out.println(tag + " : " + e.getElementsByTagName(tag).item(0).getTextContent());       //TODO: list로 반환
                }
            }
        }
    }
}
