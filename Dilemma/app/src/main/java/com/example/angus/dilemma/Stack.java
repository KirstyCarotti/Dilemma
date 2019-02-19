package com.example.angus.dilemma;

public class Stack {

	int inpointer=0;
	int outpointer=0;

	int size=10;

	Question qstStack[]=new Question[0];


	public Stack() {
		qstStack = new Question[size];

	}

	public boolean push(Question q) {

		if (inpointer<size) {
			qstStack[inpointer]=q;
			inpointer++;
			return true;
		}

		return false;
	}

	public Question pop() {

		if (outpointer==size) {
			return null;
		}

		Question temp= qstStack[outpointer];
		qstStack[outpointer]=null;
		outpointer++;
		return temp;
	}

	public boolean isEmpty() {
		if (outpointer==10) {
			return true;
		}
		return false;

	}

	public boolean isFull(){
		if (inpointer==size) return true;
		else return false;
	}

}
