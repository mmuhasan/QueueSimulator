package assignment21;

public class MyRandom 
{
	private static int rangeRandom(int s,int e)
	{
		return s+((int)(Math.random()*1000))%(e-s);
	}
	
	public static int customerArrival(){
		return rangeRandom(20, 30); 
	}
	
	public static int serviceTime(){
		return rangeRandom(160, 300);
	}
}