package Instance;

public interface ColonyInstance {
	
	/*=========*
	 * Control *
	 *=========*/
	
	/**
	 * @return true if the game can be paused by this user
	 */
	public boolean canPause();
	/**
	 * @return True if the game IS NOW UNPAUSED (whether or not it was paused beforehand)
	 */
	public boolean unPause();
	/**
	 * Pauses the game.
	 * Can be requested by local or instance users
	 * @return The time to be paused:
	 * -n: Will pause the game for at most n ms unless the pausing user unpauses the game
	 * 0: Pause is disallowed
	 * +n: Will pause the game for n ms, only the pausing user may unpause before n ms, afterwards any user may unpause
	 */
	public int pause();
	/**
	 * Checks how long the game is paused for
	 * @return The number of seconds paused, or some control information
	 * -n: the game will be unpaused FORCEFULLY in n ms
	 * 0: The game is not paused
	 * +n: the game may be unpaused by any user in n ms
	 */
	public int isPaused();
	
	/*========*
	 * Access *
	 *========*/
	
}
