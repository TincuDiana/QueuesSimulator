package controller;

import modell.Server;
import modell.SimulationManager;
import view.SimulationView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class SimulationController {
    private SimulationManager simM;
    private SimulationView simV;
    private int startSim;

    public int getStartSim() {
        return startSim;
    }
    public void setStartSim(int startSim) {
        this.startSim = startSim;
    }

    public SimulationController(SimulationManager simM, SimulationView simV) throws IOException {
        this.simM=simM;
        this.simV=simV;
        this.setStartSim(0);
        this.simV.addSimulationListener(new SimulateListener());
    }

    class SimulateListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            simM.setTimeLimit(simV.getTimeLimit());
            simM.setNumberOfClients(simV.getNrClients());
            simM.setNumberOfServers(simV.getNrOfQueues());
            simM.setMinArrivalTime(simV.getMinATime());
            simM.setMaxArrivalTime(simV.getMaxATime());
            simM.setMinProcessingTime(simV.getMinPTime());
            simM.setMaxProcessingTime(simV.getMaxPTime());
            simM.scheduler.changeStrategy(simM.selectionPolicy);
            setStartSim(1);
        }
    }
}
