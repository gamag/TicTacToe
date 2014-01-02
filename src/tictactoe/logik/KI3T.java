/**
 * This file is part of 3T
 * Copyright (C) 2011 Gabriel Margiani
 *
 * 3T is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * 3T is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with 3T.  If not, see <http://www.gnu.org/licenses/>.
 **/

package tictactoe.logik;
/**
 * Translated from C++ to JAVA for use in tictactoe GPIO project.
 * This is more a proof of concept, since it's every thing else than a clean implementation.
 */

import java.util.Date;

public class KI3T {

	private byte user;
	private byte ki;

	private int alg_pos;
	private int alg_bas;
	private boolean usingAlg1;

	private int last_set[];
	private int last_KI_set[];

	private boolean kiStarted;

	/// PRIVATE

	// the 'playground'
	// fields with value
	//		0	= free fields
	//		1	= set by KI
	//		-1	= set by user
	private byte field[][];

	// Test if two of the same player and one free field
	// are in one of the rows and sets in the free field
	// if needed.
	private boolean test1() {
		byte setp[] = new byte[3];
		setp[2] = 0;
		// lines
		for (byte i = 0; i < 3; i++) {
			if ((field[i][0] == field[i][1]) && (field[i][2] == 0) && (field[i][0] != 0)) {
				if (setp[2] != ki) {
					setp[0] = i; setp[1] = 2; setp[2] = field[i][0];
				}
			}
			else if ((field[i][0] == field[i][2]) && (field[i][1] == 0) && (field[i][0] != 0)) {
				if (setp[2] != ki) {
					setp[0] = i; setp[1] = 1; setp[2] = field[i][0];
				}
			}
			else if ((field[i][1] == field[i][2]) && (field[i][0] == 0) && (field[i][1] != 0)) {
				if (setp[2] != ki) {
					setp[0] = i; setp[1] = 0; setp[2] = field[i][1];
				}
			}
		}

		// rows
		for (byte i = 0; i < 3; i++) {
			if ((field[0][i] == field[1][i]) && (field[2][i] == 0) && (field[0][i] != 0)) {
				if (setp[2] != ki) {
					setp[0] = 2; setp[1] = i; setp[2] = field[0][i];
				}
			}
			else if ((field[0][i] == field[2][i]) && (field[1][i] == 0) && (field[0][i] != 0)) {
				if (setp[2] != ki) {
					setp[0] = 1; setp[1] = i; setp[2] = field[0][i];
				}
			}
			else if ((field[1][i] == field[2][i]) && (field[0][i] == 0) && (field[1][i] != 0)) {
				if (setp[2] != ki) {
					setp[0] = 0; setp[1] = i; setp[2] = field[1][i];
				}
			}
		}

		// diagonals

		// upper left to lower right
		if ((field[0][0] == field[1][1]) && (field[2][2] == 0) && (field[0][0] != 0)) {
			if (setp[2] != ki) {
				setp[0] = 2; setp[1] = 2; setp[2] = field[0][0];
			}
		}
		else if ((field[0][0] == field[2][2]) && (field[1][1] == 0) && (field[0][0] != 0)) {
			if (setp[2] != ki) {
				setp[0] = 1; setp[1] = 1; setp[2] = field[0][0];
			}
		}
		else if ((field[1][1] == field[2][2]) && (field[0][0] == 0) && (field[1][1] != 0)) {
			if (setp[2] != ki) {
				setp[0] = 0; setp[1] = 0; setp[2] = field[1][1];
			}
		}

		// upper right to lower left
		if ((field[0][2] == field[1][1]) && (field[2][0] == 0) && (field[0][2] != 0)) {
			if (setp[2] != ki) {
				setp[0] = 2; setp[1] = 0; setp[2] = field[0][2];
			}
		}
		else if ((field[0][2] == field[2][0]) && (field[1][1] == 0) && (field[0][2] != 0)) {
			if (setp[2] != ki) {
				setp[0] = 1; setp[1] = 1; setp[2] = field[0][2];
			}
		}
		else if ((field[1][1] == field[2][0]) && (field[0][2] == 0) && (field[1][1] != 0)) {
			if (setp[2] != ki) {
				setp[0] = 0; setp[1] = 2; setp[2] = field[1][1];
			}
		}
		

		if (setp[2] != 0) {
			field[setp[0]][setp[1]] = ki;
			last_KI_set[0] = setp[0];
			last_KI_set[1] = setp[1];
			return true;
		}

		return false;
	}


