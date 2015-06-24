package CommonInfo;

import java.util.Random;

public class XY {
	/*
	 * this version is meter, meter XY
	 */
	//XY is in meter
	public double x,y;
	
	private static final double EARTH_CIRC_METERS = 40030218; /// in meter

	public XY()	{
		x = 0;
		y = 0;
	}
	
	public XY(double x1, double y1) {
		// X is meter, Y : meter
		// x1, y1 should be meter
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
		
	    double distance = Math.sqrt((x-input.x)*(x-input.x) + (y-input.y)*(y-input.y));
	
		return distance;
	}
	
	public double calBearing(XY input)
	{
		//starting point is north side
		//return in degree
		
		double brng;
		double dx, dy;
		
		dx = input.x- this.x;
		dy = input.y - this.y;

		brng = Math.toDegrees(Math.atan2(dy,dx));
	    
	    return brng;
	    
	    //return degree;
	
	}
	
	public XY calEndPoint(double r, double angle) //angle in degree, r in meter
	{
		//return in XY(degree, degree)
		
		double dx, dy;
		dx = r * Math.sin(angle);
		dy = r * Math.cos(angle);
		
		
		XY ret =new XY(x+ dx, y + dy);
				
		return ret;
	}
	
	public XY calEndPointRect(double x, double y){// x, y in meter
		XY ret;
		//return XY(meter, meter)
		ret = new XY(this.x + x, this.y + y);
		
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

	
	public XY calEndPointObj(XY obj, double speed){
		// speed in m / s;
		
		double dx = obj.x - this.x;
		double dy = obj.y - this.y;
		double dist = obj.distance(this);
		double dt = dist / speed;
		
		double r_dx = dx/ dt;
		double r_dy = dy/ dt;
		
		XY _ret = new XY(x + r_dx, y + r_dy);
		
		return _ret;
	}
}
