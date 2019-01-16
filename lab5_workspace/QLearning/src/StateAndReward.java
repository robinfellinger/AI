public class StateAndReward {
	

		/* State discretization function for the angle controller */
		public static String getStateAngle(double angle, double vx, double vy) {
			
			return  "A:"+ Integer.toString(discretize(angle,5,-Math.PI/4,Math.PI/4));

		}

		/* Reward function for the angle controller */
		public static double getRewardAngle(double angle, double vx, double vy) {
			 return (-1 * Math.abs(angle))/Math.PI;

		}

		/* State discretization function for the full hover controller */
		public static String getStateHover(double angle, double vx, double vy) {

			int a = discretize2(angle,5,-Math.PI/4,Math.PI/4);
			int vvy = discretize2(vy,5,-1, 1);
			int vvx = discretize2(vx,5,-1, 1);
				
			 
			return  "A:"+ Integer.toString(a) + "VY:" +Integer.toString(vvy) + "VX:"+Integer.toString(vvx);
			
			
		}

		/* Reward function for the full hover controller */
		public static double getRewardHover(double angle, double vx, double vy) {
			
			double rewardVelocity = -Math.abs(vy) -Math.abs(vx);

			return 	1.9 * rewardVelocity + 45 * getRewardAngle(angle, vx,vy) ;
		}
	

	public static int discretize(double value, int nrValues, double min,
			double max) {
		if (nrValues < 2) {
			return 0;
		}

		double diff = max - min;

		if (value < min) {
			return 0;
		}
		if (value > max) {
			return nrValues - 1;
		}

		double tempValue = value - min;
		double ratio = tempValue / diff;

		return (int) (ratio * (nrValues - 2)) + 1;
	}

	
	public static int discretize2(double value, int nrValues, double min,
			double max) {
		double diff = max - min;

		if (value < min) {
			return 0;
		}
		if (value > max) {
			return nrValues - 1;
		}

		double tempValue = value - min;
		double ratio = tempValue / diff;

		return (int) (ratio * nrValues);
	}
}