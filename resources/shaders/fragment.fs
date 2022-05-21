#VERSION 400 core

in color;

out fragmentColor;

void main() {
	fragmentColor = vec4(color, 1.0);
}