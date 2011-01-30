package voldaran.com.Upright;

public class GameObject {

	public float top;
	public float left;
	public float right;
	public float bottom;
	
	public Vector2D topLeft;
	public Vector2D bottomRight;
	
	public Vector2D pos;
	public Extent extent;
	
	
	public GameObject (Vector2D v, Extent extent) {
			pos = v;
			
			top = pos.y - extent.y;
			left = pos.x - extent.x;
			right = pos.x + extent.x;
			bottom = pos.y + extent.y;
			
			topLeft = new Vector2D(left,top);
			bottomRight = new Vector2D(bottom,right);
	}
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	//position
	
	//top
	//left
	//right
	//bottom
	
	//topleft
	//bottomright
	//extent

	
}
