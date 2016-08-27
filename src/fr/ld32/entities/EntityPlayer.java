package fr.ld32.entities;

import org.lwjgl.opengl.GL11;

import fr.ld32.AABB;
import fr.ld32.Game;
import fr.ld32.LD36;
import fr.ld32.utils.Res;
import fr.mimus.jbasicgl.graphics.Color4f;
import fr.mimus.jbasicgl.graphics.Mesh;
import fr.mimus.jbasicgl.graphics.Shaders;
import fr.mimus.jbasicgl.graphics.Texture;
import fr.mimus.jbasicgl.input.Keyboard;
import fr.mimus.jbasicgl.maths.Mat4;
import fr.mimus.jbasicgl.maths.Vec2;

public class EntityPlayer extends Entity {
	long time;
	Vec2 anim;
	float dir;
	Mesh hand;
	Texture texHand;
	
	long repeatSpaceTime;
	Entity grab;
	
	public EntityPlayer(Vec2 pos)
	{
		super(pos);
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
		texture = Res.images.get("playerWalk");
		texHand = Res.images.get("playerHand");
		time = System.currentTimeMillis();
		repeatSpaceTime = System.currentTimeMillis();
		anim = new Vec2();
		dir = 1;
		grab = null;
	}

	public boolean entityAlive()
	{
		return true;
	}

	public void update(Game game, int tick, double elapse)
	{
		Keyboard keyboard = LD36.getInstance().win.getKeyboard();
		boolean moving = false;
		if (keyboard.isDown(Keyboard.KEY_A))
		{
			pos.x -= 2;
			if (System.currentTimeMillis() - time >= 800)
				time = System.currentTimeMillis();
			moving = true;
			dir = -1;
		}
		if (keyboard.isDown(Keyboard.KEY_D))
		{
			pos.x += 2;
			if (System.currentTimeMillis() - time >= 800)
				time = System.currentTimeMillis();
			moving = true;
			dir = 1;
		}
		if (keyboard.isDown(Keyboard.KEY_W) && this.inFloor)
		{
			gravity = -12;
			this.inFloor = false;
		}
		if (keyboard.isDown(Keyboard.KEY_SPACE) && System.currentTimeMillis() - repeatSpaceTime > 150)
		{
			repeatSpaceTime = System.currentTimeMillis();
			if (grab == null)
				grab = game.checkGrab(this);
			else
				grab = null;
		}
		if (this.inFloor && moving)
		{
			if (System.currentTimeMillis() - time < 800)
				anim.x = (float) Math.floor((float)(System.currentTimeMillis() - time) / 100) / 8f;
			else
				anim.x = 0;
		}
		else if (moving)
			anim.x = 2f / 8f;
		else
			anim.x = 0;
		super.update(game, tick, elapse);
		if (grab != null)
		{
			AABB box = getBox();
			AABB grabBox = grab.getBox();
			grab.pos.y = (box.y + box.h) - grabBox.h;
			grab.pos.x = (box.x + box.w / 2) - (grabBox.w / 2);
			grab.gravity = 0;
		}
	}
	
	public void render(Shaders shader, Vec2 offset)
	{
		if (mesh != null)
		{
			Vec2 matPos = pos.copy().add(offset);
			matPos.x = (float) Math.floor(matPos.x);
			matPos.y = (float) Math.floor(matPos.y);
			if (dir < 0)
				matPos.x += 32f;
			shader.setUniformMat4f("m_view", Mat4.multiply(Mat4.translate(matPos), Mat4.scale(dir, 1, 1)));
			shader.setUniform2f("anim", anim);
			texture.bind();
			mesh.render(GL11.GL_QUADS);
			texHand.bind();
			if (dir < 0)
				matPos.add(-5, 16 - (int)(anim.x * 8) % 2);
			else
				matPos.add(5, 16 - (int)(anim.x * 8) % 2);
			shader.setUniformMat4f("m_view", Mat4.multiply(Mat4.translate(matPos), Mat4.scale(dir, 1, 1)));
			shader.setUniform2f("anim", new Vec2());
			hand.render(GL11.GL_QUADS);
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
}
