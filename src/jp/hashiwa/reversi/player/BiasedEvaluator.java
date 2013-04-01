package jp.hashiwa.reversi.player;

import java.text.DecimalFormat;

import jp.hashiwa.reversi.frame.RCell;
import jp.hashiwa.reversi.frame.RFrame;

public class BiasedEvaluator extends DefaultEvaluator {

  // [ weight ]
  //
  // 300 -40  20   5   5   5   5  20 -40 300
  // -40 -80  -1  -1  -1  -1  -1  -1 -80 -40
  //  20  -1   5   1   1   1   1   5  -1  20
  //   5  -1   1   0   0   0   0   1  -1   5
  //   5  -1   1   0   0   0   0   1  -1   5
  //   5  -1   1   0   0   0   0   1  -1   5
  //   5  -1   1   0   0   0   0   1  -1   5
  //  20  -1   5   1   1   1   1   5  -1  20
  // -40 -80  -1  -1  -1  -1  -1  -1 -80 -40
  // 300 -40  20   5   5   5   5  20 -40 300

  // harf
  //
  //   0   0   1  -1   5
  //       0   1  -1   5
  //           5  -1  20
  //             -80 -40
  //                 300

  private static final int[][] WEIGHT_ARRAY;
  static {
    int cellNum = RFrame.getCellNum();
    WEIGHT_ARRAY = new int[cellNum][];
    for (int i=0 ; i<WEIGHT_ARRAY.length ; i++) {
      WEIGHT_ARRAY[i] = new int[cellNum];
    }

    int harfCellNum = cellNum/2;
    for (int i=0 ; i<cellNum ; i++) {
      for (int j=0 ; j<cellNum ; j++) {
        int ti = Math.abs((int)(i - harfCellNum + 0.5))+1;
        int tj = Math.abs((int)(j - harfCellNum + 0.5))+1;

        // swap
        if (ti < tj) {
          int t = ti;
          ti = tj;
          tj = t;
        }

        // tj, ti <= harfCellNum
        // tj <= ti
        if        (tj==harfCellNum) {
          WEIGHT_ARRAY[i][j] = 300;
        } else if (tj==harfCellNum-1) {

          if (ti==harfCellNum) {
            WEIGHT_ARRAY[i][j] =-40;
          } else {
            // ti==harfCellNum-1
            WEIGHT_ARRAY[i][j] =-80;
          }

        } else if (tj==harfCellNum-2) {

          if        (ti==harfCellNum) {
            WEIGHT_ARRAY[i][j] = 20;
          } else if (ti==harfCellNum-1) {
            WEIGHT_ARRAY[i][j] = -1;
          } else {
            // ti==harfCellNum-2
            WEIGHT_ARRAY[i][j] = 5;
          }

        } else {

          if        (ti==harfCellNum) {
            WEIGHT_ARRAY[i][j] = 5;
          } else if (ti==harfCellNum-1) {
            WEIGHT_ARRAY[i][j] = -1;
          } else if (ti==harfCellNum-2) {
            WEIGHT_ARRAY[i][j] = 1;
          } else {
            // ti <= harfCellNum-3
            WEIGHT_ARRAY[i][j] = 0;
          }

        }

      }
    }

    if (false) {
      StringBuilder sb = new StringBuilder();
      DecimalFormat df = new DecimalFormat("####");
      for (int[] xs: WEIGHT_ARRAY) {
        for (int x: xs) {
          sb.append(df.format(x));
        }
        sb.append('\n');
      }
      System.out.println(sb.toString());
    }
  }

  @Override
  protected double weight(RCell[][] cells, int x, int y) {
    return WEIGHT_ARRAY[x][y];
  }

}
