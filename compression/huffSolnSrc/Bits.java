// file: Bits.java
// author: Bob Muller
// revised: March 3, 2018
//
// The API for bit strings in the Huffman Coding Algorithm.
//
import edu.princeton.cs.algs4.BinaryOut;

public interface Bits {
  int getBits();
  int getSize();
  String toString();
  Bits addBit(int bit);
  void write(BinaryOut outFile);
}
