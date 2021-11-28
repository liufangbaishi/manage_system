package com.cheng.manage.common;

import java.util.List;

/**
 * @author weicheng
 * @version v1.0.0
 * @description
 * @name TreeNode
 * @date 2021/11/28 20:48
 */
public abstract class TreeNode<T extends TreeNode<T>> {

    // 判断是否为主枝干
    public boolean isTrunk() {
        return getParentId() == 0;
    }

    public abstract Long getParentId();

    public abstract Long getId();

    public abstract List<T> getChildren();

    public abstract void setChildren(List<T> child);
}
