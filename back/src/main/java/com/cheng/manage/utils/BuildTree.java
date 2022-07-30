package com.cheng.manage.utils;

import com.cheng.manage.common.model.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author weicheng
 * @version v1.0.0
 * @description
 * @name BuildTree
 * @date 2021/11/28 20:06
 */
public class BuildTree {

    /**
     * 构造树的另一种方法，时间复杂度更低，用空间换时间
     * @param tableList
     * @param <T>
     * @return
     */
    public static<T extends TreeNode<T>> List<T> buildTreeLessTime(List<T> tableList) {
        Map<Long, List<T>> listMap = tableList.stream().collect(Collectors.groupingBy(T::getParentId));
        tableList.forEach(item -> item.setChildren(listMap.get(item.getId())));
        return tableList.stream().filter(TreeNode::isTrunk).collect(Collectors.toList());
    }


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
