//package com.company;
import java.util.ArrayList;
import java.util.Queue;

public class Banker {

    //outstanding returns
    //termination calls
    //outstanding requests
    //
    //


    static ArrayList<Queue<Event>> stuff_that_happens;
    static Integer[] resources;


    public static void Banker(Process[] stuff_that_happens, int[] resources) {
        //event array of what happens for each process
        Event[] currentEvents = new Event[stuff_that_happens.length];

        for (int i = 0; i < stuff_that_happens.length; i++) {
            currentEvents[i] = stuff_that_happens[i].events.remove();
        }

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
                if (currentEvents[i] == null) continue;
                if (currentEvents[i].getB() > 0) {
                    currentEvents[i].decrementDelay();
                    flag = true;
                    stuff_that_happens[i].setStartTime(cycle);
                } else if (currentEvents[i].getB() == 0) {
                    //check if there is anything we can do

                    if (currentEvents[i].getInstruction().equals("initiate")) {
                        //fill the slot with a new value from the queue
                        stuff_that_happens[i].setStartTime(cycle);
                        //check initial claim against how much we have of that resource
                        //if its too big, abort immediately
                        if (currentEvents[i].getD() > resources[currentEvents[i].getC()-1]){
                            currentEvents[i] = null;
                            stuff_that_happens[i].events=null;
                        } else {
                            for (int j = 0; j < resources.length; j++) {
                                stuff_that_happens[i].setInitialClaim(currentEvents[i].getC()-1, currentEvents[i].getD());
                            }
                            currentEvents[i] = stuff_that_happens[i].events.remove();
                        }
                        queue.add(i);
                        flag = true;
                    } else if (currentEvents[i].getInstruction().equals("request")) {
                        //read which resource it wants
                        //checking for safe state
                        //proceed only if we have it
                        int numRequested = currentEvents[i].getD();
                        int resourceType = currentEvents[i].getC();
                        if (resources[resourceType - 1] >= numRequested && safe(stuff_that_happens, currentEvents[i], resources, tempResources, resourceTable)) {
                            resources[resourceType - 1] -= numRequested;
                            resourceTable[i][resourceType - 1] += numRequested;
                            if(resourceTable[i][resourceType-1]>stuff_that_happens[i].getInitialClaim(resourceType-1)){
                                for (int j = 0; j < resourceTable[i].length; j++) {
                                    tempResources[j] += resourceTable[i][j];
                                    resourceTable[i][j] = 0;
                                }
                                currentEvents[i] = null;
                                stuff_that_happens[i].events=null;
                            }else {
                                currentEvents[i] = stuff_that_happens[i].events.remove();
                            }
                            queue.add(i);
                            flag = true;
                        } else {
                            stuff_that_happens[i].incrementWaitTime();
                        }
                        //release resources into temp array
                    } else if (currentEvents[i].getInstruction().equals("release")) {
                        int numReleased = currentEvents[i].getD();
                        int resourceType = currentEvents[i].getC();
                        //int actualResources = resources[resourceType];
                        tempResources[resourceType - 1] += numReleased;
                        resourceTable[i][resourceType - 1] -= numReleased;
                        currentEvents[i] = stuff_that_happens[i].events.remove();
                        queue.add(i);
                        flag = true;
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
            int x = currentEvents.length;
            for (int i = 0; i < x; i++) {
                if (queue.subList(currentEvents.length, queue.size()).contains(queue.get(i))) {
                    queue.remove(i);
                    x--;
                    i--;
                }
            }
        }

        System.out.println("Banker" + "\n");
        int totalTime = 0;
        int totalWaitTime = 0;
        for (int i = 0; i < stuff_that_happens.length; i++) {
            if (stuff_that_happens[i].getFinishTime() > stuff_that_happens[i].getStartTime()) {
                int a = stuff_that_happens[i].getWaitTime();
                int b = stuff_that_happens[i].getFinishTime();
                double c = ((float) a / (float) b) * 100.0;
                totalWaitTime += a;
                totalTime += b;
                System.out.println("Task " + (i + 1) + "  " + b + "   " + a + "   " + c + "%" + "\n");


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
     * FIFO                             BANKER'S
     * Task 1      3   0   0%           Task 1        3   0   0%
     * Task 2      3   0   0%           Task 2        5   2  40%
     * total       6   0   0%           total         8   2  25%
     **/

    //check for deadlock!!!!!!!
    public static boolean isNotEmpty(Event array[]) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) {
                return true;
            }

        }
        return false;
    }

    static boolean safe(Process[] P, Event E, int[] R, int[] T, int[][] A) {
        //request of how much they want
        //list of current resources
        //list of processes

        //process array, event, resource allocation array: resources, temp array, and allocated

        // duplicate the resource and allocation arrays
        int[] new_R = new int[R.length];
        int[][] new_A = new int[P.length][R.length];
        for (int i = 0; i < R.length; i++) {
            new_R[i] = R[i];
            new_R[i] += T[i];
        }
        for (int i = 0; i < P.length; i++) {
            for (int j = 0; j < R.length; j++) {
                new_A[i][j] = A[i][j];
            }
        }
        new_R[E.getC()-1] -= E.getD();
        new_A[E.getA()-1][E.getC()-1] += E.getD();

        boolean[] completed = new boolean[P.length];
        for (int i = 0; i < P.length; i++) {
            completed[i] = false;
        }
        // see if you have enough resources to complete a process
        boolean safe = true;
        while (safe && !Completed(completed)) {
            safe = false;
            for (int i = 0; i < P.length; i++) {
                // if we can satisfy the difference between what
                // it has and the max claim, set to true
                boolean possible = true;
                for (int j = 0; j < new_R.length; j++) {
                    if ((!completed[i]) && (P[i].getInitialClaim(j) - new_A[i][j] > new_R[j])){
                        possible = false;
                    }
                }
                if(possible && !completed[i]){
                    for (int j = 0; j < R.length; j++) {
                        new_R[j] += new_A[i][j];
                        completed[i] = true;
                        safe = true;
                    }
                }
            }
        }
        // check if all completed, if so, return true;
        return Completed(completed);
    }

    static boolean Completed(boolean[] completed){
        for (int i = 0; i < completed.length; i++) {
            if(!completed[i]) return false;
        }
        return true;
    }
}

