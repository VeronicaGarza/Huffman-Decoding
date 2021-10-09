// file: HuffmanTree.java
// author: Bob Muller
// date: no idea
// revised: March 3, 2018
//
// This is an API for an ADT of Huffman Trees. They are used in
// a Huffman coding/decoding project in CS102.  Huffman Trees are
// weighted, full binary trees.  The compareTo function allows them
// to inhabit a priority queue.
//
public interface HuffmanTree extends Comparable<HuffmanTree> {
  int getWeight();

  char getSymbol();

  HuffmanTree getLeft();

  HuffmanTree getRight();

  boolean isLeaf();

  void computeBitCodes(SymbolTable st, Bits bits);

  String toString();
}
