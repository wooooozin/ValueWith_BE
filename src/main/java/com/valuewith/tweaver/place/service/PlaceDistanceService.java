package com.valuewith.tweaver.place.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.valuewith.tweaver.place.dto.PlaceDto;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlaceDistanceService {
  // 카카오디벨로퍼스에서 발급 받은 API 키 값
  @Value("${kakao-rest-api-key}")
  private String restApiKey;
  public List<PlaceDto> setDistancesFromApi(List<PlaceDto> placeDtos) {
    try {
      // API 엔드포인트 URL
      String apiUrl = "https://apis-navi.kakaomobility.com/v1/destinations/directions";

      for (int i = 0; i < placeDtos.size() - 1; i++) {
        // 요청 데이터
        Map<String, Object> requestData = new HashMap<>();

        requestData.put("origin", Map.of("x", placeDtos.get(i).getX(), "y", placeDtos.get(i).getY()));

        List<Map<String, Object>> destinations = new ArrayList<>();
        destinations.add(Map.of("x", placeDtos.get(i + 1).getX(), "y", placeDtos.get(i + 1).getY(), "key", placeDtos.get(i + 1).getPlaceCode()));
        requestData.put("destinations", destinations);

        requestData.put("radius", 10000);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.valueToTree(requestData);

        // JSON 형식의 요청 데이터 문자열 생성
        String requestBody = jsonNode.toString();

        log.info(requestBody.toString());

        // HTTP 클라이언트 생성
        HttpClient httpClient = HttpClient.newHttpClient();

        // HTTP 요청 객체 생성
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(apiUrl))
            .header("Content-Type", "application/json")
            .header("Authorization", "KakaoAK " + restApiKey)
            .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
            .build();

        // HTTP 요청 및 응답 처리
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // 응답 출력
        System.out.println("Response Code: " + response.statusCode());
        System.out.println("Response Body: " + response.body());

        // 응답 배열에 정리
        objectMapper = new ObjectMapper();
        jsonNode = objectMapper.readTree(response.body());

        // "routes" 배열에서 "summary" 객체의 "distance" 값을 추출하여 List에 저장
        JsonNode routesNode = jsonNode.get("routes");

        for (JsonNode routeNode : routesNode) {
          JsonNode summaryNode = routeNode.get("summary");
          Integer distance = summaryNode.get("distance").asInt();
          placeDtos.get(i).setDistance(distance * 1.0);
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return placeDtos;
  }
}
