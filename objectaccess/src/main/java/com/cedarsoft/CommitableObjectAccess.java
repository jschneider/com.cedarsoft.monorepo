package com.cedarsoft;

/**
 * Object access that offers a commit method
 *
 * @param <T> the type
 */
public interface CommitableObjectAccess<T> extends WriteableObjectAccess<T>, ObjectCommit<T> {
}