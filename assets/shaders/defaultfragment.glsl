#version 330 core

in vec4 fColor;
in vec2 fUV;
in float fTextureID;

uniform sampler2D uTextures[16];

out vec4 color;

void main() {
	int id = int(fTextureID);
	if(id == 99) {
		color = fColor;
	} else {
		color = fColor * texture(uTextures[id], fUV);
	}
}
