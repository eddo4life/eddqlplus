package view.tools;

import java.awt.Font;

import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import dao.sqlite.SystemDatabaseTreatment;

public class LookAndFeel {

	public static int look = read();

	public LookAndFeel(int look) {
		look(look);
	}

	void look(int look) {
		try {
			switch (look) {
			case 0 -> UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());// system myb metal, see later
			case 1 -> UIManager.setLookAndFeel(new NimbusLookAndFeel());// #1...default
			case 2 -> UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
			case 3 -> UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel"); // sys/Metal
			case 4 -> UIManager.getLookAndFeelDefaults().put("defaultFont", new Font("sanserif", Font.PLAIN, 10));
			case 5 -> UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			default -> UIManager.setLookAndFeel(new NimbusLookAndFeel());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void save() {
		new SystemDatabaseTreatment().updateSystem("look", look,null);
	}

	public static int read() {

		return new SystemDatabaseTreatment().getSystemDatas().getLook();
	}
}
