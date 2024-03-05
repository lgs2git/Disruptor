package com.gsl.tech.completaFuture;


import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Test {

    /**
     * @title runAsync,supplyAsync,exceptionally
     * @description runAsync无返回值，supplyAsync有返回值,exceptionally捕获异常
     * TODO get()方法需要手动抛出异常，join()是uncheck异常
     */
    public static void test1()  {
        System.out.println(Thread.currentThread().getName()+"--> 开始");
        CompletableFuture<Void> runAsync = CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread().getName()+ "--> runAsync 线程");
        });

        CompletableFuture<String> supplyAsync = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName()+"--> supplyAsync线程");
//            int a=1/0;
            return Thread.currentThread().getName()+"-->supplyAsync 线程结果";
        }).exceptionally((e)->{
            e.printStackTrace();
            return "抛出异常";
        });
        System.out.println(Thread.currentThread().getName()+"--> ces-----------03");
        System.out.println(Thread.currentThread().getName()+"-->"+supplyAsync.join());
        System.out.println(Thread.currentThread().getName()+" --> ces-----------04");
    }

    /**
     * thenApply和thenApplyAsync都会接收一个参数（上个链式函数的返回值）且有返回值
     * todo 加了Async是重新开启一个线程（线程池中任意ForkJoinPool.commonPool）
     * thenCompose会接收一个参数（上个链式函数的返回值）且有返回值，返回值CompletableFuture类型开启一个线程且有返回值
     */
    public static void test2(){
        ForkJoinPool pool = new ForkJoinPool();
        CompletableFuture<?> supplyAsync = CompletableFuture.supplyAsync(() -> {
            return "supplyAsync";
        },pool).thenApply((r) -> {//thenApply
            return r+" -- thenApply";
        }).thenCompose((c)->{//thenCompose
            return CompletableFuture.supplyAsync(()->{
                return c+"-- thenCompose";
            });
        });
        System.out.println(supplyAsync.join());
    }

    /**
     * thenApplyAsync
     * thenAccept：接收参数且无返回值
     * thenRun：不接收参数且无返回值
     */
    public static void test3(){
        System.out.println(Thread.currentThread().getName()+"--> 开始");
        CompletableFuture<?> supplyAsync = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName()+"-->supplyAsync 线程结果");
            return "supplyAsync";
        }).thenApplyAsync((r) -> {//接收参数且有返回值
            System.out.println(Thread.currentThread().getName()+"-->thenApplyAsync 结果");
            return r+" -- thenApplyAsync";
        }).thenAccept((t)->{//接收参数且无返回值
            System.out.println("thenAccept输出参数："+t);
            System.out.println(Thread.currentThread().getName()+"--> thenAccept 结果");
        }).thenRun(()->{//不接收参数且无返回值
            System.out.println(Thread.currentThread().getName()+"--> thenRun 结果");
        });
        System.out.println(Thread.currentThread().getName()+"--> 结束");
        System.out.println(supplyAsync.join());
    }

    /**
     * whenComplete和handle
     * TODO 都是当上个线程执行完返回两个参数，值和异常对象 ，whenComplete无返回，handle有返回
     */
    public static void test4(){
        CompletableFuture<?> supplyAsync1 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName()+"-->supplyAsync1 线程结果");
            return "supplyAsync";
        }).whenComplete((r,e)->{
            System.out.println("whenComplete-->"+r);
            System.out.println("whenComplete-->"+ (e!=null?e.getMessage():"无异常"));
        });

        CompletableFuture<?> supplyAsync2 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName()+"-->supplyAsync2 线程结果");
            return "supplyAsync";
        }).handle((r,e)->{
            System.out.println("handle-->"+r);
            System.out.println("handle-->"+ (e!=null?e.getMessage():"无异常"));
            return "handle";
        });
        System.out.println(supplyAsync1.join());
        System.out.println(supplyAsync2.join());
    }

    /**
     * thenCombine,thenAcceptBoth,runAfterBoth
     * TODO 都是组合结果，当所有执行完后在一起返回
     */
    public static void test5(){
        CompletableFuture<?> supplyAsync1 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName()+"-->supplyAsync1 线程结果");
            return "supplyAsync1";
        });
        CompletableFuture<?> supplyAsync2 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName()+"-->supplyAsync1 线程结果");
            return "supplyAsync2";
        });

        //supplyAsync1和supplyAsync2异步执行完成后将二者结果传入一起执行，有参有返回值
        CompletableFuture<Object> thenCombine = supplyAsync1.thenCombine(supplyAsync2, (a, b) -> {
            System.out.println(Thread.currentThread().getName()+"-->"+a);
            System.out.println(Thread.currentThread().getName()+"-->"+b);
            return "thenCombine";
        });
        //supplyAsync1和supplyAsync2异步执行完成后将二者结果传入一起执行，有参无返回值
        CompletableFuture<Void> thenAcceptBoth = supplyAsync1.thenAcceptBoth(supplyAsync2, (a, b) -> {
            System.out.println(Thread.currentThread().getName()+"-->"+a);
            System.out.println(Thread.currentThread().getName()+"-->"+b);
        });
        //supplyAsync1和supplyAsync2异步执行完成后将二者结果传入一起执行，无参无返回值
        CompletableFuture<Void> runAfterBoth = supplyAsync1.runAfterBoth(supplyAsync2,() -> {
            System.out.println(Thread.currentThread().getName()+"--> runAfterBoth");
        });

        System.out.println(Thread.currentThread().getName()+"-->"+thenCombine.join());
    }

    /**
     * applyToEither,acceptEither,runAfterEither
     * TODO 都是当有任意一个完成后立刻执行
     */
    public static void test6() throws InterruptedException {
        CompletableFuture<String> supplyAsync1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+"-->supplyAsync1 线程结果");
            return "supplyAsync1";
        });
        CompletableFuture<String> supplyAsync2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+"-->supplyAsync2 线程结果");
            return "supplyAsync2";
        });

        //supplyAsync1和supplyAsync2执行完任意一个后执行，有参有返回值
        CompletableFuture<String> applyToEither = supplyAsync1.applyToEither(supplyAsync2,a->{
            System.out.println(Thread.currentThread().getName()+"-applyToEither->"+a);
            return a;
        });

        //supplyAsync1和supplyAsync2执行完任意一个后执行，有参无返回值
        CompletableFuture<Void> thenAcceptBoth = supplyAsync1.acceptEither(supplyAsync2, (a) -> {
            System.out.println(Thread.currentThread().getName()+"-thenAcceptBoth->"+a);
        });

        //supplyAsync1和supplyAsync2执行完任意一个后执行，无参无返回值
        CompletableFuture<Void> runAfterBoth = supplyAsync1.runAfterEither(supplyAsync2,() -> {
            System.out.println(Thread.currentThread().getName()+"--> runAfterEither");
        });

        Thread.sleep(5000);
//        System.out.println(applyToEither.join());
    }


    /**
     * allOf返回的CompletableFuture是多个任务都执行完成后才会执行，只有有一个任务执行异常，
     *  则返回的CompletableFuture执行get方法时会抛出异常，如果都是正常执行，则get返回null。
     *  anyOf有一个执行完就执行
     */
    public static void test7() throws InterruptedException {
        CompletableFuture<String> supplyAsync1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+"-->supplyAsync1 线程结果");
            return "supplyAsync1";
        });
        CompletableFuture<String> supplyAsync2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+"-->supplyAsync2 线程结果");
            return "supplyAsync2";
        });
        CompletableFuture.allOf(supplyAsync1, supplyAsync2).thenApply((a)->{
            return Stream.of(supplyAsync1,supplyAsync2).map(CompletableFuture::join).collect(Collectors.joining(","));
        }).thenAccept((b)->{
            System.out.println(b);
        });

        System.out.println(CompletableFuture.anyOf(supplyAsync1, supplyAsync2).join());
        Thread.sleep(3000);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        test2();
    }
}
