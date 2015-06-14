package CommonInfo;

import java.util.Random;

public class XY {
	//XY is in degree(not radian)
	
	public double x,y;
	
	private static final double EARTH_CIRC_METERS = 40030218; /// in meter

	public XY()	{
		
		x = 0;
		y = 0;
	}
	
	public XY(double x1, double y1) {
		// X is Lat, Y : long
		// x1, y1 should be degree
		// TODO Auto-generated constructor stub
		x = x1;
		y = y1;
	}
	
	public XY(XY _xy){
		x = _xy.x;
		y = _xy.y;
	}
	
	public void setXY(XY _xy){
		x = _xy.x;
		y = _xy.y;
	}
	
	public boolean equals(XY input){
		if(Double.compare( input.x, this.x ) == 0 && Double.compare(input.y, this.y) == 0 ) // sjkwon - 5/5 
			return true;
		return false;
	}
	
	public boolean equalsWithError(XY input){
		if(Math.abs(input.x- this.x ) < 0.000001 && Math.abs(input.y - this.y) < 0.000001 ) // sjkwon - 5/5 
			return true;
		return false;
	}
	
	
	public double distance(XY input) {
		
		//return in meter
		
		//using great circle distance formula
		
		double Lat1,Lat2,Lon1,Lon2;
		
		Lat1 = Math.toRadians(this.x);
	    Lat2 = Math.toRadians(input.x);
	    Lon1 = Math.toRadians(this.y);
	    Lon2 = Math.toRadians(input.y); 
	    if(Lat1 == Lat2 && Lon1 == Lon2){
			return 0;
		}
		
	    // do the spherical trig calculation
	    double angle = Math.acos(Math.sin(Lat1) * Math.sin(Lat2)  +
	                               Math.cos(Lat1) * Math.cos(Lat2) * Math.cos(Lon1 - Lon2));
	
	    // convert back to d1egrees
	    angle = Math.toDegrees(angle);
	
	    // each degree on a great circle of Earth is 69.1105 miles
	    double distance = 69.1105 * angle;
	    distance = distance * 1.609344 * 1000; // convert into meter
	
		return distance;
	}
	
	public double calBearing(XY input)
	{
		//starting point is north side
		//return in degree
		
		double brng;
		double x1,y1;
		
		double Lat1,Lat2,Lon1,Lon2;
		
		Lat1 = Math.toRadians(this.x);
	    Lat2 = Math.toRadians(input.x);
	    Lon1 = Math.toRadians(this.y);
	    Lon2 = Math.toRadians(input.y);
	    
	    x1 = Math.sin(Lon2 - Lon1)  * Math.cos(Lat2);
	    y1 = Math.cos(Lat1) * Math.sin(Lat2) - Math.sin(Lat1) * Math.cos(Lat2) * Math.cos(Lon2- Lon1);
	    brng = Math.toDegrees(Math.atan2(x1,y1));
	    
	    return brng;
	    
	    //return degree;
	
	}
	
	public XY calEndPoint(double r, double angle) //angle in degree, r in meter
	{
		//return in XY(degree, degree)
		XY ret = new XY(0,0);
		
		double radAngle = Math.toRadians(angle);
		double Lat1, Lat2, Lon1, Lon2;
		double dist = r/1000; // convert to km
		double earth_Rad = 6378.1;
		
		Lat1 = Math.toRadians(this.x);
		Lon1 = Math.toRadians(this.y);
	
		Lat2 = Math.asin( Math.sin(Lat1)*Math.cos(dist/earth_Rad) + 
	              Math.cos(Lat1)*Math.sin(dist/earth_Rad)*Math.cos(radAngle));
		Lon2 = Lon1 + Math.atan2(Math.sin(radAngle)*Math.sin(dist/earth_Rad)*Math.cos(Lat1), 
                Math.cos(dist/earth_Rad)-Math.sin(Lat1)*Math.sin(Lat2));
		
		Lat2 = Math.toDegrees(Lat2);
		Lon2 = Math.toDegrees(Lon2);
		
		
		ret =new XY(Lat2, Lon2);
				
		return ret;
	}
	
	public XY calEndPointRect(double x, double y){// x, y in meter
		XY ret;
		//return XY(degree, degree)
		//transform into r & angle
		
		double r, angle;
		r = Math.sqrt(x*x + y * y);
		angle = 90 - Math.toDegrees(Math.atan2(y, x));
		
		ret = calEndPoint(r,angle);
		
		return ret;
	}
	
	public XY makeRandomXYRect(double x, double y){//uniform distribution range (x,y)
		// x, y in meter
		// this XY is center
		Random r1 = new Random();
		Random r2 = new Random();
		double smpX = -x/2 + x*r1.nextDouble();
		double smpY = -y/2 + y*r2.nextDouble();
		
		return calEndPointRect(smpX,smpY);
	}
	
	public XY makeRandomXYCircle(double r){
		XY ret = new XY(0,0);
		return ret;
	}
	
	public static XY parseXY(double x1,double y1){
		XY ret = new XY(x1,y1);
		
		return ret;
	}
	
	public XY makeClone(){
		XY ret= new XY(x,y);
		
		return ret;
	}
	
	public void move(XY input){
		//don't use
		x+=input.x;
		y+=input.y;
	}
	
	public void changePos(XY input){
		this.x = input.x;
		this.y = input.y;
	}
	
	public String toString(){
		return "(" + x + ", " + y + ")";
	}

}
