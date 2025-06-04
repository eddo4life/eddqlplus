package model;

public class Sys {
	
	int look,systemColor,editorColor;
	String customForeground;

	public String getCustomForeground() {
		return customForeground;
	}

	public void setCustomForeground(String customForeground) {
		this.customForeground = customForeground;
	}

	public int getEditorColor() {
		return editorColor;
	}

	public void setEditorColor(int editorColor) {
		this.editorColor = editorColor;
	}

	public Sys() {}

	public int getLook() {
		return look;
	}

	public int getSystemColor() {
		return systemColor;
	}

	public void setSystemColor(int color) {
		this.systemColor = color;
	}

	public void setLook(int look) {
		this.look = look;
	}
	
}
