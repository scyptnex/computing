package p001t040;

public class Euler017CountNumber {
	
	public static void main(String[] args) {
		String[] numberWords = new String[1001];
		numberWords[0] = "";
		numberWords[1] = "one";
		numberWords[2] = "two";
		numberWords[3] = "three";
		numberWords[4] = "four";
		numberWords[5] = "five";
		numberWords[6] = "six";
		numberWords[7] = "seven";
		numberWords[8] = "eight";
		numberWords[9] = "nine";
		numberWords[10] = "ten";
		numberWords[11] = "eleven";
		numberWords[12] = "twelve";
		numberWords[13] = "thirteen";
		numberWords[14] = "fourteen";
		numberWords[15] = "fifteen";
		numberWords[16] = "sixteen";
		numberWords[17] = "seventeen";
		numberWords[18] = "eighteen";
		numberWords[19] = "nineteen";
		numberWords[20] = "twenty";
		numberWords[30] = "thirty";
		numberWords[40] = "forty";
		numberWords[50] = "fifty";
		numberWords[60] = "sixty";
		numberWords[70] = "seventy";
		numberWords[80] = "eighty";
		numberWords[90] = "ninety";
		for(int i=0; i<100; i++){
			if(numberWords[i] == null){
				numberWords[i] = numberWords[i-(i%10)] + "" + numberWords[i%10];
			}
			System.out.println(numberWords[i]);
		}
		for(int i=100; i<1000; i++){
			numberWords[i] = numberWords[i/100] + "hundred";
			if(i%100 != 0){
				numberWords[i] = numberWords[i] + "and" + numberWords[i%100];
			}
			System.out.println(numberWords[i]);
		}
		numberWords[1000] = "onethousand";
		
		int tot = 0;
		for(String s : numberWords){
			tot+=s.length();
		}
		System.out.println(tot);
	}

}
