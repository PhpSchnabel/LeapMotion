import java.util.LinkedList;
import java.math.*;

public class PointList extends LinkedList<Point> {
	
	public String getGesture(){
        //compare first and last element
	    int compareX = Double.compare(getFirst().getX(), get(this.size()-1).getX());

		if(compareX < 0){
			return "Open";
		}
		else{
			return "Close";
		}
		
	}
	
	public String getSpeed(){
	    //more than 80 points in the excel file means, that the movement was slow
		if(size() > 80){
			return "Slow";
		}
		else{
			return "Fast";
		}
	}
	
	

	
}
