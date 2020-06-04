//package com.company;

public class Event{

    public String instruction;
    public int A;
    public int B;
    public int C;
    public int D;

    public Event(String instruction, int A, int B, int C, int D) {
        this.instruction = instruction;
        this.A = A;
        this.B = B;
        this.C = C;
        this.D = D;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public int getA() {
        return A;
    }

    public void setA(int a) {
        A = a;
    }

    public int getB() {
        return B;
    }

    public void setB(int b) {
        B = b;
    }

    public int getC() {
        return C;
    }

    public void setC(int c) {
        C = c;
    }

    public int getD() {
        return D;
    }

    public void setD(int d) {
        D = d;
    }

    public void decrementDelay(){
        B--;
    }
}

