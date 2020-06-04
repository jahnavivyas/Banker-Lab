//package com.company;

import java.util.Queue;
import java.util.ArrayDeque;

public class Process {
    Queue<Event> events;
    //make a constructor for process object: personal queue of events, add extra parameters, that we have getters for
    //start cycle, data fields
    public int waitTime;
    public int startTime;
    public int finishTime;
    public int[] initialClaim;


    public Process(int numR) {
        this.events = new ArrayDeque<Event>();
        this.startTime=0;
        this.finishTime=0;
        initialClaim = new int[numR];
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void incrementWaitTime() {
        this.waitTime++;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(int finishTime) {
        this.finishTime = finishTime;
    }

    public Queue<Event> getEvents() {
        return events;
    }

    public void setEvents(Queue<Event> events) {
        this.events = events;
    }

    public int getInitialClaim(int index) {
        return initialClaim[index];
    }

    public void setInitialClaim(int index, int initialClaim) {
        this.initialClaim[index] = initialClaim;
    }
}
