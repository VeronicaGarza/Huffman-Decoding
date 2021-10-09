// file: HuffmanTreeC.java
// author: Bob Muller
// date: March 10, 2012
// revised: March, 2014
//
// The HuffmanTreeC class implements the HuffmanTree ADT. This
// implementation has 3 constructors: one for leaves, one for interior
// nodes and one that accepts a SymbolTable as input and uses a
// java.util.PriorityQueue to build -the- Huffman Coding Tree for the
// given input text.
//

import java.util.PriorityQueue;
import java.util.SortedSet;

public class HuffmanTreeC implements HuffmanTree {

  // Instance variables.
  //
  private int weight;
  private char symbol;
  private HuffmanTree left;
  private HuffmanTree right;

  // Getters
  //
  public int getWeight() {
    return this.weight;
  }

  public char getSymbol() {
    return this.symbol;
  }

  public HuffmanTree getLeft() {
    return this.left;
  }

  public HuffmanTree getRight() {
    return this.right;
  }

  public boolean isLeaf() {
    return this.getLeft() == null;
  }

  // Make a leaf.
  //
  public HuffmanTreeC(char symbol, int frequency) {
    this.weight = frequency;
    this.symbol = symbol;
    this.left = null;
    this.right = null;
  }

  // Make an interior node.
  //
  public HuffmanTreeC(int weight, HuffmanTree left, HuffmanTree right) {
    this.weight = weight;
    this.symbol = (char) 0;
    this.left = left;
    this.right = right;
  }

  // Make a HuffmanTree by processing the symbol frequency
  // information in the symbol table.
  //
  public HuffmanTreeC(SymbolTable st) {

    PriorityQueue<HuffmanTree> pq = new PriorityQueue<HuffmanTree>();

    // Retrieve the set of keys from the symbol table. NB that the
    // keys are housed in a -sorted- set. See next comment.
    //
    SortedSet<Integer> keys = st.keySet();

    // Insert the initial leaf entries in the priority queue. NB
    // because keys is a SortedSet, the for-loop will iterate through
    // the keys in (ascending) order. This ensures that Huff and Puff
    // will enter the leaves into the pq in the same order.
    //
    for (Integer i : keys) {
      char symbol = (char) i.intValue();
      STValue stv = st.get(i);
      int frequency = stv.getFrequency();
      HuffmanTree leaf = new HuffmanTreeC(symbol, frequency);
      pq.add(leaf);
    }

    while (pq.size() > 0)
      System.out.format("Next element is %s.%n", pq.poll());

    for (Integer i : keys) {
      char symbol = (char) i.intValue();
      STValue stv = st.get(i);
      int frequency = stv.getFrequency();
      HuffmanTree leaf = new HuffmanTreeC(symbol, frequency);
      pq.add(leaf);
    }

    // Now synthesize the Huffman Coding Tree by boiling down the
    // priority queue.
    //
    while (pq.size() > 1) {

      HuffmanTree t1 = pq.poll();
      HuffmanTree t2 = pq.poll();
      int weight = t1.getWeight() + t2.getWeight();

      pq.add(new HuffmanTreeC(weight, t1, t2));
    }

    // All done. The single item in pq is the Huffman Coding
    // Tree. If this were a function, we would return it as our
    // answer. But this is a constructor so we copy its field
    // values into the field values of this.
    //
    HuffmanTree theTree = pq.poll();
    this.weight = theTree.getWeight();
    this.symbol = theTree.getSymbol();
    this.left = theTree.getLeft();
    this.right = theTree.getRight();
  }

  // The computeBitCodes function recursively walks the tree. When
  // it encounters a leaf node, it updates the symbol table entry to
  // include the computed bit sequence. NB: the Bits type is
  // immutable but the STValue type is mutable.
  //
  public void computeBitCodes(SymbolTable st, Bits bits) {
    if (this.isLeaf()) {
      Integer key = new Integer(this.getSymbol());
      STValue stv = st.get(key);
      stv.setBits(bits);
    }
    else {
      // this is an interior node (with two children).
      //
      this.getLeft().computeBitCodes(st, bits.addBit(0));
      this.getRight().computeBitCodes(st, bits.addBit(1));
    }
  }

  public String toString() {
    int weight = this.getWeight();
    if (this.isLeaf())
      return String.format("%c:%d", this.getSymbol(), weight);
    else {
      String
              ls = this.getLeft().toString(),
              rs = this.getRight().toString();
      return String.format("HT(%d, %s, %s)", weight, ls, rs);
    }
  }

  // HuffmanTrees need to be ordered because they are going to
  // reside in a PriorityQueue.
  //
  public int compareTo(HuffmanTree other) {
    int answer = new Integer(this.getWeight()).compareTo(other.getWeight());
    // Break ties in favor of element already in the pq.
    //
    return answer == 0 ? 1 : answer;
  }
}
