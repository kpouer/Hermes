package com.kpouer.hermes;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;

@RequiredArgsConstructor
@Getter
public class Target {
    private final Object listener;
    private final Method method;

    public void invoke(Object event) {
        try {
            method.invoke(listener, event);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
