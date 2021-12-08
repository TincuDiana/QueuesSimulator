package modell;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static view.SimulationView.setSimulationTextArea;

public class FilesUtil {
    public static final String FILE_NAME="threadOutput.txt";
    public BufferedWriter writer;

    public FilesUtil() throws IOException{
        writer = new BufferedWriter(new FileWriter(FILE_NAME));
    }

    public void writeData(int currentTime, List<Task> waitingTasks, List<Server> queue) throws IOException {
        String s="\nTime: " + currentTime + "\nWaiting clients: ";
        for(Task t: waitingTasks){
            s= s + t.toString();
        }
        for(int i= 0;i<queue.size();i++){
            s=s+"\nQueue " + i + ": ";
            for(Task t: queue.get(i).getTasks()) { s= s + t.toString();}
        }
        System.out.println(s);
        writer.append(s);
    }

}
