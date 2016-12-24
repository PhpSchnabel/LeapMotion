
public class Point {
	
	private double x;
	private double y;
	private double z;

	public Point(){

	}

	public Point(String sX, String sY, String sZ){
		
		x = Double.parseDouble(sX);
		y = Double.parseDouble(sY);
		z = Double.parseDouble(sZ);

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


}
