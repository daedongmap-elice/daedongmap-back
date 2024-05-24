package com.daedongmap.daedongmap.alarm.controller;

import com.amazonaws.annotation.NotThreadSafe;
import com.daedongmap.daedongmap.alarm.service.AlarmService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.assertj.core.api.Fail.fail;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AlarmControllerTest {

    @LocalServerPort
    private int port;
    private static final OkHttpClient client = new OkHttpClient();
    private static final EventSource.Factory factory = EventSources.createFactory(client);
    private static final JSONObject json = new JSONObject();

    @Test
    @DisplayName("sse connect")
    void connectToSseEmitter() throws InterruptedException {
        Long followerId = 1L;
        Long userId = 2L;

        final Request request = getConnectRequest(userId);
        final EventSourceWrapper eventSourceWrapper = new EventSourceWrapper();
        factory.newEventSource(request, eventSourceWrapper.listener);

        sleep(2000);

        if (eventSourceWrapper.receivedData.isEmpty()) {
            fail("No event received");
        }

        Assertions.assertEquals("EventStream Created. [userId=" + userId + "]", eventSourceWrapper.receivedData.get(0));
    }

    private Request getConnectRequest(Long userId) {
        return new Request.Builder()
                .url("http://localhost:8080" + "/api/alarm/subscribe?" + "userId=" + userId)
                .build();
    }

    @NotThreadSafe
    private static final class EventSourceWrapper {

        final EventSourceListener listener;
        final List<String> receivedData = new ArrayList<>();
        boolean isOpened = false;
        boolean isClosed = false;
        boolean onFailureCalled = false;

       public EventSourceWrapper() {
            this.listener = new EventSourceListener() {
                @Override
                public void onOpen(EventSource eventSource, Response response) {
                    isOpened = true;
                }

                @Override
                public void onEvent(EventSource eventSource, String id, String type, String data) {
                    receivedData.add(data);
                }

                @Override
                public void onClosed(EventSource eventSource) {
                    isClosed = true;
                }

                @Override
                public void onFailure(EventSource eventSource, Throwable t, Response response) {
                    onFailureCalled = true;
                }
            };
        }
    }
}