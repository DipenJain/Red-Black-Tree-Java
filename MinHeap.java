/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.Arrays;
/**
 *
 * @author dipen
 */
public class MinHeap 
{ 
    private Node[] Heap; 
    private int size; 
    private final int maxsize; 
    private static final int FRONT = 1;

    public MinHeap(int maxsize) 
    { 
        this.maxsize = maxsize;
        this.size = 0;
        this.Heap = new Node[this.maxsize + 1];
        Arrays.fill(this.Heap, Node.Nil);
    }
    
    // Function to return index of parent
    private int parent(int pos) 
    { 
        return pos / 2; 
    } 
  
    // Function to return the position of the 
    // left child for the node currently at pos 
    private int leftChild(int pos) 
    { 
        return (2 * pos); 
    } 
  
    // Function to return the position of 
    // the right child for the node currently at pos 
    private int rightChild(int pos) 
    { 
        return (2 * pos) + 1; 
    } 
    
    // Function to return boolean value to check if there is a left child for element at pos
    private boolean hasLeftChild(int pos) 
    { 
        return 2*pos <= size;
    }
    
    // Function to return boolean value to check if there is a right child for element at pos
    private boolean hasRightChild(int pos) 
    { 
        return 2*pos + 1 <= size;
    }
    
    // Function which swaps elements at two positions - fpos and spos
    private void swap(int fpos, int spos) 
    { 
        Node tmp; 
        tmp = Heap[fpos]; 
        Heap[fpos] = Heap[spos]; 
        Heap[spos] = tmp; 
    } 
    
    // Function to insert node in the heap
    public void insert(Node n) 
    { 
        if (size >= maxsize) { 
            return; 
        }        
        Heap[++size] = n; 
        int current = size;
        
        // Swaps elements until the new node reaches the correct position
        while (Heap[current]!= Node.Nil && Heap[parent(current)] != Node.Nil 
                && (
                (Heap[current].executed_time == Heap[parent(current)].executed_time &&
                     Heap[current].buildingNum < Heap[parent(current)].buildingNum)
                || (Heap[current].executed_time < Heap[parent(current)].executed_time)))
        { 
            swap(current, parent(current)); 
            current = parent(current); 
        } 
    }
    
    // Function to perform minheapify function on node at position pos
    private void minHeapify(int pos) 
    {
        while(hasLeftChild(pos))
        {
            int smallChild = leftChild(pos);
            
            // Find minimum of left or right child and store it in small child
            if(hasRightChild(pos))
            {
                if(Heap[rightChild(pos)].executed_time == Heap[smallChild].executed_time &&
                        Heap[rightChild(pos)].buildingNum < Heap[smallChild].buildingNum)
                {
                    smallChild = rightChild(pos);
                }
                else if(Heap[rightChild(pos)].executed_time < Heap[smallChild].executed_time)
                {
                    smallChild = rightChild(pos);
                }
            }
            
            // Check if the small child has smaller value than its parent
            // If yes, swap and continue recursively; else break
            if(Heap[pos].executed_time >= Heap[smallChild].executed_time)
            {
                if((Heap[pos].executed_time > Heap[smallChild].executed_time)||
                   (Heap[pos].executed_time == Heap[smallChild].executed_time && 
                        Heap[pos].buildingNum > Heap[smallChild].buildingNum))
                {
                    swap(pos,smallChild);
                    pos = smallChild;
                }
                else
                    break;
            }
            else
                break;
        }
    }
    
    // Function to remove and return the minimum 
    // element from the heap 
    public Node removeMin() 
    {
        Node popped = Heap[FRONT];
        if(size>0)
        {
            Heap[FRONT] = Heap[size]; 
            Heap[size--] = Node.Nil;
            minHeapify(FRONT); 
        }
        return popped; 
    } 
    
    // Testing function to print minheap contents - Not used
    public void print() 
    { 
        if(size == 1)
        {
            int parent = Heap[1].buildingNum;
            System.out.print(" PARENT : " + parent); 
            System.out.println();
        }
        else
        {
            for (int i = 1; i <= size / 2; i++) { 
                int parent = Heap[i]!=Node.Nil?Heap[i].buildingNum:-1;
                int lchild = Heap[2 * i]!=Node.Nil?Heap[2 * i].buildingNum:-1;
                int rchild = Heap[2 * i + 1]!=Node.Nil?Heap[2 * i + 1].buildingNum:-1;
                System.out.print(" PARENT : " + parent
                                 + " LEFT CHILD : " + lchild
                                 + " RIGHT CHILD :" + rchild ); 
                System.out.println(); 
            } 
        }
    } 
}