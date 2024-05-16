package com.daedongmap.daedongmap.alarm.controller;

import com.daedongmap.daedongmap.alarm.service.AlarmService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@RestController
@Slf4j
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    @GetMapping(value="/api/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "알림 구독", description = "실시간 알림을 구독합니다.")
    public ResponseEntity<SseEmitter> subscribe(@RequestParam("userId") Long userId) {
        SseEmitter emitter = alarmService.subscribe(userId);
        log.info("알림 구독 controller : " + emitter);
        return ResponseEntity.ok(emitter);
    }

}
