import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


public class xxy702{
	public static void main(String[] args) {
		@SuppressWarnings("resource")
		Scanner cin = new Scanner(System.in);
		//exercise 1
//		String s = cin.nextLine();
		//exercise 2
//		String string = cin.nextLine();
//		int s = cin.nextInt();
//		int c = cin.nextInt();
//		int r = cin.nextInt();
		//exercise 3
		int l = cin.nextInt();
		int h = cin.nextInt();
		int w = cin.nextInt();
		int t = cin.nextInt();
		double chi = cin.nextDouble();
		//System.out.println(exercise1(s));
		
//		for(int i =0; i < r; i++) {
//			exercise2(string, s, c);
//		}
//		
		for (int i = 2; i <= h; i+=2) {
			exercise3(l, i, w, t, chi);
			//System.out.println("   i   ");
		}
				
	}

	public static int exercise1(String pro){
		int output = 0;
		String[] pros = pro.split(" ");
		int h = pros.length;
		double max = 0;
		double prod = 0;
		Random rn = new Random();
		double r = rn.nextDouble();
		//match the random to the specific range of h number
		for(int i = 0; i < h; i++){
			if (pros[i].isEmpty()) {
				
			}else {
				prod = Double.parseDouble(pros[i]);
				if(max < r && r <max+prod){
					output = i;
				}
			}
			max += prod;
		}
		return output;
	}

	public static void exercise2(String strategy, int state, int crowded){
		int d = 0, s = 0;
		s = getNextState(strategy, crowded, state);
		d = getAttend(strategy, s);
		System.out.println(d + "	" + s);
		//System.out.println(p);
	}
	
	public static void exercise3(int lambda, int h, int weeks, int max_t, double chi) {

		double k = 0.6; //Limit line
		
		int c =0; // 1 if the bar is crowded and 0 otherwise 
		int tw = 0; // count week
		int tg = 0; // the generation number 
		int[] payoff= new int[lambda];
		int[] att = new int[lambda];
		
		//initial population
		String[] strategies = initial(lambda,h);
		String[] states = new String[lambda];
		
		for(int i = 0; i<lambda; i++) { // initial the states
			states[i] = "0";// each state for individual chain together
		}
		
		//Week loop
		while(tg<max_t+1) {
			if (tw == weeks-1) {
				if (tg == max_t+1) {
					break;
				}else {
					c = singleWeek(lambda, strategies, states, tw, c, tg, payoff, k);
					tg++;
					tw=0;//reset tw each generation
					strategies = evolution(payoff, strategies, lambda, chi, k, weeks, c);
				}
			}else {
				c = singleWeek(lambda, strategies, states, tw, c, tg, payoff, k);
				//System.out.println(c);
				tw++;
			}
		}
	}
	
