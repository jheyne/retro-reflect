package image.processing.deprecated;

import java.awt.Point;

import image.processing.Searcher.Direction;

public class Slope {

	public final Direction direction;
	public final Point offset;

	public Slope(Direction direction) {
		super();
		assert direction == Direction.EAST || direction == Direction.NORTH || direction == Direction.SOUTH
				|| direction == Direction.WEST;
		this.direction = direction;
		offset = new Point();
	}

	public Slope(Direction direction, int x, int y) {
		this(direction);
		setOffset(x, y);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Slope other = (Slope) obj;
		if (direction != other.direction) {
			return false;
		}
		if (offset == null) {
			if (other.offset != null) {
				return false;
			}
		} else if (!offset.equals(other.offset)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (direction == null ? 0 : direction.hashCode());
		result = prime * result + (offset == null ? 0 : offset.hashCode());
		return result;
	}

	public Slope reverse() {
		return turnLeft().turnLeft();
	}

	public void setOffset(int x, int y) {
		offset.x = x;
		offset.y = y;
	}

	public Slope turnLeft() {
		switch (direction) {
		case EAST:
			return new Slope(Direction.NORTH, offset.y, offset.x * -1);
		case NORTH:
			return new Slope(Direction.WEST, offset.y, offset.x * -1);
		case SOUTH:
			return new Slope(Direction.EAST, offset.y, offset.x * -1);
		case WEST:
			return new Slope(Direction.SOUTH, offset.y, offset.x * -1);
		default:
			break;
		}
		throw new RuntimeException("Unsupported direction");
	}

	public Slope turnRight() {
		switch (direction) {
		case EAST:
			return new Slope(Direction.SOUTH, offset.y * -1, offset.x);
		case NORTH:
			return new Slope(Direction.EAST, offset.y * -1, offset.x);
		case SOUTH:
			return new Slope(Direction.WEST, offset.y * -1, offset.x);
		case WEST:
			return new Slope(Direction.NORTH, offset.y * -1, offset.x);
		default:
			break;
		}
		throw new RuntimeException("Unsupported direction");
	}

}
