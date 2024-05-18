package com.daedongmap.daedongmap.alarm.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@RequiredArgsConstructor
public class EmitterRepository {

    // todo : 스레드 안전하다는 게 어떤 것인지 알아볼 것
    private final Map<Long, SseEmitter> emitterMap = new ConcurrentHashMap<>();

    public void save(Long id, SseEmitter emitter) {
        emitterMap.put(id, emitter);
    }

    public void delete(Long id) {
        emitterMap.remove(id);
    }

    public SseEmitter get(Long id) {
        return emitterMap.get(id);
    }

}
