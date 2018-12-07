package com.booway.mvpdemo.utils;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author wandun
 * @date 2018/12/5
 * @desc Acitivity、Fragment处理
 */

public class ActivityUtils {
    public static Fragment currentFragment;

    /**
     * add fragment to activity
     *
     * @param fragmentManager
     * @param fragment
     * @param frameId
     */
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int frameId) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }

    /**
     * switch fragment in activity
     *
     * @param fragmentManager
     * @param targetFragment
     * @param frameId
     */
    public static void switchFragment(@NonNull FragmentManager fragmentManager,
                                      @NonNull Fragment targetFragment,
                                      int frameId) {
        checkNotNull(fragmentManager);
        checkNotNull(targetFragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (!targetFragment.isAdded()) {
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            transaction.add(frameId, targetFragment, targetFragment.getClass().getName());
        } else {
            transaction.hide(currentFragment).show(targetFragment);
        }
        currentFragment = targetFragment;
        transaction.commit();
    }

}
