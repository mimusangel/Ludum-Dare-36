#version 330 core

layout (location = 0) out vec4 out_color;

in vec4 v_color;
in vec3 v_normal;
in vec2 v_tex;
in vec3 v_pos;

uniform sampler2D u_texture0;
uniform vec2 anim;
uniform int disableTexture;
uniform vec2 ligthPos;
uniform float ligthDist;

void main()
{	
	vec3 lpos = vec3(ligthPos.x, ligthPos.y, 0.0);
	float dist = distance(v_pos, lpos);
	float lDist = (1 - min(dist / ligthDist, 1.0));
	if (disableTexture > 0)
	{
		out_color = v_color * vec4(lDist, lDist, lDist, 1.0);
	}
	else
	{
		vec2 tex = v_tex + anim;
		vec4 texColor0 = texture2D(u_texture0, tex);
		out_color = v_color * texColor0 * vec4(lDist, lDist, lDist, 1.0);
	}
}
