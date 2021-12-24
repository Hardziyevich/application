package com.hardziyevich.app.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Predicate;

/**
 * Provides method for validation input data
 *
 * @param <T>
 */
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

    /**
     * Validation input data depends on predicate
     *
     * @param predicate a state of validation data
     * @param message   a string ,message for wrong data
     * @return itself
     */
    Validator<T> validator(Predicate<T> predicate, String message) {
        if (!predicate.test(object)) {
            messages.add(message);
            log.warn(message);
        }
        return this;
    }

    /**
     * Validate the correctness of the data
     *
     * @return a boolean result
     */
    boolean isEmpty() {
        return messages.isEmpty();
    }

    /**
     * Return result if data will be correct
     *
     * @return a result
     */
    T get() {
        return object;
    }

}
