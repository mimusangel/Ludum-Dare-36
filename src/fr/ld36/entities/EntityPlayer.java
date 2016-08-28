package fr.ld36.entities;

import org.lwjgl.opengl.GL11;

import fr.ld36.AABB;
import fr.ld36.Game;
import fr.ld36.LD36;
import fr.ld36.entities.spe.IMovable;
import fr.ld36.utils.Res;
import fr.mimus.jbasicgl.graphics.Color4f;
import fr.mimus.jbasicgl.graphics.Mesh;
import fr.mimus.jbasicgl.graphics.Shaders;
import fr.mimus.jbasicgl.graphics.Texture;
import fr.mimus.jbasicgl.input.Keyboard;
import fr.mimus.jbasicgl.input.Mouse;
import fr.mimus.jbasicgl.maths.Mat4;
import fr.mimus.jbasicgl.maths.Vec2;
import fr.mimus.jbasicgl.maths.Vec3;

public class EntityPlayer extends Entity {
	long time;
	Vec2 anim;
	float dir;
	boolean reversed;
	float rotateHand;

	Texture texHand;
	Texture texGrab;
	Texture texHandGrab;
	Mesh hand;
	Mesh handGrab;
	Mesh test;

	public Entity grab;
	boolean debugMode;

	int stamina;
	long staTime;
	
	public EntityPlayer(Vec2 pos)
	{
		super(pos);
		anim = new Vec2();
		debugMode = false;
		life = 3;
		stamina = 100;
		staTime = System.currentTimeMillis();
		reversed = false;
	}

	public void createEntity()
	{
		mesh = new Mesh(4);
		mesh.addVertices(0, 0).addColor(Color4f.WHITE).addTexCoord2f(0, 0);
		mesh.addVertices(32, 0).addColor(Color4f.WHITE).addTexCoord2f(1f / 8f, 0);
		mesh.addVertices(32, 64).addColor(Color4f.WHITE).addTexCoord2f(1f / 8f, 1);
		mesh.addVertices(0, 64).addColor(Color4f.WHITE).addTexCoord2f(0, 1);
		mesh.buffering();
		hand = new Mesh(4);
		hand.addVertices(0, 0).addColor(Color4f.WHITE).addTexCoord2f(0, 0);
		hand.addVertices(32, 0).addColor(Color4f.WHITE).addTexCoord2f(1f, 0);
		hand.addVertices(32, 32).addColor(Color4f.WHITE).addTexCoord2f(1f, 1);
		hand.addVertices(0, 32).addColor(Color4f.WHITE).addTexCoord2f(0, 1);
		hand.buffering();
		handGrab = new Mesh(4);
		handGrab.addVertices(0, 0).addColor(Color4f.WHITE).addTexCoord2f(0, 0);
		handGrab.addVertices(38, 0).addColor(Color4f.WHITE).addTexCoord2f(1f, 0);
		handGrab.addVertices(38, 32).addColor(Color4f.WHITE).addTexCoord2f(1f, 1);
		handGrab.addVertices(0, 32).addColor(Color4f.WHITE).addTexCoord2f(0, 1);
		handGrab.buffering();
		texture = Res.images.get("playerWalk");
		texHand = Res.images.get("playerHand");
		texGrab = Res.images.get("playerBox");
		texHandGrab = Res.images.get("boxHands");
		time = System.currentTimeMillis();
		dir = 1;
		grab = null;
		test = new Mesh(2);
		test.addVertices(0, 0).addColor(Color4f.RED).addTexCoord2f(0, 0);
		test.addVertices(40, 0).addColor(Color4f.RED).addTexCoord2f(1f, 0);
		test.buffering();
	}

	public boolean entityAlive()
	{
		return true;
	}

