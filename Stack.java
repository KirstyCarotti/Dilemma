
public class Stack {

	int pointer=0;
	int size=10;

	Question qstStack[]=new Question[0];


	public Stack() {
		qstStack = new Question[size];

	}

	public boolean push(Question q) {

		if (pointer<size) {
			qstStack[pointer]=q;
			pointer++;
			return true;
		}

		return false;
	}

	public Question pop() {

		if (pointer==0) {
			return null;
		}

		pointer--;
		Question temp= qstStack[pointer];
		qstStack[pointer]=null;
		return temp;
	}

	public boolean isEmpty() {
		if (pointer==0) {
			return true;
		}
		return true;

	}

}
