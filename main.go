package main

import (
	"fmt"
	"math/rand"
	"strconv"
	"time"
)

type Semaphore struct {
	i int
	max int
}

func (s *Semaphore) Acquire() {
	for s.isAcquired() {
		time.Sleep(time.Millisecond)
	}
	s.i++
}

func (s *Semaphore) Release() {
	if s.isAcquired() {
		s.i--
	}
}

func (s *Semaphore) isAcquired() bool {
	return s.i >= s.max
}


type Ship struct {
	id int
	capacity int
	load int
	port *Port
}

type Port struct {
	moorings int
	capacity int
	load int
	warehouse_sem Semaphore
	moorings_sem Semaphore
}

func min(a, b int) int {
    if a < b {
        return a
    }
    return b
}

func (self *Ship) UnloadSome(){
	self.port.acquireWarehouseSem()
	cap := min(self.load, self.port.getFreeSpace())
	self.load -= cap
	self.port.storeCargo(cap)
	fmt.Println("🚢 Ship " + strconv.Itoa(self.id) + " has loaded off " + strconv.Itoa(cap) + " tons. " + strconv.Itoa(self.load) + " tons abroad. " + strconv.Itoa(self.port.getLoad()) + " tons in port")
	self.port.releaseWarehouseSem()
}
func (self *Ship) LoadSome(){
	self.port.acquireWarehouseSem()
	cap := rand.Intn(min(self.capacity - self.load, self.port.getLoad()) - 30) + 30
	self.load += cap
	self.port.removeCargo(cap)	
	fmt.Println("🚢 Ship " + strconv.Itoa(self.id) + " has taken " + strconv.Itoa(cap) + " tons. " + strconv.Itoa(self.load) + " tons abroad. " + strconv.Itoa(self.port.getLoad()) + " tons in port")
	self.port.releaseWarehouseSem()
}

func (self *Ship) PortInteraction(){
	rand := time.Duration(rand.Intn(1000) + 100)
	time.Sleep(rand)
	self.UnloadSome();
	time.Sleep(rand)
	self.LoadSome();
	time.Sleep(rand)
	fmt.Println("🚢 Ship " + strconv.Itoa(self.id) + " leaves with " + strconv.Itoa(self.load) + " tons. " + strconv.Itoa(self.port.getLoad()) + " tons in port");
	
}

func (self *Ship) LifeOfShip(port *Port, id int) {
	
	fmt.Println("🚢 init ship " + strconv.Itoa(self.id))
	for {
		fmt.Println("🚢 Ship " + strconv.Itoa(self.id) + " has sailed away!")
		time.Sleep(time.Duration( rand.Intn(10000)+1000) * time.Millisecond)
		self.load = rand.Intn(self.capacity - 30 ) + 30
		fmt.Println("🚢 Ship " + strconv.Itoa(self.id) + " has come back to the port and waits to be unloaded!")
		self.port.acquireMooringsSem()
		fmt.Println("🚢 Ship " + strconv.Itoa(self.id) + " has docked successfully!")
		self.PortInteraction()
		self.port.releaseMooringsSem()
		
	}
}


func (self *Port) acquireMooringsSem(){
	self.moorings_sem.Acquire();
}

func (self *Port) releaseMooringsSem(){
	self.moorings_sem.Release();
}

func (self *Port) acquireWarehouseSem(){
	self.warehouse_sem.Acquire();
}

func (self *Port) releaseWarehouseSem(){
	self.warehouse_sem.Release();
}

func (self *Port) getFreeSpace() int {
	return self.capacity-self.load;
}

func (self *Port) getLoad() int {
	return self.load;
}

func (self *Port) storeCargo(cargo int){
	self.load+=cargo;
}

func (self *Port) removeCargo(cargo int){
	self.load-=cargo;
}

func (self *Port) LifeOfHarbor() {
	
	fmt.Println("⚓️ The port Port-Royale opens its " + strconv.Itoa(self.moorings) + " moorings to any visiting ship! load level: " + strconv.Itoa(self.load))
	for {
		self.warehouse_sem.Acquire();
		if self.load == 0 {
			fmt.Println("⚓️ ALERT: PORT EMPTY")
		}
		if self.load == self.capacity {			
			fmt.Println("⚓️ ALERT: PORT FULL")
		}
		self.warehouse_sem.Release();
		time.Sleep(time.Millisecond*100)
	}
}

func main() {
	port := Port{3, 1000, 500, Semaphore{0, 3}, Semaphore{0, 1}}
	ship_array := make([]Ship, 6)	

	
	go port.LifeOfHarbor()
	time.Sleep(time.Millisecond*1000)	
	i := 0
	for ; i < 5; i++ {
		ship_array[i] = Ship{i, 100, 0, &port}
		go ship_array[i].LifeOfShip(&port, i)
	}

	finished := make(chan bool)
	<- finished
}