	public void update(Game game, int tick, double elapse)
	{
		Keyboard keyboard = LD36.getInstance().win.getKeyboard();
		Mouse mouse = LD36.getInstance().win.getMouse();
		boolean moving = false;
		if (mouse.getX() < LD36.getInstance().win.getWidth() / 2)
			dir = -1;
		else
			dir = 1;
		float w = LD36.getInstance().win.getWidth() / 2f;
		float h = LD36.getInstance().win.getHeight() / 2f;
		if (dir < 0)
			rotateHand = (float) Math.atan2(h - mouse.getY(), w - mouse.getX());
		else
			rotateHand = (float) Math.atan2(h - mouse.getY(), mouse.getX() - w);
		if (rotateHand < -0.8f)
			rotateHand = -0.8f;
		if (rotateHand > 1.4f)
			rotateHand = 1.4f;
		if (keyboard.isDown(Keyboard.KEY_A))
		{
			pos.x -= 2;
			if (System.currentTimeMillis() - time >= 800)
				time = System.currentTimeMillis();
			moving = true;
			reversed = dir == 1;
		}
		if (keyboard.isDown(Keyboard.KEY_D))
		{
			pos.x += 2;
			if (System.currentTimeMillis() - time >= 800)
				time = System.currentTimeMillis();
			moving = true;
			reversed = dir == -1;
		}
		if (keyboard.isDown(Keyboard.KEY_W) && this.inFloor)
		{
			gravity = -12;
			this.inFloor = false;
		}
		if (keyboard.isDown(Keyboard.KEY_S) && this.inFloor)
		{
			pos.y += 8;
			gravity = 0;
			this.inFloor = false;
		}
		if (keyboard.isPress(Keyboard.KEY_SPACE))
		{
			if (grab == null)
				grab = game.checkGrab(this);
			else
			{
				((IMovable)grab).move(new Vec2(8 * dir, -8));
				grab = null;
			}
		}
		if (keyboard.isPress(Keyboard.KEY_E) && grab == null)
			game.action(this);
		
		if (keyboard.isPress(Keyboard.KEY_F3))
		{
			debugMode = !debugMode;
		}
		if (this.inFloor && moving)
		{
			if (System.currentTimeMillis() - time < 800)
				anim.x = (float) Math.floor((float)(System.currentTimeMillis() - time) / 100) / 8f;
			else
				anim.x = 0;
			if (reversed)
				anim.x  = 1f - anim.x ;
		}
		else if (moving)
			anim.x = 2f / 8f;
		else
			anim.x = 0;
		super.update(game, tick, elapse);
		
		if (System.currentTimeMillis() - staTime >= 250)
		{
			staTime = System.currentTimeMillis();
			if (grab != null)
			{
				stamina--;
				if (stamina < 0)
				{
					stamina = 0;
					((IMovable)grab).move(new Vec2(0, -8));
					grab = null;
				}
				System.out.println("stamina: " + stamina);
			}
			else
			{
				stamina++;
				if (stamina > 100)
					stamina = 100;
			}
			
		}
		if (grab != null)
		{
			Vec2 v = pos.copy().sub(grab.pos).add(0, 16f - (int)(anim.x * 8) % 2);
			if (v.length() > 30)
			{
				((IMovable)grab).move(new Vec2(4 * dir, -16));
				grab = null;
			}
			else
			{
				((IMovable)grab).move(v);
				grab.gravity = 0;
			}
		}
	}
	
	public Vec2 getLightPos()
	{
		Vec3 light = new Vec3();
		light.set(-15, 28 - (int)(anim.x * 8) % 2, 0);
		light.rotateZ(rotateHand);
		Vec2 p = pos.copy();
		if (dir < 0)
			p.add(-15, 28 - (int)(anim.x * 8) % 2);
		else
			p.add(15, 28 - (int)(anim.x * 8) % 2);
		if (dir < 0)
			return (p.add(light.xy()));
		return (p.add(-light.x, light.y));
	}
	
	public void renderGrab(Shaders shader)
	{
		if (grab != null)
		{
			texHandGrab.bind();
			Vec2 matPos = grab.pos.copy().add(-3f, 8f);
			if (dir < 0)
				matPos.x += 38f;
			shader.setUniform2f("anim", new Vec2());
			shader.setUniformMat4f("m_view", Mat4.multiply(Mat4.translate(matPos), Mat4.scale(dir, 1, 1)));
			handGrab.render(GL11.GL_QUADS);
		}
	}
	
	public void render(Shaders shader)
	{
		if (mesh != null)
		{
			Vec2 matPos = pos.copy();
			if (dir < 0)
				matPos.x += 32f;
			shader.setUniformMat4f("m_view", Mat4.multiply(Mat4.translate(matPos), Mat4.scale(dir, 1, 1)));
			shader.setUniform2f("anim", anim);
			if (grab != null)
			{
				texGrab.bind();
				mesh.render(GL11.GL_QUADS);
			}
			else
			{
				texture.bind();
				mesh.render(GL11.GL_QUADS);
				texHand.bind();
				if (dir < 0)
					matPos.add(-15, 28 - (int)(anim.x * 8) % 2);
				else
					matPos.add(15, 28 - (int)(anim.x * 8) % 2);
				Mat4 m0 = Mat4.multiply(Mat4.rotateZf(rotateHand), Mat4.translate(new Vec2(-10, -12)));
				Mat4 m1 = Mat4.multiply(Mat4.translate(matPos), Mat4.scale(dir, 1, 1));
				shader.setUniformMat4f("m_view", Mat4.multiply(m1, m0));
				shader.setUniform2f("anim", new Vec2());
				hand.render(GL11.GL_QUADS);
				if (debugMode)
				{
					m0 = Mat4.multiply(Mat4.rotateZf(rotateHand), Mat4.translate(new Vec2(15, 7.5f)));
					shader.setUniformMat4f("m_view", Mat4.multiply(m1, m0));
					shader.setUniform1i("disableTexture", 1);
					GL11.glLineWidth(3f);
					test.render(GL11.GL_LINES);
					GL11.glLineWidth(1f);
					shader.setUniform1i("disableTexture", 0);
				}
			}
			
		}
		else
		{
			shader.setUniformMat4f("m_view", Mat4.identity());
			createEntity();
		}
		Texture.unbind();
	}

	public Vec2 getOffset()
	{
		Vec2 offset = new Vec2(360 - 16, 202 - 32);
		offset.sub(pos);
		return (offset);
	}

	public AABB getBox() {
		return new AABB((int)pos.x + 4, (int)pos.y + 16, 24, 48);
	}

	public void giveDamage(Entity src, int dmg) {}
	
	public void giveDamage(int x, int y, int dmg)
	{
		if (System.currentTimeMillis() - lifeTime < 1000)
			return;
		life--;
		lifeTime = System.currentTimeMillis();
		if (life <= 0)
		{
			LD36.getInstance().gameOver();
		}
		else
		{
			pos = LD36.getInstance().game.map.spawn.copy();
			gravity = 0;
		}
	}
}