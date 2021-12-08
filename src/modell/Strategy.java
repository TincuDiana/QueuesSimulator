package modell;

import java.util.List;

public interface Strategy {
    //public int timpulDeServirePeCoada = 0;
    public void addTask(List<Server> servers, Task t);
}