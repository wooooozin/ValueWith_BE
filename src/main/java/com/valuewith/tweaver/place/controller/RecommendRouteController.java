package com.valuewith.tweaver.place.controller;

import com.valuewith.tweaver.place.dto.RecommendRouteDto;
import com.valuewith.tweaver.place.service.RecommendRouteService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/recommend/route")
public class RecommendRouteController {
  private final RecommendRouteService recommendRouteService;

  @PostMapping
  public ResponseEntity<List<RecommendRouteDto>> recommendRoute(@RequestBody List<RecommendRouteDto> places) {
    return ResponseEntity.ok(recommendRouteService.recommendRoute(places));
  }
}
