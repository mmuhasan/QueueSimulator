package assignment21;

import java.util.concurrent.Phaser;

interface Queues {
	
	public boolean newCustomer(int statusNewCustomer);
	public int 	TimeLeft();
	public void startTellers(Phaser iterations,int idOpt);
	public int 	getTotalTeller();
	public int  getTrelleriNextServiceTime(int i, int j);
	public int  getTotalServeAtTeller(int i, int j);
	public int  getTimeLeft();
	public int  getLength(int i);
	public int 	size(int i);
	public int  getTellerPerQueue();

}


class GroceryQueues implements Queues {
	volatile int 			iNextCustomer;
	final 	 GroceryQueue[] objGrocryQueues;
		  	 int			iQueueCount;
		  	 int 			iQueueLimit;
		  	 int			iTotalTeller;
	
	public GroceryQueues(int queueCount, int queueLimit)
	{
		iNextCustomer			= MyRandom.customerArrival();
		iQueueCount				= queueCount;
		iQueueLimit				= queueLimit;
		 
		
		objGrocryQueues = new GroceryQueue[queueCount];
		for(int i=0;i<queueCount;i++)
			objGrocryQueues[i] 	= new GroceryQueue(queueLimit);
		System.out.println(iQueueCount+" * "+GroceryQueue.nTeller);
		iTotalTeller			= iQueueCount*GroceryQueue.nTeller;
		System.out.println(iTotalTeller+" ");
				
				//objGrocryQueues[0].iTellerCount;
	}

	
	@Override
	public boolean newCustomer(int tryNewCus)
	{
		int 	 idQueue 		= -1;
		int		 minQueueSize 	= iQueueLimit+2;
		boolean	 success		= false;
		
		for(int i=0;i<iQueueCount;i++)
		{
			if(!objGrocryQueues[i].isFull() && minQueueSize>objGrocryQueues[i].sizeForNewCustomer())
			{
				minQueueSize 	= objGrocryQueues[i].sizeForNewCustomer();
				idQueue			= i;
			}
		}

		if(idQueue>=0)
		{
			objGrocryQueues[idQueue].addCustomer();
			success 			= true;
			iNextCustomer = MyRandom.customerArrival();
		}
		else
		{
			if(tryNewCus == 1)
				iNextCustomer = 10;
			else  if(tryNewCus == 0)
				iNextCustomer = MyRandom.customerArrival();
		}
		
		return success;
	}

	@Override
	public int TimeLeft() {
		return iNextCustomer--;
	}
	
	public int getTimeLeft(){
		return iNextCustomer;
	}

	@Override
	public void startTellers(Phaser iterations,int idOpt) {
		for(int i=0;i<iQueueCount;i++)
			objGrocryQueues[i].startTellers(iterations,i);

		
	}

	@Override
	public int getTotalTeller() {
		return iTotalTeller;
	}



	@Override
	public int getTrelleriNextServiceTime(int i, int j) {
			//return objGrocryQueues[i].objTeller[j].iServiceTime;
		return objGrocryQueues[i].getTrelleriNextServiceTime(0,j);//objTeller[j].iServiceTime;
	}

	@Override
	public int getTotalServeAtTeller(int i, int j) {
		//return objGrocryQueues[i].objTeller[j].iTotalCustomerServe;
		return objGrocryQueues[i].getTotalServeAtTeller(0,j);
	}


	@Override
	public int getLength(int i) {
		return objGrocryQueues[i].getLength(0);//.iQueueSize;
	}

	@Override
	public int size(int i) {
		return objGrocryQueues[i].size(0);//.iQueueSize;
	}


	@Override
	public int getTellerPerQueue() {
		return GroceryQueue.nTeller;
	}

}

class GroceryQueue extends CustomerQueue{
	public static int nTeller=2;
	public GroceryQueue(int queueSize) {
		super(nTeller, queueSize);
	}
	public int sizeForNewCustomer() {
		int addSize = 1;
		for(int i = 0;i<this.nTeller;i++)
			if(!objTeller[i].isServing())
				addSize = 0;
		return size(0)+addSize;
	}
}

class BankQueue extends CustomerQueue implements Queues {
	
	int 			 iNextCustomer;
	int 			 iTotalTeller;
	public BankQueue(int tellerCount,int queueSize) {
		super(tellerCount, queueSize);
		iTotalTeller = tellerCount;
		simulatorConstrain.iQueueCount=1;
	}

	@Override
	public boolean newCustomer(int tryNewCus) 
	{
		boolean success 	= addCustomer();
		if(success)
		{
			iNextCustomer 	= MyRandom.customerArrival();
		}
		else
		{
			if(tryNewCus == 1)
				iNextCustomer = 10;
			else if(tryNewCus==0)
				iNextCustomer = MyRandom.customerArrival();
		}
		return success;
	}

	@Override
	public int TimeLeft() {
		return iNextCustomer--;
	}
	
	@Override
	public int getTimeLeft() {
		return iNextCustomer;
	}


	@Override
	public int getTotalTeller() {
		return iTotalTeller;
	}

	@Override
	public int getTellerPerQueue() {
		return iTotalTeller;
	}

}
