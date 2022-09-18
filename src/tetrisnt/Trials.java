package tetrisnt;

public class Trials { //▓▓ ░░
	public static int[][] ar = new int[][]{
		{1,2,3}, 
		{4,5,6}, 
		{7,8,9}};
	public static String[][] l = new String[][]{
		{"  ","XX","  "}, 
		{"XX","XX","XX"}, 
		{"  ","  ","  "}};		
	public static int[][] piece = new int[][] {
		{0,0,1,0,0},
		{0,0,1,0,0},
		{0,0,1,0,0},
		{0,0,1,0,0},
		{0,0,0,0,0}};	
		
	Trials(){
		rotate90Left(ar);
		//rotate90Right(ar);
		for(int[] a : piece) {
			for(int b : a) {
				System.out.print(b);
			}
			System.out.println();			
		}
		rotate90Right(rotate90Right(rotate90Right(rotate90Right(l))));
		
	}
	public static String[][] rotate90Right(String[][] arr) {
		
		String[][] rot = new String[arr.length][arr.length];
		
		for(int i = 0; i < arr.length; i++) {
			for(int j = 0; j < arr.length; j++) {				
				rot[j][(arr.length-1)-i] = arr[i][j];
			}
		}
		System.out.println("rotated right:");
		for(String[] a : rot) {
			for(String b : a) {
				System.out.print(b);
			}
			System.out.println();			
		}
		return rot;
	}
	public static int[][] rotate90Left(int[][] arr) {

		int[][] rot = new int[arr.length][arr.length];

		for(int i = 0; i < arr.length; i++) {
			for(int j = 0; j < arr.length; j++) {				
				rot[(arr.length-1)-j][i] = arr[i][j];
			}
		}
		System.out.println("rotated left:");
		for(int[] a : rot) {
			for(int b : a) {
				System.out.print(b);
			}
			System.out.println();			
		}
		return rot;
	}
}
