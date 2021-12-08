package modell;


import java.util.List;

public class ConcreteStrategyTime implements Strategy {
    @Override
    public void addTask(List<Server> servers, Task t){
        int min=Integer.MAX_VALUE;
        int index=0;
        int indexMin=0;
        for(Server s: servers){
            int sum=0;
            for (Task ts: s.getTasks()){
                sum+=ts.getProcessingTime();
            }
            if(min>sum){
                min=sum;
                indexMin=index;
            }
            index++;
        }
        servers.get(indexMin).addTask(t);
        servers.get(indexMin).setCntTime( servers.get(indexMin).getCntTime()+1);
    }

}
