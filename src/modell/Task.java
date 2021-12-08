package modell;

public class Task implements  Comparable<Task>{
    private int id;
    private int arrivalTime;
    private int finishTime;
    private int processingPeriod;

    public Task(int id, int arrivalTime, int processingPeriod, int finishTime) {
        this.id=id;
        this.arrivalTime = arrivalTime;
        this.finishTime = finishTime;
        this.processingPeriod = processingPeriod;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }
    public int getFinishTime() {
        return finishTime;
    }
    public void setFinishTime(int finishTime) {
        this.finishTime = finishTime;
    }
    public int getProcessingTime() {
        return processingPeriod;
    }
    public void setProcessingTime(int processingTime) {
        this.processingPeriod = processingTime;
    }

    public String toString(){
        return "("+ id + ", " + arrivalTime + ", " + processingPeriod + "); ";
    }
    public int  compareTo(Task t){
        if(this.arrivalTime> t.arrivalTime) return 1;
        else
            if(this.arrivalTime==t.arrivalTime)
                return 0;
        return -1;
    }

}
