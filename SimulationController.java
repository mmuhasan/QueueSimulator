package assignment21;

import java.util.List;
import java.util.concurrent.Phaser;
import javax.swing.SwingWorker;

public class SimulationController extends SwingWorker<Void, Integer>{
	public static int oldSimSpeedup;

	@Override
	protected Void doInBackground() throws Exception {
		Phaser iterations = new Phaser(1);
		
		/** initialization **/
		QueueSimulator.objQueues.startTellers(iterations,0);
		
		iterations.arriveAndAwaitAdvance();
		
//		System.out.println(""+java.lang.Thread.activeCount());
		oldSimSpeedup=simulatorConstrain.iSimulationSpeedup ;
		
		while(QueueSimulator.objSimulationTimeCounter.iGetTimeLeft()!=0)
		{

			if(QueueSimulator.objQueues.TimeLeft()==0)
			{
				
				Customer.iTotalCustomer++;
				if(!QueueSimulator.objQueues.newCustomer( Customer.statusNewCustomer))
				{
					simulatorConstrain.iSimulationSpeedup = 500;
					if(Customer.statusNewCustomer==1)
						Customer.statusNewCustomer = 0;
					else
					{
						Customer.iTotalCustomerLeft++;
						Customer.iTotalCustomer--;
						Customer.statusNewCustomer = 1;
						simulatorConstrain.iSimulationSpeedup = oldSimSpeedup;
					}
				}
			}
			else if(Customer.statusNewCustomer == 0)
			{
				if(QueueSimulator.objQueues.newCustomer(2))
				{
					Customer.statusNewCustomer = 1;
					simulatorConstrain.iSimulationSpeedup = oldSimSpeedup;
				}
			}
			
			iterations.arriveAndAwaitAdvance();
			publish(0);
			if(oldSimSpeedup != -1)
				Thread.sleep(simulatorConstrain.iSimulationSpeedup);
		}
		/** Iteration **/
		
		
		
		return null;
	}
	
	@Override
	protected void process(List<Integer> chunks) {
		QueueSimulator.objSimulatorUI.updateUI();
		super.process(chunks);
	}

}

class tellerService extends Thread
{
	Teller	objTeller;
	Phaser  phase;
	
	public tellerService(Teller t, Phaser p,String name)
	{
		super(name);
		phase		= p;
		objTeller 	= t;
		phase.register();
	}
	
	@Override
	public void run() {
		phase.arriveAndAwaitAdvance();
		while(true)
		{
			if(objTeller.isServing())
			{
				objTeller.iServiceTime--;
				if(objTeller.iServiceTime==0)
					objTeller.stopService();
			}
			else objTeller.startService();
			phase.arriveAndAwaitAdvance();
		}
	}
}