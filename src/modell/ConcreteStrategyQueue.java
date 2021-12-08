package modell;

import java.util.List;

public class ConcreteStrategyQueue  implements Strategy {
    @Override
    public void addTask(List<Server> servers, Task t) {
        int minTasks = servers.get(0).getTasks().size();
        int index = 0;
        int indexMin = 0;
        for (Server s : servers) {
            int noTasks = s.getTasks().size();
            if (noTasks < minTasks) {
                minTasks = noTasks;
                indexMin = index;
            }
            index++;
        }
        servers.get(indexMin).addTask(t);
        servers.get(indexMin).setCntTime(servers.get(indexMin).getCntTime()+1);
    }
}