	public static String[] evolution(int[] payoff, String[] strategies, int lambda, double chi, double k, int weeks, int c) {
		//Selection, get the best two strategies
		int m = 0;
		int s = 0;
		int count =0;

		for(int i = 0; i<lambda; i++) {
			//Select the best two strategies with highest payoff
			if(payoff[i]>payoff[m]) {
				m = i;
			}else {
				if(payoff[i]>payoff[s]) {
					s = i;
				}
			}
			count = count + payoff[i];
		}
		String parent1 = strategies[m];
		String parent2 = strategies[s];
		//System.out.println("0");
		//System.out.println(payoff[m] + " "+payoff[s]);
		for (int i = 0; i < payoff.length; i++) {
			payoff[i] = 0;
		}
		String[] nextg = new String[lambda];
		
		double[] p1 = getP(parent1);
		double[][] a1= getM(parent1, 1);
		double[][] b1= getM(parent1, 0);
		
		double[] p2 = getP(parent2);
		double[][] a2 = getM(parent2, 1);
		double[][] b2 = getM(parent2, 0);		
			
			
		for (int g = 0; g < lambda; g++) {
			double rate = chi/lambda;
			double count_a1 = 0;
			double count_b1 = 0;
			double count_a2 = 0;
			double count_b2 = 0;
			//Mutation
			int h = p1.length;
			if(count == 0){
				return strategies;//first generation doesn't run evolution
			}
			for (int i = 0; i < h; i++) {
				if (p1[i]>k) {
					p1[i] = p1[i] * (1-rate);
				}else {
					p1[i] = p1[i] * (1+rate);
				}
				if (p2[i]>k) {
					p2[i] = p2[i] * (1-rate);
				}else {
					p2[i] = p2[i] * (1+rate);
				}
			}
			for (int i = 0; i < h; i++) {
				for (int j = 0; j < h; j++) {
					a1[i][j] = a1[i][j] * rate;
					b1[i][j] = b1[i][j] * rate;
					a2[i][j] = a2[i][j] * rate;
					b2[i][j] = b2[i][j] * rate;
					count_a1 = count_a1 + a1[i][j];
					count_b1 = count_b1 + b1[i][j];
					count_a2 = count_a2 + a2[i][j];
					count_b2 = count_b2 + b2[i][j];
				}
				for (int j = 0; j < h; j++) {
					a1[i][j] = a1[i][j]/count_a1;
					b1[i][j] = b1[i][j]/count_b1;
					a2[i][j] = a2[i][j]/count_a2;
					b2[i][j] = b2[i][j]/count_b2;
				}
			}
			
			//Crossover
			nextg[g] = h + " ";
			for (int j = 0; j < h; j++) {
				Random random = new Random();
				int t = random.nextInt(2);
				if (t==0) {
					nextg[g] = nextg[g] + p2[j] + " ";	
				}else if(t == 1){
					nextg[g] = nextg[g] + p1[j] + " ";
				}
				int q = random.nextInt(2);
				if (q==0) {
					for (int j2 = 0; j2 < h; j2++) {
						nextg[g] = nextg[g] + a1[j][j2]+" ";
					}
				}else {
					for (int j2 = 0; j2 < h; j2++) {
						nextg[g] = nextg[g] + a2[j][j2]+" ";
					}
				}
				int p = random.nextInt(2);
				if (p==0) {
					for (int i = 0; i < h; i++) {
						nextg[g] = nextg[g] + b2[j][i]+" ";
					}
				}else {
					for (int i = 0; i < h; i++) {
						nextg[g] = nextg[g] + b1[j][i]+" ";
					}
				}
			}
		}
		
		//System.out.println(nextg[0]);
		return nextg;
	}
	
	public static int singleWeek(int lambda, String[] strategies, String[] states, int tw, int c, int tg, int[] payoff, double k) {
		String a = "";
		ArrayList<Integer> attence = new ArrayList<Integer>();
		int b = 0; //the number of individuals in the bar 
		// get attendance and the number of population in the bar
		for(int i =0;i<lambda; i++) {
			int attend = 0;
			String s = strategies[i];
			attend = getAttend(s, getIndividualState(states[i],tw));// Go to bar or not this week
			b = b + attend;
			attence.add(attend);
		}
		// compute the bar is crowded or not
		if (b >= (k*lambda)) {
			c = 1;
		}else {
			c = 0;
		}
		
		// parse the attendance of population
		for(int i =0; i<attence.size();i++) {
			a = a + "	" + attence.get(i);
		}
//		System.out.println(tw + "	" + tg + "	" + b + "	"+c + a);
		if (tg ==10) {
			System.out.println(b*100/lambda);
		}
		
		//Payoff
		Integer[] s = attence.toArray(new Integer[attence.size()]);
		for(int i = 0; i < s.length; i++) {
			payoff[i] = payoff[i] + payoff(c, s[i]);
		}
		
		//Next week
		for(int i = 0; i<lambda; i++) { //the states in next week
			int is = getIndividualState(states[i],tw);
			states[i] = states[i] + String.valueOf(getNextState(strategies[i], c, is));
		}	
		attence.clear();
		return c;
	}
	
