package com.company;
import java.util.concurrent.Semaphore;

public class Port implements Runnable{
    private String name;
    private int moorings;
    private int capacity;
    private int load;
    private Semaphore mooringsSem;
    private Semaphore warehouseSem;

    public Port(String m_name, int m_moorings, int m_capacity){
        this.name=m_name;
        this.moorings=m_moorings;
        this.capacity=m_capacity;
        this.mooringsSem=new Semaphore(m_moorings);
        this.warehouseSem= new Semaphore(1);
        this.load=this.capacity/2;
    }

    public void acquireMooringsSem(){
        try {
            mooringsSem.acquire();
        }catch(Exception e){
            System.out.println("Cannot acquire moorings semaphore");
        }
    }

    public void releaseMooringsSem(){
        try {
            mooringsSem.release();
        }catch(Exception e){
            System.out.println("Cannot release moorings semaphore");
        }
    }

    public void acquireWarehouseSem(){
        try {
            warehouseSem.acquire();
        }catch(Exception e){
            System.out.println("Cannot acquire warehouse semaphore");
        }
    }

    public void releaseWarehouseSem(){
        try {
            warehouseSem.release();
        }catch(Exception e){
            System.out.println("Cannot release warehouse semaphore");
        }
    }

    public int getFreeSpace(){
        return capacity-load;
    }

    public int getLoad(){
        return load;
    }

    public void storeCargo(int cargo){
        this.load+=cargo;
    }

    public void removeCargo(int cargo){
        this.load-=cargo;
    }

    public void run(){
        System.out.printf("⚓️The port %s opens its %d moorings to any visiting ship!\n", this.name, this.moorings);
        while(true){
            try {
                warehouseSem.acquire();
                if (this.load == 0) {
                    System.out.println("⚓️ALERT: PORT EMPTY");
                }
                if (this.load == this.capacity) {
                    System.out.println("⚓️ALERT: PORT FULL");
                }
                warehouseSem.release();
                Thread.sleep(100);
            }catch(Exception e){
                System.out.println("Port management system failed!");
            }
        }
    }
}
