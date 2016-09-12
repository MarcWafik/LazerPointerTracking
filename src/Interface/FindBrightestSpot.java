package Interface;

import org.opencv.core.Core.*;

public class FindBrightestSpot {

	public int blur = 5;
	public boolean isStaticBack = false;

	public Mat firstImage;
	public Mat originalImage;
	public Mat grayImage;
	public MinMaxLocResult minMaxLocResult;

	public FindBrightestSpot() {
		originalImage = new Mat();
		grayImage = new Mat();
		//				firstImage = new Mat();
		minMaxLocResult = new MinMaxLocResult();
	}

	synchronized public void setFirstImage(Mat firstImage, boolean isStaticBack) {
		this.isStaticBack = isStaticBack;
		Imgproc.cvtColor(firstImage, this.firstImage, Imgproc.COLOR_BGR2GRAY);
	}

	synchronized public void locateHighestBrightnessPoint() {
		//ArrayList<Mat> planes = new ArrayList<Mat>();
		//Core.split(originalImage, planes);  // planes[2] is the red channel
		Imgproc.cvtColor(originalImage, grayImage, Imgproc.COLOR_BGR2GRAY);
		if (isStaticBack) {
			Core.subtract(grayImage, firstImage, grayImage);
		}
		//Imgproc.GaussianBlur(grayImage, grayImage, new Size(radius, radius), 0);
		Imgproc.medianBlur(grayImage, grayImage, blur);
		minMaxLocResult = Core.minMaxLoc(grayImage);
	}

	synchronized public void locateHighestBrightnessPointMotionDetect() {
		Imgproc.cvtColor(originalImage, grayImage, Imgproc.COLOR_BGR2GRAY);
		Core.subtract(grayImage, firstImage, grayImage);
		Imgproc.medianBlur(grayImage, grayImage, blur);
		minMaxLocResult = Core.minMaxLoc(grayImage);
		Imgproc.cvtColor(originalImage, this.firstImage, Imgproc.COLOR_BGR2GRAY);
	}
}
