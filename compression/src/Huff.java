// file: Huff.java
// author: Bob Muller
// revised: March 3, 2018
//

import edu.princeton.cs.algs4.BinaryOut;

import java.io.FileReader;
import java.io.IOException;

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
      while (c != -1) {
        c = inputFile.read();

        if (c != -1) {
          Integer key = new Integer(c);
          STValue stv = st.get(key);
          Bits bits = stv.getBits();
          bits.write(outputFile);

          if (DEBUG)
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