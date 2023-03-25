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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used to manage listeners.
 * Usually you should not use it directly, but use the {@link com.kpouer.hermes.Hermes} class instead.
 */
class ListenerManager {
    private final Map<Class<?>, List<Target>> targets = new HashMap<>();

    void subscribe(Object listener) {
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

    void unsubscribe(Object listener) {
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

    void publish(Object event) {
        var eventClass = event.getClass();
        var listeners = targets.get(eventClass);
        if (listeners != null) {
            for (var target : listeners) {
                target.invoke(event);
            }
        }
    }
}
