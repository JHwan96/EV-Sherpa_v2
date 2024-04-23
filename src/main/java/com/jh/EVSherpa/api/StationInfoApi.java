package com.jh.EVSherpa.api;

import com.jh.EVSherpa.dto.StationInfoDto;
import com.jh.EVSherpa.dto.StationInfoUpdateDto;
import com.jh.EVSherpa.dto.enums.ChargerMethod;
import com.jh.EVSherpa.dto.enums.ChargerStatus;
import com.jh.EVSherpa.dto.enums.ChargerType;
import com.jh.EVSherpa.global.config.KeyInfo;
import com.jh.EVSherpa.global.utility.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
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
public class StationInfoApi {
    @Autowired
    KeyInfo keyInfo;

    // StationInfo를 반환하는 API 호출 메서드
    public List<StationInfoDto> callStationInfoApi() {
        List<StationInfoDto> apiDto = new ArrayList<>();

        String url = /*URL*/ "http://apis.data.go.kr/B552584/EvCharger/getChargerInfo"
                + "?" + URLEncoder.encode("serviceKey", StandardCharsets.UTF_8) + "=" + keyInfo.getServerKey() /*Service Key*/
                + "&" + URLEncoder.encode("pageNo", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("7", StandardCharsets.UTF_8) /*페이지번호*/
                + "&" + URLEncoder.encode("numOfRows", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("4000", StandardCharsets.UTF_8);  /*한 페이지 결과 수 (최소 10, 최대 9999)*/
//               + "&" + URLEncoder.encode("zcode", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("11", StandardCharsets.UTF_8); /*시도 코드 (행정구역코드 앞 2자리)*/
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document parse = dBuilder.parse(url);

            parse.getDocumentElement().normalize();
            NodeList nList = parse.getElementsByTagName("item");

            for (int i = 0; i < nList.getLength(); i++) {
                Node item = nList.item(i);
                if (item.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) item;
                    StationInfoDto dto = buildStationInfoDto(element);
                    apiDto.add(dto);
                }
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        return apiDto;
    }

    // StationInfoUpdate를 반환하는 API 호출 메서드
    public List<StationInfoUpdateDto> callStationInfoApiForUpdate() {
        List<StationInfoUpdateDto> apiDto = new ArrayList<>();

        String url = /*URL*/ "http://apis.data.go.kr/B552584/EvCharger/getChargerInfo"
                + "?" + URLEncoder.encode("serviceKey", StandardCharsets.UTF_8) + "=" + keyInfo.getServerKey() /*Service Key*/
                + "&" + URLEncoder.encode("pageNo", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("7", StandardCharsets.UTF_8) /*페이지번호*/
                + "&" + URLEncoder.encode("numOfRows", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("4000", StandardCharsets.UTF_8);  /*한 페이지 결과 수 (최소 10, 최대 9999)*/
//               + "&" + URLEncoder.encode("zcode", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("11", StandardCharsets.UTF_8); /*시도 코드 (행정구역코드 앞 2자리)*/
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document parse = dBuilder.parse(url);

            parse.getDocumentElement().normalize();
            NodeList nList = parse.getElementsByTagName("item");

            for (int i = 0; i < nList.getLength(); i++) {
                Node item = nList.item(i);
                if (item.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) item;
                    StationInfoUpdateDto dto = buildStationInfoUpdateDto(element);
                    apiDto.add(dto);
                }
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        return apiDto;
    }

    //ChargerInfoDto 생성 메서드
    private StationInfoDto buildStationInfoDto(Element element) {
        return StationInfoDto.builder()
                .stationName(getTextFromTag(element, "statNm"))
                .stationChargerId(getTextFromTag(element, "statId") + getTextFromTag(element, "chgerId"))
                .chargerType(ChargerType.of(getTextFromTag(element, "chgerType")))
                .address(getTextFromTag(element, "addr"))
                .location(getTextFromTag(element, "location"))
                .position(getPositionFromString(getTextFromTag(element, "lat"), getTextFromTag(element, "lng")))
                .useTime(getTextFromTag(element, "useTime"))
                .businessId(getTextFromTag(element, "busiId"))
                .businessName(getTextFromTag(element, "bnm"))
                .operatorName(getTextFromTag(element, "busiNm"))
                .operatorCall(getTextFromTag(element, "busiCall"))
                .status(ChargerStatus.of(getTextFromTag(element, "stat")))
                .stationUpdateDate(DateTimeUtils.dateTimeFormat(getTextFromTag(element, "statUpdDt")))
                .lastChargeStart(DateTimeUtils.dateTimeFormat(getTextFromTag(element, "lastTsdt")))
                .lastChargeEnd(DateTimeUtils.dateTimeFormat(getTextFromTag(element, "lastTedt")))
                .nowChargeStart(DateTimeUtils.dateTimeFormat(getTextFromTag(element, "nowTsdt")))
                .output(integerToString(element, "output"))
                .chargerMethod(ChargerMethod.of(getTextFromTag(element, "method")))
                .zcode(getTextFromTag(element, "zcode"))
                .kind(getTextFromTag(element, "kind"))
                .kindDetail(getTextFromTag(element, "kindDetail"))
                .parkingFree(getTextFromTag(element, "parkingFree"))
                .notation(getTextFromTag(element, "note"))
                .limitYn(getTextFromTag(element, "limitYn"))
                .limitDetail(getTextFromTag(element, "limitDetail"))
                .deleteYn(getTextFromTag(element, "delYn"))
                .deleteDetail(getTextFromTag(element, "delDetail"))
                .trafficYn(getTextFromTag(element, "trafficYn"))
                .build();
    }

    private StationInfoUpdateDto buildStationInfoUpdateDto(Element element) {
        return StationInfoUpdateDto.builder()
                .stationChargerId(getTextFromTag(element, "statId") + getTextFromTag(element, "chgerId"))
                .chargerType(ChargerType.of(getTextFromTag(element, "chgerType")))
                .useTime(getTextFromTag(element, "useTime"))
                .operatorName(getTextFromTag(element, "busiNm"))
                .operatorCall(getTextFromTag(element, "busiCall"))
                .output(integerToString(element, "output"))
                .parkingFree(getTextFromTag(element, "parkingFree"))
                .notation(getTextFromTag(element, "note"))
                .limitYn(getTextFromTag(element, "limitYn"))
                .limitDetail(getTextFromTag(element, "limitDetail"))
                .deleteYn(getTextFromTag(element, "delYn"))
                .deleteDetail(getTextFromTag(element, "delDetail"))
                .trafficYn(getTextFromTag(element, "trafficYn"))
                .build();
    }

    private Integer integerToString(Element element, String tag) {
        String output = getTextFromTag(element, tag);
        if(output.isEmpty()){
            return null;
        } else {
            return Integer.parseInt(output);
        }
    }

    private String getTextFromTag(Element element, String tag) {
        return element.getElementsByTagName(tag).item(0).getTextContent();
    }

    private Point getPositionFromString(String latitude, String longitude) { //TODO: Utility로 이동 고려
        GeometryFactory geometryFactory = new GeometryFactory();
        double lat = Double.parseDouble(latitude);
        double lng = Double.parseDouble(longitude);
        Point point = geometryFactory.createPoint(new Coordinate(lng, lat));
        int SRID = 4326;
        point.setSRID(SRID);

        return point;
    }
}
