import edu.princeton.cs.algs4.BinaryIn;

import java.io.FileWriter;
import java.io.IOException;

public class Puff {
  public static final boolean DEBUG = false;

  private final String[] args;

  public Puff(String[] args) {
    this.args = args;
  }

  private void go() {
    FileIO io = new FileIOC();
    BinaryIn inputFile = io.openBinaryInputFile(this.args[0]);
    int magic = inputFile.readShort();
    if (magic != Huff.MAGIC_NUMBER) {
      System.out.println("First two bytes do not match up with magic number.");
      System.exit(1);
    }
    
    SymbolTable st;
    st = new SymbolTableC(inputFile);

    // size of frequency table
    int size = 0;
    for (STValue x : st.values()) {
      size += x.getFrequency();
    }
    HuffmanTree hct;
    hct = new HuffmanTreeC(st);

    FileWriter outputFile = io.openOutputFile();

    for (int i = 0; i < size; i++) {
      try {
        HuffmanTree t = hct;
        while (!t.isLeaf()) {
          int bit = inputFile.readInt(1);
          t = bit == 0 ? t.getLeft() : t.getRight();
        }
        try {
          outputFile.write(t.getSymbol());
        }
        catch (IOException e) {
          System.out.println("Encountered IOException when writing to the output file.");
        }
      }
      catch (RuntimeException r) {
        break;
      }
    }
    try {
      outputFile.flush();
      outputFile.close();
    }
    catch (IOException e) {
      System.out.println("Encountered IOException when trying to close the output file.");
    }
  }

  public static void main(String[] args) {
    new Puff(args).go();
  }
}