package com.sqweebloid.utils;

import java.awt.Point;

import com.sqweebloid.interfaces.Client;

/**
 * Represents the different perspective utilities that we use for things
 * such as converting points between matrices.
 * @author `Discardedx2
 */
public class Perspective {

	/**
	 * The SIN values of a given set of angles.
	 */
	public static int[] SINE = new int[2048];
	/**
	 * The COS values of a given set of angles.
	 */
	public static int[] COSINE = new int[2048];

	/**
	 * Translates a point in the perspective matrix to a point on the camera matrix.
	 * @param client The client to translate.
	 * @param plane The plane to translate on.
	 * @param x The x value where x = local << 7
	 * @param y The y value where y = local << 7
	 * @return The translated point on the camera matrix.
	 */
	public static final Point translate_cam_matrix(Client client, int plane, int x, int y) {
		if (x < 128 || y < 128 || x > 13056 || y > 13056) {
			return new Point(-1, -1);
		}
		
		int z = get_tile_height(client, client.getPlane(), x, y) - plane;
		x -= client.getCameraX();
		z -= client.getCameraZ();
		y -= client.getCameraY();
		
		int pitch_sin = SINE[client.getCamPitch()];
		int pitch_cos = COSINE[client.getCamPitch()];
		int yaw_sin = SINE[client.getCamYaw()];
		int yaw_cos = COSINE[client.getCamYaw()];
		
		int _angle = y * yaw_sin + x * yaw_cos >> 16;
		
		y = y * yaw_cos - x * yaw_sin >> 16;
		x = _angle;
		_angle = z * pitch_cos - y * pitch_sin >> 16;
		y = z * pitch_sin + y * pitch_cos >> 16;
		z = _angle;

		if (y >= 50) {
			return new Point(256 + (x << 9) / y, (_angle << 9) / y + 167);
		}

		return new Point(-1, -1);
	}

	/**
	 * Gets the height of a tile in the current perspective.
	 * @param client The client we are currently using.
	 * @param plane The plane the tile is located on.
	 * @param x The baseX of the tile.
	 * @param y The baseY of the tile.
	 * @return The height of a tile at a given position.
	 */
	public static final int get_tile_height(Client client, int plane, int x, int y) {
		int xx = x >> 7;
		int yy = y >> 7;
		if (xx < 0 || yy < 0 || xx > 103 || yy > 103) {
			return 0;
		}
		
		int planea = plane;
		if (planea < 3 && (client.getSceneFlags()[1][xx][yy] & 0x2) == 2) {
			planea++;
		}
		
		int aa = client.getTileOffsets()[planea][xx][yy] * (128 - (x & 0x7F)) + client.getTileOffsets()[planea][xx + 1][yy] * (x & 0x7F) >> 7;
		int ab = client.getTileOffsets()[planea][xx][yy + 1] * (128 - (x & 0x7F)) + client.getTileOffsets()[planea][xx + 1][yy + 1] * (x & 0x7F) >> 7;
		return aa * (128 - (y & 0x7F)) + ab * (y & 0x7F) >> 7;
	}

	static {
		for(int i = 0; i < SINE.length; i++) {
			SINE[i] = (int)(65536.0D * Math.sin((double)i * 0.0030679615D));
			COSINE[i] = (int)(65536.0D * Math.cos((double)i * 0.0030679615D));
		}
	}

}