	/// The KI/AI functions
	// all functions return true if they have set on a field.
	
	// Level 0, fill the field (adjusted for tictactoe GPIO.)
	private boolean ki_0() {
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				if (field[x][y] == 0) {
					field[x][y] = ki;
					last_KI_set[0] = x;
					last_KI_set[1] = y;
					return true;
				}
			}
		}
		return false;
	}

	// level 2 - Standard - plays using two algorithms randomly
	// Level 1 and 3 have been removed from the java version.
	private boolean ki_2() {
		if (kiStarted) {
			return (usingAlg1) ? alg_s1() : alg_s2();
		}

		return alg_su();
	}

	/// Algorithms

	// to use if ki starts
	private boolean alg_s1() {
		switch (alg_pos) {
			case 0:
				return alg_set(0, 0);
			case 1:
				if (alg_get(1, 1) != 0) {
					return alg_set(2, 2);
				}
				if (alg_get(0, 1) != 0 || alg_get(0, 2) != 0) {
					return alg_set(2, 0);
				}
				return alg_set(0, 2);
			case 2:
				if (alg_get(2, 0) != 0 || (alg_get(0, 1) == alg_get(1, 0))) {
					return alg_set(2, 2);
				}
				return alg_set(2, 0);
			default:
				return false;
		}
	}

	private boolean alg_s2() {
		switch (alg_pos) {
			case 0:
				return alg_set(1, 1);
			case 1:
				if ((last_set[0] != 1) && (last_set[1] != 1)) {
					field[(last_set[0] != 0) ? 0 : 2][(last_set[1] != 0) ? 0 : 2] = ki;
					last_KI_set[0] = (last_set[0] != 0) ? 0 : 2;
					last_KI_set[1] = (last_set[1] != 0) ? 0 : 2;
					return true;
				}
				if ((!(alg_get(1, 0) != 0)) && (!(alg_get(1, 2) != 0))) {
					return alg_set(1, 0);
				}
				return alg_set(2, 1);
			case 2:
				if ((alg_get(2, 1) == ki) || (alg_get(1, 0) == ki)) {
					return alg_set(2, 0);
				}
				if (!alg_set(0, 0)) {
					if (!alg_set(2, 0)) {
						if (!alg_set(2, 2)) {
							return alg_set(0, 2);
						}
					}
				}
				return true;
			default:
				return ki_0();
		}
	}

	// to use if the user starts
	private boolean alg_su()
	{
		switch (alg_pos) {
			case 0:
				if (alg_get(1, 1) != 0) {
					return alg_set(0, 2);
				}
				return alg_set(1, 1);
			case 1:
				if ((alg_get(1, 1) == user) && (alg_get(0, 2) == ki)) {
					if (alg_get(0, 0) != 0) {
						return alg_set(2, 2);
					}
					return alg_set(0, 0);
				}
				if (alg_get(0, 0) != 0 || alg_get(2, 2) != 0) {
					if (alg_get(0, 0) == 0) {
						return alg_set(0, 0);
					}
					if (alg_get(2, 2) == 0) {
						return alg_set(2, 2);
					}
					return alg_set(2, 1);
				}
				if (alg_get(0, 2) != 0 || alg_get(2, 0) != 0 ) {
					if (alg_get(0, 2) == 0) {
						return alg_set(0, 2);
					}
					if (alg_get(2, 0) == 0) {
						return alg_set(2, 0);
					}
					return alg_set(2, 1);
				}
				if (alg_get(0, 1) != 0 && alg_get(2, 1) != 0) {
					return alg_set(1, 0);
				}
				if (alg_get(1, 0) != 0 && alg_get(1, 2) != 0) {
					return alg_set(2, 1);
				}
			case 2:
				if (((alg_get(1, 0) == ki) && (alg_get(0, 1) == user) && alg_get(2, 1) != 0) ||
						((alg_get(2, 1) == ki) && (alg_get(1, 0) == user) && alg_get(1, 2) != 0)) {
					return alg_set(2, 0);
				}
			default:
				return ki_0();
		}
	}

	private boolean alg_set(int x, int y) {
		int nx, ny;
		switch (alg_bas) {
			case 1:
				nx = (y == 1) ? 1 : (y == 2) ? 0 : 2;
				ny = x;
				break;
			case 2:
				nx = (x == 1) ? 1 : (x == 2) ? 0 : 2;
				ny = (y == 1) ? 1 : (y == 2) ? 0 : 2;
				break;
			case 3:
				nx = (y == 1) ? 1 : (y == 2) ? 0 : 2;
				ny = (x == 1) ? 1 : (x == 2) ? 0 : 2;
				break;
			default:
				nx = x;
				ny = y;
		}
		if (field[nx][ny] == 0) {
			field[nx][ny] = ki;
			last_KI_set[0] = nx;
			last_KI_set[1] = ny;
			return true;
		}
		return false;
	}


	private byte alg_get(int x, int y) {
		int nx, ny;
		switch (alg_bas) {
			case 1:
				nx = (y == 1) ? 1 : (y == 2) ? 0 : 2;
				ny = x;
				break;
			case 2:
				nx = (x == 1) ? 1 : (x == 2) ? 0 : 2;
				ny = (y == 1) ? 1 : (y == 2) ? 0 : 2;
				break;
			case 3:
				nx = (y == 1) ? 1 : (y == 2) ? 0 : 2;
				ny = (x == 1) ? 1 : (x == 2) ? 0 : 2;
				break;
			default:
				nx = x;
				ny = y;
		}
		return field[nx][ny];
	}

	/// PUBLIC

	// level = difficulty (1,2,3)
	public KI3T(boolean changeId) {
		kiStarted = false;
		alg_pos = 0;

		if (changeId) {
			ki = -1;
			user = 1;
		}
		else {
			ki = 1;
			user = -1;
		}

		Date d = new Date();
		long ag = d.getTime();
		alg_bas = (int)(ag % 4);

		field = new byte[3][3];
		for (int i = 0; i < 3; i++) {
			for (int x = 0; x < 3; x++) {
				field[i][x] = 0;
			}
		}

		last_set = new int[2];
		last_KI_set = new int[2];

	}

	// starts the game with the KI
	// return false on error
	public boolean start() {
		kiStarted = true;
		return next();
	}


	// Sets the field at posX,posY as field of the user
	// and lets the KI play.
	// return false on error (if posX,posY is already set)
	// moKI = false
	public boolean set(int posX, int posY, boolean noKI) {
		if (field[posX][posY] == 0) {
			field[posX][posY] = user;
			last_set[0] = posX;
			last_set[1] = posY;
			if (!noKI) {
				return next();
			}
		}
		return false;
	}

	// makes the KI set one field
	// used by set
	public boolean next() {
		if (alg_pos < 1) {
			Date d = new Date();
			long a = d.getTime();
			usingAlg1 = (a % 2) == 0;
		}
		if (!test1()) {
			if (!ki_2()) {
				return false;
			}
		}
		alg_pos ++;
		return true;
	}

	// returns a read-only pointer to field
	public byte getFieldAt(int x, int y) {
		return field[x][y];
	}

	public int getLastX() {
		return last_KI_set[0];
	}

	public int getLastY() {
		return last_KI_set[1];
	}
}
