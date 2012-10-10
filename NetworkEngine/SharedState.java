import java.util.*;

public abstract class SharedState extends Thread{

	private final ArrayList<Integer> ord;
	private final ArrayList<Integer> code;
	private final ArrayList<String> desc;
	private final ArrayList<Double> val;
	private final ArrayList<Double> d1;
	private final ArrayList<Double> d2;
	private int valCounter;
	private final int msPerTick;
	private final int ticksPerUpdate;
	private long totalTicks;
	private boolean runningLoop;
	
	public SharedState(int tps, int tpu){
		valCounter = 0;
		ord = new ArrayList<Integer>();
		code = new ArrayList<Integer>();
		desc = new ArrayList<String>();
		val = new ArrayList<Double>();
		d1 = new ArrayList<Double>();
		d2 = new ArrayList<Double>();
		msPerTick = 1000/tps;
		ticksPerUpdate = tpu;
		runningLoop = false;
	}
	
	private void networkUpdate(){
		
	}
	
	public final int init(double startval, int valType, String description){
		return init(startval, 0, 0, valType, description);
	}
	public final int init(double startval, double firstDiff, double secondDiff, int valType, String description){
		int ret = valCounter;
		ord.add(ret);
		code.add(valType);
		desc.add(description);
		val.add(startval);
		d1.add(firstDiff);
		d2.add(secondDiff);
		valCounter++;
		return ret;
	}
	
	public abstract void tick();
	public abstract void render();
	
	public void stopLoop(){
		runningLoop = false;
	}
	public void startLoop(){
		this.start();
	}
	public void run(){
		long lastLoop = System.currentTimeMillis();
		runningLoop = true;
		totalTicks = 0;
		while(runningLoop){
			long tloop = System.currentTimeMillis();
			long tdiff = tloop - lastLoop;
			int numTicks = (int)tdiff/msPerTick;
			lastLoop = lastLoop + msPerTick*numTicks;
			for(int t=0; t<totalTicks; t++){
				if(totalTicks%ticksPerUpdate == 0) networkUpdate();
				tick();
				totalTicks++;
			}
			render();
			Thread.yield();
		}
	}
}