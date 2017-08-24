# Android中的线程

> **Android 中线程的使用**



## 线程

*Android官网文档->https://developer.android.com/guide/components/processes-and-threads.html?hl=zh-cn#Threads*

> 应用启动时，系统会为应用创建一个线程，称为主线程；它负责UI的绘制以及UI的事件响应交互，也称为UI线程；
系统不会为每个组件实例创建单独的线程，同一进程中的所有组件都在主线程实例化，并且每个组件的资源调用都由主线程分配，因此响应系统回调都在主线程进行。
因为主线程要处理UI的绘制及事件的交互，所以主线程中不能进行耗时的操作（网络访问，数据库操作），一旦主线程进行耗时操作就会出现阻塞，UI事件就没办法响应了，就会出现ANR，这是非常不友好的。

Android UI是非线程安全的，所以关于UI的操作只能在UI线程操作，所以Android单线程模式必须遵守两条规则

1. 不能阻塞UI线程
2. UI操作要在UI线程，不要在 UI 线程之外访问 Android UI 工具包

### 工作线程

> 为了保证应用的顺畅，所有耗时的操作都在工作线程中进行。

遵循上述的两条规则，不能再UI线程之外的线程访问UI，但是网络访问结果是在工作线程，要将结果填充到UI中怎么办呢，Android提供了几种方法在工作线程中访问UI

- Activity.runOnUiThread(Runnable)
- View.post(Runnable)
- View.postDelayed(Runnable, long)
- Handler

*还有一种方式 AsyncTask;*

### 线程相关API

 - Runnable
 - Thread
 - Callable<V>
 - Future<?>
 - RunnableFuture<V>
 - FutureTask<V>
 - Executor
 - ExecutorService
 - ThreadPoolExecutor
 - Executors
 
## 创建线程
 
 > 有两种方式创建一个线程。
 
 1. 继承 Thread类
 2. 实现 Runnable接口
 
 > 继承Thread类 重写 run方法，在调用start()后JVM会自动调用run()方法

```
    /**
     * 通过继承Thread 创建一个Thread
     */
    class MyThread extends Thread {

        public MyThread(String name) {
            super(name);
        }

        /**
         * 重写run方法 JVM会自动调用此方法
         */
        @Override
        public void run() {
           // logic code
        }

        /**
         * 重载（Overload）run()方法 和普通的方法一样，并不会在该线程的start()方法被调用后被JVM自动运行
         *
         * @param str
         */
        public void run(String str) {
            Log.e("run", str + "");
        }
    }


```

> 实现 Runnable接口 

```

    /**
     * 通过实现 Runnable 创建一个线程
     */
    class MyRunnable implements Runnable {

        /**
         * JVM会自动调用此方法
         */
        @Override
        public void run() {
           //logic code
        }
    }

```

### 启动线程

> 继承Thread方式

```
 MyThread thread0 = new MyThread();
  thread0.start();
```

> 实现 Runnable方式

```
 MyRunnable myRunnable = new MyRunnable();
 Thread thread1 = new Thread(myRunnable);
 thread1.start();
```

### Runnable和Thread两种方式的区别和联系


> 其实这块主要是围绕着接口和抽象类的区别以及一些设计原则而言的。

1. Java是单继承的，你继承了Thread类再也无法继承别的类。
2. Java虽然是单继承但是可以实现多接口的，即使你实现Runnable，你也可以实现别的接口。
3. 在面向对象编程中，继承一个类就意味着要使用或者改善某些功能，如果不准备改善Thread提供的封装好的功能，使用Runnable更好。
4. Runnable接口表示一个任务，可以被任意一个普通线程，线程池或者其他方式执行。逻辑上的分离，Runnable比Thread更好。
5. 将任务分离为Runnable，就可以重用或者通过其他方式执行它，Thread一旦完成就无法重新启动了。
6. 线程池的接口接收的参数为Runnable
7. 用Runnable能够代表一个线程就不必继承Thread，那样就额外的继承了Thread的全部方法
8. 继承Thread，你就必须为每个线程都创建一个实例，然后为每个实例分配内存。
9. 写一个接口而不是实现，会让程序更容易扩展。

*Thread类也是实现了Runnable接口*

> 参考资料

- https://www.linkedin.com/pulse/20140917120728-90925576-difference-between-implements-runnable-and-extends-thread-in-java
- https://my.oschina.net/leejun2005/blog/483999
- 这个重点看评论->http://mars914.iteye.com/blog/1508429

