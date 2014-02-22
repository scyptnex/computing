package scyp.net;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Unstream {
	
	public static int BUFFER_SIZE = 4096;
	
	/**
	 * Reads the input stream, returning its contents as an array of lines.
	 * Closes the stream on completion.
	 * @param is the source input stream
	 * @return an ArrayList containing the input stream's contents broken up by line
	 * @throws IOException for any stream error
	 */
	public static ArrayList<String> toListOfLines(final InputStream is) throws IOException{
		ArrayList<String> ret = new ArrayList<String>();
		for(String line : toLineIterable(is)){
			ret.add(line);
		}
		return ret;
	}
	
	/**
	 * Provides a means of iterating over the contents of a stream by newlines.
	 * Closes the input stream once hasNext() == false.
	 * @param is the source input stream
	 * @return an iterable object over the lines from the input stream
	 * @throws IOException on streaming errors of any kind
	 */
	public static Iterable<String> toLineIterable(final InputStream is){
		return new Iterable<String>(){
			@Override
			public Iterator<String> iterator() {
				return new Iterator<String>(){
					private Scanner internal = new Scanner(is);
					private boolean finished = false;
					@Override
					public boolean hasNext() {
						return !finished && internal.hasNextLine();
					}
					@Override
					public String next() {
						String ret = internal.nextLine();
						if(!hasNext()){
							internal.close();
							finished = true;
						}
						return ret;
					}
					@Override
					public void remove() {
						throw new UnsupportedOperationException("Cannot remove from an input stream");
					}
				};
			}
		};
	}
	
	/**
	 * Writes the contents of the input stream to the file.
	 * Closes the input stream on completion.
	 * @param is the source input stream
	 * @param target the destination file
	 * @throws IOException on some reading error
	 */
	public static void toFile(InputStream is, File target) throws IOException{
		connectThenClose(is, new FileOutputStream(target));
	}
	
	/**
	 * Calls connectStreams(is, os) then closes both streams.
	 */
	public static void connectThenClose(InputStream is, OutputStream os) throws IOException{
		connectStreams(is, os);
		is.close();
		os.close();
	}
	
	/**
	 * Reads the contents of is into os.
	 * @param is the source input stream
	 * @param os the destination output stream
	 * @throws IOException on some reading error
	 */
	public static void connectStreams(InputStream is, OutputStream os) throws IOException{
		int read = 1;
		byte[] buffer = new byte[BUFFER_SIZE];
		while(read > 0){
			read = is.read(buffer);
			if(read > 0){
				os.write(buffer, 0, read);
			}
		}
	}
	
}
