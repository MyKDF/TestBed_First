package ru.samsung.itschool.testbed_first;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import android.os.Message;



public class Console {

	private static BlockingQueue<String> readLineArrayList = new LinkedBlockingQueue<String>();

	
	public static void process(String c) {
		println(c);
			try {
				readLineArrayList.put(c);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}

	public static String getString() {

			try {
				String s = readLineArrayList.take();
				
				return s;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//		}
		return "0";

	}

	
	public static int getInteger() {

			return Integer.parseInt(getString());


	}
	
	
	public static void println(String c) {
		Message m = MainActivity.h.obtainMessage(0, c);
		MainActivity.h.sendMessage(m);

	}

}
