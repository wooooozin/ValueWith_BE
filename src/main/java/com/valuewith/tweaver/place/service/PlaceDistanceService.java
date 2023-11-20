package com.valuewith.tweaver.place.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.valuewith.tweaver.place.dto.PlaceDto;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
      for (int i = 0; i < placeDtos.size() - 1; i++) {
        // API 엔드포인트 URL
        String apiUrl = "https://apis-navi.kakaomobility.com/v1/directions";

        // 쿼리 파라미터 구성
        Map<Object, Object> params = new HashMap<>();
        params.put("origin", placeDtos.get(i).getX() + "," + placeDtos.get(i).getY());
        params.put("destination", placeDtos.get(i + 1).getX() + "," + placeDtos.get(i + 1).getY());

        // 쿼리 파라미터를 문자열로 변환
        StringBuilder queryString = new StringBuilder();
        for (Map.Entry<Object, Object> entry : params.entrySet()) {
          if (queryString.length() > 0) {
            queryString.append("&");
          }
          queryString.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
          queryString.append("=");
          queryString.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
        }

        // URI 생성
        URI uri = URI.create(apiUrl + "?" + queryString.toString());

        // HTTP 클라이언트 생성
        HttpClient httpClient = HttpClient.newHttpClient();

        // HTTP 요청 객체 생성
        HttpRequest request = HttpRequest.newBuilder()
            .uri(uri)
            .header("Authorization", "KakaoAK " + restApiKey)
            .GET()
            .build();

        // HTTP 요청 및 응답 처리
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // 응답 출력
        System.out.println("Response Code: " + response.statusCode());
        System.out.println("Response Body: " + response.body());

        // 응답 배열에 정리z
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.body());

        // "routes" 배열에서 "summary" 객체의 "distance" 값을 추출하여 List에 저장
        JsonNode routesNode = jsonNode.get("routes");
        if (routesNode.isArray() && routesNode.size() > 0) {
          JsonNode summaryNode = routesNode.get(0).get("summary");
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
