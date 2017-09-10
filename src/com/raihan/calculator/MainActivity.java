package com.raihan.calculator;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	
	public static final String PREFS_NAME = "CalPrefsFile";
	public static final String INVALID = "Invalid Expression";
	private EditText textBox;
	private int curPos;
	private String textBoxText;
	private String inputText;
	SharedPreferences calPref;

	private void initialize(View btn) {
		curPos = textBox.getSelectionStart();
		textBoxText = textBox.getText().toString();
		if(checkString(textBoxText)) {
			textBox.setText("");
			textBoxText = "";
			curPos = 0;
		}
		inputText = ((Button) btn).getText().toString();
	}
	
	private boolean isArithmetic(char ch) {
		return ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '^';
	}
	
	private boolean isSign(char ch) {
		return isArithmetic(ch) || ch == '√'; //paste here root over sign and save file in UTF-8 format
	}
	
	private boolean isDigit(char ch) {
		return (ch >= '0' && ch <= '9') || ch=='E';
	}

	private boolean isUnary(char ch) {
		return ch == '-'
				? textBoxText.length() == 0 
					|| isSign(textBoxText.charAt(curPos - 1))
				: false;
	}
	
	private boolean notUsed(char ch, int pos) {
		char[] text = textBoxText.toCharArray();
		while(pos != 0) {
			if(!isDigit(text[pos - 1])) {
				if(isArithmetic(text[pos - 1])) return true;
				if(text[pos - 1] == ch) return false;
			}
			pos--;
		}    	
		return true;
	}

	private void setText(String text, int pos) {
		textBox.setText("");
		textBox.append(text);
		textBox.setSelection(pos);
	}
	
	private void insertText(String newText, int newPosFromCur) {
		textBoxText = new StringBuilder(textBox.getText()).insert(curPos, newText).toString();
		setText(textBoxText, curPos + newPosFromCur);
	}
	
	private void replaceText(String newText, int newPosFromCur) {
		String text = textBoxText;
		text = text.substring(0, curPos - 1) + newText;
		if(curPos != textBox.getText().length())
			 text += textBoxText.substring(curPos);
		setText(text, curPos + newPosFromCur);
	}
	
	private String readData(){
		this.calPref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
		return calPref.getString("result", "0");
	}
	
	private void writeData(String data){
		this.calPref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
		this.calPref.edit().clear().putString("result", data).commit(); //not checking return boolean for now
	}
	
	private String calculate(String expression){
		Evaluate evalObj = new Evaluate(expression);
		double result = evalObj.eval();
		String resultText;
		if(evalObj.invalid)
			resultText = INVALID;
		else if(result == Math.rint(result)) {
			resultText = new Integer((int)result).toString();
			if(resultText.equals(String.valueOf(Integer.MAX_VALUE)))
				resultText = new Double(result).toString();
		}
		else
			resultText = new Double(result).toString();
		return resultText;
	}
	
	private Boolean checkString(String str){
		return str.equals(INVALID)
				|| str.equals("NaN")
				|| str.contains("Infinity");
	}
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);
	    
	    textBox = (EditText)findViewById(R.id.editText1);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
	    this.calPref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
	    String savedExpression = calPref.getString("expression", ""); 
		setText(savedExpression, savedExpression.length());
	}
	
	@Override
	public void onPause(){
		super.onPause();
		
		this.calPref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
		this.calPref.edit().clear().putString("expression", textBox.getText().toString()).commit();
	}
	
	public void equalBtn(View btn) {
		String resultText = calculate(textBox.getText().toString());
		setText(resultText, resultText.length());
	}
	
	public void numBtn(View btn) {
		initialize(btn);

		insertText(inputText, 1);
	}
	
	public void signBtn(View btn) {
		initialize(btn);
		
		if(curPos == 0) {
			if(isUnary(inputText.charAt(0)))
				insertText(inputText, 1);
		}
		else if(isDigit(textBoxText.charAt(curPos - 1))) {
			insertText(inputText, 1);
		}
		else if(textBoxText.charAt(curPos - 1) == '.') {
			insertText("0" + inputText, 2);
		}
		else if(curPos < textBox.getText().length() && textBoxText.charAt(curPos) == '.') {
			insertText(inputText + "0", 2);
		}
		else if(isSign(textBoxText.charAt(curPos - 1))) {
			if(isUnary(inputText.charAt(0)))
				insertText(inputText, 1);
			else if(curPos > 1 && isDigit(textBoxText.charAt(curPos - 2)))
				replaceText(inputText, 0);
		}
	}

	public void rootBtn(View btn) {
		initialize(btn);
		int pos = curPos;
		if(notUsed('√', curPos)) { //paste here root over sign and save file in UTF-8 format
			char[] text = textBoxText.toCharArray();
			while(curPos != 0) {
				if(! isDigit(text[curPos - 1]) && text[curPos - 1] != '.') {
					break;
				}
				curPos--;
			}
			insertText(inputText, pos - curPos + 1);
		}
	}
	
	public void dotBtn(View btn) {
		initialize(btn);
		
		if(! notUsed('.', curPos)) {
			return;
		}
		if( curPos == 0 || !isDigit(textBoxText.charAt(curPos - 1))) {
			insertText("0" + inputText, 2);
		}
		else if( isDigit(textBoxText.charAt(curPos - 1))) {
			insertText(inputText, 1);
		}
	}
	
	public void clearBtn(View btn) {
		textBox.setText("");
	}
	
	public void backBtn(View btn) {
		initialize(btn);
		
	 	if( curPos == 0) {
	 		return;
	 	}
	 	else {
	 		replaceText("", -1);
	 	}
	}
	
	public void memoryPlus(View btn){
		String resultText = calculate(readData() + "+" + textBox.getText().toString());
		
		if (checkString(resultText))
			return;
		writeData(resultText);
	}
	
	public void memoryMinus(View btn){		
		String resultText = calculate(readData() + "-" + textBox.getText().toString());
		
		if (checkString(resultText))
			return;
		writeData(resultText);
	}
	
	public void memoryResult(View btn){
		String savedResult = readData();
		setText(savedResult, savedResult.length());
	}
	
	public void memoryClear(View btn){
		writeData("0");
	}
}