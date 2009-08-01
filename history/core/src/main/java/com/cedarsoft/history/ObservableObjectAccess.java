package com.cedarsoft.history;

import com.cedarsoft.CommitableObjectAccess;

/**
 * @param <T> the type
 */
public interface ObservableObjectAccess<T> extends CommitableObjectAccess<T>, ObservableCollection<T> {
}