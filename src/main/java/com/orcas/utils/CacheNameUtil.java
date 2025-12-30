package com.orcas.utils;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class CacheNameUtil {

    private CacheNameUtil() {}

    public static Set<String> getAllCacheNames() {

        return Stream.of(CacheNames.class.getDeclaredFields())
                .filter(field -> field.getType().equals(String.class))
                .map(field -> {
                    try {
                        return (String) field.get(null); // static field
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toSet());
    }
}