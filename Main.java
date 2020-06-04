//package com.company;
import java.util.Scanner;
import java.io.*;

public class Main {


    /**
     * public static int numProcesses;
     * public static Scanner randomScanner;
     * public static boolean verbose = false;
     **/


    public static void main(String[] args) {

        // if(args.length==3 && args[2].equals("--verbose")) verbose = true;
/*
        File x = new File(args[0]);
        randomScanner = new Scanner(new File("input.txt"));
        Scanner scan = new Scanner(x);
        //int cycle = 0;
        //create object

        File x = new File(args[0]);
*/
        try {
            Scanner scan = new Scanner(new File(args[0]));
            //Scanner scan = new Scanner(x);
            //WE ARE Scanning in the input's information
            int numProcesses = Integer.parseInt(scan.next());
            int resourceTypes = Integer.parseInt(scan.next());
            int[] resources = new int[resourceTypes];
            for (int i = 0; i < resourceTypes; i++) {
                resources[i] = Integer.parseInt(scan.next());
            }
            Process[] stuff_that_happens = new Process[numProcesses];
            // initialize process objects
            for (int i = 0 ; i < stuff_that_happens.length; i++){
                stuff_that_happens[i] =  new Process(resourceTypes);
            }

            Event temp;
            //for each process, there is a queue of events
            while(scan.hasNext()) {
                temp = new Event(scan.next(), Integer.parseInt(scan.next()), Integer.parseInt(scan.next()), Integer.parseInt(scan.next()), Integer.parseInt(scan.next()));
                ///*list of inputs*/);
                stuff_that_happens[temp.getA()-1].events.add(temp);
            }
            scan.close();

            OptimisticManager.OptimisticManager(stuff_that_happens, resources);

            Scanner scan2 = new Scanner(new File(args[0]));

            scan2.next();
            scan2.next();
            for (int i = 0; i < resourceTypes; i++) {
                scan2.next();
            }

            for (int i = 0 ; i < stuff_that_happens.length; i++){
                stuff_that_happens[i] =  new Process(resourceTypes);
            }

            //for each process, there is a queue of events
            while(scan2.hasNext()) {
                temp = new Event(scan2.next(), Integer.parseInt(scan2.next()), Integer.parseInt(scan2.next()), Integer.parseInt(scan2.next()), Integer.parseInt(scan2.next()));
                ///*list of inputs*/);
                stuff_that_happens[temp.getA()-1].events.add(temp);
            }
            scan2.close();

            Banker.Banker(stuff_that_happens, resources);

            /*
            for (int i =0; i < numProcesses; i++){
                processes[i] = new Process(Integer.parseInt(scan.next()), Integer.parseInt(scan.next()), Integer.parseInt(scan.next()), Integer.parseInt(scan.next()));

            }
            */

            /*
        while (nextline ){

            int numProcesses = scan[0];
            ArrayList<Process> processes = new ArrayList<Process>();
            //Queue<Process> processRequests = new Queue<Process>();
            ArrayList of queue that hold event objects
                    events are all in a queue
                    each queue related to one specific process

                    /*
                    int[] resources = new int[numRe]
                    ArrayList<Queue<Event>> stuff_that_happens = new ArrayList<Queue<Event>>()
                    Event temp;

                    //inside loop
                    temp = new Event()
                    stuff_that_happens.get(temp.getProcess()).add(temp);


                     */

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}








/*
        try{
            randomScanner = new Scanner(new File("random-numbers.txt"));
            Scanner scan = new Scanner(x);
            numProcesses = Integer.parseInt(scan.next());
            Process [] processes =  new Process[numProcesses];
            for (int i =0; i < numProcesses; i++){
                processes[i] = new Process(Integer.parseInt(scan.next()), Integer.parseInt(scan.next()), Integer.parseInt(scan.next()), Integer.parseInt(scan.next()));

            }
            */