package models;

public class Timestamp implements Comparable<Timestamp>{
    long val;
    public Timestamp(long val){
        this.val = val;
    }

    @Override
    public int compareTo(Timestamp o) {
        return Long.compare(val, o.val);
    }
}
