package jp.hashiwa.reversi.frame;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class RConsole extends JScrollPane {
  private static final String lineSeparator = "\n";

  private JTextArea textArea;
  private final StringBuilder sb = new StringBuilder();

  RConsole(int length) {
    super(getPlainComponen());
    textArea = tmpTextArea;

    setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    setPreferredSize(new Dimension(length, 100));

//    setAutoscrolls(true);
  }

  JTextArea getTextArea() {
    return textArea;
  }

  void appendTextLine(String line) {
//    sb.setLength(0);
//    sb.append(textArea.getText());
//    sb.append(lineSeparator);
//    sb.append(line);
//    textArea.setText(sb.toString());

    textArea.append(line + lineSeparator);
    getVerticalScrollBar().setValue(getVerticalScrollBar().getMaximum());
  }

  private static JTextArea tmpTextArea;
  private static Component getPlainComponen() {
    tmpTextArea = new JTextArea();
    tmpTextArea.setWrapStyleWord(true);
    tmpTextArea.setLineWrap(false);

    return tmpTextArea;
  }

}
