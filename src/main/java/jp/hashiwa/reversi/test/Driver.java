package jp.hashiwa.reversi.test;

import java.util.concurrent.CountDownLatch;

public class Driver {
  static final int N = 10;
  public static void main(String[] args) throws InterruptedException {
    CountDownLatch startSignal = new CountDownLatch(2);
    CountDownLatch doneSignal = new CountDownLatch(N);

    for (int i = 0; i < N; ++i) // create and start threads
      new Thread(new Worker(startSignal, doneSignal)).start();

    doSomethingElse();            // don't let run yet
    startSignal.countDown();      // let all threads proceed
    doSomethingElse();
    doneSignal.await();           // wait for all to finish

    System.out.println("main finish.");
  }

  static void doSomethingElse() {
    System.out.println("doSomethingElse.");
  }
}

class Worker implements Runnable {
  private final CountDownLatch startSignal;
  private final CountDownLatch doneSignal;
  Worker(CountDownLatch startSignal, CountDownLatch doneSignal) {
     this.startSignal = startSignal;
     this.doneSignal = doneSignal;
  }
  public void run() {
     try {
       startSignal.await();
       doWork();
       doneSignal.countDown();
     } catch (InterruptedException ex) {} // return;
  }

  void doWork() {
    System.out.println("doWork.");
  }
}