## 资源共享及同步问题

> 关于多线程资源共享，多线程并发操作有随机性，不能保证每个线程都顺序的去访问某个资源，在多个线程同时去访问一个资源的时候要进行资源的同步.



经典的卖票例子

### 资源共享
 
 > 资源共享，多个线程并发执行访问同一个资源，才是共享的资源。

*票*

```
    /**
     * 票
     */
    class Tickets {
        int num = 5;

        public int get() {
            return num;
        }

        public void set(int num) {
            this.num = num;
        }
    }
```

*卖票程序*

```

    /**
     * 卖票 程序
     */
    class SaleRunnable implements Runnable {

        private Tickets tickets;

        public SaleRunnable(Tickets tickets) {
            this.tickets = tickets;
        }

        @Override
        public void run() {
            while (tickets.get() > 0) {
                Log.e(Thread.currentThread().getName(), "销售第" + tickets.get() + "张，剩余" + (tickets.get() - 1) + "张");
                tickets.set(tickets.get() - 1);
                try {
                    //延迟 1000
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Log.e(Thread.currentThread().getName(), "没票了");

        }
    }

```

*卖票*

```
                //票资源
                Tickets tickets = new Tickets();
                SaleRunnable saleRunnable = new SaleRunnable(tickets);
                //开三个窗口使用同一程序去卖票 
                new Thread(saleRunnable, "窗口零").start();
                new Thread(saleRunnable, "窗口一").start();
                new Thread(saleRunnable, "窗口二").start();
```

*卖票结果*

多线程并发执行的结果就是 多个窗口同时售票，同一时间销售掉了同一个票，尴尬了，一个票卖了多次

```
08-22 14:23:41.066 E/窗口零: 销售第5张，剩余4张
08-22 14:23:41.066 E/窗口一: 销售第4张，剩余3张
08-22 14:23:41.076 E/窗口二: 销售第3张，剩余2张
08-22 14:23:42.066 E/窗口零: 销售第2张，剩余1张
08-22 14:23:42.076 E/窗口二: 销售第2张，剩余1张
08-22 14:23:42.076 E/窗口一: 销售第2张，剩余1张
08-22 14:23:43.076 E/窗口一: 没票了
08-22 14:23:43.076 E/窗口二: 没票了
08-22 14:23:43.076 E/窗口零: 没票了

08-22 14:24:29.866 E/窗口零: 销售第5张，剩余4张
08-22 14:24:29.866 E/窗口一: 销售第4张，剩余3张
08-22 14:24:29.866 E/窗口二: 销售第3张，剩余2张
08-22 14:24:30.866 E/窗口零: 销售第2张，剩余1张
08-22 14:24:30.866 E/窗口二: 销售第1张，剩余0张
08-22 14:24:30.866 E/窗口一: 销售第2张，剩余1张
08-22 14:24:31.866 E/窗口零: 没票了
08-22 14:24:31.866 E/窗口二: 没票了
08-22 14:24:31.866 E/窗口一: 没票了

08-22 14:24:51.906 E/窗口二: 销售第5张，剩余4张
08-22 14:24:51.906 E/窗口一: 销售第4张，剩余3张
08-22 14:24:51.906 E/窗口零: 销售第3张，剩余2张
08-22 14:24:52.906 E/窗口二: 销售第2张，剩余1张
08-22 14:24:52.906 E/窗口一: 销售第2张，剩余1张
08-22 14:24:52.916 E/窗口零: 没票了
08-22 14:24:53.916 E/窗口二: 没票了
08-22 14:24:53.916 E/窗口一: 没票了
```

### 资源同步

> Java 同步块（synchronized block）用来标记方法或者代码块是同步的

*java中每个对象都对应于一个称为“互斥锁”的标志，这个标志用来保证在任何时刻，只能有一个线程访问该对象。
如果系统中的资源当前没有被使用，线程可以得到“互斥锁”，即线程可以得到资源的使用权。
当线程执行完毕后，他放弃“互斥锁”，如果一个线程获得“互斥锁”时，其余的线程就必须等待当前线程结束并放弃“互斥锁”。
在java中，提供了关键字synchronized来实现对象的“互斥锁”关系。
**当某个对象或方法用关键字synchronized修饰时，表明该对象或方法在任何一个时刻只能有一个线程访问。**
如果synchronized用在类的声明中，表明该类中的所有方法都是synchronized的。*

在这个例子中，我们只需要将“票”这个资源同步即可

