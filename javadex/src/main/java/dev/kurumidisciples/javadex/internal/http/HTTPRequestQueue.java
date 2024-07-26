package dev.kurumidisciples.javadex.internal.http;

import okhttp3.Request;
import java.util.LinkedList;
import java.util.Queue;

public class HTTPRequestQueue {
    private Queue<Request> queue;

    public HTTPRequestQueue() {
        this.queue = new LinkedList<>();
    }

    public void addRequest(Request request) {
        this.queue.add(request);
    }

    public Request getNextRequest() {
        return this.queue.poll();
    }

    public boolean hasRequests() {
        return !this.queue.isEmpty();
    }
}