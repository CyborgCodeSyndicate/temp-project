package com.theairebellion.zeus.framework.storage;

import com.theairebellion.zeus.framework.parameters.Late;
import org.springframework.core.ParameterizedTypeReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Storage {

    private final Map<Enum<?>, List<Object>> data = new HashMap<>();


    public <T> void put(Enum<?> key, T value) {
        data.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }


    public <T> T get(Enum<?> key, Class<T> clazz) {
        List<Object> values = data.get(key);
        if (values == null || values.isEmpty()) {
            return null;
        }
        Object latest = values.get(values.size() - 1);
        return castOrNull(latest, clazz);
    }


    public <T> T get(Enum<?> key, ParameterizedTypeReference<T> typeReference) {
        List<Object> values = data.get(key);
        if (values == null || values.isEmpty()) {
            return null;
        }
        Object latest = values.get(values.size() - 1);
        return castOrNullTypeRef(latest, typeReference);
    }


    public <T> T getByIndex(Enum<?> key, int index, Class<T> clazz) {
        List<Object> values = data.get(key);
        if (values == null || index < 1 || index > values.size()) {
            return null;
        }
        int idx = values.size() - index;
        Object value = values.get(idx);
        return castOrNull(value, clazz);
    }


    public <T> T getByIndex(Enum<?> key, int index, ParameterizedTypeReference<T> typeReference) {
        List<Object> values = data.get(key);
        if (values == null || index < 1 || index > values.size()) {
            return null;
        }
        int idx = values.size() - index;
        Object value = values.get(idx);
        return castOrNullTypeRef(value, typeReference);
    }


    public <T> T getByClass(Enum<?> key, Class<T> clazz) {
        List<Object> values = data.get(key);
        if (values == null) {
            return null;
        }
        for (int i = values.size() - 1; i >= 0; i--) {
            Object value = values.get(i);
            T casted = castOrNull(value, clazz);
            if (casted != null) {
                return casted;
            }
        }
        return null;
    }


    public <T> T getByClass(Enum<?> key, ParameterizedTypeReference<T> typeReference) {
        List<Object> values = data.get(key);
        if (values == null) {
            return null;
        }
        for (int i = values.size() - 1; i >= 0; i--) {
            Object value = values.get(i);
            T casted = castOrNullTypeRef(value, typeReference);
            if (casted != null) {
                return casted;
            }
        }
        return null;
    }


    public <T> List<T> getAllByClass(Enum<?> key, Class<T> clazz) {
        List<Object> values = data.get(key);
        List<T> result = new ArrayList<>();
        if (values != null) {
            for (Object value : values) {
                T casted = castOrNull(value, clazz);
                if (casted != null) {
                    result.add(casted);
                }
            }
        }
        return result;
    }


    public <T> List<T> getAllByClass(Enum<?> key, ParameterizedTypeReference<T> typeReference) {
        List<Object> values = data.get(key);
        List<T> result = new ArrayList<>();
        if (values != null) {
            for (Object value : values) {
                T casted = castOrNullTypeRef(value, typeReference);
                if (casted != null) {
                    result.add(casted);
                }
            }
        }
        return result;
    }


    public Storage sub(Enum<?> subKey) {
        List<Object> values = data.get(subKey);
        if (values == null || values.isEmpty()) {
            Storage newSub = new Storage();
            data.put(subKey, new ArrayList<>(Collections.singletonList(newSub)));
            return newSub;
        }

        Object existingLatest = values.get(values.size() - 1);
        if (existingLatest instanceof Storage) {
            return (Storage) existingLatest;
        }

        throw new IllegalStateException(
            "Key " + subKey + " is already used for a non-storage value: " + existingLatest);
    }


    public void joinLateArguments() {
        data.forEach((anEnum, objects) -> {
            List<Object> updatedObjects = new ArrayList<>();
            for (Object o : objects) {
                if (o instanceof Late<?>) {
                    try {
                        Late<?> late = (Late<?>) o;
                        Object join = late.join();
                        updatedObjects.add(join);
                    } catch (Exception ignored) {
                    }
                } else {
                    updatedObjects.add(o);
                }
            }
            objects.clear();
            objects.addAll(updatedObjects);
        });
    }


    @SuppressWarnings("unchecked")
    private <T> T castOrNull(Object value, Class<T> clazz) {
        if (value == null) {
            return null;
        }
        if (clazz.isInstance(value)) {
            return (T) value;
        }
        return null;
    }


    @SuppressWarnings("unchecked")
    private <T> T castOrNullTypeRef(Object value, ParameterizedTypeReference<T> typeReference) {
        if (value == null) {
            return null;
        }
        if (typeReference.getType().getTypeName().equals(value.getClass().getTypeName())) {
            return (T) value;
        }
        return null;
    }

}
