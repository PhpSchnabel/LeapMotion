
public class Main {
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PointList pointList = Transformer.readCSV();
		System.out.println(pointList.getGesture());
		System.out.println(pointList.getSpeed());

	}

}
