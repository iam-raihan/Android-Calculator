package com.raihan.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AndroidCalculatorActivity extends Activity {
	
	private EditText textBox;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        textBox = (EditText)findViewById(R.id.editText1);
    }
    
    public void numBtn (View btn) {
    	int cursorPosStart = textBox.getSelectionStart();
    	CharSequence textBoxText = new StringBuilder(textBox.getText()).insert(cursorPosStart, ((Button) btn).getText().toString());
		setTextBoxText(textBoxText, cursorPosStart);
    }
    
    public void signBtn (View btn) {
    	int cursorPosStart = textBox.getSelectionStart();
    	CharSequence textBoxText = textBox.getText();
    	if ( cursorPosStart == 0) {
    		return;
    	}
    	if (textBoxText.charAt(cursorPosStart - 1) == '+'
				|| textBoxText.charAt(cursorPosStart - 1) == '-'
				|| textBoxText.charAt(cursorPosStart - 1) == '*'
				|| textBoxText.charAt(cursorPosStart - 1) == '/') {
			String text = textBoxText.toString();
			text = text.substring(0, cursorPosStart - 1)+((Button) btn).getText().toString();
			if (cursorPosStart != textBox.getText().length()) {
				 text += textBoxText.toString().substring(cursorPosStart);
			}
			setTextBoxText(text, cursorPosStart - 1);
			return;
		}
    	if (cursorPosStart == textBox.getText().length()) {
    		if (Character.isDigit(textBoxText.charAt(cursorPosStart - 1))) {
        		textBoxText = new StringBuilder(textBox.getText()).insert(cursorPosStart, ((Button) btn).getText().toString());
            	setTextBoxText(textBoxText, cursorPosStart);
        	}
    		else if (textBoxText.charAt(cursorPosStart - 1) == '.') {
        		textBoxText = new StringBuilder(textBox.getText()).insert(cursorPosStart, "0" + ((Button) btn).getText().toString());
            	setTextBoxText(textBoxText, cursorPosStart + 1);
        	}
    	}
    	else if (cursorPosStart < textBox.getText().length()) {
    		if (Character.isDigit(textBoxText.charAt(cursorPosStart - 1)) && Character.isDigit(textBoxText.charAt(cursorPosStart))) {
        		textBoxText = new StringBuilder(textBox.getText()).insert(cursorPosStart, ((Button) btn).getText().toString());
            	setTextBoxText(textBoxText, cursorPosStart);
        	}
    		else if (textBoxText.charAt(cursorPosStart - 1) == '.') {
        		textBoxText = new StringBuilder(textBox.getText()).insert(cursorPosStart, "0" + ((Button) btn).getText().toString());
            	setTextBoxText(textBoxText, cursorPosStart + 1);
        	}
    		else if (textBoxText.charAt(cursorPosStart) == '.') {
        		textBoxText = new StringBuilder(textBox.getText()).insert(cursorPosStart, ((Button) btn).getText().toString() + "0");
            	setTextBoxText(textBoxText, cursorPosStart + 1);
        	}
    	}
    	
    }
    
    public void dotBtn (View btn) {
    	int cursorPosStart = textBox.getSelectionStart();
    	CharSequence textBoxText = textBox.getText();
    	if ( cursorPosStart == 0 
    			|| (!Character.isDigit(textBoxText.charAt(cursorPosStart - 1)) 
    					&& textBoxText.charAt(cursorPosStart - 1) != '.')) {
    		textBoxText = new StringBuilder(textBox.getText()).insert(cursorPosStart, "0" + ((Button) btn).getText().toString());
        	setTextBoxText(textBoxText, cursorPosStart + 1);
    	}
    	if ( cursorPosStart != 0 && Character.isDigit(textBoxText.charAt(cursorPosStart - 1)) && !dotUsed(cursorPosStart)) {
    		textBoxText = new StringBuilder(textBox.getText()).insert(cursorPosStart, ((Button) btn).getText().toString());
        	setTextBoxText(textBoxText, cursorPosStart);
    	}
    }
    
    private boolean dotUsed (int pos) {
    	char[] text = textBox.getText().toString().toCharArray();
    	if (text.length == 0) return false;
    	boolean flag = false;
    	int curPos = pos;
    	while (curPos != 0) {
    		if (!Character.isDigit(text[curPos - 1])) {
    			flag = text[curPos - 1] == '.';
    			break;
    		}
    		curPos--;
    	}
    	if (! flag) {
    		curPos = pos;
    		while (curPos < text.length) {
        		if (!Character.isDigit(text[curPos - 1])) {
        			flag = text[curPos - 1] == '.';
        			break;
        		}
        		curPos++;
        	}
    	}
    	
    	return flag;
    }
    
    private void setTextBoxText(CharSequence text, int pos) {
    	textBox.setText("");
		textBox.append(text);
		textBox.setSelection(pos + 1);
    }
}