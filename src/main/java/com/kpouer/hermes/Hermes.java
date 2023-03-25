/*
The MIT License (MIT)
Copyright (c) 2023 Matthieu Casanova

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package com.kpouer.hermes;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * The main class of the Hermes library.
 */
public class Hermes {
    private final Executor executor;
    private final ListenerManager listenerManager = new ListenerManager();

    /**
     * Will create Hermes with a fixed thread pool of 4 threads
     */
    public Hermes() {
        this(Executors.newFixedThreadPool(4));
    }

    /**
     * Will create Hermes with the given executor
     *
     * @param executor the executor to use
     */
    public Hermes(Executor executor) {
        this.executor = executor;
    }

    /**
     * Subscribe the given listener to this Hermes.
     * The listener can have several methods annotated with {@link Listener}
     * @param listener a listener
     */
    public void subscribe(Object listener) {
        listenerManager.subscribe(listener);
    }

    /**
     * Unsubscribe the given listener from this Hermes.
     * @param listener a listener
     */
    public void unsubscribe(Object listener) {
        listenerManager.unsubscribe(listener);
    }

    /**
     * Publish an event to all the listeners.
     * The event will be published in the current thread.
     * @param event the event
     */
    public void publish(Object event) {
        listenerManager.publish(event);
    }

    /**
     * Publish an event to all the listeners.
     * The event will be published in a background thread of the executor.
     * @param event the event
     */
    public void publishInBackground(Object event) {
        executor.execute(() -> publish(event));
    }
}
