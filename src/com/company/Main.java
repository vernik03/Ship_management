package com.company;

public class Main {

    public static void main(String[] args) {
        Port port= new Port("Port-Royale", 3, 500);
        Ship ship1 = new Ship("1",  180, port);
        Ship ship2 = new Ship("2",  50, port);
        Ship ship3 = new Ship("3", 220, port);
        Ship ship4 = new Ship("4", 20, port);
        Ship ship5 = new Ship("5", 145, port);
        Ship[] ships= {ship1, ship2, ship3, ship4, ship5};


        Thread portThread= new Thread(){
            public void run() {
                port.run();
            }
        };
        portThread.start();
        for (Ship ship : ships){
            Thread thread= new Thread(){
                public void run() {
                    ship.run();
                }
            };
            thread.start();
        }
    }
}
