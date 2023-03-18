/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kpouer.hermes;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Hermes {
    private final Executor executor;
    private final ListenerManager listenerManager = new ListenerManager();

    public Hermes() {
        this(Executors.newFixedThreadPool(4));
    }

    public Hermes(Executor executor) {
        this.executor = executor;
    }

    public void subscribe(Object listener) {
        listenerManager.subscribe(listener);
    }

    public void unsubscribe(Object listener) {
        listenerManager.unsubscribe(listener);
    }

    public void publish(Object event) {
        listenerManager.publish(event);
    }

    public void publishInBackground(Object event) {
        executor.execute(() -> publish(event));
    }
}
