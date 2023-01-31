package PrAndCon;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @AUTHOR: dhg
 * @DATE: 2023/1/31 10:42
 * @DESCRIPTION:
 */

public class BlockingQueue {
    public static void main(String[] args) throws Exception {
        //指定阻塞队列容量为 100
        LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
//        //put 带有阻塞功能， offer不带有阻塞功能
//        queue.put(10);
//        //take带有阻塞功能,poll不带有阻塞功能
//        queue.take();
        //生产者线程
        Thread producer = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10000; i++) {
                    try {
//                        Thread.sleep(1000);
                        queue.put(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("生产了 "+i);
                }
            }
        });
        producer.start();
        //消费者线程
        Thread consumer = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
//                        Thread.sleep(1000);
                        Integer value = queue.take();
                        System.out.println("消费了 "+value);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        consumer.start();

        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
