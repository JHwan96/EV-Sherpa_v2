package com.jh.EVSherpa.api;

import com.jh.EVSherpa.dto.StationInfoDto;
import com.jh.EVSherpa.dto.StationInfoUpdateDto;
import com.jh.EVSherpa.dto.enums.ChargerMethod;
import com.jh.EVSherpa.dto.enums.ChargerStatus;
import com.jh.EVSherpa.dto.enums.ChargerType;
import com.jh.EVSherpa.exception.ApiProblemException;
import com.jh.EVSherpa.global.config.KeyInfo;
import com.jh.EVSherpa.global.utility.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class StationInfoApi {
    @Autowired
    KeyInfo keyInfo;

    // StationInfo를 반환하는 API 호출 메소드

    public List<List<StationInfoDto>> callStationInfoApi() {
        List<List<StationInfoDto>> apiDtoList = new ArrayList<>();
        List<String> urlList = new ArrayList<>();
        int totalCount = getTotalCount();
        int pageCount = (totalCount / 9999) + 1;

        for (int i = 1; i <= pageCount; i++) {
            String temp = "http://apis.data.go.kr/B552584/EvCharger/getChargerInfo"
                    + "?serviceKey=" + keyInfo.getServerKey() /*Service Key*/
                    + "&pageNo=" + i // 페이지번호
                    + "&numOfRows=9999";  // 한 페이지 결과 수 (최소 10, 최대 9999)
            urlList.add(temp);
        }

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            for (int i = 0; i < 20/*urlList.size()*/; i++) {
                log.info("callStationInfoApi : {}번째", i);
                String url = urlList.get(i);
                List<StationInfoDto> tempDto = new ArrayList<>();

                Document parse = dBuilder.parse(url);
                parse.getDocumentElement().normalize();
                NodeList nList = parse.getElementsByTagName("item");

                for (int j = 0; j < nList.getLength(); j++) {
                    Node item = nList.item(j);
                    if (item.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) item;
                        StationInfoDto dto = buildStationInfoDto(element);
                        tempDto.add(dto);
                    }
                }
                apiDtoList.add(tempDto);
            }
            log.info("Dto transform END");
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new ApiProblemException("API 호출에 문제가 발생했습니다.");
        }
        return apiDtoList;
    }

    // 테스트용 (XML)
    public List<StationInfoDto> callStationInfoApiForXML() {
        List<StationInfoDto> apiDtoList = new ArrayList<>();
        String url = "http://apis.data.go.kr/B552584/EvCharger/getChargerInfo"
                + "?serviceKey=" + keyInfo.getServerKey() /*Service Key*/
                + "&pageNo=1" // 페이지번호
                + "&numOfRows=8000";  // 한 페이지 결과 수 (최소 10, 최대 9999)

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            Document parse = dBuilder.parse(url);
            parse.getDocumentElement().normalize();
            NodeList nList = parse.getElementsByTagName("item");

            for (int j = 0; j < nList.getLength(); j++) {
                Node item = nList.item(j);
                if (item.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) item;
                    StationInfoDto dto = buildStationInfoDto(element);
                    apiDtoList.add(dto);
                }
            }

            log.info("Dto transform END");
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new ApiProblemException("API 호출에 문제가 발생했습니다.");
        }
        return apiDtoList;
    }

    // 테스트용2 (JSON)
    public List<StationInfoDto> callStationInfoApiForJson() {
        List<StationInfoDto> apiDtoList = new ArrayList<>();
        String urlBuilder = "http://apis.data.go.kr/B552584/EvCharger/getChargerInfo" /*URL*/
                + "?serviceKey=" + keyInfo.getServerKey() /*Service Key*/
                + "&pageNo=1" /*페이지번호*/
                + "&numOfRows=9999" /*한 페이지 결과 수 (최소 10, 최대 9999)*/
                + "&dataType=JSON";
        try {
            URL url = new URL(urlBuilder);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String result = br.readLine();

            JSONParser jsonParser = new JSONParser();
            JSONObject parse = (JSONObject) jsonParser.parse(result);
            long totalCount = (long) parse.get("totalCount");

            JSONObject items = (JSONObject) parse.get("items");
            JSONArray item = (JSONArray) items.get("item");

            for (int i = 0; i < item.size(); i++) {
                JSONObject jsonObject = (JSONObject) item.get(i);
                StationInfoDto stationInfo = getStationInfoDtoFronJson(jsonObject);
                apiDtoList.add(stationInfo);
            }
            log.info("dtoList size : {}", apiDtoList.size());
        } catch (Exception e) {
            throw new ApiProblemException("API 호출에 문제가 발생했습니다.");
        }
        return apiDtoList;
    }


    // 전체 개수 가져오는 메소드

    private int getTotalCount() {
        String url = "http://apis.data.go.kr/B552584/EvCharger/getChargerInfo"
                + "?serviceKey=" + keyInfo.getServerKey() /*Service Key*/
                + "&pageNo=1" /*페이지번호*/
                + "&numOfRows=10";  /*한 페이지 결과 수 (최소 10, 최대 9999)*/
        int totalCount;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document parse = dBuilder.parse(url);

            Element headerElement = (Element) parse.getElementsByTagName("header").item(0);
            Element totalCountElement = (Element) headerElement.getElementsByTagName("totalCount").item(0);
            String total = totalCountElement.getTextContent();
            totalCount = Integer.parseInt(total);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new ApiProblemException("API 호출에 문제가 발생했습니다.");
        }
        return totalCount;
    }
    // StationInfoUpdate를 반환하는 API 호출 메소드

    public List<StationInfoUpdateDto> callStationInfoApiForUpdateForTest() {
        long start = System.currentTimeMillis();
        List<StationInfoUpdateDto> apiDto = new ArrayList<>();

        String url = /*URL*/ "http://apis.data.go.kr/B552584/EvCharger/getChargerInfo"
                + "?" + URLEncoder.encode("serviceKey", StandardCharsets.UTF_8) + "=" + keyInfo.getServerKey() /*Service Key*/
                + "&" + URLEncoder.encode("pageNo", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("1", StandardCharsets.UTF_8) /*페이지번호*/
                + "&" + URLEncoder.encode("numOfRows", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("9999", StandardCharsets.UTF_8);  /*한 페이지 결과 수 (최소 10, 최대 9999)*/
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
        long end = System.currentTimeMillis();
        log.info("callStationInfoApiForUpdate : {}s", (float) (end - start) / 1000);
        return apiDto;
    }

    public List<List<StationInfoUpdateDto>> callStationInfoApiForUpdate() {
        List<List<StationInfoUpdateDto>> apiDtoList = new ArrayList<>();
        List<String> urlList = new ArrayList<>();
        int totalCount = getTotalCount();
        int pageCount = (totalCount / 9999) + 1;

        for (int i = 1; i <= pageCount; i++) {
            String temp = "http://apis.data.go.kr/B552584/EvCharger/getChargerInfo"
                    + "?serviceKey=" + keyInfo.getServerKey() /*Service Key*/
                    + "&pageNo=" + i // 페이지번호
                    + "&numOfRows=9999";  // 한 페이지 결과 수 (최소 10, 최대 9999)
            urlList.add(temp);
        }

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            for (int i = 0; i < 20/*urlList.size()*/; i++) {
                log.info("Start : {}번째", i);
                String url = urlList.get(i);
                List<StationInfoUpdateDto> tempDto = new ArrayList<>();

                Document parse = dBuilder.parse(url);
                parse.getDocumentElement().normalize();
                NodeList nList = parse.getElementsByTagName("item");

                for (int j = 0; j < nList.getLength(); j++) {
                    Node item = nList.item(j);
                    if (item.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) item;
                        StationInfoUpdateDto dto = buildStationInfoUpdateDto(element);
                        tempDto.add(dto);
                    }
                }
                apiDtoList.add(tempDto);
            }
            log.info("Dto transform END");
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new ApiProblemException("API 호출에 문제가 발생했습니다.");
        }
        return apiDtoList;
    }


    private StationInfoDto getStationInfoDtoFronJson(JSONObject jsonObject) {
        return StationInfoDto.builder()
                .stationName(getStringFromJson(jsonObject, "statNm"))
                .stationChargerId(getStringFromJson(jsonObject, "statId") + getStringFromJson(jsonObject, "chgerId"))
                .chargerType(ChargerType.of(getStringFromJson(jsonObject, "chgerType")))
                .address(getStringFromJson(jsonObject, "addr"))
                .location(getStringFromJson(jsonObject, "location"))
                .position(getPositionFromString(getStringFromJson(jsonObject, "lat"), getStringFromJson(jsonObject, "lng")))
                .useTime(getStringFromJson(jsonObject, "useTime"))
                .businessId(getStringFromJson(jsonObject, "busiId"))
                .businessName(getStringFromJson(jsonObject, "bnm"))
                .operatorName(getStringFromJson(jsonObject, "busiNm"))
                .operatorCall(getStringFromJson(jsonObject, "busiCall"))
                .status(ChargerStatus.of(getStringFromJson(jsonObject, "stat")))
                .stationUpdateDate(DateTimeUtils.dateTimeFormat(getStringFromJson(jsonObject, "statUpdDt")))
                .lastChargeStart(DateTimeUtils.dateTimeFormat(getStringFromJson(jsonObject, "lastTsdt")))
                .lastChargeEnd(DateTimeUtils.dateTimeFormat(getStringFromJson(jsonObject, "lastTedt")))
                .nowChargeStart(DateTimeUtils.dateTimeFormat(getStringFromJson(jsonObject, "nowTsdt")))
                .output(stringToIntegerJson(jsonObject, "output"))
                .chargerMethod(ChargerMethod.of(getStringFromJson(jsonObject, "method")))
                .zcode(getStringFromJson(jsonObject, "zcode"))
                .zscode(getStringFromJson(jsonObject, "zscode"))
                .kind(getStringFromJson(jsonObject, "kind"))
                .kindDetail(getStringFromJson(jsonObject, "kindDetail"))
                .parkingFree(getStringFromJson(jsonObject, "parkingFree"))
                .notation(getStringFromJson(jsonObject, "note"))
                .limitYn(getStringFromJson(jsonObject, "limitYn"))
                .limitDetail(getStringFromJson(jsonObject, "limitDetail"))
                .deleteYn(getStringFromJson(jsonObject, "delYn"))
                .deleteDetail(getStringFromJson(jsonObject, "delDetail"))
                .trafficYn(getStringFromJson(jsonObject, "trafficYn"))
                .build();
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
                .output(stringToInteger(element, "output"))
                .chargerMethod(ChargerMethod.of(getTextFromTag(element, "method")))
                .zcode(getTextFromTag(element, "zcode"))
                .zscode(getTextFromTag(element, "zscode"))
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
                .output(stringToInteger(element, "output"))
                .status(ChargerStatus.of(getTextFromTag(element, "stat")))
                .stationUpdateDate(DateTimeUtils.dateTimeFormat(getTextFromTag(element, "statUpdDt")))
                .lastChargeStart(DateTimeUtils.dateTimeFormat(getTextFromTag(element, "lastTsdt")))
                .lastChargeEnd(DateTimeUtils.dateTimeFormat(getTextFromTag(element, "lastTedt")))
                .nowChargeStart(DateTimeUtils.dateTimeFormat(getTextFromTag(element, "nowTsdt")))
                .parkingFree(getTextFromTag(element, "parkingFree"))
                .notation(getTextFromTag(element, "note"))
                .limitYn(getTextFromTag(element, "limitYn"))
                .limitDetail(getTextFromTag(element, "limitDetail"))
                .deleteYn(getTextFromTag(element, "delYn"))
                .deleteDetail(getTextFromTag(element, "delDetail"))
                .trafficYn(getTextFromTag(element, "trafficYn"))
                .build();
    }

    private String getStringFromJson(JSONObject jsonObject, String key) {
        return (String) jsonObject.get(key);
    }

    private Integer stringToInteger(Element element, String tag) {
        String output = getTextFromTag(element, tag);
        if (output.isEmpty()) {
            return null;
        } else {
            return Integer.parseInt(output);
        }
    }

    private Integer stringToIntegerJson(JSONObject jsonObject, String key) {
        String output = getStringFromJson(jsonObject, key);
        if (output.isEmpty()) {
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
        Point point = geometryFactory.createPoint(new Coordinate(lng, lat));        // 변경 해봐야할 부분
        int SRID = 4326;
        point.setSRID(SRID);

        return point;
    }
}
