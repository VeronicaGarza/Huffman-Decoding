// file: SymbolTable.java
// author: Bob Muller
// revised: March 3, 2018
//
// An API for a symbol table ADT used in the Huffman Coding Algorithm
// part of a problem set in CSCI 1102.
//

import edu.princeton.cs.algs4.BinaryOut;

import java.util.Collection;
import java.util.SortedSet;

public interface SymbolTable {
  Object put(Integer key, STValue value);

  STValue get(Integer key);

  boolean containsKey(Object key);

  int size();

  boolean isEmpty();

  SortedSet<Integer> keySet();

  Collection<STValue> values();

  void writeFrequencyTable(BinaryOut outputFile);

  String toString();
}