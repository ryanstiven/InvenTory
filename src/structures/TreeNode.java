package structures;

import models.Motorcycle;

public class TreeNode {
    public Motorcycle data;
    public TreeNode left;
    public TreeNode right;

    public TreeNode(Motorcycle data) {
        this.data = data;
        this.left = null;
        this.right = null;
    }
}
