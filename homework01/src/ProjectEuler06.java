public class ProjectEuler06 {

	public static int sumSquares(int max) {

		int sum = 0;
		for (int i = 1; i <= max; i++) {
			sum = sum + (i * i);
		}

		return sum;
	}

	public static int squareSums(int max) {

		int sum = 0;
		for (int i = 1; i <= max; i++) {
			sum = sum + i;
		}

		return sum * sum;
	}

	public static int solve(int max) {

		int d = squareSums(max) - sumSquares(max);
		return d;
	}

	public static void main(String[] args) {

		int n = 100;
		System.out.printf("sumOfSquares(%d) = %d\n", n, sumSquares(n));
		System.out.printf("squareOfSums(%d) = %d\n", n, squareSums(n));
		System.out.printf("solve(%d) = %d\n", n, solve(n));

	}
}