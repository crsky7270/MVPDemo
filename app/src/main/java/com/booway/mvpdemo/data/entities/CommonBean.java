package com.booway.mvpdemo.data.entities;

import android.arch.persistence.room.Ignore;

/**
 * 创建人：wandun
 * 创建时间：2018/12/19
 * 描述：${DESC}
 */

public class CommonBean {

    @Ignore
    private String AttrA;

    @Ignore
    private String AttrB;

    public String getAttrA() {
        return AttrA;
    }

    public void setAttrA(String attrA) {
        AttrA = attrA;
    }

    public String getAttrB() {
        return AttrB;
    }

    public void setAttrB(String attrB) {
        AttrB = attrB;
    }
}
