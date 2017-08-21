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

#### 区别

> 其实这块主要是围绕着接口和抽象类的区别以及一些设计原则而言的。

1. 继承方式
    Java是单继承的，你继承了Thread类再也无法继承别的类，扩展性不太好。
2. 可扩展性
    Java虽然是单继承但是可以实现多接口的，即使你实现Runnable，你也可以实现别的接口，
3. 面向对象设计
    应该优先实现Runnable接口，
4. 低耦合
5. 功能开销