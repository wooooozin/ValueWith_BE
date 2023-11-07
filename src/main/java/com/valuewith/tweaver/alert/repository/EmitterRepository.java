package com.valuewith.tweaver.alert.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Repository
public class EmitterRepository {
  private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
  private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

  public SseEmitter save(String id, SseEmitter sseEmitter) {
    emitters.put(id, sseEmitter);
    return sseEmitter;
  }

  public void saveEventCache(String id, Object event) {
    eventCache.put(id, event);
  }

  public Map<String, SseEmitter> findAllStartById(String id) {
    return emitters.entrySet().stream()
        .filter(entry -> entry.getKey().startsWith(id))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  public Map<String, Object> findAllEventCacheStartById(String userId) {
    return eventCache.entrySet().stream()
        .filter(entry -> entry.getKey().startsWith(userId))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  public void deleteById(String id) {
    emitters.remove(id);
  }

}
