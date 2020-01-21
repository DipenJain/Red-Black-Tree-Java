/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.util.*;
/**
 *
 * @author dipen
 */
public class risingCity {
    
    Node current;
    MinHeap h;
    RedBlackTree t;
    int counter;
    int consecDays;
    
    /**
     * Constructor which initializes all class members
    */
    risingCity()
    {
        this.h = new MinHeap(2000);
        this.t = new RedBlackTree();
        this.current = t.Nil;
        this.counter = 0;
        this.consecDays = 0;
    }
    
    /**
     * Takes a node object as an input and inserts it into RBT and minheap if it isn't already present in the RBT.
     * If it is present in the RBT, an error is thrown
     * @param {@code newNode} node reference which needs to be inserted into minheap and RBT
    */
    public void Insert(Node newNode)
    {
        t.insert(newNode);
        h.insert(newNode);
    }
    
    /**
     * Takes a buildingNum as an input and check and outputs the (buildingNum,exec_time,total_time) if
     * present in the RBT else prints (0,0,0)
     * Takes an output string to which the result is appended and is later written to a file.
     * @param {@code bNum} building Num for which details should be printed
     * @param {@code output} string to which the output is appended
    */
    public void PrintBuilding(StringBuilder output,int bNum)
    {
        Node toPrintNode = t.nodeDetails(bNum);
        if(toPrintNode == t.Nil)
            output.append("(0,0,0)\n");
        else
            output.append("("+ toPrintNode.buildingNum + ","+toPrintNode.executed_time+","+toPrintNode.total_time+")\n");
    }
    
    /**
     * Takes two input integer values as range and stores the buildings present in that range
     * Takes an output string to which the result is appended and is later written to a file.
     * @param {@code b1,b2} two integers within the range of which all values in RBT should be printed
     * @param {@code output} string to which the output is appended
    */
    public void PrintBuilding(StringBuilder output, int b1, int b2)
    {
        List<Node> list = t.nodesInRange(b1,b2);
        if (!list.isEmpty())
        {
            for (Node node: list){
                output.append("("+node.buildingNum+","+node.executed_time+","+node.total_time + "),");
            }
            output.deleteCharAt(output.length()-1);//remove last comma
            output.append("\n");
        }
        else
        {
            output.append("(0,0,0)\n");
        }
    }
    
    /**
     * Selects a building to work on from the heap by doing a remove min and returns the building.
     * If there is no current building -> It performs remove min
     * else it works on the current building.
     * 
     * @param currentNode - of type {@code Node} 
     * @return object {@code currentNode}
    */
    public Node workBuilding(Node currentNode)
    {
        if(currentNode == t.Nil)
        {
            currentNode = h.removeMin();
        }
        if(currentNode!=t.Nil)
        {
            currentNode.executed_time++;
            this.consecDays++;
        }
    return currentNode;
    }
    
    /**
     * @param args the command line arguments - reads the input file name into args
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        risingCity obj = new risingCity();
        File ipFile = new File(args[0]);
        File opFile = new File("output_file.txt"); 
        
        BufferedReader br = new BufferedReader(new FileReader(ipFile)); 
        BufferedWriter bw = new BufferedWriter(new FileWriter(opFile));
        StringBuilder output = new StringBuilder();
        String curCmd = br.readLine();
        while (true)
        {
            // Call work function to select a particular building and work on it
            obj.current = obj.workBuilding(obj.current);
            
            // case when no building to work on and no command to execute - close all connections and quit
            // construction complete
            if(obj.current == obj.t.Nil && curCmd == null)
            {
                bw.write(output.toString().trim());
                bw.close();
                break;
            }
            
            // if command exists in file and global counter value matches counter in input file
            // then read and perform the step
            if(curCmd != null && obj.counter == Integer.parseInt(curCmd.split(":")[0]))
            {
                String[] param = curCmd.substring(curCmd.indexOf('(') + 1, curCmd.indexOf(')')).split(",");
                if(curCmd.contains("Insert"))
                {    
                    int bNum = Integer.parseInt(param[0]);
                    int tt = Integer.parseInt(param[1]);
                    Node newNode = new Node(bNum,tt);
                    // tries to insert node, if duplicate is there - error will be thrown
                    try
                    {
                        obj.Insert(newNode);
                    }
                    // Handling the error
                    catch(Exception e)
                    {
                        output.append(e.getMessage());
                        bw.write(output.toString());
                        bw.close();
                        br.close();
                        System.exit(0);
                    }
                }
                else if(curCmd.contains("PrintBuilding"))
                {
                    // Print between a range
                    if(param.length>1)
                    {
                        int param1 = Integer.parseInt(param[0]);
                        int param2 = Integer.parseInt(param[1]);
                        obj.PrintBuilding(output, param1, param2);
                    }
                    
                    // Print details of a particular building
                    else
                    {
                        int param1 = Integer.parseInt(param[0]);
                        obj.PrintBuilding(output, param1);
                    }
                }
                curCmd = br.readLine();   // store and read the next input
                if(curCmd == null)        // If no input to read - close file reader connection
                    br.close();
            }
                   
            if(obj.current!=obj.t.Nil)
            {
                // When construction of a building is over
                // Delete from RBT, reset current and consecDays values
                if(obj.current.executed_time == obj.current.total_time)
                {
                    output.append("("+obj.current.buildingNum + "," + obj.counter+")\n");
                    obj.consecDays = 0;
                    obj.t.delete(obj.current);
                    obj.current = obj.t.Nil;
                }
                
                // When a building is worked on for 5 days
                // Reinsert into heap and reset global variables
                else if(obj.consecDays == 5)
                {
                    obj.h.insert(obj.current);
                    obj.current = obj.t.Nil;
                    obj.consecDays = 0;
                }
            }
            obj.counter++;  // Increment the global counter
        }
    }
}
