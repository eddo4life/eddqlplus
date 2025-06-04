package view.iconmaker;

import java.awt.Color;

import javax.swing.Icon;

import jiconfont.icons.font_awesome.FontAwesome;

public class _Icon {

	int size=40;
	
	public _Icon() {

	}

	public Icon messageIcon() {

		return new IconFontGenerator(FontAwesome.ASSISTIVE_LISTENING_SYSTEMS, size, new Color(0,131,193)).getIcon();

	}

	public Icon exceptionIcon() {

		return new IconFontGenerator(FontAwesome.EXCLAMATION_TRIANGLE, size, new Color(223,208,0)).getIcon();

	}


		public Icon failedIcon() {

			return new IconFontGenerator(FontAwesome.TIMES_CIRCLE, size, new Color(170,0,20)).getIcon();

		}


	public Icon succesIcon() {
		return new IconFontGenerator(FontAwesome.CHECK_CIRCLE_O, size, new Color(0,120,69)).getIcon();

	}

	public Icon questionIcon() {

		return new IconFontGenerator(FontAwesome.QUESTION_CIRCLE_O, size, Color.darkGray).getIcon();

	}
	
}
