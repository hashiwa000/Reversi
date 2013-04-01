package jp.hashiwa.reversi.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Driver2 {
  static final int N = 10;

  public static void main(String[] args) throws InterruptedException {
    CountDownLatch doneSignal = new CountDownLatch(N);
    ExecutorService pool = Executors.newSingleThreadExecutor();

    for (int i = 0; i < N; ++i) // create and start threads
      pool.execute(new WorkerRunnable(doneSignal, i));

    doneSignal.await();           // wait for all to finish
    System.out.println("finish");

    pool.shutdown();
  }
}

class WorkerRunnable implements Runnable {
  private final CountDownLatch doneSignal;
  private final int i;
  WorkerRunnable(CountDownLatch doneSignal, int i) {
     this.doneSignal = doneSignal;
     this.i = i;
  }
  public void run() {
     try {
       doWork(i);
       doneSignal.countDown();
     } catch (Exception ex) {} // return;
  }

  void doWork(int i) {
    System.out.println("doWork: " + i);
  }
}