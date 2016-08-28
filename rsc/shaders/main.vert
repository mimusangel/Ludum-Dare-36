#version 330 core

layout (location = 0) in vec3 in_position;
layout (location = 1) in vec4 in_color;
layout (location = 2) in vec2 in_texCoord;
layout (location = 3) in vec3 in_normal;

uniform mat4 m_proj;
uniform mat4 m_offset;
uniform mat4 m_view;

out vec4 v_color;
out vec3 v_normal;
out vec2 v_tex;
out vec3 v_pos;

void main()
{
	gl_Position = m_proj * m_offset * m_view * vec4(in_position, 1.0);
	v_color = in_color;
	v_normal = in_normal;
	v_tex = in_texCoord;
	v_pos = (m_view * vec4(in_position, 1.0)).xyz;
}
