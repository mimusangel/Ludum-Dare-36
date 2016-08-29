package fr.ld36.entities;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import fr.ld36.AABB;
import fr.ld36.Game;
import fr.ld36.LD36;
import fr.ld36.items.Item;
import fr.ld36.items.Weapon;
import fr.ld36.render.Animation;
import fr.ld36.utils.Audio;
import fr.ld36.utils.Res;
import fr.mimus.jbasicgl.graphics.Color4f;
import fr.mimus.jbasicgl.graphics.Mesh;
import fr.mimus.jbasicgl.graphics.Shaders;
import fr.mimus.jbasicgl.graphics.Texture;
import fr.mimus.jbasicgl.maths.Mat4;
import fr.mimus.jbasicgl.maths.Vec2;
import fr.mimus.jbasicgl.maths.Vec3;

public class EntityMummy extends Entity{

	private Animation anim;
	private Animation animIdle;
	
	//0 idle, 1 chasse player
	int state = 0;
	
	float speed = 15;
	float runSpeed = 25;
	boolean faceRight = false;
	
	float lastX;
	float deltaX = -1;
	long lastFlip;
	
	Mesh hand;
	float angle;
	float targetAngle;
	boolean isAttacking;
	
	float attackDelay = 1.5f;
	float attackDelayCounter;

	Audio hit;
	Mesh test;
	public EntityMummy(Vec2 pos) {
		super(pos);
		anim = new Animation(9, 1, 32, 64, Res.images.get("momie"), 1, 8, 0.15f, true, 1);
		animIdle = new Animation(9, 1, 32, 64, Res.images.get("momie"), 0, 0, 0.15f, true, 1);
		
		hand = new Mesh(4);
		hand.addVertices(0, 0).addColor(Color4f.WHITE).addTexCoord2f(0, 0);
		hand.addVertices(32, 0).addColor(Color4f.WHITE).addTexCoord2f(1f, 0);
		hand.addVertices(32, 32).addColor(Color4f.WHITE).addTexCoord2f(1f, 1);
		hand.addVertices(0, 32).addColor(Color4f.WHITE).addTexCoord2f(0, 1);
		hand.buffering();
		
		Random rand = new Random();
		this.life = 5 + rand.nextInt(11);
		hit = Audio.list.get("rsc/sounds/hit.wav");

		test = new Mesh(1);
		test.addVertices(0, 0).addColor(Color4f.RED);
		test.buffering();
	}

	@Override
	public void createEntity() {
	}

	@Override
	public void render(Shaders shader) {
		super.render(shader);
		
		if(deltaX == 0){
			animIdle.render(shader, pos);
			anim.stop();
		}
		else{
			anim.render(shader, pos);
			if(anim.isPaused())
				anim.start();
		}
			
		
		Vec2 matPos = pos.copy();
		Res.images.get("mummyHands").bind();
		
		if(faceRight)
			matPos.add(14, 28);
		else
			matPos.add(18, 28);
		
		Vec2 handPos = new Vec2(0,0);
		if(anim.getFrame() == 1)
			handPos.add(0, 0);
		if(anim.getFrame() == 2)
			handPos.add(3, 0);
		if(anim.getFrame() == 3)
			handPos.add(-1, 0);
		if(anim.getFrame() == 4)
			handPos.add(-3, 0);
		if(anim.getFrame() == 5)
			handPos.add(0, 0);
		if(anim.getFrame() == 6)
			handPos.add(3, 0);
		if(anim.getFrame() == 7)
			handPos.add(-1, 0);
		if(anim.getFrame() == 8)
			handPos.add(-3, 0);
		
		handPos.mul(faceRight?-1:1);
		
		matPos.add(handPos);

		Mat4 m0 = Mat4.multiply(Mat4.rotateZf(angle), Mat4.translate(new Vec2(-12, -15.5f)));
		Mat4 m1 = Mat4.multiply(Mat4.translate(matPos), Mat4.scale(faceRight?1:-1, 1, 1));
		shader.setUniformMat4f("m_view", Mat4.multiply(m1, m0));
		shader.setUniform2f("anim", new Vec2());
		hand.render(GL11.GL_QUADS);
		
		Texture.unbind();
		
		if (LD36.getInstance().game.player.debugMode)
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
	
	public Vec2 getImpact()
	{
		Vec2 f = pos.copy().add(15, 28);
		Vec3 r = new Vec3(24, 7, 0);
		r.rotateZ(-angle);
		if (!faceRight)
			r.x = -r.x;
		f.add(r.xy());
		return (f);
	}

	@Override
	public void update(Game game, int tick, double elapse) {
		super.update(game, tick, elapse);
		
		deltaX = pos.x-lastX;
		lastX = pos.x;
		
		EntityPlayer p = LD36.getInstance().game.player;
		anim.update();
		
		//Idle
		if(state == 0)
		{
			pos.add((float)(speed * (faceRight ? 1: -1) * elapse), 0f);
			if(this.distance(p)<200 && Math.abs(pos.copy().y - p.pos.y) < 50){
				state = 1;
			}
			if(deltaX == 0 && System.currentTimeMillis() - lastFlip >= 1000){
				faceRight = !faceRight;
				lastFlip = System.currentTimeMillis();
			}
		}
		//Chasse state
		else if(state == 1){
			faceRight = p.pos.copy().x - pos.copy().x > 0;
			
			int distance = this.distance(p);
			if(distance>250 || Math.abs(pos.copy().y - p.pos.y) > 50){
				state = 0;
			}else if(distance > 20){
				pos.add((float)(runSpeed * (faceRight ? 1: -1) * elapse), 0f);
			}
			if(distance < 30){
				attack();
			}
		}
		
		//Rotate hand
		if(isAttacking){
			angle -= 0.2f;
			if(angle < targetAngle){
				isAttacking = false;
				angle = 0;
			}
			if (tick % 5 == 0)
				game.giveDamage(this, getImpact(), 1);
		}
		
		//Flip animation to face with the mummy direction
		animIdle.setFlip(!faceRight);
		anim.setFlip(!faceRight);
		
		if(attackDelayCounter <= attackDelay){
			attackDelayCounter += elapse;
		}
	}
	
	private void attack(){
		if(attackDelayCounter >= attackDelay && !isAttacking){
			targetAngle = (float) (angle - Math.PI * 2);
			isAttacking = true;
			attackDelayCounter = 0;
		}
	}

	@Override
	public void giveDamage(Entity src, int dmg) {
		if (System.currentTimeMillis() - lifeTime < 300)
			return ;
		life -= dmg;
		lifeTime = System.currentTimeMillis();
		hit.play();
		Vec2 v = src.pos.copy().sub(pos);
		v.normalise();
		this.velocity.x = -v.x * 5;
		this.velocity.y -= 4;
	}

	@Override
	public void giveDamage(int x, int y, int dmg) {
	}

	@Override
	public AABB getBox() {
		return new AABB((int) pos.x, (int) pos.y, 32, 64);
	}

	@Override
	public Entity copy() {
		return new EntityMummy(pos.copy());
	}

}
