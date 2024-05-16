package com.daedongmap.daedongmap.alarm.service;

import com.daedongmap.daedongmap.alarm.repository.EmitterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private Map<Long, Set<SseEmitter>> container = new HashMap<>();
    private final EmitterRepository emitterRepository;


    /**
     * 클라이언트가 구독을 위해 호출하는 메서드
     *
     * @param userId - 구독하는 클라이언트의 사용자 아이디
     * @return SseEmitter - 서버에서 보낸 이벤트 Emitter
     */
    public SseEmitter subscribe(Long userId) {
        SseEmitter emitter = createEmitter(userId);
        sendToClient(userId, "EventStream Created. [userId=" + userId + "]");

        return emitter;
    }

    public void notify(Long userId, Object event) {
        sendToClient(userId, event);
    }

    /**
     * 사용자 아이디를 기반으로 이벤트 Emitter 생성
     *
     * @param id - 사용자 아이디
     * @return SseEmitter - 생성된 이벤트 Emitter
     */
    private SseEmitter createEmitter(Long id) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(id, emitter);

        // 이미터가 완료될 때, 이미터를 삭제
        emitter.onCompletion(() -> emitterRepository.delete(id));
        // 이미터가 타임아웃 됐을 때, 이미터를 삭제
        emitter.onTimeout(() -> emitterRepository.delete(id));

        return emitter;
    }

    /**
     * 클라이언트에게 데이터를 전송
     *
     * @param id   - 데이터를 받을 사용자의 아이디
     * @param data - 전송할 데이터
     */
    public void sendToClient(Long id, Object data) {
        SseEmitter emitter = emitterRepository.get(id);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .id(String.valueOf(id))
                        .name("sse")
                        .data(data));
            } catch (IOException exception) {
                emitterRepository.delete(id);
                emitter.completeWithError(exception);
            }
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
