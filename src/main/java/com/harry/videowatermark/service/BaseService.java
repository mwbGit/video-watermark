package com.harry.videowatermark.service;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 描述:
 *
 * @author mengweibo@kanzhun.com
 * @create 2020/7/31
 */

@Service
public interface BaseService<T> {

    T selectByKey(Object key);

    int save(T entity);

    int saveNotNull(T entity);

    int delete(Object key);

    int updateAll(T entity);

    int updateNotNull(T entity);

    T selectOneByExample(Object example);

    List<T> selectByExample(Object example);

    List<T> selectAll();
}