多个线程都是访问的这一个实例，所以同步这个实例方法，就可以了；

```
    /**
     * 卖票 程序
     */
    class SaleRunnable implements Runnable {

        private Tickets tickets;

        public SaleRunnable(Tickets tickets) {
            this.tickets = tickets;
        }

        @Override
        public void run() {
            while (true) {
                if (sale()) {
                    break;
                }
                try {
                    //延迟 1000
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Log.e(Thread.currentThread().getName(), "没票了");

        }

        private synchronized boolean sale() {
            if (tickets.get() > 0) {
                Log.e(Thread.currentThread().getName(), "销售第" + tickets.get() + "张，剩余" + (tickets.get() - 1) + "张");
                tickets.set(tickets.get() - 1);
                return false;
            }
            return true;

        }
    }

```

或者

```
    /**
     * 卖票窗口
     */
    class SaleThread extends Thread {

        private Tickets tickets;

        public SaleThread(Tickets ti, String name) {
            super(name);
            tickets = ti;
        }

        @Override
        public void run() {
            while (true) {
                //同步这个资源
                synchronized (tickets) {
                    if (tickets.get() > 0) {
                        Log.e(Thread.currentThread().getName(), "销售第" + tickets.get() + "张，剩余" + (tickets.get() - 1) + "张");
                        tickets.set(tickets.get() - 1);
                    } else {
                        break;
                    }
                }
                try {
                    //延迟 1000
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Log.e(Thread.currentThread().getName(), "没票了");
        }
    }

```

> 学习资料

- http://ifeve.com/java-concurrency-thread-directory/
- http://ifeve.com/synchronized-blocks/
- http://student-lp.iteye.com/blog/2083170
- http://www.cnblogs.com/dennisit/archive/2013/02/24/2925288.html
- http://lanvis.blog.163.com/blog/static/26982162009798422547/

## 线程状态

> 1. 新生状态（new）

当一个线程的实例被创建即使用`new`关键字后台Thread类或者其子类创建一个线程对象后，此时该线程就处于新生状态，处于新生状态的线程有自己的内存空间，但该线程并没有运行，此时线程还不是活着的（not alive）

> 2. 就绪状态（Runnable）

通过调用线程实例的`start()`方法来启动线程使线程进入就绪状态(runnable);处于就绪状态的线程已经具备了运行条件，但还没被分配到CPU就是不一定会被立即执行，此时处于线程就绪队列，等待线程为期分配CPU，等待状态不是执行状态；此时线程是活着的（alive）；

> 3. 运行状态（Running）

一旦获取CPU（被JVM选中），线程就进入运行（running）状态，线程`run()`方法才开始被执行；在运行状态的线程执行自己的run()方法中的操作，知道调用其他的方法而终止、或者等待某种资源而阻塞、或者完成任务而死亡；如果在给定的时间片内没有执行结束，就会被系统给换下来回到线程的就绪状态；此时线程是活着的（alive）；

> 4. 阻塞状态（Blocked）

通过调用`join()`,`sleep()`,`wait()`或者资源被占用使线程处于阻塞(blocked)状态；处于Blocked状态的线程仍然是活着的（alive）；

> 5. 死亡状态（Dead）

当一个线程的`run()`方法运行完毕或被中断或被异常退出，该线程到达死亡(dead)状态。此时可能仍然存在一个该Thread的实例对象，但该Thread已经不可能在被作为一个可被独立执行的线程对待了，线程的独立的call stack已经被dissolved。一旦某一线程进入Dead状态，他就再也不能进入一个独立线程的生命周期了。对于一个处于Dead状态的线程调用start()方法，会出现一个运行期(runtime exception)的异常；处于Dead状态的线程不是活着的（not alive）。


> 学习资料

- http://www.cnblogs.com/DreamSea/archive/2012/01/11/JavaThread.html
- http://www.cnblogs.com/whoislcj/p/5603277.html
- http://student-lp.iteye.com/blog/2083170

## 线程通信

> Java中常规的通信方式这里我就不说了，看一下Android的消息机制

Java常规的通信方式传送门->http://ifeve.com/thread-signaling

*Android中的消息机制可以用于线程间通信也可用于在各个组件间通信,这里只总结一下怎么在线程间使用*

消息机制中重要的API

