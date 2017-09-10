package com.raihan.calculator;

public class Evaluate {
	private int pos = -1;
	private int ch;							//pointer
	private String expression;
	public boolean invalid = false;
	
	public Evaluate(String str) {
		this.expression = str;
	}

	private void nextChar() {				//move pointer to next
        ch = (++pos < expression.length()) 
        		? expression.charAt(pos) 
        				: -1;
    }
	
	private boolean isDigit() {
		return (ch >= '0' && ch <= '9') || ch=='E';
	}
	
	private double getNumber(int startPos) {
		while (isDigit() || ch == '.')
        	nextChar();
        return Double.parseDouble(expression.substring(startPos, this.pos));
	}
	
	private boolean isSign(int sign) {		//check if current pointer is a sign
        if (ch == sign) {
            nextChar();
            return true;
        }
        return false;
    }
	
	public double eval() {		
		try {
			nextChar();
	        double x = getExpression();
	        if (pos < expression.length()) 
	        	throw new RuntimeException();
	        return x;
		}
		catch (RuntimeException ex){
			invalid = true;
			return 0;
		}
	}

	private double getExpression() {		//solves 3+3 or 3-3
        double x = getTerm();
        for (;;) {
            if (isSign('+')) 
            	x += getTerm();
            else if (isSign('-'))
            	x -= getTerm();
            else
            	return x;
        }
    }

	private double getTerm() {				//solves: 3*3 or 9/3
        double x = getFactor();
        for (;;) {
            if (isSign('*')) 
            	x *= getFactor();
            else if (isSign('/')) 
            	x /= getFactor();
            else
            	return x;
        }
    }

	private double getFactor() {			//solves: root_over(9.5) or 3.5^3 or -3 or 35.8 or 10  
        if (isSign('-')) 
        	return -getFactor(); 			// unary minus
        if (isSign('√')) //paste here root over sign and save file in UTF-8 format
        	return Math.sqrt(getFactor());
        
        double num;
        int startPos = this.pos;
        if (isDigit() || ch == '.')
            num = getNumber(startPos);
        else
            throw new RuntimeException();
        
        if (isSign('^')) 
        	num = Math.pow(num, getFactor());

        return num;
    }	
}