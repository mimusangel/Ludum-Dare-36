#version 330 core

layout (location = 0) out vec4 out_color;

in vec4 v_color;
in vec3 v_normal;

void main()
{
	out_color = v_color;
}