- Message 线程间通信就是在传递消息，Message就是消息的载体。常用的有四个字段：arg1，arg2，what，obj。obj可以携带Object对象，其余三个可以携带整形数据
- MessageQueue 消息队列，它主要用于存放所有通过Handler发送的消息（也就是一个个Message），这部分的消息会一直存在于消息队列中，等待被处理。每个线程中只会有一个MessageQueue对象。
- Looper 每个线程通过Handler发送的消息都保存在，MessageQueue中，Looper通过调用loop()的方法，就会进入到一个无限循环当中，然后每当发现MessageQueue中存在一条消息，就会将它取出，并传递到Handler的handleMessage()方法中。
- Handler 发送消息，处理消息
- Thread 线程 每个线程中只会有一个Looper对象。

> 运行机制

在哪个Thread中创建Handler，默认情况下Handler就会获取哪个线程中的Looper（前提是Looper创建好了）；handler发送消息就是将消息发送到了自己持有的这个Looper对象里；
Looper内有一个MessageQueue，消息就存放在队列里，一旦Looper的`loop()`方法被调用就会开启无限循环模式，一直循环遍历这个队列，从中取Handler发送的消息，没有消息就阻塞；一旦有消息就唤醒线程取出来；
从MessageQueue中取出的消息，会调用本身target持有的handler实例来处理这个消息;

**综上所述，线程间通信handler就可以实现；**

> 主线程给工作线程发消息

想要在主线程给工作线程发消息，我们就得持有在工作线程中创建的handler；
而创建handler之前必须先初始化一下Looper对象；
handler创建完之后就开启Looper的无限循环来等待消息

**创建一个线程并创建一个handler**

```
 Handler handlerA = null;
 
    class ThreadA extends Thread implements Handler.Callback {

        public ThreadA() {
            super("ThreadA");
        }

        @Override
        public void run() {
            //创建此线程的Looper对象 ，一个线程只能有一个，所以此方法只能调用一次
            Looper.prepare();
            handlerA = new Handler(this);
            //开始循环遍历
            Looper.loop();
        }

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    Log.e("handleMessage", Thread.currentThread().getName() + ";src->" + msg.obj);
                    break;
            }
            return false;
        }
    }

```

**使用handlerA发送消息**

```
                //创建一个消息
                Message msg = new Message();
                msg.what = 100; //标识你这消息想要干啥
                msg.obj = Thread.currentThread().getName();

                /**
                 * 因为handler 里持有所在线程的Looper，所有handler发送的消息会在所在线程中执行
                 */
                //发送给A线程
                handlerA.sendMessage(msg);
```

**看一个log** 处理线程是ThreadA，消息来源是Main线程
```
08-23 16:26:02.609 E/handleMessage: ThreadA;src->main
```

> 工作线程发给主线程

与上面的同理，想要给主线程发送消息，拿到主线程的handler就可以了；

因为点击事件是在UI线程中响应的，所以想让工作线程给主线程发送一个消息就麻烦一点，我这里为了测试做了个中转，先给B线程发送一个信号，B接到这个信号就给主线程发消息

```

    class ThreadB extends Thread implements Handler.Callback {

        public ThreadB() {
            super("ThreadB");
        }

        @Override
        public void run() {
            Looper.prepare();
            handlerB = new Handler(this);
            Looper.loop();

        }

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    Log.e("handleMessage", Thread.currentThread().getName() + ";src->" + msg.obj);
                    break;
                case 101:   //给main发送一个消息
                    Message message = new Message();
                    message.what = 100;
                    message.obj = Thread.currentThread().getName();
                    handler.sendMessage(message);
                    break;
            }
            return false;
        }
    }

```

**在主线程创建的handler **
 
```
    /**
     * Main 的handler
     * 程序启动的时候就为Main线程创建了Looper
     */
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Log.e("handleMessage", Thread.currentThread().getName() + ";src->" + msg.obj);
            return false;
        }
    });

```

*主线程的handler创建时没有提前创建Looper也没有调用Looper的`loop()`方法，是因为程序在启动的时候已经为主线程创建好了Looper，并且调用了`loop()`,一直在等待消息*

> 工作线程给工作线程发消息

跟上面两个一样，想给哪个线程发消息就要先拿到哪个线程的handler；我这里就不贴代码了；


> 学习资料

- http://www.jianshu.com/p/02962454adf7
- http://www.jianshu.com/p/7657f541c461
- http://www.cnblogs.com/younghao/p/5116819.html
- http://www.voidcn.com/article/p-pmejydob-bbs.html

## 线程池

> 为啥使用线程池

