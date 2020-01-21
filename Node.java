/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author dipen
 */
enum Color{
    RED,
    BLACK
}
//Node class is used as a building block for RBT and MinHeap.
public class Node
{
    //Nil node is used to represent external nodes and is initialized dummy values
    
    public static final Node Nil = new Node(-1,-1,-1);
    public int buildingNum, executed_time, total_time;
    Color color;
    Node left, right, parent;
    
    //Node class constructor when a new node is created from input.
    public Node(int buildingNum, int total_time)
    {
        this.buildingNum = buildingNum;
        this.total_time = total_time;
        this.executed_time = 0;
        this.color = Color.BLACK;
        this.left = Nil;
        this.right = Nil;
        this.parent = Nil;
    }
    
    // Constructor called to create nil node.
    public Node(int buildingNum, int executed_time, int total_time)
    {
        this.buildingNum = buildingNum;
        this.total_time = total_time;
        this.executed_time = executed_time;
        this.color = Color.BLACK;
        this.left = Nil;
        this.right = Nil;
        this.parent = Nil;
    }
}