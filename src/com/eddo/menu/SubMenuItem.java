package com.eddo.menu;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class SubMenuItem extends JButton {

    private final RippleEffect rippleEffect = new RippleEffect(this);

    public SubMenuItem(String text) {
        super(text);
        setContentAreaFilled(false);
        setHorizontalAlignment(SwingConstants.LEFT);
        initStyle();
    }

    private void initStyle() {
        Color color = UIManager.getColor("raven.submenu.ripplecolor");
        if (color != null) {
            rippleEffect.setRippleColor(color);
        } else {
            rippleEffect.setRippleColor(Color.WHITE);
        }
    }

    @Override
    public void updateUI() {
        super.updateUI();
        if (rippleEffect != null) {
            initStyle();
        }
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        rippleEffect.reder(grphcs, new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
        super.paintComponent(grphcs);
    }
}
