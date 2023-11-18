package com.valuewith.tweaver.place.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.valuewith.tweaver.place.dto.RecommendRouteDto;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendRouteService {

  // 카카오디벨로퍼스에서 발급 받은 API 키 값
  @Value("${kakao-rest-api-key}")
  private String restApiKey;

  public List<RecommendRouteDto> recommendRoute(List<RecommendRouteDto> points) {
    int[][] graph = calculateDistanceMatrix(points);
    // TSP 수행
    List<RecommendRouteDto> shortestPath = solveTSP(points, graph);
    
    return shortestPath;
  }

  public List<RecommendRouteDto> solveTSP(List<RecommendRouteDto> locations,
      int[][] distanceMatrix) {
    int size = locations.size();

    // 모든 가능한 순열을 생성
    List<List<RecommendRouteDto>> allPermutations = new ArrayList<>();
    permute(locations, 1, size - 1, allPermutations);

    // 최적 경로 및 거리 초기화
    List<RecommendRouteDto> optimalPath = null;
    int minDistance = Integer.MAX_VALUE;

    // 모든 순열에 대해 거리 계산 및 최적 경로 갱신
    for (List<RecommendRouteDto> permutation : allPermutations) {
      int distance = calculateTotalDistance(permutation, distanceMatrix);
      if (distance < minDistance) {
        minDistance = distance;
        optimalPath = new ArrayList<>(permutation);
      }
    }

    return optimalPath;
  }

  // 모든 루트의 경우의 수를 구하는 부분
  private void permute(List<RecommendRouteDto> locations, int start, int end,
      List<List<RecommendRouteDto>> result) {
    if (start == end) {
      result.add(new ArrayList<>(locations));
    } else {
      for (int i = start; i <= end; i++) {
        swap(locations, start, i);
        permute(locations, start + 1, end, result);
        swap(locations, start, i);
      }
    }
  }

  private void swap(List<RecommendRouteDto> locations, int i, int j) {
    RecommendRouteDto temp = locations.get(i);
    locations.set(i, locations.get(j));
    locations.set(j, temp);
  }

  private int calculateTotalDistance(List<RecommendRouteDto> path, int[][] distanceMatrix) {
    int totalDistance = 0;

    for (int i = 0; i < path.size() - 1; i++) {
      int fromIndex = path.get(i).getIndex(); // 객체의 인덱스로 객체를 식별
      int toIndex = path.get(i + 1).getIndex();
      totalDistance += distanceMatrix[fromIndex][toIndex];
    }

    return totalDistance;
  }

  public int[] getDistancesFromApi(RecommendRouteDto sourcePlace,
      List<RecommendRouteDto> destinationPlaces) {
    int[] resultResult = new int[destinationPlaces.size()];

    try {
      // API 엔드포인트 URL
      String apiUrl = "https://apis-navi.kakaomobility.com/v1/destinations/directions";

      // 요청 데이터
      Map<String, Object> requestData = new HashMap<>();

      requestData.put("origin", Map.of("x", sourcePlace.getX(), "y", sourcePlace.getY()));

      List<Map<String, Object>> destinations = new ArrayList<>();

      for (int i = 0; i < destinationPlaces.size(); i++) {
        destinations.add(
            Map.of("x", destinationPlaces.get(i).getX(), "y", destinationPlaces.get(i).getY(),
                "key", destinationPlaces.get(i).getPlaceCode()));
      }

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
      HttpResponse<String> response = httpClient.send(request,
          HttpResponse.BodyHandlers.ofString());

      // 응답 출력
      System.out.println("Response Code: " + response.statusCode());
      System.out.println("Response Body: " + response.body());

      // 응답 배열에 정리
      objectMapper = new ObjectMapper();
      jsonNode = objectMapper.readTree(response.body());

      // "routes" 배열에서 "summary" 객체의 "distance" 값을 추출하여 List에 저장
      JsonNode routesNode = jsonNode.get("routes");
      List<Integer> distanceList = new ArrayList<>();

      for (JsonNode routeNode : routesNode) {
        JsonNode summaryNode = routeNode.get("summary");
        int distance = summaryNode.get("distance").asInt();
        distanceList.add(distance);
      }

      // List를 int[] 배열로 변환
      resultResult = distanceList.stream().mapToInt(Integer::intValue).toArray();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return resultResult;
  }

  public int[][] calculateDistanceMatrix(List<RecommendRouteDto> points) {
    int size = points.size();
    int[][] distanceMatrix = new int[size][size];

    for (int i = 0; i < size; i++) {
      RecommendRouteDto sourcePlace = points.get(i);

      // 도착지 좌표 리스트
      List<RecommendRouteDto> destinationPlaces = new ArrayList<>(points);
      destinationPlaces.remove(i); // 자기 자신을 제외한 도착지 리스트

      // 호출할 API를 사용하여 sourcePlace에서 모든 destinationPlaces 간의 거리를 계산
      int[] distances = getDistancesFromApi(sourcePlace, destinationPlaces);

      // 결과를 distanceMatrix에 저장
      for (int j = 0; j < destinationPlaces.size(); j++) {
        if (j >= i) {
          distanceMatrix[i][j+1] = distances[j];
        } else {
          distanceMatrix[i][j] = distances[j];
        }
      }
    }
    return distanceMatrix;
  }
}
