package image.processing.vision.drawable;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;

import javax.swing.JPanel;

public class VectorCanvas extends JPanel {

	private static final long serialVersionUID = 1L;
	List<Drawable> drawables;

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (drawables != null) {
			for (final Drawable drawable : drawables) {
				drawable.draw((Graphics2D) g);
			}
		}
	}

	public void setDrawables(List<Drawable> drawables) {
		if (this.drawables != null) {
			for (final Drawable drawable : this.drawables) {
				this.repaint(drawable.getBounds().getBounds());
			}
		}
		for (final Drawable drawable : drawables) {
			this.repaint(drawable.getBounds().getBounds());
		}
		this.drawables = drawables;
	}

}
