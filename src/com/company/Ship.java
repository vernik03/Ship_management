package com.company;

import java.util.Random;

public class Ship implements Runnable{
    private String name;
    private String color;
    private Port port;
    private int capacity;
    private int load;

    public Ship(String m_name, int m_capacity, Port m_port){
        this.name=m_name;
        this.port=m_port;
        this.capacity=m_capacity;
        this.load=this.capacity/2;
    }
    private void UnloadSome(){
        port.acquireWarehouseSem();
        int cap=Math.min(this.load, port.getFreeSpace());
        this.load-= cap;
        port.storeCargo(cap);
          System.out.printf("Ship %s has loaded off %d tons. %d tons abroad. %d tons in port\n", this.name, cap, this.load, port.getLoad());
        port.releaseWarehouseSem();
    }
    private void LoadSome(){
        port.acquireWarehouseSem();
        int cap=(int)Math.round(Math.random()*Math.min(this.capacity- this.load, port.getLoad()));
        this.load+= cap;
        port.removeCargo(cap);
          System.out.printf("Ship %s has taken %d tons. %d tons abroad. %d tons in port\n", this.name, cap, this.load, port.getLoad());
        port.releaseWarehouseSem();
    }

    private void PortInteraction(){
        try {
            Thread.sleep((long) Math.floor(Math.random() * 1000 + 100));
            this.UnloadSome();
            Thread.sleep((long) Math.floor(Math.random() * 1000 + 100));
            this.LoadSome();
            Thread.sleep((long) Math.floor(Math.random() * 1000 + 100));
              System.out.printf("Ship %s leaves with %d tons. %d tons in port\n", this.name, this.load, port.getLoad());
        }catch(Exception e){
            System.out.println("Error while processing cargo in the port");
        }
    }

    public void run(){
        while(true){
            try {
                  System.out.printf("Ship %s has sailed away!\n", this.name);
                Thread.sleep((long) Math.floor(Math.random()*10000+1000));
                this.load=(int)Math.round(Math.random()*this.capacity);
                  System.out.printf("Ship %s has come back to the port and waits to be unloaded!\n", this.name);
                port.acquireMooringsSem();
                  System.out.printf("Ship %s has docked successfully!\n", this.name);
                this.PortInteraction();
                port.releaseMooringsSem();
            }catch (Exception e){
                System.out.println("Error in ship routine!");
            }
        }
    }
}
