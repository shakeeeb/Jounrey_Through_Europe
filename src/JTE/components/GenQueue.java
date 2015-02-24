/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package JTE.components;

import java.util.ArrayList;
import java.util.LinkedList;
/**
 *
 * @author saleh
 */
public class GenQueue<E> {
  private LinkedList<E> list = new LinkedList();

  public GenQueue(){}
  
  public void enqueue(E item) {
    list.addLast(item);
  }

  public E dequeue() {
    return list.poll();
  }

  public boolean hasItems() {
    return !list.isEmpty();
  }

  public int size() {
    return list.size();
  }

  public void addItems(GenQueue<? extends E> q) {
    while (q.hasItems())
      list.addLast(q.dequeue());
  }
  
  public boolean isEmpty(){
      return list.isEmpty();
  }
  
  public ArrayList<E> toArrayList(){
      ArrayList<E> arr = new ArrayList(list);
      return arr;
  }
}
