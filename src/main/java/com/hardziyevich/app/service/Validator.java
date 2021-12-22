package com.hardziyevich.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Predicate;

class Validator<T> {

    private final T object;
    private final List<String> messages = new ArrayList<>();
    private static final Logger log = LoggerFactory.getLogger(Validator.class);

    private Validator(T object) {
        this.object = object;
    }

    static <T> Validator<T> of(T object) {
        return new Validator<>(Objects.requireNonNull(object));
    }

    Validator<T> validator(Predicate<T> predicate, String message) {
        if (!predicate.test(object)) {
            messages.add(message);
            log.warn(message);
        }
        return this;
    }

    boolean isEmpty() {
        return messages.isEmpty();
    }

    T get() {
        return object;
    }
}
