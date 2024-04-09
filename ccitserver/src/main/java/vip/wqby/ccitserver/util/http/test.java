package vip.wqby.ccitserver.util.http;

    class MyThread extends Thread{
        public int x = 0;
        public void run(){
            //**************found*****************
            System.out.println(++x);
        }
    }

    //**************found*****************
    class R implements Runnable{
        private Object object = new Object();
        private int x = 0;
        //**************found*****************
        public void run(){
            synchronized (object){
                System.out.println(++x);
            }

        }
    }

    public class test {
        public static void main(String[] args) throws Exception{

            for(int i=0;i<5;i++){
                Thread t = new MyThread();
                t.start();
            }

            Thread.sleep(1000);
            R r = new R();
            //**************found*****************
            for(int i=0;i< 5;i++){
                Thread t = new Thread(r);
                //**************found*****************
                t.start();
            }
        }
    }


