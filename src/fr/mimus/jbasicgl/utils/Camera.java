package fr.mimus.jbasicgl.utils;
import fr.mimus.jbasicgl.graphics.Shaders;
import fr.mimus.jbasicgl.input.Keyboard;
import fr.mimus.jbasicgl.input.Mouse;
import fr.mimus.jbasicgl.maths.Mat4;
import fr.mimus.jbasicgl.maths.Vec3;

public class Camera
{
	public Vec3	pos = new Vec3();
	public Vec3	rot = new Vec3();
	public float	moveSpeed;
	public float	rotSpeed;
	
	public Camera()
	{
		this(0.075f, 0.75f);
	}
	
	public Camera(float moveSpeed, float rotSpeed)
	{
		this.moveSpeed = moveSpeed;
		this.rotSpeed = rotSpeed;
	}
	
	public void render(Shaders shader, String uniformName)
	{
		shader.setUniformMat4f(uniformName, Mat4.multiply(
			Mat4.rotate(rot.x, rot.y, rot.z),
			Mat4.translate(-pos.x, -pos.y, -pos.z))
		);
	}
	
	public void update(Keyboard keyboard, Mouse mouse)
	{
		if (mouse.isDown(Mouse.MOUSE_BUTTON_LEFT))
			mouse.setGrabbed(true);
		if (keyboard.isDown(Keyboard.KEY_ESCAPE))
			mouse.setGrabbed(false);
		
		if (mouse.isGrabbed())
		{
			rot.x += mouse.getDY() * rotSpeed;
			rot.y += mouse.getDX() * rotSpeed;
			if (rot.x > 90)
				rot.x = 90;
			if (rot.x < -90)
				rot.x = -90;
		}
		else
			return ;
		
		if (keyboard.isDown(Keyboard.KEY_W))
		{
			Vec3 forward = forward(rot);
			pos.add(forward.mul(-moveSpeed));
		}
		if (keyboard.isDown(Keyboard.KEY_S))
		{
			Vec3 forward = forward(rot);
			pos.add(forward.mul(moveSpeed));
		}
		if (keyboard.isDown(Keyboard.KEY_A))
		{
			Vec3 straf = straf(rot);
			pos.add(straf.mul(moveSpeed));
		}
		if (keyboard.isDown(Keyboard.KEY_D))
		{
			Vec3 straf = straf(rot);
			pos.add(straf.mul(-moveSpeed));
		}
		if (keyboard.isDown(Keyboard.KEY_SPACE))
			pos.y += moveSpeed;
		if (keyboard.isDown(Keyboard.KEY_LEFT_SHIFT))
			pos.y -= moveSpeed;
	}
	
	public static Vec3 forward(Vec3 rot)
    {
		Vec3 r = new Vec3();
    	float cosY = (float) Math.cos(Math.toRadians(rot.y - 90));
		float sinY = (float) Math.sin(Math.toRadians(rot.y - 90));
		float cosP = (float) Math.cos(Math.toRadians(-rot.x));
		float sinP = (float) Math.sin(Math.toRadians(-rot.x));
		
		r.x = -(cosY * cosP);
		r.y = -sinP;
		r.z = sinY * cosP;
		r.normalize();
		return (r);
    }
    
    public static Vec3 straf(Vec3 rot)
    {
		Vec3 r = new Vec3();
    	float cosY = (float) Math.cos(Math.toRadians(rot.y));
		float sinY = (float) Math.sin(Math.toRadians(rot.y));
		float cosP = (float) Math.cos(Math.toRadians(-rot.x));
		
		r.x = -(cosY * cosP);
		r.z = sinY * cosP;
		r.normalize();
		return (r);
    }
}
