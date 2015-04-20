package com.opensource.ssdb.pool;

import java.io.Closeable;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.opensource.ssdb.exception.SSDBConnectionException;
import com.opensource.ssdb.exception.SSDBException;


public abstract class Pool<T> implements Closeable {
  protected GenericObjectPool<T> internalPool;

  /**
   * Using this constructor means you have to set and initialize the internalPool yourself.
   */
  public Pool() {
  }

  @Override
  public void close() {
    closeInternalPool();
  }

  public boolean isClosed() {
    return this.internalPool.isClosed();
  }

  public Pool(final GenericObjectPoolConfig poolConfig, PooledObjectFactory<T> factory) {
    initPool(poolConfig, factory);
  }

  public void initPool(final GenericObjectPoolConfig poolConfig, PooledObjectFactory<T> factory) {

    if (this.internalPool != null) {
      try {
        closeInternalPool();
      } catch (Exception e) {
      }
    }

    this.internalPool = new GenericObjectPool<T>(factory, poolConfig);
  }

  public T getResource() {
    try {
      return internalPool.borrowObject();
    } catch (Exception e) {
      throw new SSDBConnectionException("Could not get a resource from the pool", e);
    }
  }

  public void returnResourceObject(final T resource) {
    if (resource == null) {
      return;
    }
    try {
      internalPool.returnObject(resource);
    } catch (Exception e) {
      throw new SSDBException("Could not return the resource to the pool", e);
    }
  }

  public void returnBrokenResource(final T resource) {
    if (resource != null) {
      returnBrokenResourceObject(resource);
    }
  }

  public void returnResource(final T resource) {
    if (resource != null) {
      returnResourceObject(resource);
    }
  }

  public void destroy() {
    closeInternalPool();
  }

  protected void returnBrokenResourceObject(final T resource) {
    try {
      internalPool.invalidateObject(resource);
    } catch (Exception e) {
      throw new SSDBException("Could not return the resource to the pool", e);
    }
  }

  protected void closeInternalPool() {
    try {
      internalPool.close();
    } catch (Exception e) {
      throw new SSDBException("Could not destroy the pool", e);
    }
  }
}
