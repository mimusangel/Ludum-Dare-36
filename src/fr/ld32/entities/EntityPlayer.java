package fr.ld32.entities;

import org.lwjgl.opengl.GL11;

import fr.ld32.AABB;
import fr.ld32.LD36;
import fr.mimus.jbasicgl.graphics.Color4f;
import fr.mimus.jbasicgl.graphics.Mesh;
import fr.mimus.jbasicgl.graphics.Shaders;
import fr.mimus.jbasicgl.graphics.Texture;
import fr.mimus.jbasicgl.input.Keyboard;
import fr.mimus.jbasicgl.maths.Mat4;
import fr.mimus.jbasicgl.maths.Vec2;

public class EntityPlayer extends Entity {
	long time;
	float anim;
	float dir;
	public EntityPlayer(Vec2 pos)
	{
		super(pos);
	}

	public void createEntity()
	{
		mesh = new Mesh(6);
		mesh.addVertices(0, 0).addColor(Color4f.WHITE).addTexCoord2f(0, 0);
		mesh.addVertices(32, 0).addColor(Color4f.WHITE).addTexCoord2f(0.1f, 0);
		mesh.addVertices(32, 64).addColor(Color4f.WHITE).addTexCoord2f(0.1f, 1);
		mesh.addVertices(0, 64).addColor(Color4f.WHITE).addTexCoord2f(0, 1);
		mesh.buffering();
		texture = Texture.FileTexture("rsc/images/playerWalk.png", false);
		time = System.currentTimeMillis();
		anim = 0;
		dir = 1;
	}

	public boolean entityAlive()
	{
		return true;
	}

	public void update(int tick, double elapse)
	{
		Keyboard keyboard = LD36.getInstance().win.getKeyboard();
		boolean moving = false;
		if (keyboard.isDown(Keyboard.KEY_A))
		{
			pos.x -= 2;
			if (System.currentTimeMillis() - time >= 1000)
				time = System.currentTimeMillis();
			moving = true;
			dir = -1;
		}
		if (keyboard.isDown(Keyboard.KEY_D))
		{
			pos.x += 2;
			if (System.currentTimeMillis() - time >= 1000)
				time = System.currentTimeMillis();
			moving = true;
			dir = 1;
		}
		if (keyboard.isDown(Keyboard.KEY_W) && this.inFloor)
		{
			gravity = -16;
			this.inFloor = false;
		}
		if (this.inFloor && moving)
		{
			if (System.currentTimeMillis() - time < 1000)
				anim = (float) Math.floor((float)(System.currentTimeMillis() - time) / 100);
			else
				anim = 0;
		}
		else if (moving)
			anim = 3;
		else
			anim = 0;
		super.update(tick, elapse);
	}
	
	public void render(Shaders shader, Vec2 offset)
	{
		if (texture != null)
			texture.bind();
		if (mesh != null)
		{
			Vec2 matPos = pos.copy().add(offset);
			matPos.x = (float) Math.floor(matPos.x);
			matPos.y = (float) Math.floor(matPos.y);
			if (dir < 0)
				matPos.x += 32f;
			shader.setUniformMat4f("m_view", Mat4.multiply(Mat4.translate(matPos), Mat4.scale(dir, 1, 1)));
			shader.setUniform1f("anim", anim / 10.0f);
			mesh.render(GL11.GL_QUADS);
		}
		else
			shader.setUniformMat4f("m_view", Mat4.identity());
		Texture.unbind();
	}

	public Vec2 getOffset()
	{
		Vec2 offset = new Vec2(360 - 16, 202 - 32);
		offset.sub(pos);
		return (offset);
	}

	public AABB getBox() {
		return new AABB((int)pos.x, (int)pos.y + 16, 32, 48);
	}
}
