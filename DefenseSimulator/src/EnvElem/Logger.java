package EnvElem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Logger {
	
	private static PrintWriter _writer;
	private static int time = 0;

	public Logger() {
		
	}
	
	public static void Activated(){
		try {
			_writer = new PrintWriter(new File("Logs/output.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void Disactivated(){
		_writer = null;
	}
	
	public static void IncreaseTime(){
		time++;
		println("Current Time : " + String.valueOf(time));
	}
	
	public static void println(String _string){
		_writer.println(_string);
		_writer.flush();
	}
	
	public static void print(String _string){
		_writer.print(_string);
		_writer.flush();
	}

}
