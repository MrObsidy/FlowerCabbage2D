#version 330 core

layout (location=0) in vec3 aPos;
layout (location=1) in vec4 aColor;
layout (location=2) in vec2 aUV;
layout (location=3) in float aTextureID;

uniform mat4 uProjMatrix;
uniform mat4 uViewMatrix;

//out vec2 UV;
out vec4 fColor;
out vec2 fUV;
out float fTextureID;

void main() {
	fUV = aUV;
	fTextureID = aTextureID;
	fColor = aColor;
	gl_Position = uProjMatrix * uViewMatrix * vec4(aPos, 1.0);
}
