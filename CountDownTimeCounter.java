package assignment21;

public class CountDownTimeCounter {
	private long totalTime; 
	private long elapse; 
	
	/** iSimulationTime is in minutes **/
	public CountDownTimeCounter(int iSimulationTime) {
			totalTime 	= iSimulationTime*60;
			elapse		= 0;
	}
	
	public long iGetTimeLeft()
	{
		long left = totalTime-elapse;
		countDownTick();
		return left; 		
	}

	public String getSimulationTime() {
		return makeTimeString(totalTime);
	}

	public String strGetTimeLeft() {
		return makeTimeString(totalTime - elapse); 
	}
	
	public void countDownTick()
	{
		changeTimer(1);
	}
	
	public void changeTimer(int n)
	{
		elapse = elapse + n;
		/*
		if( simulatorConstrain.iSimulationSpeedup == -1 
				&& ((elapse > 1800 && elapse < 2100)
				|| (elapse > 3550 && elapse < 3800)
				|| (elapse > 5350 && elapse < 5700)
				|| (elapse > 0 && elapse < 300))			
			)
		{
			simulatorConstrain.iSimulationSpeedup = 800;
		
		}
		else simulatorConstrain.iSimulationSpeedup = -1;
		SimulationController.oldSimSpeedup = simulatorConstrain.iSimulationSpeedup;
		*/
	}
	
	private String makeTimeString (long ilSec)
	{
		if(ilSec<=0)
			return "00:00:00";
		long	t;
		String 	Hr,Min,Sec;
		
		Hr			= prependZero((int)(ilSec/3600));
		
		t			= ilSec % 3600;
		Min			= prependZero((int)(t/60));
		
		t			= t % 60;
		Sec			= prependZero((int)t);
		
		return Hr+":"+Min+":"+Sec;		
	}
	
	private String prependZero(int n)
	{
		return (n<10? "0"+n: ""+n);
	}
}
