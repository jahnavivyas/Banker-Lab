//package com.company;
import java.util.ArrayList;
import java.util.Queue;

public class OptimisticManager {

    //outstanding returns
    //termination calls
    //outstanding requests
    //
    //

    //arraylist of queues of events, holds each event as a queue
    //we also create an array of resources
    static ArrayList<Queue<Event>> stuff_that_happens;
    static Integer[] resources;


    public static void OptimisticManager(Process[] stuff_that_happens, int[] resources) {

        Event[] currentEvents = new Event[stuff_that_happens.length];

        for (int i = 0; i < stuff_that_happens.length; i++) {
            currentEvents[i] = stuff_that_happens[i].events.remove();
        }
        //we have a matrix of the currently used resources
        //we have a temp resource array until the cycle is over to return them
        int[][] resourceTable = new int[stuff_that_happens.length][resources.length];
        int[] tempResources = new int[resources.length];

        ArrayList<Integer> queue = new ArrayList<Integer>();
        for (int i = 0; i < currentEvents.length; i++) {
            queue.add(i);
        }

        int cycle = -1;
        while (isNotEmpty(currentEvents)) {
            boolean flag = false;
            cycle++;
            //move out of temp and 0 out the values
            for (int i = 0; i < tempResources.length; i++) {
                resources[i] += tempResources[i];
                tempResources[i] = 0;
            }

            for (int k = 0; k < currentEvents.length; k++) {
                int i = queue.get(k).intValue();
                if(currentEvents[i]==null) continue;
                if (currentEvents[i].getB() > 0) {
                    currentEvents[i].decrementDelay();
                    flag = true;
                    stuff_that_happens[i].setStartTime(cycle);
                }
                else if (currentEvents[i].getB() == 0) {
                    //check if there is anything we can do
                    //initiate doesnt do anything in OM
                    if (currentEvents[i].getInstruction().equals("initiate")) {
                        //fill the slot with a new value from the queue
                        stuff_that_happens[i].setStartTime(cycle);
                        currentEvents[i] = stuff_that_happens[i].events.remove();
                        queue.add(i);
                        flag = true;
                    } else if (currentEvents[i].getInstruction().equals("request")) {
                        //read which resource it wants
                        int numRequested = currentEvents[i].getD();
                        int resourceType = currentEvents[i].getC();
                        if (resources[resourceType - 1] >= numRequested) {
                            resources[resourceType - 1] -= numRequested;
                            resourceTable[i][resourceType - 1] += numRequested;
                            currentEvents[i] = stuff_that_happens[i].events.remove();
                            queue.add(i);
                            flag = true;
                            //we cannot grant more resrouces than we have or we have to wait
                        }
                        else {
                            stuff_that_happens[i].incrementWaitTime();
                        }
                        //they are put into the temp array to wait for 1 cycle
                    } else if (currentEvents[i].getInstruction().equals("release")) {
                        int numReleased = currentEvents[i].getD();
                        int resourceType = currentEvents[i].getC();
                        //int actualResources = resources[resourceType];
                        tempResources[resourceType-1] += numReleased;
                        resourceTable[i][resourceType-1] -= numReleased;
                        currentEvents[i] = stuff_that_happens[i].events.remove();
                        queue.add(i);
                        flag = true;
                        //return to the resources list
                    } else if (currentEvents[i].getInstruction().equals("terminate")) {
                        for (int j = 0; j < resourceTable[i].length; j++) {
                            tempResources[j] += resourceTable[i][j];
                            resourceTable[i][j] = 0;
                        }
                        queue.add(i);
                        flag = true;
                        stuff_that_happens[i].setFinishTime(cycle);
                        currentEvents[i] = null;
                    }
                }
                //all values in currentEvents are not null)
                //if it is null then that process is finished
            }
            //here wea re removing dupilicates
            int x = currentEvents.length;
            for (int i = 0; i < x; i++) {
                if(queue.subList(currentEvents.length, queue.size()).contains(queue.get(i))) {
                    queue.remove(i);
                    x--;
                    i--;
                }
            }
            if (!flag) {
                // deadlock state
                //how do we deal with deadlock? we nullify the process and return the resources
                System.out.println("Deadlock state: cannot grant request. Will abort processes.");
                for (int i = 0; i < stuff_that_happens.length; i++) {
                    if (currentEvents[i] != null) {
                        currentEvents[i] = null;
                        stuff_that_happens[i].events=null;
                        for (int j = 0; j < resourceTable[i].length; j++) {
                            tempResources[j] += resourceTable[i][j];
                            resourceTable[i][j] = 0;
                        }
                        if(!deadlocked(currentEvents, resources, tempResources)) break;
                    }
                }
            }
        }

        System.out.println("FIFO" + "\n");
        int totalTime = 0;
        int totalWaitTime = 0;
        for (int i = 0; i < stuff_that_happens.length; i++) {
            if (stuff_that_happens[i].getFinishTime() > stuff_that_happens[i].getStartTime()) {
                int a = stuff_that_happens[i].getWaitTime();
                int b = stuff_that_happens[i].getFinishTime();
                double c = ((float) a /(float) b) * 100.0;
                totalWaitTime += a;
                totalTime += b;
                System.out.println("Task " + (i+1) + "  " + b + "   " + a + "   " + c + "%" + "\n");


            } else {
                System.out.println("The process was aborted.");
            }



            //If deadlock is
            //detected, print a message and abort the lowest numbered deadlocked task after releasing all its resources. If
            //deadlock remains, print another message and abort the next lowest numbered deadlocked task, etc.

        }
        double d = ((float) totalWaitTime / (float) totalTime) * 100.0;
        System.out.println("Total " + totalTime + "   " + totalWaitTime + "   " + d + "%" + "\n");

    }

    //At the end of the run, print, for each task, the time taken, the waiting time, and the percentage of time
    //spent waiting. Also print the total time for all tasks, the total waiting time, and the overall percentage of
    //time spent waiting.

    /**

    FIFO                             BANKER'S
    Task 1      3   0   0%           Task 1        3   0   0%
    Task 2      3   0   0%           Task 2        5   2  40%
    total       6   0   0%           total         8   2  25%


**/

    //check for deadlock!!!!!!!


    public static boolean isNotEmpty(Event array []) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) {
                return true;
            }

        }
        return false;
    }
//checking for deadlock
    static boolean deadlocked(Event[] C, int[] R, int[] T){
        for (int i = 0; i < C.length; i++){
            if(C[i] != null){
                if(C[i].getD() <= R[C[i].getC()-1]+T[C[i].getC()-1]) return false;
            }
        }
        return true;
    }
}

