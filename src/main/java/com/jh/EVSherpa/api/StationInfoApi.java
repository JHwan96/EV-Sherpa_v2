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
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class StationInfoApi {
    @Autowired
    KeyInfo keyInfo;

    //TODO: 사용할 것 (JSON)
    //전체 저장
    public List<List<StationInfoDto>> callAllStationInfoApi(int totalCount) {
        List<List<StationInfoDto>> apiDtoLists = new ArrayList<>();
        List<String> urlList = new ArrayList<>();
        int pageCount = (totalCount / 9999) + 1;
        log.info("totalCountJson : {}, pageCount : {}", totalCount, pageCount);

        for (int i = 1; i < pageCount + 1; i++) {
            String tempStr = "http://apis.data.go.kr/B552584/EvCharger/getChargerInfo" /*URL*/
                    + "?serviceKey=" + keyInfo.getServerKey() /*Service Key*/
                    + "&pageNo=" + i /*페이지번호*/
                    + "&numOfRows=9999" /*한 페이지 결과 수 (최소 10, 최대 9999)*/
                    + "&dataType=JSON";
            urlList.add(tempStr);
        }

        HttpClient httpClient = HttpClient.newHttpClient();
        List<CompletableFuture<List<StationInfoDto>>> futureList = new ArrayList<>();

        for (String urlBuilder : urlList) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlBuilder))
                    .build();
            CompletableFuture<HttpResponse<String>> future = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            CompletableFuture<List<StationInfoDto>> asyncDtoList = future.thenApply(HttpResponse::body)
                    .thenApplyAsync(result -> {
                        List<StationInfoDto> apiDtoList = new ArrayList<>();
                        try {
                            JSONParser jsonParser = new JSONParser();
                            JSONObject parse = (JSONObject) jsonParser.parse(result);

                            JSONObject items = (JSONObject) parse.get("items");
                            JSONArray item = (JSONArray) items.get("item");

                            for (int j = 0; j < item.size(); j++) {
                                JSONObject jsonObject = (JSONObject) item.get(j);
                                StationInfoDto stationInfo = getStationInfoDtoFromJson(jsonObject);
                                apiDtoList.add(stationInfo);
                            }
                            log.info("dtoList size : {}", apiDtoList.size());
                        } catch (Exception e) {
                            throw new ApiProblemException("API 호출에 문제가 발생했습니다.");
                        }
                        return apiDtoList;
                    })
                    .exceptionally(ex -> {
                        throw new ApiProblemException("API 호출에 문제가 발생했습니다.");
                    });
            futureList.add(asyncDtoList);
        }
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0]));
        allOf.join();

        for (CompletableFuture<List<StationInfoDto>> future : futureList) {
            apiDtoLists.add(future.join());
        }

        return apiDtoLists;
    }


    // 9999개만 저장
    //test용
    public List<StationInfoDto> callStationInfoApiForTest() {
//        List<StationInfoDto> apiDtoList = new ArrayList<>();
        String urlBuilder = "http://apis.data.go.kr/B552584/EvCharger/getChargerInfo" /*URL*/
                + "?serviceKey=" + keyInfo.getServerKey() /*Service Key*/
                + "&pageNo=1" /*페이지번호*/
                + "&numOfRows=9999" /*한 페이지 결과 수 (최소 10, 최대 9999)*/
                + "&dataType=JSON";

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlBuilder))
                .build();
        CompletableFuture<HttpResponse<String>> future = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        CompletableFuture<List<StationInfoDto>> asyncDtoList = future.thenApply(HttpResponse::body)
                .thenApplyAsync(result -> {
                    List<StationInfoDto> apiDtoList = new ArrayList<>();
                    try {
                        JSONParser jsonParser = new JSONParser();
                        JSONObject parse = (JSONObject) jsonParser.parse(result);

                        JSONObject items = (JSONObject) parse.get("items");
                        JSONArray item = (JSONArray) items.get("item");

                        for (int i = 0; i < item.size(); i++) {
                            JSONObject jsonObject = (JSONObject) item.get(i);
                            StationInfoDto stationInfo = getStationInfoDtoFromJson(jsonObject);
                            apiDtoList.add(stationInfo);
                        }
                        log.info("dtoList size : {}", apiDtoList.size());
                    } catch (Exception e) {
                        throw new ApiProblemException("API 호출에 문제가 발생했습니다.");
                    }
                    return apiDtoList;
                })
                .exceptionally(ex -> {
                    throw new ApiProblemException("API 호출에 문제가 발생했습니다.");
                });

        try {
            return asyncDtoList.get();
        } catch (Exception e) {
            throw new ApiProblemException("API 호출에 문제가 발생했습니다.");
        }
    }

    // StationInfoUpdate를 반환하는 API 호출 메소드 (JSON)
    public List<List<StationInfoUpdateDto>> callStationInfoApiForUpdate(int totalCount) {
        int pageCount = (totalCount / 9999) + 1;
        List<List<StationInfoUpdateDto>> apiDtoLists = new ArrayList<>();
        List<String> urlList = new ArrayList<>();

        for (int i = 1; i < pageCount + 1; i++) {
            String tempStr = "http://apis.data.go.kr/B552584/EvCharger/getChargerInfo" /*URL*/
                    + "?serviceKey=" + keyInfo.getServerKey() /*Service Key*/
                    + "&pageNo=" + i /*페이지번호*/
                    + "&numOfRows=9999" /*한 페이지 결과 수 (최소 10, 최대 9999)*/
                    + "&dataType=JSON";
            urlList.add(tempStr);
        }

        log.info("totalCount : {}", totalCount);

        HttpClient httpClient = HttpClient.newHttpClient();
        List<CompletableFuture<List<StationInfoUpdateDto>>> futureList = new ArrayList<>();

        for (String urlBuilder : urlList) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlBuilder))
                    .build();

            CompletableFuture<HttpResponse<String>> future = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            CompletableFuture<List<StationInfoUpdateDto>> asyncDtoList = future.thenApply(HttpResponse::body)
                    .thenApplyAsync(result -> {
                        List<StationInfoUpdateDto> apiDtoList = new ArrayList<>();
                        try {
                            JSONParser jsonParser = new JSONParser();
                            JSONObject parse = (JSONObject) jsonParser.parse(result);

                            JSONObject items = (JSONObject) parse.get("items");
                            JSONArray item = (JSONArray) items.get("item");

                            for (int i = 0; i < item.size(); i++) {
                                JSONObject jsonObject = (JSONObject) item.get(i);
                                StationInfoUpdateDto stationInfo = getStationInfoUpdateDtoFromJson(jsonObject);
                                apiDtoList.add(stationInfo);
                            }
                            log.info("dtoList size : {}", apiDtoList.size());
                        } catch (Exception e) {
                            throw new ApiProblemException("API 호출에 문제가 발생했습니다.");
                        }
                        return apiDtoList;
                    })
                    .exceptionally(ex -> {
                        throw new ApiProblemException("API 호출에 문제가 발생했습니다.");
                    });
            futureList.add(asyncDtoList);
        }
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0]));
        allOf.join();

        for(CompletableFuture<List<StationInfoUpdateDto>> future : futureList) {
            apiDtoLists.add(future.join());
        }
        return apiDtoLists;

    }

    // 9999개를 갱신하는 메소드 (테스트용)
    public List<StationInfoUpdateDto> callStationInfoApiForUpdateTest() {
        String urlBuilder = "http://apis.data.go.kr/B552584/EvCharger/getChargerInfo" /*URL*/
                + "?serviceKey=" + keyInfo.getServerKey() /*Service Key*/
                + "&pageNo=1" /*페이지번호*/
                + "&numOfRows=9999" /*한 페이지 결과 수 (최소 10, 최대 9999)*/
                + "&dataType=JSON";

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlBuilder))
                .build();

        CompletableFuture<HttpResponse<String>> future = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        CompletableFuture<List<StationInfoUpdateDto>> asyncDtoList = future.thenApply(HttpResponse::body)
                .thenApplyAsync(result -> {
                    List<StationInfoUpdateDto> apiDtoList = new ArrayList<>();
                    try {
                        JSONParser jsonParser = new JSONParser();
                        JSONObject parse = (JSONObject) jsonParser.parse(result);

                        JSONObject items = (JSONObject) parse.get("items");
                        JSONArray item = (JSONArray) items.get("item");

                        for (int i = 0; i < item.size(); i++) {
                            JSONObject jsonObject = (JSONObject) item.get(i);
                            StationInfoUpdateDto stationInfo = getStationInfoUpdateDtoFromJson(jsonObject);
                            apiDtoList.add(stationInfo);
                        }
                        log.info("dtoList size : {}", apiDtoList.size());
                    } catch (Exception e) {
                        throw new ApiProblemException("API 호출에 문제가 발생했습니다.");
                    }
                    return apiDtoList;
                })
                .exceptionally(ex -> {
                    throw new ApiProblemException("API 호출에 문제가 발생했습니다.");
                });

        try {
            return asyncDtoList.get();
        } catch (Exception e) {
            throw new ApiProblemException("API 호출에 문제가 발생했습니다.");
        }
    }


    // 전체 개수 반환 (9999개 기준)
    // TODO: 사용할 것
    public int callApiForTotalCount() {
        String urlBuilder = "http://apis.data.go.kr/B552584/EvCharger/getChargerInfo" /*URL*/
                + "?serviceKey=" + keyInfo.getServerKey() /*Service Key*/
                + "&pageNo=1" /*페이지번호*/
                + "&numOfRows=9999" /*한 페이지 결과 수 (최소 10, 최대 9999)*/
                + "&dataType=JSON";

        long start = System.currentTimeMillis();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(urlBuilder);
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            String result = EntityUtils.toString(response.getEntity());

            log.info("getTotalCountJson: {}s", (float) (System.currentTimeMillis() - start) / 1000);
            try {
                JSONParser jsonParser = new JSONParser();
                JSONObject parse = (JSONObject) jsonParser.parse(result);
                long totalCount = (long) parse.get("totalCount");

                log.info("dtoList size : {}", totalCount);
                return (int) totalCount;
            } catch (Exception e) {
                throw new ApiProblemException("API 호출에 문제가 발생했습니다.");
            }
        } catch (Exception e) {
            throw new ApiProblemException("API 호출에 문제가 발생했습니다.");
        }
    }

    private StationInfoDto getStationInfoDtoFromJson(JSONObject jsonObject) {
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
                .output(stringToIntegerFromJson(jsonObject, "output"))
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

    private StationInfoUpdateDto getStationInfoUpdateDtoFromJson(JSONObject jsonObject) {
        return StationInfoUpdateDto.builder()
                .stationChargerId(getStringFromJson(jsonObject, "statId") + getStringFromJson(jsonObject, "chgerId"))
                .chargerType(ChargerType.of(getStringFromJson(jsonObject, "chgerType")))
                .useTime(getStringFromJson(jsonObject, "useTime"))
                .operatorName(getStringFromJson(jsonObject, "busiNm"))
                .operatorCall(getStringFromJson(jsonObject, "busiCall"))
                .status(ChargerStatus.of(getStringFromJson(jsonObject, "stat")))
                .stationUpdateDate(DateTimeUtils.dateTimeFormat(getStringFromJson(jsonObject, "statUpdDt")))
                .lastChargeStart(DateTimeUtils.dateTimeFormat(getStringFromJson(jsonObject, "lastTsdt")))
                .lastChargeEnd(DateTimeUtils.dateTimeFormat(getStringFromJson(jsonObject, "lastTedt")))
                .nowChargeStart(DateTimeUtils.dateTimeFormat(getStringFromJson(jsonObject, "nowTsdt")))
                .output(stringToIntegerFromJson(jsonObject, "output"))
                .parkingFree(getStringFromJson(jsonObject, "parkingFree"))
                .notation(getStringFromJson(jsonObject, "note"))
                .limitYn(getStringFromJson(jsonObject, "limitYn"))
                .limitDetail(getStringFromJson(jsonObject, "limitDetail"))
                .deleteYn(getStringFromJson(jsonObject, "delYn"))
                .deleteDetail(getStringFromJson(jsonObject, "delDetail"))
                .trafficYn(getStringFromJson(jsonObject, "trafficYn"))
                .build();
    }

    private String getStringFromJson(JSONObject jsonObject, String key) {
        return (String) jsonObject.get(key);
    }

    private Integer stringToIntegerFromJson(JSONObject jsonObject, String key) {
        String output = getStringFromJson(jsonObject, key);
        if (output.isEmpty()) {
            return null;
        } else {
            return Integer.parseInt(output);
        }
    }

    private Point getPositionFromString(String latitude, String longitude) {
        GeometryFactory geometryFactory = new GeometryFactory();
        double lat = Double.parseDouble(latitude);
        double lng = Double.parseDouble(longitude);
        Point point = geometryFactory.createPoint(new Coordinate(lng, lat));    // lng, lat 순
        int SRID = 4326;
        point.setSRID(SRID);

        return point;
    }
}
