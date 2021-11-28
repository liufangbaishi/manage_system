package com.cheng.manage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author weicheng
 * @version v1.0.0
 * @description
 * @name BuildTree
 * @date 2021/11/28 20:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
class TreeNode {
    private String id;
    private String parentId;
    private String name;
    private List<TreeNode> children;

    public TreeNode(String id, String name, String parentId) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
    }
}
public class BuildTreeTest {
    public static void main(String[] args) {
        TreeNode t1 = new TreeNode("1","p1","0");
        TreeNode t2 = new TreeNode("2","p2","0");
        TreeNode t3 = new TreeNode("3","p11","1");
        TreeNode t4 = new TreeNode("4","p12","1");
        TreeNode t5 = new TreeNode("5","p21","2");
        TreeNode t6 = new TreeNode("6","p22","2");
        TreeNode t7 = new TreeNode("7","p23","2");
        TreeNode t8 = new TreeNode("8","p111","3");
        TreeNode t9 = new TreeNode("9","p123","3");

        List<TreeNode> treeList = new ArrayList<>();
        Collections.addAll(treeList, t1,t2,t3,t4,t5,t6,t7,t8,t9);

        pretty(treeList);
    }

    private static void pretty(List<TreeNode> treeList) {
//        System.out.println(toTree01(treeList));
        System.out.println(toTree02(treeList));
    }

    /**
     * 方式一：使用两层循环实现
     * @return
     */
    public static List<TreeNode> toTree01(List<TreeNode> treeList) {
        List<TreeNode> retList = new ArrayList<>();

        for (TreeNode parent : treeList) {
            if ("0".equals(parent.getParentId())) {
                retList.add(parent);
            }

            for (TreeNode child : treeList) {
                if (child.getParentId().equals(parent.getId())) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(child);
                }
            }
        }
        return retList;
    }

    /**
     * 方式二：使用递归实现
     * @param treeList
     * @return
     */
    public static List<TreeNode> toTree02(List<TreeNode> treeList) {
        List<TreeNode> retList = new ArrayList<>();
        for (TreeNode parent : treeList) {
            if ("0".equals(parent.getParentId())) {
                retList.add(findChildren(parent, treeList));
            }
        }
        return retList;
    }

    private static TreeNode findChildren(TreeNode parent, List<TreeNode> treeList) {
        for (TreeNode child : treeList) {
            if (parent.getId().equals(child.getParentId())) {
                if (parent.getChildren() == null) {
                    parent.setChildren(new ArrayList<>());
                }
                parent.getChildren().add(findChildren(child, treeList));
            }
        }
        return parent;
    }

}

