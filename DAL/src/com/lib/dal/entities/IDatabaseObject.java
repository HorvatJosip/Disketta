package com.lib.dal.entities;

import java.util.function.Function;

public interface IDatabaseObject<T> {
    Function<Object[], T> getConverter(Object... additionalData);
}
