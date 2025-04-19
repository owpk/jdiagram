
public abstract class Car implements Movable, Flexible {

	private int weight;
	private int speed;
	private boolean engineOn;
	private static boolean sideMirrorOn;
	private static boolean lightOn;
	public static final int DIRECTION = 7;

	public abstract void startEngine();

	public abstract void stopEngine();

	public abstract void sideMirrorTurnOn();

	public abstract void sideMirrorTurnOff();

	public abstract int currentSpeed();

	public abstract void accelerate();

	public abstract void brake();

	public abstract void turnRight();

	public abstract void turnLeft();

	@Override
	public void move() {
		// TODO Auto-generated method stub
	}
}