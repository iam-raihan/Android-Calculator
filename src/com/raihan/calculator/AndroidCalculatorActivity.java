package com.raihan.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AndroidCalculatorActivity extends Activity {
	
	private EditText textBox;
	private int curPos;
	private String textBoxText;
	private String inputText;

	private void initialize (View btn) {
		curPos = textBox.getSelectionStart();
		textBoxText = textBox.getText().toString();
		if (textBoxText.contains("Invalid Expression") 
				|| textBoxText.contains("NaN")
				|| textBoxText.contains("Infinity")
				|| textBoxText.contains("E")) {
			textBox.setText("");
			textBoxText = "";
			curPos = 0;
		}
		inputText = ((Button) btn).getText().toString();
	}
	
	private boolean isArithmetic (char ch) {
		return ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '^';
	}
	
	private boolean isSign (char ch) {
		return isArithmetic(ch) || ch == '√';
	}
	
	private boolean isDigit (char ch) {
		return ch >= '0' && ch <= '9'; // or return Character.isDigit(char);
	}

	private boolean isUnary (char ch) {
		return ch == '-'
				? textBoxText.length() == 0 
					|| isSign(textBoxText.charAt(curPos - 1))
				: false;
	}
	
	private boolean notUsed (char ch, int pos) {
		char[] text = textBoxText.toCharArray();
		while (pos != 0) {
			if (!isDigit(text[pos - 1])) {
				if (isArithmetic(text[pos - 1])) return true;
				if (text[pos - 1] == ch) return false;
			}
			pos--;
		}    	
		return true;
	}

	private void setText (String text, int pos) {
		textBox.setText("");
		textBox.append(text);
		textBox.setSelection(pos);
	}
	
	private void insertText (String newText, int newPosFromCur) {
		textBoxText = new StringBuilder(textBox.getText()).insert(curPos, newText).toString();
		setText(textBoxText, curPos + newPosFromCur);
	}
	
	private void replaceText (String newText, int newPosFromCur) {
		String text = textBoxText;
		text = text.substring(0, curPos - 1) + newText;
		if (curPos != textBox.getText().length())
			 text += textBoxText.substring(curPos);
		setText(text, curPos + newPosFromCur);
	}
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);
	    
	    textBox = (EditText)findViewById(R.id.editText1);
	}
	
	public void equalBtn (View btn) {
		Evaluate evalObj = new Evaluate(textBox.getText().toString());
		double result = evalObj.eval();
		String resultText;
		if (evalObj.invalid)
			return; //resultText = "Invalid Expression";
		else if (result == Math.rint(result)) {
			resultText = new Integer((int)result).toString();
			if (resultText.equals(String.valueOf(Integer.MAX_VALUE)))
				resultText = new Double(result).toString();
		}
		else
			resultText = new Double(result).toString();
		setText(resultText, resultText.length());
	}
	
	public void numBtn (View btn) {
		initialize(btn);

		insertText(inputText, 1);
	}
	
	public void signBtn (View btn) {
		initialize(btn);
		
		if (curPos == 0) {
			if (isUnary(inputText.charAt(0)))
				insertText(inputText, 1);
		}
		else if (isDigit(textBoxText.charAt(curPos - 1))) {
			insertText(inputText, 1);
		}
		else if (textBoxText.charAt(curPos - 1) == '.') {
			insertText("0" + inputText, 2);
		}
		else if (curPos < textBox.getText().length() && textBoxText.charAt(curPos) == '.') {
			insertText(inputText + "0", 2);
		}
		else if (isSign(textBoxText.charAt(curPos - 1))) {
			if (isUnary(inputText.charAt(0)))
				insertText(inputText, 1);
			else if (curPos > 1 && isDigit(textBoxText.charAt(curPos - 2)))
				replaceText(inputText, 0);
		}
	}

	public void rootBtn (View btn) {
		initialize(btn);
		int pos = curPos;
		if (notUsed('√', curPos)) {
			char[] text = textBoxText.toCharArray();
			while (curPos != 0) {
				if (! isDigit(text[curPos - 1]) && text[curPos - 1] != '.') {
					break;
				}
				curPos--;
			}
			insertText(inputText, pos - curPos + 1);
		}
	}
	
	public void dotBtn (View btn) {
		initialize(btn);
		
		if (! notUsed('.', curPos)) {
			return;
		}
		if ( curPos == 0 || !isDigit(textBoxText.charAt(curPos - 1))) {
			insertText("0" + inputText, 2);
		}
		else if ( isDigit(textBoxText.charAt(curPos - 1))) {
			insertText(inputText, 1);
		}
	}
	
	public void clearBtn (View btn) {
		textBox.setText("");
	}
	
	public void backBtn (View btn) {
		initialize(btn);
		
	 	if ( curPos == 0) {
	 		return;
	 	}
	 	else {
	 		replaceText("", -1);
	 	}
	}
}