package engine;

public interface GameFace {
	
	public void update(long msgId, long sentFrame, int msgType, byte[] msg);
	
	public void setTicksPerSecondAt(long msgId, long sentFrame, int ticksPerSecond, long startFrame);
	
	public void acceptMessage(long msgId, long sentFrame, boolean accepted);
	
	public void pauseAtTick(long msgId, long sentTick, long pauseTick);
	
}
