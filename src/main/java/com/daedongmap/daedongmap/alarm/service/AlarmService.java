package com.daedongmap.daedongmap.alarm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


@Service
@Slf4j
@RequiredArgsConstructor
public class AlarmService {

    private HashMap<Long, Set<SseEmitter>> container = new HashMap<>();

    public SseEmitter subscribe(Long userId) {
        SseEmitter sseEmitter = new SseEmitter(300000L);

        final SseEmitter.SseEventBuilder sseEventBuilder = SseEmitter.event()
                .name("subscribe")
                .data("subscribed!!")
                .reconnectTime(3000L);

        sendEvent(sseEmitter, sseEventBuilder);

        Set<SseEmitter> sseEmitters = container.getOrDefault(userId, new HashSet<>());
        sseEmitters.add(sseEmitter);
        container.put(userId, sseEmitters);

        sseEmitter.onCompletion(() -> sseEmitters.remove(sseEmitter));

        return sseEmitter;
    }

    private static void sendEvent(final SseEmitter sseEmitter, final SseEmitter.SseEventBuilder sseEventBuilder) {
        try {
            sseEmitter.send(sseEventBuilder);
        } catch (IOException e) {
            sseEmitter.complete();
        }
    }

    // 팔로우 했을 때, 알람 보내기
    public void sendAlarmToFollowee(Long followerId, Long followingId) {
        log.info("sendAlarmToFollowee : " + followingId);
        Set<SseEmitter> sseEmitters = container.getOrDefault(followingId, new HashSet<>());
        sseEmitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("follow")
                        .data("You have a new follower: " + followerId));
            } catch (IOException e) {
                log.error("Error sending notification to followee", e);
            }
        });
    }

}
