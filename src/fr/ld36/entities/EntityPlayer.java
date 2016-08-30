package fr.ld36.entities;

import org.lwjgl.opengl.GL11;

import fr.ld36.AABB;
import fr.ld36.Game;
import fr.ld36.LD36;
import fr.ld36.entities.spe.IMovable;
import fr.ld36.items.*;
import fr.ld36.map.Map;
import fr.ld36.utils.Audio;
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
	Mesh backpack;
	Mesh item;
	Mesh test;

	Vec2 grabOrigin;
	public Entity grab;
	int stamina;
	long staTime;
	int money;
	Inventory inv;
	public int selectedSlot = 0;
	Audio sfxDead;
	double timePlay;
	
	public boolean debugMode;
	public boolean editMode;
	public boolean noclip;
	public boolean gridAlign;
	
	public boolean isAttacking = false;
	float startAngle = 0;

	Audio hit;
	//Argent pour augment d'un la taille du sac
	int bagIncrement = 200;

	public EntityPlayer(Vec2 pos)
	{
		super(pos);
		anim = new Vec2();
		debugMode = false;
		editMode = false;
		noclip = false;
		gridAlign = true;
		life = 3;
		stamina = 100;		
		staTime = System.currentTimeMillis();
		reversed = false;
		timePlay = 0;
		money = 0;
		inv = new Inventory(this);
		inv.addItem(new ItemFlashlight());
		inv.addItem(new ItemLoupe());
		sfxDead = Audio.list.get("rsc/sounds/dead.wav");
		hit = Audio.list.get("rsc/sounds/hit2.wav");		
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
		
		backpack = new Mesh(4);
		backpack.addVertices(0, 0).addColor(Color4f.WHITE).addTexCoord2f(0, 0);
		backpack.addVertices(32, 0).addColor(Color4f.WHITE).addTexCoord2f(1f / 5f, 0);
		backpack.addVertices(32, 32).addColor(Color4f.WHITE).addTexCoord2f(1f / 5f, 1);
		backpack.addVertices(0, 32).addColor(Color4f.WHITE).addTexCoord2f(0, 1);
		backpack.buffering();
		
		item = new Mesh(4);
		item.addVertices(0, 0).addColor(Color4f.WHITE).addTexCoord2f(0, 0);
		item.addVertices(32, 0).addColor(Color4f.WHITE).addTexCoord2f(1f, 0);
		item.addVertices(32, 32).addColor(Color4f.WHITE).addTexCoord2f(1f, 1);
		item.addVertices(0, 32).addColor(Color4f.WHITE).addTexCoord2f(0, 1);
		item.buffering();
		
		// Test
		test = new Mesh(1);
		test.addVertices(0, 0).addColor(Color4f.RED);
		test.buffering();
	}

	public boolean entityAlive()
	{
		return true;
	}

	public void update(Game game, int tick, double elapse)
	{
		timePlay += elapse;
		LD36 ld36 = LD36.getInstance();
		Keyboard keyboard = ld36.win.getKeyboard();
		Mouse mouse = ld36.win.getMouse();
		boolean moving = false;
		float w = ((pos.x + getOffset().x) + 16) * ld36.getScaleX();
		if (mouse.getX() < w)
			dir = -1;
		else
			dir = 1;
		
		if(isAttacking){
			rotateHand -= 20 * elapse;
			if(rotateHand < startAngle - Math.PI*2){
				isAttacking = false;
				if(inv.isItem(selectedSlot) && inv.getItem(selectedSlot) instanceof ItemLife){
					inv.removeItem(selectedSlot);
					life++;
				}
			}
			if (tick % 3 == 0)
			{
				if(inv.isItem(selectedSlot) && inv.getItem(selectedSlot) instanceof Weapon)
					game.giveDamage(this, getImpact(), (int)((Weapon)inv.getItem(selectedSlot)).getDamage());
				else
					game.giveDamage(this, getImpact(), 1);
			}
		}
		else
		{
			float h = ((pos.y + getOffset().y) + 32) * ld36.getScaleY();
			if (dir < 0)
				rotateHand = (float) Math.atan2(h - mouse.getY(), w - mouse.getX());
			else
				rotateHand = (float) Math.atan2(h - mouse.getY(), mouse.getX() - w);
			if (rotateHand < -0.8f)
				rotateHand = -0.8f;
			if (rotateHand > 1.4f)
				rotateHand = 1.4f;
		}
		if (keyboard.isDown(Keyboard.KEY_A))
		{
			pos.x -= 2 + (noclip ? 2 : 0);
			if (System.currentTimeMillis() - time >= 800)
				time = System.currentTimeMillis();
			moving = true;
			reversed = dir == 1;
		}
		if (keyboard.isDown(Keyboard.KEY_D))
		{
			pos.x += 2 + (noclip ? 2 : 0);
			if (System.currentTimeMillis() - time >= 800)
				time = System.currentTimeMillis();
			moving = true;
			reversed = dir == -1;
		}
		if (keyboard.isDown(Keyboard.KEY_W) && (this.inFloor || noclip))
		{
			if (noclip)
				pos.y -= 4;
			else
			{
				velocity.y = -12;
				this.inFloor = false;
				Audio.list.get("rsc/sounds/jump.wav").play();
			}
		}
		if (keyboard.isDown(Keyboard.KEY_S) && (this.inFloor || noclip))
		{
			if (noclip)
				pos.y += 4;
			else
			{
				pos.y += 8;
				velocity.y = 0;
				this.inFloor = false;
			}
		}
		if (keyboard.isPress(Keyboard.KEY_SPACE))
		{
			if (grab == null)
			{
				grab = game.checkGrab(this);
				if (grab != null)
					grabOrigin = grab.pos.copy();
			}	
			else
			{
				grab.velocity.add(new Vec2(8 * dir, -4));
				grab = null;
			}
		}
		if (keyboard.isPress(Keyboard.KEY_E) && grab == null)
			game.action(this);
		
		if (keyboard.isPress(Keyboard.KEY_F3))
		{
			debugMode = !debugMode;
			if (!debugMode)
			{
				editMode = false;
				noclip = false;
			}
		}
		if (debugMode)
		{
			if (keyboard.isPress(Keyboard.KEY_F8))
			{
				editMode = !editMode;
				if (!editMode)
					noclip = false;
			}
			if (editMode && keyboard.isPress(Keyboard.KEY_F5))
				noclip = !noclip;
			if (keyboard.isPress(Keyboard.KEY_F6))
				gridAlign = !gridAlign;
			if (editMode)
			{
				if (keyboard.isPress(Keyboard.KEY_F2))
				{
					ld36.game.map.adminEditSave();
				}
			}
		}
		if(mouse.isDown(Mouse.MOUSE_BUTTON_1)){
			attack();
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
		{
			anim.x = 2f / 8f;
		}
		else
		{
			anim.x = 0;
		}
		if (!noclip)
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
					grab.velocity.add(new Vec2(0, -4));
					grab = null;
				}
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
				grab.velocity.add(new Vec2(4 * dir, -4));
				grab = null;
			}
			else
			{
				AABB box = grab.getBox();
				int animx = (int)(anim.x * 8);
				int boxDisplacement = animx==2||animx==6?1:
					animx==3||animx==5?2:
					animx==4?3:0;
				((IMovable)grab).move(pos.copy().add(16, 32 + boxDisplacement).sub(box.w / 2, box.h / 2));
				grab.velocity.y = 0;
			}
		}
		if(!editMode && mouse.getWheelY() != 0){
			selectedSlot = selectedSlot == 0? 1 : 0;
		}
		if(keyboard.isDown(Keyboard.KEY_1)){
			selectedSlot = 0;
		}
		if(keyboard.isDown(Keyboard.KEY_2)){
			selectedSlot = 1;
		}
		if(keyboard.isDown(Keyboard.KEY_Q)){
			inv.dropItem(selectedSlot);
		}
	}
	
	public void renderGrab(Shaders shader)
	{
		if (grab != null)
		{
			texHandGrab.bind();
			int animx = (int)(anim.x * 8);
			int boxDisplacement = animx==2||animx==6?1:
				animx==3||animx==5?2:
				animx==4?3:0;
			AABB box = grab.getBox();
			Vec2 grabPos = pos.copy().add(16, 32 + boxDisplacement).sub(box.w / 2, box.h / 2);
			Vec2 matPos = grabPos.add(-3f, 8f);
			matPos.x = (float) Math.floor(matPos.x);
			matPos.y = (float) Math.floor(matPos.y);
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
				
				//Backpack rendering
				int bagStep = money / bagIncrement;
				if(bagStep > 4)
					bagStep = 4;
				
				shader.setUniformMat4f("m_view", Mat4.multiply(Mat4.translate(matPos.copy().add(0,12 - (anim.x * 8 % 2))), Mat4.scale(dir, 1, 1)));
				shader.setUniform2f("anim", bagStep/5f, 0);
				Res.images.get("backpack").bind();
				backpack.render(GL11.GL_QUADS);
				Texture.unbind();
				shader.setUniform2f("anim", 0, 0);
				
				//Item in hand rendering
				if(inv.isItem(selectedSlot)){					
					Item itemSlot = inv.getItem(selectedSlot);
					Mat4 i0 = Mat4.multiply(Mat4.rotateZf(rotateHand), Mat4.translate(new Vec2(7, 9)));
					Mat4 i1 = Mat4.multiply(Mat4.translate(matPos.copy().add(15 * dir, 28 - (int)(anim.x * 8) % 2)), Mat4.scale(dir, 1, 1));
					Mat4 i = Mat4.multiply(Mat4.rotateZ(itemSlot.rotate), Mat4.translate(itemSlot.handOffset));
					shader.setUniformMat4f("m_view",Mat4.multiply(i1, Mat4.multiply(i0, i)));
					
					itemSlot.getTexture().bind();
					item.render(GL11.GL_QUADS);
					Texture.unbind();
				}
				
				//Hand rendering
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
					Vec2 f = getImpact();
					shader.setUniformMat4f("m_view", Mat4.translate(f));
					shader.setUniform1i("disableTexture", 1);
					GL11.glPointSize(3f);
					test.render(GL11.GL_POINTS);
					GL11.glPointSize(1f);
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
	
	public Vec2 getImpact()
	{
		if(inv.isItem(selectedSlot)){					
			Item itemSlot = inv.getItem(selectedSlot);
			if (itemSlot instanceof Weapon)
			{
				Weapon weapon = (Weapon)itemSlot;
				Vec2 f = pos.copy().add(15 - dir, 28 - (anim.x * 8 % 2));
				Vec3 r = new Vec3(10, 7, 0);
				Vec3 w = new Vec3(weapon.getImpact(), 0);
				w.rotateZ(-weapon.rotate);
				r.add(w);
				r.rotateZ(-rotateHand);
				if (dir < 0)
					r.x = -r.x;
				f.add(r.xy());
				return (f);
			}
		}
		Vec2 f = pos.copy().add(15 - dir, 28 - (anim.x * 8 % 2));
		Vec3 r = new Vec3(24, 7, 0);
		r.rotateZ(-rotateHand);
		if (dir < 0)
			r.x = -r.x;
		f.add(r.xy());
		return (f);
	}
	
	public boolean viewSystem()
	{
		if(inv.isItem(selectedSlot))
			return (inv.getItem(selectedSlot) instanceof ItemLoupe);
		return (false);
	}
	
	public Item getItemSelect()
	{
		if(inv.isItem(selectedSlot))
			return (inv.getItem(selectedSlot));
		return (null);
	}

	public Vec2 getOffset()
	{
		Game game = LD36.getInstance().game;
		if (game == null)
			return (new Vec2());
		Map map = game.map;
		Vec2 offset = new Vec2();
		if (pos.x >= (360 - 16))
		{
			offset.x = (360 - 16) - pos.x;
			float mx = map.getWidth() * 32 - 720f;
			if (offset.x < -mx)
				offset.x = -mx;
		}
		if (pos.y >= (202 - 32))
		{
			offset.y = (202 - 32) - pos.y;
			float my = map.getHeight() * 32 - 405f;
			if (offset.y < -my)
				offset.y = -my;
		}
		return (offset);
	}

	public AABB getBox() {
		return new AABB((int)pos.x + 4, (int)pos.y + 16, 24, 48);
	}

	public boolean isTouch()
	{
		return (System.currentTimeMillis() - lifeTime < 10000);
	}
	
	public void giveDamage(Entity src, int dmg)
	{
		if (noclip || editMode)
			return;
		if (isTouch())
			life -= dmg;
		else
			lifeTime = System.currentTimeMillis();
		Vec2 v = src.pos.copy().sub(pos);
		v.normalise();
		this.velocity.x = -v.x * 5;
		this.velocity.y -= 4;
		hit.play();
		if (life <= 0)
		{
			endGame();
		}
	}
	
	public void giveDamage(int x, int y, int dmg)
	{
		if (noclip || editMode)
			return;
		life -= dmg;
		if (life <= 0)
		{
			endGame();
		}
		else
		{
			sfxDead.setVolume(0.6f).play();
			Entity e = new EntityBones(pos.copy());
			e.velocity.y = -4;
			LD36.getInstance().game.addEntity(e);
			pos = LD36.getInstance().game.map.spawn.copy();
			velocity.y = 0;
			if (grab != null)
			{
				grab.pos = grabOrigin;
				grab = null;
				grabOrigin = null;
			}
		}
	}
	
	public int getStamina()
	{
		return (stamina);
	}
	
	public void addMoney(int m){
		money += m;
	}
	
	public int getMoney(){
		return money;
	}
	
	public Inventory getInv(){
		return inv;
	}

	public Entity copy()
	{
		return new EntityPlayer(pos.copy());
	}
	
	public boolean lookRight(){
		return dir == 1;
	}
	
	public void attack(){
		if(!isAttacking)
		{
			isAttacking = true;
			startAngle = rotateHand;
		}
	}

	public void endGame()
	{
		LD36.getInstance().endGame(money + life * 1000, timePlay);
	}
}