	public static int getNextState(String strategy, int crowded, int state) {
		int nextstate = 0;
		String[] strings = strategy.split(" ");
		//System.out.println(strategy);
		int h = Integer.parseInt(strings[0]);
		double[][] A = new double[h][h];
		double[][] B = new double[h][h];
		String string = "";
		A = getM(strategy, 1);
		B = getM(strategy, 0);
		// Next state
		if (crowded == 1) {
			string = String.valueOf(A[state][0]);
			for (int i = 1; i < h; i++) {
				string = string + " " + String.valueOf(A[state][i]);
			}
		}else if (crowded == 0) {
			string = String.valueOf(B[state][0]);
			for (int i = 1; i < h; i++) {
				string = string + " " + String.valueOf(B[state][i]) ;
			}
		}	
		
		nextstate = exercise1(string);
		return nextstate;
	}
	
	public static double[][] getM(String strategy, int AB) {
		String[] strings = strategy.split(" ");
		int h = Integer.parseInt(strings[0]);
		double[][] A = new double[h][h];
		double[][] B = new double[h][h];
		int g = 2*h+1;
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < h; j++) {
				A[i][j] = Double.parseDouble(strings[2+g*i+j]);
				B[i][j] = Double.parseDouble(strings[2+h+g*i+j]);
			}
		}
		
		if (AB == 1) {
			return A;
		}else {
			return B;
		}
	}
	
	public static int getAttend(String strategy, int state) {
		int d= 0;
		double[] p = getP(strategy);
		double reverse = 1-p[state];
		String s = String.valueOf(reverse) + " " + String.valueOf(p[state]);
		d = exercise1(s);
		return d;
	}
	
	public static double[] getP(String strategy) {
		String[] strings = strategy.split(" ");
		int h = Integer.parseInt(strings[0]);
		//System.out.println(strings.length);
		int g = 2*h+1;
		double[] p = new double[h];
		for(int i = 0; i < h; i++) {
			p[i] = Double.parseDouble(strings[g*i+1]);
			//System.out.println(p[i]);
		}
		return p;
	}
	
 	public static int payoff(int crowded, Integer attence) {
		int payoff=0;
		if ((attence == 0 && crowded == 1) || (attence == 1 && crowded ==0)) {
			payoff=1;	
		}
		return payoff;
	}
	
	public static int getIndividualState(String states, int week) {
		int state = 0;
		Character ch = states.charAt(week);
		state = Integer.parseInt(ch.toString());
		//System.out.println(state);
		//each char stands for a state
		return state;
	}
	
	public static String[] initial(int lambda, int h) {
		//Generate possibility
		String[] strategies = new String[lambda];
		double[] P = new double[h];
		double[][] A = new double[h][h];
		double[][] B = new double[h][h];
		
		for (int i = 0; i<lambda ; i++) {
			P = generateP(lambda, h);
			A = generateM(lambda, h);
			B = generateM(lambda, h);
			//build a string of strategy
			strategies[i] = String.valueOf(h)+" ";
			for(int j=0; j<h; j++) {
				strategies[i] = strategies[i] + P[j] + " ";
				for(int t =0; t<h;t++) {
					strategies[i] = strategies[i] + A[j][t] + " ";
				}
				
				for(int t =0; t<h;t++) {
					strategies[i] = strategies[i] + B[j][t] + " ";
				}
			}
		}
		//System.out.println(strategies[0]);
		return strategies;
	}
	
	public static double[] generateP(int lambda, int h) {
		Random rn = new Random();
		double[] P = new double[h];
		for (int i =0; i<h; i++) {
			P[i] = rn.nextDouble();
		}
		
		return P;
	}
	
	public static double[][] generateM(int lambda, int h){
		double n = 0;
		Random rn = new Random();
		double[][] M =new double[h][h];
		for (int i= 0; i<h;i++) {
			n = 0;
			//all the probabilities sum up to 1
			for(int j = 0; j<h; j++) {
				M[i][j] = rn.nextDouble();
				n = M[i][j] + n;
			}
			
			for (int j = 0; j < h; j++) {
				M[i][j] = M[i][j]/n;
			}
		}
		return M;
	}
}


