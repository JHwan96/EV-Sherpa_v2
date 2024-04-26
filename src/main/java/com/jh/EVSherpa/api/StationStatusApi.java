package com.jh.EVSherpa.api;

import com.jh.EVSherpa.dto.StationStatusDto;
import com.jh.EVSherpa.dto.StationStatusUpdateDto;
import com.jh.EVSherpa.dto.enums.ChargerStatus;
import com.jh.EVSherpa.global.config.KeyInfo;
import com.jh.EVSherpa.global.utility.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class StationStatusApi {
    @Autowired
    KeyInfo keyInfo;

    //충전소 상태 정보 반환 API
    public List<StationStatusDto> callStationStatusApi() {
        List<StationStatusDto> apiDto = new ArrayList<>();

        String url = /*URL*/ "http://apis.data.go.kr/B552584/EvCharger/getChargerStatus"
                + "?" + URLEncoder.encode("serviceKey", StandardCharsets.UTF_8) + "=" + keyInfo.getServerKey() /*Service Key*/
                + "&" + URLEncoder.encode("pageNo", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("1", StandardCharsets.UTF_8) /*페이지 번호*/
                + "&" + URLEncoder.encode("numOfRows", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("1000", StandardCharsets.UTF_8) /*한 페이지 결과 수 (최소 10, 최대 9999)*/
                + "&" + URLEncoder.encode("period", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("3", StandardCharsets.UTF_8); /*상태갱신 조회 범위(분) (기본값 5, 최소 1, 최대 10)*/
//                    + "&" + URLEncoder.encode("zcode", "UTF-8") + "=" + URLEncoder.encode("11", "UTF-8"); /*시도 코드 (행정구역코드 앞 2자리)*/
        System.out.println(url);
        try {
            DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dbuilder = dbfactory.newDocumentBuilder();
            Document parse = dbuilder.parse(url);

            parse.getDocumentElement().normalize();
            NodeList nList = parse.getElementsByTagName("item");

            for (int i = 0; i < nList.getLength(); i++) {
                Node item = nList.item(i);
                if (item.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) item;

                    StationStatusDto check = StationStatusDto.builder()
                            .businessId(getTextFromTag(e, "busiId"))
                            .stationChargerId(getTextFromTag(e, "statId")+getTextFromTag(e, "chgerId"))
                            .status(ChargerStatus.of(getTextFromTag(e, "stat")))
                            .stationUpdateDate(DateTimeUtils.dateTimeFormat(getTextFromTag(e, "statUpdDt")))
                            .lastChargeStart(DateTimeUtils.dateTimeFormat(getTextFromTag(e, "lastTsdt")))
                            .lastChargeEnd(DateTimeUtils.dateTimeFormat(getTextFromTag(e, "lastTedt")))
                            .nowChargeStart(DateTimeUtils.dateTimeFormat(getTextFromTag(e, "nowTsdt")))
                            .build();
                    apiDto.add(check);
                }
            }
        } catch (IOException | ParserConfigurationException | SAXException e) { //TODO: 차후 처리
            e.printStackTrace();
        }
        log.info("apiDto size : {}", apiDto.size());
        return apiDto;
    }
    //충전소 상태 정보 반환 API
    public List<StationStatusUpdateDto> callStationStatusApiForUpdate() {
        List<StationStatusUpdateDto> apiDto = new ArrayList<>();

        String url = /*URL*/ "http://apis.data.go.kr/B552584/EvCharger/getChargerStatus"
                + "?" + URLEncoder.encode("serviceKey", StandardCharsets.UTF_8) + "=" + keyInfo.getServerKey() /*Service Key*/
                + "&" + URLEncoder.encode("pageNo", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("1", StandardCharsets.UTF_8) /*페이지 번호*/
                + "&" + URLEncoder.encode("numOfRows", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("1000", StandardCharsets.UTF_8) /*한 페이지 결과 수 (최소 10, 최대 9999)*/
                + "&" + URLEncoder.encode("period", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("3", StandardCharsets.UTF_8); /*상태갱신 조회 범위(분) (기본값 5, 최소 1, 최대 10)*/
//                    + "&" + URLEncoder.encode("zcode", "UTF-8") + "=" + URLEncoder.encode("11", "UTF-8"); /*시도 코드 (행정구역코드 앞 2자리)*/
        System.out.println(url);
        try {
            DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dbuilder = dbfactory.newDocumentBuilder();
            Document parse = dbuilder.parse(url);

            parse.getDocumentElement().normalize();
            NodeList nList = parse.getElementsByTagName("item");

            for (int i = 0; i < nList.getLength(); i++) {
                Node item = nList.item(i);
                if (item.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) item;

                    StationStatusUpdateDto check = StationStatusUpdateDto.builder()
                            .stationChargerId(getTextFromTag(e, "statId")+getTextFromTag(e, "chgerId"))
                            .status(ChargerStatus.of(getTextFromTag(e, "stat")))
                            .stationUpdateDate(DateTimeUtils.dateTimeFormat(getTextFromTag(e, "statUpdDt")))
                            .lastChargeStart(DateTimeUtils.dateTimeFormat(getTextFromTag(e, "lastTsdt")))
                            .lastChargeEnd(DateTimeUtils.dateTimeFormat(getTextFromTag(e, "lastTedt")))
                            .nowChargeStart(DateTimeUtils.dateTimeFormat(getTextFromTag(e, "nowTsdt")))
                            .build();
                    apiDto.add(check);
                }
            }
        } catch (IOException | ParserConfigurationException | SAXException e) { //TODO: 차후 처리
            e.printStackTrace();
        }
        log.info("apiDto size : {}", apiDto.size());
        return apiDto;
    }



    private String getTextFromTag(Element element, String tag) {
        return element.getElementsByTagName(tag).item(0).getTextContent();
    }

}
