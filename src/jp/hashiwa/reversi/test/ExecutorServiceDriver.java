package jp.hashiwa.reversi.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceDriver {
  public static void main(String[] args) {
    ExecutorService pool = Executors.newSingleThreadExecutor();

    System.out.println("execute start");
    for (int i=0 ; i<10 ; i++) {
      final int cnt = i;
//      final CountDownLatch start

      pool.execute(new Runnable() {

        @Override
        public void run() {
          System.out.println("run: " + cnt);
        }
      });

    }

    System.out.println("execute end");

  }
}
