package com.multicert.CertExercise.utils;

import java.math.BigInteger;
import java.util.Random;

public class RandomUtils {
	public static BigInteger randomBigInt(int digits) {
		Random rand = new Random();
	    StringBuilder sb = new StringBuilder(digits);
	
	    // First digit can't be 0
	    sb.append(rand.nextInt(9) + 1);
	
	    int limit = digits - 1;
	    for (int i = 0; i < limit; i++)
	        sb.append(rand.nextInt(10));
	
	    return new BigInteger(sb.toString());
	}
}
