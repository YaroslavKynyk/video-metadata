package com.goldmediatech.vms.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * A simple messaging stub.
 * This class created to simulate a message queue for video metadata processing.
 * Helps in testing asynchronous operations without needing a real message broker.
 */
public final class MessageQueue {
    private MessageQueue() {
        // Prevent instantiation
    }

    public static final String TOPIC_VIDEO_METADATA = "video_metadata";

    private static final Map<String, Queue<String>> broker = new HashMap<>();

    public static void createTopic(final String key) {
        broker.putIfAbsent(key, new LinkedList<>());
    }

    public static void sendMessage(String key, String message) {
        Queue<String> queue = broker.get(key);
        if (queue != null) {
            queue.offer(message);
        }
    }

    public static String receiveMessage(String key) {
        Queue<String> queue = broker.get(key);
        return queue != null ? queue.poll() : null;
    }

    public static long count(String key) {
        Queue<String> queue = broker.get(key);
        return queue != null ? queue.size() : 0;
    }
}
