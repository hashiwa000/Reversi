package jp.hashiwa.reversi.frame;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;

public class RFrame extends JFrame {

  public static final String DEFAULT_TITLE = "Reversi";
  public static final int DEFAULT_CELL_NUM = 10;
  public static final int DEFAULT_CELL_LENGTH = 500;
  private static final String CELL_NUM_SYSPROP = "jp.hashiwa.reversi.cellnum";

  private static int cellNum;

  static {
    String cellNumStr = System.getProperty(CELL_NUM_SYSPROP, Integer.toString(DEFAULT_CELL_NUM));
    try {
      cellNum = Integer.parseInt(cellNumStr);
    } catch(NumberFormatException e) {
      throw new Error(CELL_NUM_SYSPROP + " is need to be integer.", e);
    }
  }

  public static int getCellNum() {
    return cellNum;
  }

  private RBoard board;
  private RConsole console;

  public RFrame() throws Exception {
    this(DEFAULT_TITLE, DEFAULT_CELL_LENGTH, cellNum, true);
  }
  public RFrame(boolean visible) throws Exception {
    this(DEFAULT_TITLE, DEFAULT_CELL_LENGTH, cellNum, visible);
  }
  public RFrame(String name, int frameLength, int cellNum, boolean visible) throws Exception {
    super(name);

    if (frameLength % 2 != 0) throw new Exception("frameLength must be even number");

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(frameLength, frameLength+100);
    setVisible(visible);

    init(frameLength, cellNum);
  }

  private void init(int frameLength, int cellNum) {
    Container c = getContentPane();
    c.setLayout(new BorderLayout());

    board = new RBoard(frameLength/cellNum-5, cellNum);
    c.add(board, BorderLayout.CENTER);

    console = new RConsole(frameLength);
    c.add(console, BorderLayout.SOUTH);

    console.appendTextLine("start");
  }

  public RBoard getBoard() {
    return board;
  }

  public void appendToConsole(String line) {
    if (isVisible()) console.appendTextLine(line);
  }
}
