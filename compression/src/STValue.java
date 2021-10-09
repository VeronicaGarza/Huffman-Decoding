// file: STValue.java
// author: Bob Muller
// revised: March 3, 2018
//
// This file contains an API for an ADT of symbol table values. It is
// used as part of a Huffman coding/decoding algorithm.  A symbol table
// value packages up an integer frequency as well as the binary address
// of the symbol in the Huffman Coding Tree.
//
// NB: This is a mutable type.
//
public interface STValue {

  int getFrequency();

  Bits getBits();

  void setFrequency(int frequency);

  void setBits(Bits bits);

  String toString();
}
