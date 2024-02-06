package game.entities;

public enum Direction {
	NORTH('N'),
	EAST('E'),
	SOUTH('S'),
	WEST('W'),
	ANY('A');

	public final char dirInt;

	private Direction(char dirInt) {
		this.dirInt = dirInt;
	}

	@Override
	public String toString() {
		return String.valueOf(this.dirInt);
	}

	public boolean equalsTo(Direction col) {
		return this.equals(col) || col.equals(ANY) || this.equals(ANY);
	}

	public static Direction getDirectionByChar(char c) {
		switch (c) {
			case 'n':
			case 'N': {
				return NORTH;
			}
			case 's':
			case 'S': {
				return SOUTH;
			}
			case 'e':
			case 'E': {
				return EAST;
			}
			case 'w':
			case 'W': {
				return WEST;
			}
			default: {
				return ANY;
			}
		}
	}

	public String moveSetString() {
		switch (this) {
			case NORTH: {
				return "move_up";
			}
			case SOUTH: {
				return "move_down";
			}
			case EAST: {
				return "move_right";
			}
			case WEST: {
				return "move_left";
			}
			default: {
				return "";
			}
		}
	}
}
