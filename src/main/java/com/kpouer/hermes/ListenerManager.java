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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListenerManager {
    private final Map<Class<?>, List<Target>> targets = new HashMap<>();

    public void subscribe(Object listener) {
        var methods = listener.getClass().getMethods();
        for (var method : methods) {
            var annotation = method.getAnnotation(Listener.class);
            if (annotation != null) {
                var parameters = method.getParameterTypes();
                if (parameters.length != 1) {
                    throw new IllegalArgumentException("Listener method must have exactly one parameter");
                }
                var eventClass = method.getParameterTypes()[0];
                List<Target> listeners = targets.computeIfAbsent(eventClass, k -> new ArrayList<>());
                listeners.add(new Target(listener, method));
            }
        }
    }

    public void unsubscribe(Object listener) {
        var methods = listener.getClass().getMethods();
        for (var method : methods) {
            var annotation = method.getAnnotation(Listener.class);
            if (annotation != null) {
                var parameters = method.getParameterTypes();
                if (parameters.length != 1) {
                    throw new IllegalArgumentException("Listener method must have exactly one parameter");
                }
                var eventClass = method.getParameterTypes()[0];
                List<Target> listeners = targets.get(eventClass);
                listeners.removeIf(target -> target.getListener() == listener);
                if (listeners.isEmpty()) {
                    targets.remove(eventClass);
                }
            }
        }
    }

    public void publish(Object event) {
        var eventClass = event.getClass();
        var listeners = targets.get(eventClass);
        if (listeners != null) {
            for (var target : listeners) {
                target.invoke(event);
            }
        }
    }
}
