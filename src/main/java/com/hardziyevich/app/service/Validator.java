package com.hardziyevich.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Predicate;

/**
 * Provides method for validation input data
 *
 * @param <T>
 */
public class Validator<T> {

    private final T object;
    private final List<String> messages = new ArrayList<>();
    private static final Logger log = LoggerFactory.getLogger(Validator.class);

    private Validator(T object) {
        this.object = object;
    }

    public static <T> Validator<T> of(T object) {
        return new Validator<>(Objects.requireNonNull(object));
    }

    /**
     * Validation input data depends on predicate
     *
     * @param predicate a state of validation data
     * @param message   a string ,message for wrong data
     * @return itself
     */
    public Validator<T> validator(Predicate<T> predicate, String message) {
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
    public boolean isEmpty() {
        return messages.isEmpty();
    }

    /**
     * Return result if data will be correct
     *
     * @return a result
     */
    public T get() {
        return object;
    }
}
