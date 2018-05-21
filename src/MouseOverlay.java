package com.sqweebloid.jane;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import javax.inject.Inject;
import org.someclient.api.Point;
import org.someclient.client.ui.overlay.Overlay;
import org.someclient.client.ui.overlay.OverlayLayer;
import org.someclient.client.ui.overlay.OverlayPosition;

import com.sqweebloid.jane.automata.tools.input.MouseState;

public class MouseOverlay extends Overlay {
    @Inject
    public MouseState state;

	public MouseOverlay() {
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ALWAYS_ON_TOP);
	}

	@Override
	public Dimension render(Graphics2D graphics) {
        double radius = 5;
        Shape circle = new Ellipse2D.Double(
                state.getX() - radius,
                state.getY() - radius,
                2.0 * radius,
                2.0 * radius);

        graphics.draw(circle);

		return null;
	}
}
