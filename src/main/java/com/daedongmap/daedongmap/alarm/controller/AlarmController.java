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
@RequestMapping("/api/alarm")
@RequiredArgsConstructor
@Slf4j
public class AlarmController {

    private final AlarmService alarmService;

    @GetMapping(value="/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "알림 구독", description = "실시간 알림을 구독합니다.")
    public SseEmitter subscribe(@RequestParam("userId") Long userId) {
        log.info("알림 구독 api 호출 - userId : " + userId);
        return alarmService.subscribe(userId);
    }

    // 임시로 데이터 변경시켜, 클라이언트로 알림을 주기 위함
    @PostMapping("/send-data")
    public void sendData(@RequestParam("userId") Long userId) {
        log.info("데이터 전송 api 호출 - userId : " + userId);
        alarmService.notify(userId, "data!!!!!");
    }

}
