package engine;

public interface GameClient {
	
	public boolean prepareUpdate(long nextTick, long updateTime, int updateType, byte[] update);
	
	public boolean tick(long curTick, int msThisTick);
	
	public void render();
	
}
