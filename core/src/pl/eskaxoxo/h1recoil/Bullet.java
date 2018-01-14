package pl.eskaxoxo.h1recoil;

public class Bullet {

	int x;
	int y;
	int time;
	
	public Bullet(int x, int y, int time) {
		super();
		this.x = x;
		this.y = y;
		this.time = time;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
}
