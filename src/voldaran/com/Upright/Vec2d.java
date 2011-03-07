package voldaran.com.Upright;


public class Vec2d {
	public long x;
	public long y;
	
	public Vec2d(){
		x = 0;
		y = 0;
	}
	
	public Vec2d(Vec2d vector){
		this(vector.x, vector.y);
	}
	
	public Vec2d(long x, long y){
		this.x = x;
		this.y = y;
	}
	
	public Vec2d(double x, double y){
		this(round(x), round(y));
	}
	
	static private long round(double n){
		return (long)(Math.floor(n + 0.5d));
	}
	
	@Override public boolean equals(Object aThat){
		if(this == aThat) return true;
		
		if(!(aThat instanceof Vec2d)) return false;
		
		Vec2d that = (Vec2d) aThat;
		
		return (x == that.x) && (y == that.y);
	}
	
	@Override 
	public String toString(){
		return "(" + x / 1000 + "," + y / 1000 + ")";
	}
	
	public Vec2d set(Vec2d vector){
		this.x = vector.x;
		this.y = vector.y;
		return this;
	}
	
	public Vec2d set(long x, long y){
		this.x = x;
		this.y = y;
		return this;
	}
	
	public Vec2d set(double x, double y){
		return set(round(x), round(y));
	}
	
	public Vec2d add(Vec2d vector){
		x += vector.x;
		y += vector.y;
		return this;
	}
	
	public Vec2d add(long scalar){
		x += scalar;
		y += scalar;
		return this;
	}
	
	public Vec2d add(long x, long y){
		this.x += x;
		this.y += y;
		return this;
	}
	
	public Vec2d add(double scalar){
		x += round(scalar);
		y += round(scalar);
		return this;
	}
	
	public Vec2d add(double x, double y){
		this.x += round(x);
		this.y += round(y);
		return this;
	}
	
	public Vec2d sub(Vec2d vector){
		x -= vector.x;
		y -= vector.y;
		return this;
	}
	
	public Vec2d sub(long scalar){
		x -= scalar;
		y -= scalar;
		return this;
	}
	
	
	public Vec2d clear(){
		this.x = 0;
		this.y = 0;
		return this;
	}
	
	public Vec2d subtract(Vec2d vector2) {
		Vec2d vector1 = new Vec2d(this);
		return vector1.sub(vector2);
	}
	public Vec2d sub(long x, long y){
		this.x -= x;
		this.y -= y;
		return this;
	}
	
	public Vec2d sub(double scalar){
		x -= round(scalar);
		y -= round(scalar);
		return this;
	}
	
	public Vec2d sub(double x, double y){
		this.x -= round(x);
		this.y -= round(y);
		return this;
	}
	
	public Vec2d dot(Vec2d vector){
		x *= vector.x;
		y *= vector.y;
		return this;
	}
	
	public Vec2d dot(long x, long y){
		this.x *= x;
		this.y *= y;
		return this;
	}
	
	public Vec2d dot(double x, double y){
		this.x = round(this.x * x);
		this.y = round(this.y * y);
		return this;
	}
	
	public long cross(Vec2d vector){
		return this.x * vector.y - this.y * vector.x;
	}
	
	public Vec2d mul(long scalar){
		x *= scalar;
		y *= scalar;
		return this;
	}
	
	public Vec2d mul(double scalar){
		x = (long)(x * scalar);
		y = (long)(y * scalar);
		return this;
	}
	
	public Vec2d div(long scalar){
		x = (long)(x / scalar);
		y = (long)(y / scalar);
		return this;
	}
	
	public Vec2d div(double scalar){
		x = (long)(x / scalar);
		y = (long)(y / scalar);
		return this;
	}
	
	public long len(){
		return round(Math.sqrt(x * x + y * y)+ 0.5d);
	}
	
	public long len2(){
		return x * x + y * y;
	}
	
	public Vec2d negative() {
		return new Vec2d(-x,-y);
		
	}
	
	public Vec2d normalize(){
		return this.div(this.len() / 1000.0);
	}
	
	public boolean up(){
		return y < 0;
	}
	
	public boolean down(){
		return y > 0;
	}
	
	public boolean left(){
		return x < 0;
	}
	
	public boolean right(){
		return x > 0;
	}

	
	//sets the vector to a fictitious Void value
	public void setVoid(){
		x = -478643;  //ISVOID SPELLED OUT ON A PHONE
		y = -478643;
	}
	
	public boolean isVoid(){
		return (x==-478643&&y==-478643);
	}
	
}