package assignment21;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class SimulatorUI extends JFrame
{
	private 			 JLabel 		timeCounter;
	private				 JLabel			nextCustomerIn;
	private			     JLabel			totalCustomer;
	private 			 JLabel			totalCustomerLeft;
	private				 JLabel[]   	trailerStatus;
	private				 JLabel[]		trailerServeCount;
	private				 JLabel     	totalCustomerServed;
	private				 JLabel     	avgServiceTime;
	private				 JProgressBar[]	queueStatus;
	
	public SimulatorUI() 
	{
		int totalTrailor,n; 
		timeCounter 		= new JLabel(QueueSimulator.objSimulationTimeCounter.strGetTimeLeft());
		nextCustomerIn		= new JLabel("0");
		totalCustomer		= new JLabel("0");
		totalCustomerLeft	= new JLabel("0");
		queueStatus			= new JProgressBar[simulatorConstrain.iQueueCount];
		
		totalTrailor		= QueueSimulator.objQueues.getTotalTeller();
		trailerStatus		= new JLabel[totalTrailor];
		trailerServeCount	= new JLabel[totalTrailor];
		totalCustomerServed = new JLabel("0");
		avgServiceTime		= new JLabel("0.00");
		n					= 0;

		String strQueueStatus	= "0 / "+ simulatorConstrain.iQueueLimit;
		for(int i=0;i<simulatorConstrain.iQueueCount;i++)
		{
			queueStatus[i] = new JProgressBar(0,simulatorConstrain.iQueueLimit);
			queueStatus[i].setStringPainted(true);
			queueStatus[i].setValue(0);
			queueStatus[i].setString(strQueueStatus);
			
			for(int j=0;j< QueueSimulator.objQueues.getTellerPerQueue();j++)
			{
				trailerStatus	 [n] 	= new JLabel(QueueSimulator.objQueues.getTrelleriNextServiceTime(i,j)+" Sec        ");
				trailerServeCount[n++]	= new JLabel("("+QueueSimulator.objQueues.getTotalServeAtTeller(i,j)+")");
			}
		}
	}
	
	public void init() 
	{
		int n = 0;
		setTitle("Grocery Queue Simulator");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	    
		
			Box box = Box.createVerticalBox();	
				
				Box box1 = Box.createHorizontalBox();
		    	box1.add(new JLabel("Simulation Time: "));
		    	box1.add(new JLabel(QueueSimulator.objSimulationTimeCounter.getSimulationTime()));
		    	box1.add(Box.createHorizontalGlue());
		    	box1.add(new JLabel("                                    "));
		    	box1.add(Box.createHorizontalGlue());
		    	box1.add(new JLabel("Time Left: "));
		    	box1.add(timeCounter);
		    
		    box.add(box1,BorderLayout.NORTH);
			    
		    	Box box2 = Box.createHorizontalBox();
		    	box2.add(new JLabel("Number of Trailor: "+simulatorConstrain.iQueueCount) );
		    	box2.add(Box.createHorizontalGlue());
		    	box2.add(new JLabel("Queue Length: "+simulatorConstrain.iQueueLimit) );
		    
		    box.add(box2,BorderLayout.CENTER);
		    	
		    	box1 = Box.createHorizontalBox();
				box1.add(new JLabel("Next Customer in: "));
				box1.add(nextCustomerIn);
				
			box.add(box1,BorderLayout.SOUTH);
			box.add(new JLabel("                      "));
		    
		add(box, BorderLayout.NORTH);
		
			box = Box.createVerticalBox();
			for(int i = 0; i<simulatorConstrain.iQueueCount;i++)
			{
				box1 = Box.createHorizontalBox();
					
					box2 = Box.createVerticalBox();
					for(int j=0;j<QueueSimulator.objQueues.getTellerPerQueue();j++)
					{
						box2.add(new JLabel("EST"));
						box2.add(trailerStatus[n]);
						box2.add(trailerServeCount[n++]);
					}
				
				box1.add(box2);
				box1.add(queueStatus[i]);
				box1.add(new JLabel("    "));
					
			box.add(box1);
			box.add(new JLabel("-------------------------------"));
			}
			box.add(new JLabel("                      "));
			
	    add(box, BorderLayout.CENTER);

	    	box = Box.createHorizontalBox();
	    
	    		box1 = Box.createVerticalBox();
	    		box1.add(new JLabel("Total Customer Enter : "));
	    		box1.add(new JLabel("Total Customer Serve : "));
	    		box1.add(new JLabel("Total Customer Left : "));
	    		box1.add(new JLabel("Total Customer agerage time to serve : "));
	    
	    	box.add(box1);
	    	
	    	box2 = Box.createVerticalBox();
	    		box2.add(totalCustomer);
	    		box2.add(totalCustomerServed);
	    		box2.add(totalCustomerLeft);
	    		box2.add(avgServiceTime);
	    
	    	box.add(box2);
	    add(box, BorderLayout.SOUTH);


	    pack();
	    setVisible(true);
	}

	public void updateUI()
	{
		
		timeCounter	  	 	 .setText(QueueSimulator.objSimulationTimeCounter.strGetTimeLeft());
		
		if(Customer.statusNewCustomer==0)
			nextCustomerIn	 .setText("Waiting..("+QueueSimulator.objQueues.getTimeLeft()+")");
		else nextCustomerIn	 .setText(QueueSimulator.objQueues.getTimeLeft()+" Sec");
		
		totalCustomer 	 	 .setText(Customer.iTotalCustomer+"");
		totalCustomerLeft	 .setText(Customer.iTotalCustomerLeft+"");
		totalCustomerServed	 .setText(Customer.iTotalCustomerServed+"");
		avgServiceTime		 .setText(Customer.getAgvServeTime()+ " Sec ");
		
		int n = 0;
		for(int i=0;i<simulatorConstrain.iQueueCount;i++)
		{
			queueStatus[i].setValue(QueueSimulator.objQueues.size(i));
			queueStatus[i].setString(QueueSimulator.objQueues.size(i)+" / "+ simulatorConstrain.iQueueLimit);
			
			for(int j=0;j<QueueSimulator.objQueues.getTellerPerQueue();j++)
			{
				String str = QueueSimulator.objQueues.getTrelleriNextServiceTime(i,j)+" Sec        ";
				if(QueueSimulator.objQueues.getTrelleriNextServiceTime(i,j)<10)
					str = "00"+str ;
				else if(QueueSimulator.objQueues.getTrelleriNextServiceTime(i,j)<100)
					str = "0"+str;
				trailerStatus	 [n]  .setText(str);
				trailerServeCount[n++].setText("("+QueueSimulator.objQueues.getTotalServeAtTeller(i,j)+")");
			}
		}
	}
}
