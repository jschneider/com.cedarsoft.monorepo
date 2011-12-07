package com.cedarsoft.test.utils;

import com.google.common.base.Joiner;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.Nonnull;

import org.junit.rules.*;
import org.junit.runner.*;
import org.junit.runners.model.*;


public class ThreadRule implements TestRule {
  @Override
  public Statement apply(final Statement base, Description description) {
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        before();
        try {
          base.evaluate();
        }
        finally {
          after();
        }
      }
    };
  }

  private Collection<Thread> initialThreads;

  private void before() {
    if (initialThreads != null) {
      throw new IllegalStateException("???");
    }

    initialThreads = Thread.getAllStackTraces().keySet();
  }

  @Nonnull
  public Collection<? extends Thread> getInitialThreads() {
    if (initialThreads == null) {
      throw new IllegalStateException("not initialized yet");
    }
    return Collections.unmodifiableCollection(initialThreads);
  }

  private void after() {
    Collection<Thread> threadsNow = Thread.getAllStackTraces().keySet();

    Set<Thread> remainingThreads = new HashSet<Thread>(threadsNow);
    remainingThreads.removeAll(initialThreads);

    for (Iterator<Thread> iterator = remainingThreads.iterator(); iterator.hasNext();) {
      Thread remainingThread = iterator.next();
      if (!remainingThread.isAlive()) {
        iterator.remove();
      }
    }

    if (!remainingThreads.isEmpty()) {
      throw new IllegalStateException("Some threads have been left:\n" + buildMessage(remainingThreads));
    }
  }

  @Nonnull
  private String buildMessage(@Nonnull Set<Thread> remainingThreads) {
    StringBuilder builder = new StringBuilder();

    for (Thread remainingThread : remainingThreads) {
      builder.append("---------------");
      builder.append("\n");
      builder.append(remainingThread);
      builder.append("\n");
      builder.append("---------------");
      builder.append("\n");
      builder.append(Joiner.on("\n").join(remainingThread.getStackTrace()));
      builder.append("\n");
    }

    return builder.toString();
  }
}
