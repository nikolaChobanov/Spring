package com.example.demo;

public enum Directions {
    NORTH ("north"),
    EAST("east"),
    SOUTH("south"),
    WEST("west");

    private final String direciton;
    private static Directions[] vals = values();
   private Directions(String dir) {
       direciton=dir;
    }

    public boolean equalsName(String name){
       return name.equals(name);
    }

    @Override
    public String toString() {
        return this.direciton;
    }

    public Directions getNext(){
        return vals[(this.ordinal()+1) % vals.length];
    }
    public Directions getPrevious() {
        return vals[(ordinal()-1) % vals.length];
    }
}
