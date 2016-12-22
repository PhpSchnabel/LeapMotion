
public class Point {
	
	private double x;
	private double y;
	private double z;
	private double velocityX;
	private double velocityY;
	private double velocityZ;

	public Point(){

	}

	public Point(String sX, String sY, String sZ, String velocityX, String velocityY, String velocityZ){
		
		x = Double.parseDouble(sX);
		y = Double.parseDouble(sY);
		z = Double.parseDouble(sZ);
		
		this.velocityX = Math.abs(Double.parseDouble(velocityX));
		this.velocityY = Math.abs(Double.parseDouble(velocityY));
		this.velocityZ = Math.abs(Double.parseDouble(velocityZ));
		
	}

	public void setX(double x){this.x = x;}

	public void setY(double y){this.y = y;}

	public void setZ(double z){this.z = z;}

	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
	
	public double getZ(){
		return z;
	}
	
	public double getVelocityX(){
		return velocityX;
	}
	
	public double getVelocityY(){
		return velocityY;
	}
	
	public double getVelocityZ(){
		return velocityZ;
	}

}