- 减少频繁创建销毁线程带来的开销
- 复用创建好的线程
- 对线程进行简单的管理

**`ExecutorService`是一个接口，定义了线程池的方法。**

```
public interface ExecutorService extends Executor {

    void shutdown();//关闭线程池，不再接受新任务，当所有任务都执行完毕后，关闭线程池

    List<Runnable> shutdownNow(); //关闭线程池，阻止等待任务启动并试图停止当前正在执行的任务，停止接收新的任务，返回处于等待的任务列表

    boolean isShutdown(); //是否关闭

    boolean isTerminated();//如果关闭后所有任务都已完成，则返回 true。注意，除非首先调用 shutdown 或 shutdownNow，否则 isTerminated 永不为 true。

    boolean awaitTermination(long var1, TimeUnit var3) throws InterruptedException;//等待（阻塞）直到关闭或最长等待时间或发生中断,timeout - 最长等待时间 ,unit - timeout 参数的时间单位  如果此执行程序终止，则返回 true；如果终止前超时期满，则返回 false 

    <T> Future<T> submit(Callable<T> var1); //提交一个返回值的任务用于执行，返回一个表示任务的未决结果的 Future。该 Future 的 get 方法在成功完成时将会返回该任务的结果。

    <T> Future<T> submit(Runnable var1, T var2); // 提交一个 Runnable 任务用于执行，并返回一个表示该任务的 Future。该 Future 的 get 方法在成功完成时将会返回给定的结果

    Future<?> submit(Runnable var1);///提交一个 Runnable 任务用于执行，并返回一个表示该任务的 Future。该 Future 的 get 方法在成功 完成时将会返回 null

    <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> var1) throws InterruptedException; //执行给定的任务，当所有任务完成时，返回保持任务状态和结果的 Future 列表。返回列表的所有元素的 Future.isDone() 为 true。

    <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> var1, long var2, TimeUnit var4) throws InterruptedException; //执行给定的任务，当所有任务完成时，返回保持任务状态和结果的 Future 列表。返回列表的所有元素的 Future.isDone() 为 true。

    <T> T invokeAny(Collection<? extends Callable<T>> var1) throws InterruptedException, ExecutionException; //执行给定的任务，如果在给定的超时期满前某个任务已成功完成（也就是未抛出异常），则返回其结果。一旦正常或异常返回后，则取消尚未完成的任务。

    <T> T invokeAny(Collection<? extends Callable<T>> var1, long var2, TimeUnit var4) throws InterruptedException, ExecutionException, TimeoutException;
}
```

> `ThreadPoolExecutor`具体实现了`ExecutorService`接口，提供了一系列的参数来配置线程池，熟悉`ThreadPoolExecutor`可自定义线程池。 

```
    public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              ThreadFactory threadFactory,
                              RejectedExecutionHandler handler) {
```

- corePoolSize 核心线程池数量
- maximumPoolSize 最大线程数量
- keepAliveTime  非核心线程闲置的超时时长，超过这个时长，非核心线程就会被回收,当allowCoreThreadTimeOut为true时，keepAliveTime同样作用于核心线程。
- unit 时间单位，常用的为 TimeUnit.MILLISECONDS(毫秒)、TimeUnit.SECONDS（秒）、TimeUnit.MINUTES(分钟)，详情-》https://docs.oracle.com/javase/6/docs/api/java/util/concurrent/TimeUnit.html
- workQueue 线程池中的任务队列，通过execute方法提交的Runnable对象会存储在这个参数中
- threadFactory  线程工厂，为线程池提供创建线程的功能，是个接口，提供Thread newThread(Runnable r)方法；通常用默认的`Executors.defaultThreadFactory()`即可
- handler 拒绝策略，当线程池无法执行新任务时，可能由于线程队列已满或无法成功执行任务，这时候 ThreadPoolExecutor会调用handler的 rejectedExecution的方法，默认会抛出RejectedExecutionException

Android通过`Executors`为我们提供了几种线程池

