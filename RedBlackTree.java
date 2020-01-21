/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.*;
/**
 *
 * @author dipen
 */
public class RedBlackTree {
    public Node Nil; //external node initialized in constructor
    public Node root;
    
    //Constructor for RBT
    public RedBlackTree(){
        Nil = Node.Nil;
        root = Nil;
        root.left = Nil;
        root.right = Nil;
    }
    
    //Function to replace n1 with n2
    private void nodeTransplant(Node n1, Node n2)
    {
        //case when n1 is the root - root needs to be changed
        if(n1.parent == Nil)
        {
            root = n2;
        }
        //case when n1 is the left child of it's parent
        else if(n1 == n1.parent.left)
        {
            n1.parent.left = n2;
        }
        //case when n1 is the right child of it's parent
        else
        {
            n1.parent.right = n2;
        }
        n2.parent = n1.parent;
    }
    
    //Function to find the minimum node in the subtree rooted at x
    private Node minimumNode(Node x)
    {
        while(x.left!=Nil)
            x = x.left;
        return x;
    }
    
    //Function to rotate left at node x
    private void leftRotate(Node x)
    {
        Node y = x.right;
        x.right = y.left;
        if(y.left != Nil)
            y.left.parent = x;
        y.parent = x.parent;
        if(x.parent == Nil)
        {
            root = y;
        }
        else
        {
            if(x == x.parent.left)
                x.parent.left = y;
            else
                x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }
    
    //Function to rotate right at node x
    private void rightRotate(Node x)
    {
        Node y = x.left;
        x.left = y.right;
        if(y.right!=Nil)
            y.right.parent = x;
        y.parent = x.parent;
        if(x.parent == Nil)
        {
            root = y;
        }
        else
        {
            if(x == x.parent.right)
                x.parent.right = y;
            else
                x.parent.left = y;
        }
        y.right = x;
        x.parent = y;
    }
    
    //Function to insert into RBT
    public void insert(Node newNode)
    {
        Node y = Nil;
        Node x = root;
        //Find the correct position of the new node to be inserted
        //If the node already exists, then throws an error
        while(x != Nil)
        {
            y = x;
            if(newNode.buildingNum < x.buildingNum)
                x = x.left;
            else if(newNode.buildingNum > x.buildingNum)
                x = x.right;
            else if(newNode.buildingNum == x.buildingNum)
                throw new IllegalArgumentException("Same building number trying to be reinserted");
        }
        newNode.parent = y;
        if(y == Nil) //New node is the root of the tree
        {
            root = newNode;
        }
        else
        {
            if(newNode.buildingNum < y.buildingNum)
            {
                y.left = newNode;
            }
            else
            {
                y.right = newNode;
            }
        }
        newNode.left = Nil;
        newNode.right = Nil;
        newNode.color = Color.RED;
        //Fix the double red scenario caused by insert
        insertFix(newNode);
    }
    
    //Function to fix anomalies du to double red scenario by performing rotations or color swaps
    private void insertFix(Node z)
    {
        while(z.parent.color == Color.RED)
        {
            //Case when parent is left child of grand parent
            if(z.parent == z.parent.parent.left)
            {
                Node uncle = z.parent.parent.right;
                // Case when uncle is red, do color swap and move two levels up
                if(uncle.color == Color.RED)
                {
                    z.parent.color = Color.BLACK;
                    uncle.color = Color.BLACK;
                    z.parent.parent.color = Color.RED;
                    z = z.parent.parent;
                }
                //Uncle is black - two cases LRb and LLb
                else
                {
                    //LRb - two rotations rotate left and then rotate right
                    if(z == z.parent.right)
                    {
                        z = z.parent;
                        leftRotate(z);
                    }
                    //LLb - only 1 - right rotation
                    z.parent.color = Color.BLACK;
                    z.parent.parent.color = Color.RED;
                    rightRotate(z.parent.parent);
                }
            }
            //Case when parent is right child of grandparent
            else if(z.parent == z.parent.parent.right)
            {
                Node uncle = z.parent.parent.left;
                // Case when uncle is red, do color swap and move two levels up
                if(uncle.color == Color.RED)
                {
                    z.parent.color = Color.BLACK;
                    uncle.color = Color.BLACK;
                    z.parent.parent.color = Color.RED;
                    z = z.parent.parent;
                }
                //Uncle is black - two cases LRb and LLb
                else
                {
                    //LRb - two rotations rotate right and then rotate left
                    if(z == z.parent.left)
                    {
                        z = z.parent;
                        rightRotate(z);
                    }
                    //LLb - only 1 - left rotation
                    z.parent.color = Color.BLACK;
                    z.parent.parent.color = Color.RED;
                    leftRotate(z.parent.parent);
                }
            }
        }
        root.color = Color.BLACK;
    }
    
    //Function to delete the node z
    public void delete(Node z)
    {
        Node y = z;
        Color yOc = y.color;
        Node x;
        if(z.left == Nil)
        {
            x = z.right;
            nodeTransplant(z,z.right);
        }
        else if(z.right == Nil)
        {
            x = z.left;
            nodeTransplant(z,z.left);
        }
        else
        {
            y = minimumNode(z.right);
            yOc = y.color;
            x = y.right;
            if(y.parent == z)
            {
                x.parent = y;
            }
            else
            {
                nodeTransplant(y,y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            nodeTransplant(z,y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }
        if(yOc == Color.BLACK) // Anomaly caused and needs to be fixed by calling the fix function
            deleteFix(x);
    }
    
    //Function to fix the anomaly at node x due to delete
    private void deleteFix(Node x)
    {
        while(x!=root && x.color == Color.BLACK)
        {
            if(x == x.parent.left)
            {
                // Sibling node on the right
                Node w = x.parent.right;
                // Case1: when x sibling is red 
                // In this case switch colors and rotate to fix the anomaly
                if(w.color == Color.RED)
                {
                    w.color = Color.BLACK;
                    x.parent.color = Color.RED;
                    leftRotate(x.parent);
                    w = x.parent.right;
                }
                
                // Case 2: When x sibling w is black, and both of w children are black
                // switch color of sibling and move up till the anomaly is fixed
                if(w.left.color == Color.BLACK && w.right.color == Color.BLACK)
                {
                    w.color = Color.RED;
                    x = x.parent;
                }
                else
                {
                    // Case 3: When x sibling w is black, w left child is red, and w right child is black
                    if(w.right.color == Color.BLACK)
                    {
                        w.left.color = Color.BLACK;
                        w.color = Color.RED;
                        rightRotate(w);
                        w = x.parent.right;
                    }
                    // Case 4: x sibling w is black, and w right child is red
                    w.color = x.parent.color;
                    x.parent.color = Color.BLACK;
                    w.right.color = Color.BLACK;
                    leftRotate(x.parent);
                    x = root;
                }
            }
            
            // Below are the symmetric cases when sibling is on the left
            else
            {
                Node w = x.parent.left;
                if(w.color == Color.RED)
                {
                    w.color = Color.BLACK;
                    x.parent.color = Color.RED;
                    rightRotate(x.parent);
                    w = x.parent.left;
                }
                if(w.right.color == Color.BLACK && w.left.color == Color.BLACK)
                {
                    w.color = Color.RED;
                    x = x.parent;
                }
                else
                {
                    if(w.left.color == Color.BLACK)
                    {
                        w.right.color = Color.BLACK;
                        w.color = Color.RED;
                        leftRotate(w);
                        w = x.parent.left;
                    }
                    w.color = x.parent.color;
                    x.parent.color = Color.BLACK;
                    w.left.color = Color.BLACK;
                    rightRotate(x.parent);
                    x = root;
                }
            }
        }
        x.color = Color.BLACK;
    }
    
    
    //Function to return the list of nodes present in the range of b1 to b2 in the RBT
    public List<Node> nodesInRange(int b1, int b2)
    {
        List<Node> nodesFound = new LinkedList<Node>();
        valuesInRange(root, nodesFound, b1, b2);
        return nodesFound;
    }
    
    // Function to find and return the node with the given building number
    // returns nil if there exists no such building
    public Node findNode(int b1)
    {
        Node n = root;
        while(n != Nil)
        {
            if(n.buildingNum == b1)
                break;
            else if (n.buildingNum<b1)
                n = n.right;
            else if (n.buildingNum>b1)
                n = n.left;
        }
        return n;
    }
    
    // Function to return a node if the building number exists or returns a nil node if not present
    public Node nodeDetails(int b1)
    {
        Node n = findNode(b1);
        return n;
    }
    
    //Function to add elements to a list present in a range in the RBT
    private void valuesInRange(Node node, List<Node> list, int b1, int b2) {
        if (node == Nil) {
            return;
        }
        if (node.buildingNum>b1) {
            valuesInRange(node.left, list, b1, b2);
        }
        
        //Add the element to the list if it's present in the list
        if (b1 <= node.buildingNum && b2 >= node.buildingNum) {
            list.add(node);
        }

        if (node.buildingNum<b2) {
            valuesInRange(node.right, list, b1, b2);
        }
    }
    
    //Helper function to test and print the RBT - Not being used
    public void print(Node node) 
    {
        if (node == Nil) {
            return;
        }
        Queue<Node> q = new LinkedList<Node>();
        q.add(node);
        while(!q.isEmpty())
        {
            Node tmp = q.poll();
            int pbNum = tmp.parent == Nil?-1:tmp.parent.buildingNum;
            int lbNum = tmp.left == Nil?-1:tmp.left.buildingNum;
            int rbNum = tmp.right == Nil?-1:tmp.right.buildingNum;
            System.out.print(((tmp.color==Color.RED)?"Color: Red ":"Color: Black ")+"Building Num: "+tmp.buildingNum+
        " Parent: "+pbNum+ " Left: "+lbNum + " Right: "+rbNum+"\n");
            if(tmp.left!=Nil)
                q.add(tmp.left);
            if(tmp.right!=Nil)
                q.add(tmp.right);
        }
    }
    
}