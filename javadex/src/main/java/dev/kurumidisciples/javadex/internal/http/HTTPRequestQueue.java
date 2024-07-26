package dev.kurumidisciples.javadex.internal.http;

import okhttp3.Request;
import java.util.LinkedList;
import java.util.Queue;

/**
 * <p>HTTPRequestQueue class.</p>
 *
 * @author Hacking Pancakez
 * @version $Id: $Id
 */
public class HTTPRequestQueue {
    private Queue<Request> queue;

    /**
     * <p>Constructor for HTTPRequestQueue.</p>
     */
    public HTTPRequestQueue() {
        this.queue = new LinkedList<>();
    }

    /**
     * <p>addRequest.</p>
     *
     * @param request a {@link okhttp3.Request} object
     */
    public void addRequest(Request request) {
        this.queue.add(request);
    }

    /**
     * <p>getNextRequest.</p>
     *
     * @return a {@link okhttp3.Request} object
     */
    public Request getNextRequest() {
        return this.queue.poll();
    }

    /**
     * <p>hasRequests.</p>
     *
     * @return a boolean
     */
    public boolean hasRequests() {
        return !this.queue.isEmpty();
    }
}
