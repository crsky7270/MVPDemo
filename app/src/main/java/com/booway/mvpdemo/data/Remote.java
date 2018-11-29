package com.booway.mvpdemo.data;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Created by wandun on 2018/11/29.
 */
@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Remote {
}
