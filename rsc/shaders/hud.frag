#version 330 core

layout (location = 0) out vec4 out_color;

in vec4 v_color;
in vec3 v_normal;
in vec2 v_tex;
in vec3 v_pos;

uniform sampler2D u_texture0;
uniform int disableTexture;
uniform vec2 offsetTexture;
uniform vec2 mulTexture;
void main()
{	

	if (disableTexture > 0)
	{
		out_color = v_color;
	}
	else
	{
		vec2 tex = v_tex * mulTexture + offsetTexture;
		vec4 texColor0 = texture2D(u_texture0, tex);
		out_color = v_color * texColor0;
	}
}