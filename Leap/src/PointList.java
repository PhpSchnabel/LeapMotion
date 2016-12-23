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

	public void distanceToFirstPoint(){
		Double avgDistanceX = 0.0;
		Double avgDistanceY = 0.0;

		//die Koordinaten des ersten Punktes speichern
        double x = getFirst().getX();
        double y = getFirst().getY();

        for(int i = 0; i < this.size()-1; i++){

            //Abstand vom ersten Punkt zu Punkt i bestimmen
            avgDistanceX += Math.abs(get(i).getX()-x);
            avgDistanceY += Math.abs(get(i).getY()-y);

			System.out.println("x: "+x+" y: "+y);
		}

		//Durchschnitt des Abstandes berechnen => wenn avgX sehr groß ist, wird es eine rechts/links Bewegung sein
        //ist avgY sehr groß, wird es ein klopfen sein usw..
		avgDistanceX = avgDistanceX/(this.size()-1);
        avgDistanceY = avgDistanceY/(this.size()-1);

		System.out.println("avg x: "+  avgDistanceX.intValue() + " avg y: "+avgDistanceY.intValue());

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
