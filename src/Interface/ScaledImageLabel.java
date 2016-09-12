package Interface;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

public class ScaledImageLabel extends JLabel {

	@Override
	protected void paintComponent(Graphics g) {
		ImageIcon icon = (ImageIcon) getIcon();
		if (icon != null) {
			drawScaledImage(icon.getImage(), this, g);
		}
	}

	public static Image toBufferedImage(Mat matrix) {
		int type = BufferedImage.TYPE_BYTE_GRAY;
		if (matrix.channels() > 1) {
			type = BufferedImage.TYPE_3BYTE_BGR;
		}

		int bufferSize = matrix.channels() * matrix.cols() * matrix.rows();
		byte[] buffer = new byte[bufferSize];
		matrix.get(0, 0, buffer);
		BufferedImage image = new BufferedImage(matrix.cols(), matrix.rows(), type);
		final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		System.arraycopy(buffer, 0, targetPixels, 0, buffer.length);
		return image;
	}

	public static void drawScaledImage(Image image, Component canvas, Graphics g) {
		int imgWidth = image.getWidth(null);
		int imgHeight = image.getHeight(null);

		double imgAspect = (double) imgHeight / imgWidth;

		int canvasWidth = canvas.getWidth();
		int canvasHeight = canvas.getHeight();

		double canvasAspect = (double) canvasHeight / canvasWidth;

		int x1 = 0; // top left X position
		int y1 = 0; // top left Y position
		int x2 = 0;	// bottom right X position
		int y2 = 0;	// bottom right Y position

		if (canvasAspect > imgAspect) {
			y1 = canvasHeight;
			// keep image aspect ratio
			canvasHeight = (int) (canvasWidth * imgAspect);
			y1 = (y1 - canvasHeight) / 2;
		} else {
			x1 = canvasWidth;
			// keep image aspect ratio
			canvasWidth = (int) (canvasHeight / imgAspect);
			x1 = (x1 - canvasWidth) / 2;
		}
		x2 = canvasWidth + x1;
		y2 = canvasHeight + y1;

		g.drawImage(image, x1, y1, x2, y2, 0, 0, imgWidth, imgHeight, null);
	}
}