|方法|说明|
|:---|:---|
|Executors.newCachedThreadPool()|缓存线程池，线程数量不定，最大线程数为Integer.MAX_VALUE(相当于任意大),有新任务时会检查是否有空闲线程，没有则会创建线程，空闲线程超过60s会被回收，任何任务都会被立即执行，适合大量的耗时较少任务|
|Executors.newFixedThreadPool(int nThreads)|固定型线程池，线程数量固定，只有核心线程并且无超时机制，当所有线程都执行任务时，新任务进入队列等待。能够快速响应请求|
|Executors.newScheduledThreadPool(int corePoolSize) |调度型线程池，核心数量固定，非核心数量无限制，非核心线程一旦空闲立马回收。会根据Scheduled(任务列表)进行延迟执行，或者是进行周期性的执行.适用于一些周期性的工作|
|EExecutors.newSingleThreadExecutor() |单例线程池，只有一个核心线程，所有任务都在这个线程中串行执行，不需要处理线程同步问题，在任意的时间段内，线程池中只有一个线程在工作...|

在`ExecutorService`的方法中可以看到线程池除了可执行`Runnable`接口还可以执行`Callable<V>` 接口,并且可以通过`Future<V>`来感知线程状态和结果。

因为`Runnable `的返回值为`void` 无法获取执行完毕后的结果 ，所以才有了`Callable<V>`，可以返回一个结果
```
public interface Callable<V> {
    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    V call() throws Exception;
}
```

`Future<V>` 定义了几种方法来感知线程状态和获取结果 ，可以理解为管理线程的。Future提供了三种功能：

1. 判断任务是否完成；
2. 能够中断任务；
3. 能够获取任务执行结果。

```
public interface Future<V> {

    /**
     * 尝试取消执行此任务。 如果任务已经完成，已经被取消或由于某种其他原因而无法取消，则此尝试将失败。返回false。
     * 当此方法调用时此任务尚未开始，则此任务不会被运行。返回true。
     * 如果任务已经开始，则{mayInterruptIfRunning}参数确定执行此任务的线程是否应该中断，以试图停止该任务。
     * 此方法返回后，对{@link #isDone}的后续调用将始终返回{@code true}。 
     * 如果此方法返回{@code true}，则后续调用{@link #isCancelled}将始终返回{@code true}。
     * @param mayInterruptIfRunning 线程在运行是否中断; 如果值为true表示中断正在进行的任务返回则返回true，值为false表示不中断返回false，如果任务无法取消，通常是因为它已经正常完成;
     * {@code true} otherwise
     */
    boolean cancel(boolean mayInterruptIfRunning);

    /**
     * 如果在完成之前被取消 则返回 true，否则返回false
     *
     * @return {@code true} if this task was cancelled before it completed
     */
    boolean isCancelled();

    /**
     * 任务已经完成 返回 true。
     * 任务被取消，异常，或者正常完成都会返回 true。此方法返回结果表示任务是否允许完毕
     * @return {@code true} if this task completed
     */
    boolean isDone();

    /**
     * 获取任务完成的结果，如果任务没有执行完毕，则阻塞线程，直到拿到结果
     * @return the computed result
     * @throws CancellationException if the computation was cancelled
     * @throws ExecutionException if the computation threw an
     * exception
     * @throws InterruptedException if the current thread was interrupted
     * while waiting
     */
    V get() throws InterruptedException, ExecutionException;

    /**
     * 用来获取执行结果，如果在指定时间内，还没获取到结果，就直接返回null
     * @param timeout 指定等待时间
     * @param unit 等待时间单位
     * @return the 执行结果
     * @throws CancellationException if the computation was cancelled
     * @throws ExecutionException if the computation threw an
     * exception
     * @throws InterruptedException if the current thread was interrupted
     * while waiting
     * @throws TimeoutException if the wait timed out
     */
    V get(long timeout, TimeUnit unit)
        throws InterruptedException, ExecutionException, TimeoutException;
}
```

因为`Future`只是一个接口，所以是无法直接用来创建对象使用的，因此就有了下面的`FutureTask`。

可以看出RunnableFuture继承了Runnable接口和Future接口，而FutureTask实现了RunnableFuture接口。
所以它既可以作为Runnable被线程执行，又可以作为Future得到Callable的返回值。
```
public class FutureTask<V> implements RunnableFuture<V> {}
```

```
public interface RunnableFuture<V> extends Runnable, Future<V> {

    void run();
}
```




> 学习资料

- https://tom510230.gitbooks.io/android_ka_fa_yi_shu_tan_suo/content/chapter11.html
- http://www.cnblogs.com/whoislcj/p/5607734.html
- http://blog.csdn.net/u010687392/article/details/49850803
- http://www.cnblogs.com/RGogoing/p/4766971.html
- http://www.cnblogs.com/dolphin0520/p/3949310.html


> Demo https://github.com/sky-mxc/AndroidDemo/tree/master/thread