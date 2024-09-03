package com.jh.EVSherpa.api;

import com.jh.EVSherpa.dto.StationStatusDto;
import com.jh.EVSherpa.dto.enums.ChargerStatus;
import com.jh.EVSherpa.exception.ApiProblemException;
import com.jh.EVSherpa.global.config.KeyInfo;
import com.jh.EVSherpa.global.utility.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
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
public class StationStatusApi {
    @Autowired
    KeyInfo keyInfo;

    //충전소 상태 정보 반환 API
    public List<StationStatusDto> callStationStatusApi() {
        String url = /*URL*/ "http://apis.data.go.kr/B552584/EvCharger/getChargerStatus"
                + "?serviceKey=" + keyInfo.getServerKey() /*Service Key*/
                + "&pageNo=1" /*페이지 번호*/
                + "&numOfRows=9999" /*한 페이지 결과 수 (최소 10, 최대 9999)*/
                + "&period=3" /*상태갱신 조회 범위(분) (기본값 5, 최소 1, 최대 10)*/
                + "&dataType=JSON";
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        CompletableFuture<HttpResponse<String>> future = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        CompletableFuture<List<StationStatusDto>> asyncDtoList = future.thenApply(HttpResponse::body)
                .thenApplyAsync(result -> {
                    List<StationStatusDto> apiDtoList = new ArrayList<>();
                    try {
                        JSONParser jsonParser = new JSONParser();
                        JSONObject parse = (JSONObject) jsonParser.parse(result);
                        long totalCount = (long) parse.get("totalCount");
                        log.info("StationStatus totalCount : {}", totalCount);

                        JSONObject items = (JSONObject) parse.get("items");
                        JSONArray item = (JSONArray) items.get("item");

                        for (int i = 0; i < item.size(); i++) {
                            JSONObject jsonObject = (JSONObject) item.get(i);
                            StationStatusDto stationStatus = buildStationStatusDtoFromJson(jsonObject);
                            apiDtoList.add(stationStatus);
                        }
                        log.info("StationStatusDto size : {}", apiDtoList.size());
                    } catch (Exception e) {
                        throw new ApiProblemException("상태정보 API 호출에 문제가 발생했습니다.");
                    }
                    return apiDtoList;
                });
        try {
            return asyncDtoList.join();
        } catch(Exception e){
            throw new ApiProblemException("상태정보 API 호출에 문제가 발생했습니다.");
        }
    }

    private StationStatusDto buildStationStatusDtoFromJson(JSONObject o) {
        return StationStatusDto.builder()
                .businessId(getStringFromJson(o, "busiId"))
                .stationChargerId(getStringFromJson(o, "statId") + getStringFromJson(o, "chgerId"))
                .status(ChargerStatus.of(getStringFromJson(o, "stat")))
                .stationUpdateDate(DateTimeUtils.dateTimeFormat(getStringFromJson(o,"statUpdDt")))
                .lastChargeStart(DateTimeUtils.dateTimeFormat(getStringFromJson(o, "lastTsdt")))
                .lastChargeEnd(DateTimeUtils.dateTimeFormat(getStringFromJson(o, "lastTedt")))
                .nowChargeStart(DateTimeUtils.dateTimeFormat(getStringFromJson(o, "nowTsdt")))
                .build();
    }

    private String getStringFromJson(JSONObject jsonObject, String tag) {
        return (String) jsonObject.get(tag);
    }
}
