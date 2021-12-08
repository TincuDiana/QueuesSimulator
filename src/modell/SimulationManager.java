package modell;


import controller.SimulationController;
import view.SimulationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class SimulationManager implements Runnable {
    private int timeLimit;
    private int minArrivalTime;
    private int maxArrivalTime;
    private int minProcessingTime;
    private int maxProcessingTime;
    private int numberOfServers;
    private int numberOfClients;
    public int maxNoClientsPerServer = 100;
    public String str = "";
    public SelectionPolicy selectionPolicy = SelectionPolicy.SHORTEST_TIME;
    public Scheduler scheduler = new Scheduler(getNumberOfServers(), maxNoClientsPerServer);
    private SimulationView frame;
    private SimulationController controller;
    public List<Task> generatedTasks;
    public FilesUtil fileUtil = new FilesUtil();

    public int currentTime = 0;
    public int maxTime = 0;

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public int getMinArrivalTime() {
        return minArrivalTime;
    }

    public void setMinArrivalTime(int minArrivalTime) {
        this.minArrivalTime = minArrivalTime;
    }

    public int getMaxArrivalTime() {
        return maxArrivalTime;
    }

    public void setMaxArrivalTime(int maxArrivalTime) {
        this.maxArrivalTime = maxArrivalTime;
    }

    public int getMinProcessingTime() {
        return minProcessingTime;
    }

    public void setMinProcessingTime(int minProcessingTime) {
        this.minProcessingTime = minProcessingTime;
    }

    public int getMaxProcessingTime() {
        return maxProcessingTime;
    }

    public void setMaxProcessingTime(int maxProcessingTime) {
        this.maxProcessingTime = maxProcessingTime;
    }

    public int getNumberOfServers() {
        return numberOfServers;
    }

    public void setNumberOfServers(int numberOfServers) {
        this.numberOfServers = numberOfServers;
    }

    public int getNumberOfClients() {
        return numberOfClients;
    }

    public void setNumberOfClients(int numberOfClients) {
        this.numberOfClients = numberOfClients;
    }

    public void generateRandomTasks(int numberOfClients) {
        generatedTasks = new ArrayList<>();
        for (int i = 0; i < getNumberOfClients(); i++) {
            int randomProcessingTime = ThreadLocalRandom.current().nextInt(getMinProcessingTime(), getMaxProcessingTime() + 1);
            int randomArrivalTime = ThreadLocalRandom.current().nextInt(getMinArrivalTime(), getMaxArrivalTime() + 1);
            Task newTask = new Task(i, randomArrivalTime, randomProcessingTime, randomArrivalTime + randomProcessingTime);
            this.generatedTasks.add(newTask);
        }
        Collections.sort(generatedTasks);
    }

    public SimulationManager() throws IOException, InterruptedException {
        frame = new SimulationView();
        controller = new SimulationController(this, frame);
        frame.setVisible(true);

        while (controller.getStartSim() == 0) {
            Thread.sleep(100);
        }
        this.generateRandomTasks(getNumberOfClients());
        {
            this.scheduler = new Scheduler(frame.getNrOfQueues(), maxNoClientsPerServer);
            for (Server s : this.scheduler.getServers()) {
                new Thread(s).start();
            }
            this.scheduler.changeStrategy(selectionPolicy);
        }
    }

    public void calcAverageWaitingTime() throws IOException {
        int sum = 0;
        for (Server s : this.scheduler.getServers()) {
            sum += s.timpulDeAsteptarePeCoada;
        }
        double avgTime = (double) sum / (getNumberOfClients() - generatedTasks.size());
        String st = "\nAverage waiting time is: " + avgTime;
        this.fileUtil.writer.append(st);
    }

    public void calcAverageServiceTime() throws IOException {
        int sum = 0;
        for (Server s : scheduler.getServers()) {
            sum += s.timpulDeServirePeCoada;
        }
        double avgTime = (double) sum / (getNumberOfClients() - generatedTasks.size());
        String st = "\nAverage service time is: " + avgTime;
        this.fileUtil.writer.append(st);
    }

    public void peakHour() {
        int sum = 0;
        int maxsum = 0;
        for (Server s : scheduler.getServers()) {
            sum += s.nrClientiPeCoada;
        }
        if (maxsum < sum) {
            maxsum = sum;
            maxTime = currentTime;
        }
        String st = "\nPeak hour: " + maxTime;
        try {
            this.fileUtil.writer.append(st);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String updateFrame(int currentTime, List<Task> waitingTasks, List<Server> queue) throws IOException {
        this.str = "\nTime: " + currentTime + "\nWaiting clients: ";
        for (Task t : waitingTasks) {
            this.str = this.str + t.toString();
        }
        for (int i = 0; i < queue.size(); i++) {
            this.str = this.str + "\nQueue " + i + ": ";
            for (Task t : queue.get(i).getTasks()) {
                this.str = this.str + t.toString();
            }
        }
        return str;
    }

    @Override
    public void run() {
        while (currentTime < getTimeLimit() && controller.getStartSim() == 1) {
            try { SimulationView.simulationTextArea.append(updateFrame(currentTime, generatedTasks, scheduler.getServers())); } catch (IOException e) { e.printStackTrace(); }
            try { fileUtil.writeData(currentTime, generatedTasks, scheduler.getServers()); } catch (IOException e) { e.printStackTrace(); } catch (Exception e) { e.printStackTrace(); }
            List<Task> toRemove = new ArrayList<>();
            for (Task l : generatedTasks) {
                if (l.getArrivalTime() == currentTime + 1) {
                    scheduler.dispatchTask(l);
                    toRemove.add(l);
                }
            }
            generatedTasks.removeAll(toRemove);
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            List<Task> toRemove2 = new ArrayList<>();
            for (Server s : scheduler.getServers()) {
                Task l = s.getTasks().peek();
                if (l != null) {
                    if (l.getProcessingTime() <= 0) {
                        toRemove2.add(l);
                        s.getTasks().remove(l);
                        s.nrClientiPeCoada--;
                    } else l.setProcessingTime(l.getProcessingTime() - 1);
                }
            }
            currentTime++;
        }
        this.peakHour();
        try { this.calcAverageWaitingTime(); } catch (IOException e) { e.printStackTrace(); }
        try { this.calcAverageServiceTime(); } catch (IOException e) { e.printStackTrace(); }
        try { fileUtil.writer.close(); } catch (IOException e) { e.printStackTrace(); }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        SimulationManager gen = new SimulationManager();
        Thread t = new Thread(gen);
        t.start();
    }
}
