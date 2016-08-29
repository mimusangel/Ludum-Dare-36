package fr.ld36.utils;

public class FInterpolate {
	public static float interpolate (float var, float target, float increment){
		var += var > target ? -1 : 1 * increment;
		return var;
	}
	
	public static float interpolate (float var, float target, float increment, double elapsed){
		if(elapsed < 0.01)
			var += (var > target ? -1 : 1) * increment * elapsed;
		return var;
	}
}
