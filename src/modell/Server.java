package modell;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;
    private int timeInQueue=0;
    private int timeOfServiceInQueue=0;
    private int cntTime=0;
    public int timpulDeAsteptarePeCoada=0;
    public int timpulDeServirePeCoada=0;
    public int nrClientiPeCoada=0;
    public int getCntTime() {
        return cntTime;
    }

    public void setCntTime(int cntTime) {
        this.cntTime = cntTime;
    }

    public int getTimeInQueue() {
        return timeInQueue;
    }
    public int getTimeOfServiceInQueue() {
        return timeOfServiceInQueue;
    }

    public void setTimeInQueue(int timeInQueue) {
        this.timeInQueue = timeInQueue;
    }

    public void setTimeOfServiceInQueue(int timeOfServiceInQueue) {
        this.timeOfServiceInQueue = timeOfServiceInQueue;
    }

    public BlockingQueue<Task> getTasks() {
        return tasks;
    }

    public Server(BlockingQueue<Task> tasks) {
        this.waitingPeriod = new AtomicInteger();
        this.tasks = tasks;
    }

    public void addTask(Task newTask) {
        this.tasks.add(newTask);
        this.waitingPeriod.addAndGet(newTask.getProcessingTime());
        newTask.setFinishTime(newTask.getFinishTime() + waitingPeriod.get());
        timpulDeAsteptarePeCoada+=waitingPeriod.get();
        timpulDeServirePeCoada += newTask.getProcessingTime();
        nrClientiPeCoada++;
    }

    @Override
    public void run() {
        for (Task t : this.tasks) {
            try {
                wait(t.getProcessingTime());

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setTimeOfServiceInQueue(getTimeOfServiceInQueue()+t.getProcessingTime());
            setTimeInQueue(getTimeInQueue()+ t.getFinishTime());
            waitingPeriod.set(waitingPeriod.get() - t.getProcessingTime());
            t.setProcessingTime(t.getProcessingTime() - 1);
        }
    }

    public Task[] getArrayTasks() {
        return (Task[]) tasks.toArray();
    }

}
