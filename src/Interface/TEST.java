package Interface;

import java.awt.*;
import javax.swing.*;

import static javax.swing.JOptionPane.ERROR_MESSAGE;

public class TEST {

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	private static JFrame frame;
	private static ScaledImageLabel imageLabel;
	private static FindBrightestSpot BS;

	public static void mahin(String[] args) throws AWTException {
		initGUI();
		runMainLoop();
	}

	private static void initGUI() {
		frame = new JFrame("Camera Input Example");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(0, 0);
		frame.setSize(1366, 768);
		imageLabel = new ScaledImageLabel();
		frame.add(imageLabel);
		frame.setVisible(true);
	}

	private static void runMainLoop() throws AWTException {
		BS = new FindBrightestSpot();
		Mat webcamMatImage = new Mat();
		Robot robot = new Robot();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		//video
		VideoCapture capture = new VideoCapture(0);
		capture.set(Videoio.CAP_PROP_FRAME_WIDTH, 853);
		capture.set(Videoio.CAP_PROP_FRAME_HEIGHT, 480);
		//capture.set(Videoio.CV_CAP_PROP_ISO_SPEED, 50);
		//capture.set(Videoio.CAP_PROP_WHITE_BALANCE_RED_V, 100);
		if (capture.isOpened()) {
			while (true) {
				capture.read(webcamMatImage);
				if (!webcamMatImage.empty()) {

					BS.originalImage = webcamMatImage;
//															if(BS.firstImage==null)
					//																	BS.firstImage=BS.originalImage;

					BS.locateHighestBrightnessPoint();
					webcamMatImage = BS.grayImage;
					System.out.println(BS.minMaxLocResult.maxVal);
					if (BS.minMaxLocResult.maxVal > 85F) {  //<<< change the value in the if
						robot.mouseMove(
								(int) (screenSize.width / BS.originalImage.size().width * BS.minMaxLocResult.maxLoc.x),
								(int) (screenSize.height / BS.originalImage.size().height * BS.minMaxLocResult.maxLoc.y));

						Imgproc.circle(webcamMatImage, BS.minMaxLocResult.maxLoc, 8, new Scalar(0, 255, 0));
					}
					imageLabel.setIcon(new ImageIcon(ScaledImageLabel.toBufferedImage(webcamMatImage)));
				} else {
					JOptionPane.showMessageDialog(null, "-- Frame not captured --", "-- Frame not captured --", ERROR_MESSAGE);
					break;
				}
			}
		} else {
			JOptionPane.showMessageDialog(null, "Couldn't open capture.", "Error", ERROR_MESSAGE);
		}
	}
}
