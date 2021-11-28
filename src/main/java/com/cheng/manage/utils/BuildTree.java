package com.cheng.manage.utils;

import com.cheng.manage.common.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author weicheng
 * @version v1.0.0
 * @description
 * @name BuildTree
 * @date 2021/11/28 20:06
 */
public class BuildTree {

    public static<T extends TreeNode<T>> List<T> buildTree(List<T> tableList) {
        List<T> treeList = new ArrayList<>();
        for (T item : tableList) {
            if (item.isTrunk()) {
                treeList.add(findChildren(item, tableList));
            }
        }
        return treeList;
    }

    private static<T extends TreeNode<T>> T findChildren(T parentNode, List<T> nodeList) {
        for (T node : nodeList) {
            if (parentNode.getId().equals(node.getParentId())) {
                if (parentNode.getChildren() == null) {
                    parentNode.setChildren(new ArrayList<>());
                }
                parentNode.getChildren().add(findChildren(node, nodeList));
            }
        }
        return parentNode;
    }
}
