#version 330 core

layout (location = 0) out vec4 out_color;

in vec4 v_color;
in vec3 v_normal;
in vec2 v_tex;

uniform sampler2D u_texture0;
uniform float anim;

void main()
{
	vec2 tex = v_tex;
	tex.x += anim;
	vec4 texColor0 = texture2D(u_texture0, tex);
	out_color = v_color * texColor0;
}
