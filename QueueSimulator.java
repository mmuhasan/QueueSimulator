package assignment21;

import java.util.concurrent.Phaser;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.SwingUtilities;


class simulatorConstrain{
	public static int 		iSimulationSpeedup;
	public static int 		iSimulationTime;
	public static int		iQueueCount;
	public static int		iQueueLimit;
	public static String	strSimtype;
}


public class QueueSimulator {
	public static Queues				objQueues;
	public static SimulatorUI   		objSimulatorUI;
	public static SimulationController	objSimulationController;
	public static CountDownTimeCounter  objSimulationTimeCounter;
	
	public static void main(String[] args) {
		
		simulatorConstrain.iSimulationSpeedup = -1;
		simulatorConstrain.iSimulationTime 	= Integer.parseInt(args[0]);
		simulatorConstrain.strSimtype		= 				   args[1];
		simulatorConstrain.iQueueCount		= Integer.parseInt(args[2]);
		simulatorConstrain.iQueueLimit		= Integer.parseInt(args[3]);
		objSimulationTimeCounter			= new CountDownTimeCounter(simulatorConstrain.iSimulationTime);
		
		if(args.length>4)
			simulatorConstrain.iSimulationSpeedup = Integer.parseInt(args[4]);
		
		if(simulatorConstrain.strSimtype.equalsIgnoreCase("grocery"))
			objQueues 						= 	new GroceryQueues(simulatorConstrain.iQueueCount,
														  		  simulatorConstrain.iQueueLimit);
		else
			objQueues		 				=	new BankQueue	 (simulatorConstrain.iQueueCount,
			  		  											  simulatorConstrain.iQueueLimit);
		objSimulatorUI 			= new SimulatorUI();
		objSimulationController	= new SimulationController();
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				objSimulatorUI.init();
				objSimulationController.execute();	
			}
		});	
	}

}


class CustomerQueue {
		          int 		iTellerCount;
		          int		iQueueSize;
		  private int 		iQueueElementsCount;
		  
		  		  Teller[]	objTeller;
	final private Lock		lock;
	
	public CustomerQueue(int trellerCount,int queueSize) {
	
		iTellerCount		= trellerCount;
		iQueueSize			= queueSize;
		iQueueElementsCount = 0;
		
		objTeller			= new Teller[iTellerCount];
		for(int i=0;i<iTellerCount;i++)
			objTeller[i] 	= new Teller(this);
		
		lock				= new ReentrantLock();
	}
	
	public boolean getCustomer() {
		boolean success = false;
		lock.lock();
		try{
			if(!isEmpty())
			{
				iQueueElementsCount--;
				success = true;
			}
		}
		finally{
			lock.unlock();
		}
		return success;
	}
	
	public boolean addCustomer(){
		boolean success = false;
		lock.lock();
		try{
			if(!isFull())
			{
				iQueueElementsCount++;
				success = true;
			}
		}
		finally{
			lock.unlock();
		}
		return success;
	}
	
	public boolean isFull(){
		return iQueueElementsCount == iQueueSize;
	}
	
	public boolean isEmpty(){
		return iQueueElementsCount==0;
	}
	
	public int size(int i){
		return iQueueElementsCount;
	}
	public void startTellers(Phaser iterations,int qid) {
		for(int i=0;i<iTellerCount;i++)
			objTeller[i].startTeller(iterations,qid,i);
	}

	public int getTrelleriNextServiceTime(int i,int j)
	{
		return objTeller[j].iServiceTime;
	}
	
	public int getTotalServeAtTeller(int i, int j)
	{
		return objTeller[j].iTotalCustomerServe;
	}
	
	public int getLength(int i)
	{
		return iQueueSize;
	}
}


class Teller 
{
	private CustomerQueue	objQueue;
	private boolean 		bServing;
	public	int				iTotalCustomerServe;
	public 	int				iTotalServiceTime;
	public 	int 			iServiceTime;
	private	int 			iServiceTimeSave;
	
	public Teller(CustomerQueue q)
	{
		iTotalCustomerServe = 0;
		iTotalServiceTime	= 0;
		objQueue			= q;
		bServing 			= false;
	}
	
	public void startTeller(Phaser p,int qid, int tid) 
	{	
		tellerService threadTellers = new tellerService(this,p,"Teller-"+qid+"("+tid+")");
		threadTellers.setDaemon(true);
		threadTellers.start();
	}

	public boolean startService()
	{
		if(objQueue.isEmpty())
			return false;
		
		if(objQueue.getCustomer())
		{
			bServing 		= true;
			iServiceTime 	= MyRandom.serviceTime();
			iServiceTimeSave= iServiceTime;
			return true;
		}
		return false;
	}
	
	public void stopService()
	{
		bServing = false;
		iTotalServiceTime += iServiceTimeSave;  
		iTotalCustomerServe++;
		Customer.addCustomerInfo(iServiceTimeSave);
	}
	
	public boolean isServing()
	{
		return bServing;
	}
}

class Customer{
	public static  int		statusNewCustomer = 1;
	public static  int		iTotalCustomer=0;
	public static  int		iTotalCustomerServed=0;
	public static  int		iTotalCustomerLeft=0;
	public static  int		iTotalTimeUsed=0;
	private static Lock		l=new ReentrantLock();
	
	
	public static void addCustomerInfo(int stime)
	{
		l.lock();
			iTotalCustomerServed ++;
			iTotalTimeUsed+=stime;
		l.unlock();
	}
	public static double getAgvServeTime()
	{
		return Math.floor(((double)iTotalTimeUsed)/iTotalCustomerServed * 100)/100;
	}
}