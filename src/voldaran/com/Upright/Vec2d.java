package voldaran.com.Upright;

public class Vec2d {
	public long x;
	public long y;
	
	public Vec2d(){
		x = 0;
		y = 0;
	}
	
	public Vec2d(long x, long y){
		this.x = x;
		this.y = y;
	}
	
	public Vec2d(float x, float y){
		this(round(x), round(y));
	}
	
	public Vec2d(Vec2d vector){
		this(vector.x, vector.y);
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
	
	@Override public String toString(){
		return x + "," + y;
	}
	
	public Vec2d set(long x, long y){
		this.x = x;
		this.y = y;
		return this;
	}
	
	public Vec2d set(float x, float y){
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
	
	public Vec2d add(float scalar){
		x += round(scalar);
		y += round(scalar);
		return this;
	}
	
	public Vec2d add(float x, float y){
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
	
	public Vec2d sub(long x, long y){
		this.x -= x;
		this.y -= y;
		return this;
	}
	
	public Vec2d sub(float scalar){
		x -= round(scalar);
		y -= round(scalar);
		return this;
	}
	
	public Vec2d sub(float x, float y){
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
	
	public Vec2d dot(float x, float y){
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
	
	public Vec2d mul(float scalar){
		x = round(x * scalar);
		y = round(y * scalar);
		return this;
	}
	
	public Vec2d div(long scalar){
		x = round(x / scalar);
		y = round(y / scalar);
		return this;
	}
	
	public Vec2d div(float scalar){
		x = round(x / scalar);
		y = round(y / scalar);
		return this;
	}
	
	public long len(){
		return round(Math.sqrt(x * x + y * y)+ 0.5d);
	}
	
	public long len2(){
		return x * x + y * y;
	}
	
	public Vec2d normalize(){
		return this.div(this.len());
	}
}