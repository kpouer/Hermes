package com.kpouer.hermes;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HermesTest {
    private Hermes hermes;
    private MyReceiver receiver;
    private Event event;

    @BeforeEach
    void setUp() {
        hermes = new Hermes();
        receiver = new MyReceiver();
        event = new Event("Hello");
    }

    @Test
    void publish() {
        hermes.subscribe(receiver);
        hermes.publish(event);
        assertEquals("Hello", receiver.getReceivedMessage(), "The message should be received");
        assertSame(Thread.currentThread(), receiver.getReceivedThread(), "The message should be received in the same thread");
        hermes.publish(new Event("World"));
        assertEquals("World", receiver.getReceivedMessage(), "The message should be received");
        assertSame(Thread.currentThread(), receiver.getReceivedThread(), "The message should be received in the same thread");
        hermes.unsubscribe(receiver);
        hermes.publish(new Event("Not received"));
        assertEquals("World", receiver.getReceivedMessage(), "The message should be received");
    }

    @Test
    void publishInBackground() throws InterruptedException {
        hermes.subscribe(receiver);
        hermes.publishInBackground(event);
        // wait for the message to be received
        Thread.sleep(100L);
        assertEquals("Hello", receiver.getReceivedMessage(), "The message should be received");
        assertNotSame(Thread.currentThread(), receiver.getReceivedThread(), "The message should be received in a different thread");
    }

    @Getter
    private static class MyReceiver {
        private String receivedMessage;
        private Thread receivedThread;
        @Listener
        public void onEvent(Event event) {
            receivedMessage = event.getMessage();
            receivedThread = Thread.currentThread();
        }
    }

    @Getter
    @RequiredArgsConstructor
    private static class Event {
        private final String message;
    }
}