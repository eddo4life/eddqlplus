package view.resize;

import view.Home;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Resize {

	int x = Home.frame.getWidth();

	public Resize(Component component, String panel) {
		switch (panel) {
		case "toolbar" -> resizingToolBar(x, component);
//		case "modupdate" -> resizingModUpdate(x, component);
//		case "moddelete" -> resizingModDelete(x, component);
//		case "renameCol" -> resizingModUpdate(x, component);
//		case "tabName" -> resizingTabName(x, component);
		case "tabData" -> resizingTabData(x, component);
		//default -> resizing(component);
		}
		Home.frame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent evt) {
				x = Home.frame.getWidth();
				switch (panel) {
				case "toolbar" -> resizingToolBar(x, component);
//				case "modupdate" -> resizingModUpdate(x, component);
//				case "moddelete" -> resizingModDelete(x, component);
//				case "renameCol" -> resizingModRenameCol(x, component);
//				case "tabName" -> resizingTabName(x, component);
				case "tabData" -> resizingTabData(x, component);
				// default -> resizing(component);
				}
				Home.frame.revalidate();
				Home.frame.repaint();
				component.revalidate();
				component.repaint();
			}
		});
	}

//	private void resizingTabName(int x, Component component) {
//		if (x < 381) {
//			component.setPreferredSize(new Dimension(0, 135));
//			Modification.center.setLayout(new FlowLayout(1));
//		}
//		if (x <= 497) {
//			component.setPreferredSize(new Dimension(0, 100));
//			Modification.center.setLayout(new FlowLayout(1));
//		} else
//			component.setPreferredSize(new Dimension(0, 65));
//		Modification.center.setLayout(new FlowLayout(1));
//	}

	private void resizingTabData(int x, Component component) {
		int y = 120;
		if (x < 149) {
			component.setPreferredSize(new Dimension(0, y + (9 * 30)));
		} else if (x < 180) {
			component.setPreferredSize(new Dimension(0, y + (8 * 30)));
		} else if (x < 200) {
			component.setPreferredSize(new Dimension(0, y + (7 * 30)));
		} else if (x < 211) {
			component.setPreferredSize(new Dimension(0, y + (6 * 30)));
		} else if (x < 256) {
			component.setPreferredSize(new Dimension(0, y + (5 * 30)));
		} else if (x < 316) {
			component.setPreferredSize(new Dimension(0, y + (4 * 30)));
		} else if (x < 401) {
			component.setPreferredSize(new Dimension(0, y + (3 * 30)));
		} else if (x < 544) {
			component.setPreferredSize(new Dimension(0, y + (2 * 30)));
		} else if (x < 943) {
		} else if (x < 1039) {
			component.setPreferredSize(new Dimension(0, y + (1 * 30)));
		} else {
			component.setPreferredSize(new Dimension(0, y + (0 * 30)));
		}
	}

//	private void resizing(Component component) {
//		component.setPreferredSize(new Dimension(0, 35));
//		Modification.center.setLayout(new FlowLayout(1));
//	}

	private void resizingToolBar(int x, Component component) {
		int y = 20;
		 if (x < 342) {
			component.setPreferredSize(new Dimension(0, (35 * 3) - y));
		} else if (x < 755) {
			component.setPreferredSize(new Dimension(0, 35 * 2));
		} else {
			component.setPreferredSize(new Dimension(0, 35));
		}
	}

//	private void resizingModRenameCol(int x, Component component) {
//
//		if (x < 451) {
//			component.setPreferredSize(new Dimension(0, 35 * 2));
//			Modification.center.setLayout(new FlowLayout(0));
//		} else {
//			component.setPreferredSize(new Dimension(0, 35));
//			Modification.center.setLayout(new FlowLayout(1));
//		}
//	}

//	private void resizingModUpdate(int x, Component component) {
//
//		if (x < 306) {
//			component.setPreferredSize(new Dimension(0, 35 * 5));
//			Modification.center.setLayout(new FlowLayout(0));
//		} else if (x < 380) {
//			component.setPreferredSize(new Dimension(0, 35 * 4));
//			Modification.center.setLayout(new FlowLayout(0));
//		} else if (x < 476) {
//			component.setPreferredSize(new Dimension(0, 35 * 3));
//		} else if (x < 825) {
//			Modification.center.setLayout(new FlowLayout(0));
//			component.setPreferredSize(new Dimension(0, 35 * 2));
//		} else {
//			component.setPreferredSize(new Dimension(0, 35));
//			Modification.center.setLayout(new FlowLayout(1));
//		}
//
//	}

//	private void resizingModDelete(int x, Component component) {
//		if (x < 583) {
//			component.setPreferredSize(new Dimension(0, 35 * 2));
//			Modification.center.setLayout(new FlowLayout(0));
//		} else {
//			component.setPreferredSize(new Dimension(0, 35));
//			Modification.center.setLayout(new FlowLayout(1));
//		}
//	}

}
