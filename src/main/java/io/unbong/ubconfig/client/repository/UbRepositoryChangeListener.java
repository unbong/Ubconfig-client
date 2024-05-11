package io.unbong.ubconfig.client.repository;

import io.unbong.ubconfig.client.spring.ConfigMeta;

public interface UbRepositoryChangeListener<T> {

    void onChange(UbRepositoryChangeEvent<T> event);

    record UbRepositoryChangeEvent<T>(ConfigMeta meta, T config){};
}
