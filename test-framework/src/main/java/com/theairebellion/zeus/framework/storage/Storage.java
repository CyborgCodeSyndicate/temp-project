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
        return getLatestValue(key, clazz, null);
    }


    public <T> T get(Enum<?> key, ParameterizedTypeReference<T> typeReference) {
        return getLatestValue(key, null, typeReference);
    }


    public <T> T get(DataExtractor<T> extractor, Class<T> clazz) {
        Object result = (extractor.getSubKey() != null)
                            ? sub(extractor.getSubKey()).get(extractor.getKey(), Object.class)
                            : get(extractor.getKey(), Object.class);

        return clazz.cast(extractor.extract(result));
    }


    public <T> T getByIndex(Enum<?> key, int index, Class<T> clazz) {
        return getValueByIndex(key, index, clazz, null);
    }


    public <T> T getByIndex(Enum<?> key, int index, ParameterizedTypeReference<T> typeReference) {
        return getValueByIndex(key, index, null, typeReference);
    }


    public <T> T getByClass(Enum<?> key, Class<T> clazz) {
        return findByClass(key, clazz, null);
    }


    public <T> T getByClass(Enum<?> key, ParameterizedTypeReference<T> typeReference) {
        return findByClass(key, null, typeReference);
    }


    public <T> List<T> getAllByClass(Enum<?> key, Class<T> clazz) {
        return findAllByClass(key, clazz, null);
    }


    public <T> List<T> getAllByClass(Enum<?> key, ParameterizedTypeReference<T> typeReference) {
        return findAllByClass(key, null, typeReference);
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
        data.replaceAll((key, objects) -> {
            List<Object> updatedObjects = new ArrayList<>();
            for (Object o : objects) {
                if (o instanceof Late<?>) {
                    try {
                        updatedObjects.add(((Late<?>) o).join());
                    } catch (Exception ignored) {
                    }
                } else {
                    updatedObjects.add(o);
                }
            }
            return updatedObjects;
        });
    }


    @SuppressWarnings("unchecked")
    private <T> T castOrNull(Object value, Class<T> clazz) {
        return (clazz.isInstance(value)) ? (T) value : null;
    }


    @SuppressWarnings("unchecked")
    private <T> T castOrNullTypeRef(Object value, ParameterizedTypeReference<T> typeReference) {
        return (value != null && typeReference.getType().getTypeName().equals(value.getClass().getTypeName())) ?
                   (T) value : null;
    }


    private <T> T getLatestValue(Enum<?> key, Class<T> clazz, ParameterizedTypeReference<T> typeReference) {
        List<Object> values = data.get(key);
        if (values == null || values.isEmpty()) {
            return null;
        }
        Object latest = values.get(values.size() - 1);
        return clazz != null ? castOrNull(latest, clazz) : castOrNullTypeRef(latest, typeReference);
    }


    private <T> T getValueByIndex(Enum<?> key, int index, Class<T> clazz, ParameterizedTypeReference<T> typeReference) {
        List<Object> values = data.get(key);
        if (values == null || index < 1 || index > values.size()) {
            return null;
        }
        Object value = values.get(values.size() - index);
        return clazz != null ? castOrNull(value, clazz) : castOrNullTypeRef(value, typeReference);
    }


    private <T> T findByClass(Enum<?> key, Class<T> clazz, ParameterizedTypeReference<T> typeReference) {
        List<Object> values = data.get(key);
        if (values == null) {
            return null;
        }
        for (int i = values.size() - 1; i >= 0; i--) {
            Object value = values.get(i);
            T casted = clazz != null ? castOrNull(value, clazz) : castOrNullTypeRef(value, typeReference);
            if (casted != null) {
                return casted;
            }
        }
        return null;
    }


    private <T> List<T> findAllByClass(Enum<?> key, Class<T> clazz, ParameterizedTypeReference<T> typeReference) {
        List<Object> values = data.get(key);
        List<T> result = new ArrayList<>();
        if (values != null) {
            for (Object value : values) {
                T casted = clazz != null ? castOrNull(value, clazz) : castOrNullTypeRef(value, typeReference);
                if (casted != null) {
                    result.add(casted);
                }
            }
        }
        return result;
    }

}
