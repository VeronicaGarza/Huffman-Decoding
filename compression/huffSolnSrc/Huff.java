// file: Huff.java
// author: Bob Muller
// revised: March 3, 2018
//
// This is the Huffman compression half of the Huffman
// compression/decompression algorithm. (This is sometimes called a
// codec for "code"/"decode".)
//
// KNOWN PROBLEMS:
//
// 1. This program isn't working properly on files other than standard
//    ASCII text, that is, character codes 00 - 127. This seems to be a
//    problem with the read function.
//
// The Algorithm
//
// The algorithm implemented here accepts a single command line
// argument that should be a filename of the form name.txt, where the
// extension ".txt" is required. It then proceeds as follows:
//
// 1. Read all of the characters from the input file and create a
//    symbol table that records the frequency of each character that
//    appears in the input file.
//
// 2. Use the constructed symbol table as the basis for constructing
//    the corresponding Huffman coding tree.
//
// 3. Using the Huffman coding tree, fill in the symbol table entry
//    for each character to record the bit string assigned to that
//    character in the Huffman coding tree.
//
// 4. Create the compressed file.
//
//    A. Write out the 2-byte bit string 0x0bc0 that identifies the
//       file as one that is compressed by our algorithm.
//
//    B. Write out the 4-byte int size of the frequency table.
//
//    C. Write out the frequency table. Each entry in the table is a
//       pair of values occupying a total of 5 bytes. In particular,
//       with 1 byte for the character code followed by the 4-byte int
//       giving the character's frequency in the input file.
//
//    D. Now the symbol table can be used to look up the bit strings
//       representing each character. Read through the input file one
//       more time, for each occurrence of a character in the input
//       file, write out its bit string to the binary output file.
//
//    CONVENTIONS: In addition to the above binary file format, we
//      require that:
//
//      1. When the priority queue is constructed, the symbols are
//      inserted in ascending order of the symbols.  For example, if
//      the symbols are: C1, B1, A2, they would be inserted in the
//      order: A2 then B1 then C1. The pq will then order them by
//      their weights: B1, C1, A2. (See 2.)
//
//      2. When a Huffman tree with weight w is inserted into the
//      priority queue, if there are already trees in the queue with
//      the same weight, the one being inserted should be put BEHIND
//      all others of the same weight;
//
//      3. When bit patterns are being assigned to characters, the bit
//      strings are built-up by adding bits to the RIGHT (rather than
//      the left) and a leftward branch in the tree is assigned a 0
//      bit and a rightward branch is assigned a 1 bit.
//
// Structure of zip file:
//
// File       ::= 2-BYTE-MAGIC# 4-BYTE-TABLESIZE TABLE BINARYCODE
// TABLEENTRY ::= 1-BYTE-SYMBOL 4-BYTE-INT-FREQUENCY
//

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import edu.princeton.cs.algs4.BinaryOut;

public class Huff {

  // The following creates a hexadecimal (base 16) constant.
  //
  public static final int MAGIC_NUMBER = 0x0bc0;
  public static final boolean DEBUG = false;

  private final String[] args;

  public Huff(String[] args) {

    this.args = args;
  }

  // This is the main routine in the Huffman Coding Algorithm.
  //
  private void go() {

    FileIO io = new FileIOC();
    FileReader inputFile = io.openInputFile(this.args[0]);

    SymbolTable st;
    HuffmanTree ht;

    if (DEBUG) {
      System.out.format("go: opened input file %s\n", this.args[0]);

      st = new SymbolTableC(inputFile);
      System.out.format("Symbol table = %s\n", st.toString());
      ht = new HuffmanTreeC(st);
      System.out.format("Huffman coding tree = %s\n", ht.toString());

      // We'll now recursively walk the tree building up the bit
      // strings as we go.  When we reach a leaf node, we'll add
      // the computed bit string to its symbol table entry. This
      // will facilitate writing the bit strings for the input
      // letters.
      //
      ht.computeBitCodes(st, new BitsC());
      System.out.format("Symbol table = %s\n", st.toString());
    }
    else {
      st = new SymbolTableC(inputFile);
      ht = new HuffmanTreeC(st);
      ht.computeBitCodes(st, new BitsC());
    }
    // We now have everything we need to write the compressed
    // file. First reopen the source file.
    //
    inputFile = io.openInputFile(this.args[0]);

    BinaryOut outputFile = io.openBinaryOutputFile();

    // 1. write the magic number.
    //
    outputFile.write(MAGIC_NUMBER, 16);

    // 2. write out the frequency table.
    //
    if (DEBUG)
      System.out.format("symbol table size = %d\n", st.size());

    st.writeFrequencyTable(outputFile);

    // 3. read through the input text file again. This time, write
    // the variable length bit strings to the binary output file.
    //
    int c = 0;
    try {
      while(c != -1) {
        c = inputFile.read();

        if(c != -1) {
          Integer key = new Integer(c);
          STValue stv = st.get(key);
          Bits bits = stv.getBits();
          bits.write(outputFile);

          if(DEBUG)
            System.out.format("wrote %c = %s\n", (char) c, bits.toString());
        }
      }
      inputFile.close();
      outputFile.flush();
      outputFile.close();
    }
    catch (IOException e) {
      System.out.format("go: hit with this IOException\n");
    }
  }

    public static void main(String[] args) {
      new Huff(args).go();
    }
}
