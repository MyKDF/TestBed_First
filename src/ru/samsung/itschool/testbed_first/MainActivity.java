package ru.samsung.itschool.testbed_first;

import ru.samsung.itschool.main_program.Program;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
//comment

public class MainActivity extends Activity {

	private static final class PrintoutHandler extends Handler {
		public void handleMessage(android.os.Message msg) {
			// обновляем TextView
			String readText = consoleWrite.getText().toString();
			consoleWrite.setText(readText + msg.obj + "\n");
		}
	}

	private static EditText targetEditText;
	private static TextView consoleWrite;
	public static Handler h;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		targetEditText = (EditText) findViewById(R.id.console);
		consoleWrite = (TextView) findViewById(R.id.consoleWrite);
		h = new PrintoutHandler();

		try {
			Thread t = new Thread(new Runnable() {

				@Override
				public void run() {
					Program.main();
				}

			});
			t.start();
		} catch (Throwable error) {
			String message = "Ошибка : " + error.toString();
			message += "\n==========="
					+ "\nДалее следуют строки, где произошла ошибка.\n"
					+ "Первая строка соответствует возникновению ошибки.\n";
			for (StackTraceElement e : error.getStackTrace()) {
				String pos = e.toString();
				String packageName = "main_program";
				String prefixToSearch = packageName + ".";
				// Печатаем только те строчки ошибки, которые
				// написаны самим
				// учеником.
				boolean doPrint = pos.contains(prefixToSearch);
				for (String substr : new String[] { "Program.println",
						"Program.readInt", "Program.readDouble" }) {
					if (pos.contains(prefixToSearch + substr)) {
						doPrint = false;
					}
				}
				if (doPrint) {
					message += "\n"
							+ pos.substring(pos.indexOf(prefixToSearch)
									+ prefixToSearch.length());
				}

				MainActivity.targetEditText.setText(message);
			}
		}

		targetEditText.setOnKeyListener(new View.OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					Console.process(targetEditText.getText().toString());
					targetEditText.setText("");
					return true;
				}
				return false;
			}
		});

	}

	/*
	 * targetEditText.setOnEditorActionListener(new OnEditorActionListener() {
	 * 
	 * @Override public boolean onEditorAction(TextView v, int actionId,
	 * KeyEvent event) { // if (event.getAction() == KeyEvent.ACTION_DOWN) {
	 * 
	 * if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED) { CharSequence
	 * currentText = v.getText(); Console.end=currentText.length(); CharSequence
	 * subText= currentText.subSequence(Console.end-1, 7); v.setText(subText);
	 * // v.append(currentText); // v.moveCursorToVisibleOffset();
	 * 
	 * try { Console.console = ((EditText) findViewById(R.id.console));
	 * Program.main(); was_error = false; } catch (Throwable error) { String
	 * message = "Ошибка : " + error.toString(); message += "\n===========" +
	 * "\nДалее следуют строки, где произошла ошибка.\n" +
	 * "Первая строка соответствует возникновению ошибки.\n"; for
	 * (StackTraceElement e : error.getStackTrace()) { String pos =
	 * e.toString(); String packageName = "main_program"; String prefixToSearch
	 * = packageName + "."; // Печатаем только те строчки ошибки, которые //
	 * написаны самим // учеником. boolean doPrint =
	 * pos.contains(prefixToSearch); for (String substr : new String[] {
	 * "Program.println", "Program.readInt", "Program.readDouble" }) { if
	 * (pos.contains(prefixToSearch + substr)) { doPrint = false; } } if
	 * (doPrint) { message += "\n" + pos.substring(pos .indexOf(prefixToSearch)
	 * + prefixToSearch.length()); } } v.setText(message); was_error = true; }
	 * 
	 * return true; } return false; }
	 * 
	 * });
	 */
	// if (! targetEditText.getText().toString().equals(""))

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
