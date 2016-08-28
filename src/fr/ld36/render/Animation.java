package fr.ld36.render;

import org.lwjgl.opengl.GL11;

import fr.mimus.jbasicgl.graphics.Color4f;
import fr.mimus.jbasicgl.graphics.Mesh;
import fr.mimus.jbasicgl.graphics.Shaders;
import fr.mimus.jbasicgl.graphics.Texture;
import fr.mimus.jbasicgl.maths.Mat4;
import fr.mimus.jbasicgl.maths.Vec2;

public class Animation {
	int columns, lines, width, height;
	int start, end;
	
	Texture tex;
	
	Mesh m;
	boolean isPaused = false, 
			isFlipped = false,
			isReversed = false,
			isLooping = false;
	int frame;
	float delay;
	
	long lastFrame;
	
	public Animation(int c, int w, Texture texture, float secDelay){
		this(c, 1, w, w, texture, 0, c-1, secDelay, false);
	}
	
	public Animation(int c, int w, int h, Texture texture, float secDelay){
		this(c, 1, w, h, texture, 0, c-1, secDelay, false);
	}
	
	public Animation(int c, int w, Texture texture, float secDelay, boolean loop){
		this(c, 1, w, w, texture, 0, c-1, secDelay, loop);
	}
	
	public Animation(int c, int w, Texture texture, int s, int e, float secDelay){
		this(c, 1, w, w, texture, s, e, secDelay, false);
	}
	
	public Animation(int c, int w, Texture texture, int s, int e, float secDelay, boolean loop){
		this(c, 1, w, w, texture, s, e, secDelay, loop);
	}
	
	public Animation(int c, int l, int w, int h, Texture texture, int s, int e, float secDelay, boolean loop){
		columns = c;
		lines = l;
		width = w;
		height = h;
		start = s;
		end = e;
		
		tex = texture;
		
		delay = secDelay;
		
		isLooping = loop;
		
		m = new Mesh(4);
		m.addVertices(0,0).addTexCoord2f(0, 0).addColor(Color4f.WHITE);
		m.addVertices(width,0).addTexCoord2f(1f / columns, 0).addColor(Color4f.WHITE);
		m.addVertices(width,height).addTexCoord2f(1f / columns, 1f / lines).addColor(Color4f.WHITE);
		m.addVertices(0,height).addTexCoord2f(0, 1f / lines).addColor(Color4f.WHITE);
		
		m.buffering();
	}
	
	public void render(Shaders shader, Vec2 pos){
		if(isFlipped) pos.add(new Vec2((float)width,0f));
		shader.setUniformMat4f("m_view", Mat4.multiply(Mat4.translate(pos), Mat4.scale(isFlipped?-1:1, 1, 1)));
		shader.setUniform2f("anim", new Vec2((float) frame / columns, 0));
		
		if(tex != null){
			tex.bind();
			m.render(GL11.GL_QUADS);
			Texture.unbind();
		}
	}
	
	public void update(){
		if(!isPaused)
		{
			if(System.currentTimeMillis() - lastFrame > delay * 1000)
			{
				frame += isReversed?-1:1;
				lastFrame = System.currentTimeMillis();
			}
			if(frame > end){
				if(isLooping)
				{
					frame = start;
				}
				else
				{
					isPaused = true;
					frame = end;
				}
			}
			if(frame < start){
				if(isLooping)
				{
					frame = end;
				}
				else
				{
					isPaused = true;
					frame = start;
				}
			}
		}
	}
	
	public void togglePause() {
		isPaused = !isPaused;
	}
	
	public void toggleFlip() {
		isFlipped = !isFlipped;
	}
	
	public void toggleReverse() {
		setReverse(!isReversed);
	}
	
	public void toggleLoop() {
		isLooping = !isLooping;
	}
	
	public void restart()
	{
		if(isReversed) frame = end;
		else frame = start;
		setPause(false);
	}
	
	public void start()
	{
		if(isReversed) 
		{
			if (frame != end)
				this.toggleReverse();
		}
		else
		{
			if (frame != start)
				this.toggleReverse();
		}
		setPause(false);
	}
	
	public void setPause(boolean bool) {
		isPaused = bool;
	}
	
	public void setFlip(boolean bool) {
		isFlipped = bool;
	}
	
	public void setReverse(boolean bool) {
		isReversed = bool;
		if(isReversed) frame = end;
		else frame = start;
	}
	
	public void setLoop(boolean bool) {
		isLooping = bool;
	}
	
	public void setDelay(float d){
		delay = d;
	}
	
	public int getFrame(){
		return frame;
	}

	public boolean isEnded(){
		if (isLooping)
			return (false);
		if(isPaused)
			return (true);
		if(isReversed)
			return (frame == start);
		return (frame == end);
	}
	
	public boolean isReversed(){
		return isReversed;
	}
}
