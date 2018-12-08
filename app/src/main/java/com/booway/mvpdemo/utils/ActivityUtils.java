package com.booway.mvpdemo.utils;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author wandun
 * @date 2018/12/5
 * @desc Acitivity、Fragment处理
 */

public class ActivityUtils {
    public static Fragment currentFragment;
    public static Type currentClassType;
    private static HashMap<Type, Fragment> mapping;

    /**
     * switch fragment in activity
     *
     * @param fragmentManager
     * @param targetFragment
     * @param frameId
     */
    public static void switchFragment(@NonNull Type type,
                                      @NonNull FragmentManager fragmentManager,
                                      @NonNull Fragment targetFragment,
                                      int frameId) {
        checkNotNull(type);
        checkNotNull(fragmentManager);
        checkNotNull(targetFragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        //切换了页面，拿回当前Activity最后的Fragment
        if (currentClassType != null && currentClassType != type) {
            currentFragment = mapping.get(type);
        }

        //判断当前Fragment是否为激活状态，非激活
        if (!targetFragment.isAdded()) {
            //当前Fragment不为空且没有进行Activity切换，必须隐藏当前Fragment
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            //新增TargetFragment
            transaction.add(frameId, targetFragment, targetFragment.getClass().getName());
        } else {
            //激活状态下,切换Activie需要隐藏之前Activity最后显示的Fragment
            if (currentClassType != type) {
                transaction.hide(mapping.get(type));
            } else {
                transaction.hide(currentFragment);
            }
            transaction.show(targetFragment);
        }
        currentFragment = targetFragment;
        currentClassType = type;
        transaction.commit();
        saveTypeMapping(type);
    }

    //存储每个Activity最后的Fragment
    private static void saveTypeMapping(Type type) {
        if (mapping == null)
            mapping = new HashMap<>();
        if (mapping.containsKey(type)) {
            mapping.remove(type);
        }
        mapping.put(type, currentFragment);
    }
}
