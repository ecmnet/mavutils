/****************************************************************************
 *
 *   Copyright (c) 2020 Eike Mansfeld ecm@gmx.de. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 ****************************************************************************/

package com.comino.mavutils;

public class MSPMathUtils {

	public static final double 	TO_RAD   					= Math.PI / 180.0;
	public static final double 	FROM_RAD 					= 180.0 / Math.PI ;
	public static final double   PI2						= 2f*Math.PI;
	private static final double CONSTANTS_RADIUS_OF_EARTH  	= 6371000.0;

	private static  Reference   ref                         = new Reference();

	public static void map_projection_init(double lat0, double lon0) {
		ref.lat_rad = lat0 * TO_RAD;
		ref.lon_rad = lon0 * TO_RAD;
		ref.sin_lat = Math.sin(ref.lat_rad);
		ref.cos_lat = Math.cos(ref.lat_rad);
		ref.init    = true;
	}

	public static float map_projection_distance(double lat, double lon, double lat2, double lon2, float[] translation) {
		float[] p1 = new float[2]; float[] p2 = new float[2];

		if(!ref.init)
			map_projection_init(lat,lon);

		map_projection_project(lat,  lon,  p1);
		map_projection_project(lat2, lon2, p2);

		if(translation != null) {
			translation[0] = p2[0] - p1[0];
			translation[1] = p2[1] - p1[1];
		}

		return (float)Math.hypot(p2[0] - p1[0], p2[1] - p1[1]);
	}

	public static void reset_map_projection() {
		ref.init = false;
	}

	public static boolean is_projection_initialized() {
		return ref.init;
	}


	public static boolean map_projection_project(double lat, double lon, float[] xy) {

		if(!ref.init)
			return false;

		double lat_rad = lat * TO_RAD;
		double lon_rad = lon * TO_RAD;

		double sin_lat = Math.sin(lat_rad);
		double cos_lat = Math.cos(lat_rad);
		double cos_d_lon = Math.cos(lon_rad - ref.lon_rad);

		double arg = ref.sin_lat * sin_lat + ref.cos_lat * cos_lat * cos_d_lon;
		double c = Math.acos(arg);

		double k = 1.0;

		if (Math.abs(c) > 0) {
			k = (c / Math.sin(c));
		}

		xy[0] = (float)(k * (ref.cos_lat * sin_lat - ref.sin_lat * cos_lat * cos_d_lon) * CONSTANTS_RADIUS_OF_EARTH);
		xy[1] = (float)(k * cos_lat * Math.sin(lon_rad - ref.lon_rad) * CONSTANTS_RADIUS_OF_EARTH );

		return true;
	}

	public static boolean map_projection_reproject(float x, float y, float z, double[] latlon) {

		if(!ref.init)
			return false;

		double x_rad = (double)x / CONSTANTS_RADIUS_OF_EARTH;
		double y_rad = (double)y / CONSTANTS_RADIUS_OF_EARTH;

		double c = Math.sqrt(x_rad * x_rad + y_rad * y_rad);

		double lat_rad;
		double lon_rad;

		if (Math.abs(c) > 0) {

			double sin_c = Math.sin(c);
			double cos_c = Math.cos(c);

			lat_rad = Math.asin(cos_c * ref.sin_lat + (x_rad * sin_c * ref.cos_lat) / c);
			lon_rad = (ref.lon_rad + Math.atan2(y_rad * sin_c, c * ref.cos_lat * cos_c - x_rad * ref.sin_lat * sin_c));
		} else {
			lat_rad = ref.lat_rad;
			lon_rad = ref.lon_rad;
		}

		latlon[0] = lat_rad * FROM_RAD;
		latlon[1] = lon_rad * FROM_RAD;

		return true;
	}


	public static float getDistance(double lat1, double lon1, double lat2, double lon2) {


		float distance = (float) (
				Math.acos(Math.sin(lat1*TO_RAD) * Math.sin(lat1*TO_RAD)
						+ Math.cos(lat1*TO_RAD) * Math.cos(lat2*TO_RAD) *
						Math.cos((lon2 - lon1)*TO_RAD)
						)
				* CONSTANTS_RADIUS_OF_EARTH );

		return distance;
	}

	public static float getLocalDistance(float x1, float y1, float z1, float x2, float y2, float z2 ) {
		return (float)Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1)+(z2-z1)*(z2-z1));
	}

	public static float[] eulerAnglesByQuaternion(float euler[], float[] q) {
		euler[0] = (float)Math.atan2(2.0 * (q[0] * q[1] + q[2] * q[3]), 1.0 - 2.0 * (q[1] * q[1] + q[2] * q[2]));
		euler[1] = (float)Math.asin(2 * (q[0] * q[2] - q[3] * q[1]));
		euler[2] = (float)Math.atan2(2.0 * (q[0] * q[3] + q[1] * q[2]), 1.0 - 2.0 * (q[2] * q[2] + q[3] * q[3]));
		return euler;
	}

	public static float fromRad(double radians) {
		float deg = (float)(radians * FROM_RAD ) % 360;
		if(deg<0) deg += 360;
		return deg;

	}

	public static float fromRadSigned(double radians) {
		float deg = (float)(radians * FROM_RAD ) % 360;
		return deg;

	}

	public static float toRad(double angle) {
		return (float)(angle * TO_RAD);
	}

	public static float[] rotateRad(float[] rotated, float posx, float posy, float heading_rad) {
		rotated[0] =  posx * (float)Math.cos(heading_rad) + posy * (float)Math.sin(heading_rad);
		rotated[1] = -posx * (float)Math.sin(heading_rad) + posy * (float)Math.cos(heading_rad);
		return rotated;
	}

	public static float constraint(float value, float max, float min) {
		if(!Float.isNaN(max) && value > max) return max;
		if(!Float.isNaN(min) && value < min) return min;
		return value;
	}

	public static double normAngle(double a, double center) {
		if(Double.isInfinite(a))
			return 0;
		return a - PI2 * Math.floor((a + Math.PI - center) / PI2);
	}
	

	public static float normAngle(float a) {
		return a - (float)PI2 * (float)Math.floor((a + (float)Math.PI - 0.5f) / (float)PI2);

	}

	public static float normAngle2(float angle) {
		angle = normAngle(angle);
		if(angle > (float)Math.PI) return  (float)- (PI2 - angle);
		return angle;
	}

	public static float normAngle(float angle1, float angle2, float center) {
		return (float)normAngle(Math.abs(angle1 - angle2), center);
	}


	public static float normAngleDiff(float angle1, float angle2) {
		return (float)normAngle(Math.abs(angle1 - angle2),0.5);
	}

	public static float expo(float x, float e) {
		return (1 - e) * x + e * x * x * x;
	}

	public static float superexpo(float x, float e, float g) {
		return expo(x, e) * (1 - g) / (1 - Math.abs(x) * g);
	}

	private static class Reference {
		double lat_rad = 0;
		double lon_rad = 0;
		double sin_lat = 0;
		double cos_lat = 0;

		boolean init  = false;


	}

	public static void main(String[] args)  {
		float a = toRad(-360);
		float b = toRad( 359);

		System.out.println(fromRadSigned(normAngle(b-a)));
	}
}


