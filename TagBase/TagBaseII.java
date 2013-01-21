
import java.io.*;
import java.util.*;

public class TagBaseII {
	
	public final File mainDir;
	
	private final ArrayList<String> names;
	private final HashMap<String, Integer> indexes;
	private final HashMap<String, String> tags;
	private final HashMap<String, String> dates;
	private final HashMap<String, Long> sizes;
	private final HashMap<String, File> locs;
	
	public TagBaseII(File md){
		mainDir = md;
		names = new ArrayList<String>();
		indexes = new HashMap<String, Integer>();
		tags = new HashMap<String, String>();
		dates = new HashMap<String, String>();
		sizes = new HashMap<String, Long>();
		locs = new HashMap<String, File>();
	}
	
}
