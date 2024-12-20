import java.awt.Color;

/** A library of image processing functions. */
public class Runigram {

	public static void main(String[] args) {
	    
		//// Hide / change / add to the testing code below, as needed.
		
		// Tests the reading and printing of an image:	
		Color[][] tinypic = read("tinypic.ppm");
		print(tinypic);

		// Creates an image which will be the result of various 
		// image processing operations:
		Color[][] image;

		// Tests the horizontal flipping of an image:
		image = flippedHorizontally(tinypic);
		System.out.println();
		print(image);
		
		//// Write here whatever code you need in order to test your work.
		//// You can continue using the image array.
		image = flippedVertically(tinypic);
		System.out.println();
		print(image);

		image = grayScaled(tinypic);
		System.out.println();
		print(image);

		image = scaled(tinypic,3,5);
		System.out.println();
		print(image);
	}

	/** Returns a 2D array of Color values, representing the image data
	 * stored in the given PPM file. */
	public static Color[][] read(String fileName) {
		In in = new In(fileName);
		// Reads the file header, ignoring the first and the third lines.
		in.readString();
		int numCols = in.readInt();
		int numRows = in.readInt();
		in.readInt();
		// Creates the image array
		Color[][] image = new Color[numRows][numCols];
		// Reads the RGB values from the file into the image array. 
		// For each pixel (i,j), reads 3 values from the file,
		// creates from the 3 colors a new Color object, and 
		// makes pixel (i,j) refer to that object.
		while(!in.isEmpty()){
			for (int i = 0; i < image.length; i ++){
				for (int j = 0; j < image[i].length; j++){
					Color rgb = new Color (in.readInt(),in.readInt(),in.readInt());
					image[i][j] = rgb;
				}
			}
		}
		return image;
	}

    // Prints the RGB values of a given color.
	private static void print(Color c) {
	    System.out.print("(");
		System.out.printf("%3s,", c.getRed());   // Prints the red component
		System.out.printf("%3s,", c.getGreen()); // Prints the green component
        System.out.printf("%3s",  c.getBlue());  // Prints the blue component
        System.out.print(")  ");
	}

	// Prints the pixels of the given image.
	// Each pixel is printed as a triplet of (r,g,b) values.
	// This function is used for debugging purposes.
	// For example, to check that some image processing function works correctly,
	// we can apply the function and then use this function to print the resulting image.
	private static void print(Color[][] image) {
		for (int i = 0; i < image.length; i ++){
			for (int j = 0; j < image[i].length; j++){
				print(image[i][j]);
			}
			System.out.println("");
		}
	}
	
	/**
	 * Returns an image which is the horizontally flipped version of the given image. 
	 */
	public static Color[][] flippedHorizontally(Color[][] image) {
		Color [][] horImage = new Color[image.length][image[0].length];
		int colIndex = horImage[0].length - 1;
		for (int i = 0; i < horImage.length; i ++){
			for (int j = 0; j < horImage[i].length; j++){
				horImage[i][j] =  image[i][colIndex - j];
			}
		}
		return horImage;
	}
	
	/**
	 * Returns an image which is the vertically flipped version of the given image. 
	 */
	public static Color[][] flippedVertically(Color[][] image){
		Color [][] vertImage = new Color[image.length][image[0].length];
		int rowIndex = vertImage.length - 1;
		for (int i = 0; i < vertImage.length; i ++){
			for (int j = 0; j < vertImage[i].length; j++){
				vertImage[i][j] =  image[rowIndex - i][j];
			}
		}
		return vertImage;
	}

	
	// Computes the luminance of the RGB values of the given pixel, using the formula 
	// lum = 0.299 * r + 0.587 * g + 0.114 * b, and returns a Color object consisting
	// the three values r = lum, g = lum, b = lum.
	private static Color luminance(Color pixel) {
		int red = pixel.getRed();
		int green = pixel.getGreen();
		int blue = pixel.getBlue();
		int lumiColor = (int)((0.299 * red) + (0.587 * green) + (0.114 * blue));
		Color lumiChange = new Color (lumiColor,lumiColor,lumiColor);
		return lumiChange;
	}
	
	/**
	 * Returns an image which is the grayscaled version of the given image.
	 */
	public static Color[][] grayScaled(Color[][] image) {
		Color [][] graiedImage = new Color[image.length][image[0].length];
		for (int i = 0; i < graiedImage.length; i++){
			for (int j = 0; j < graiedImage[i].length; j++){
				graiedImage[i][j] = luminance(image[i][j]);
			}
		}
		return graiedImage;
	}	
	
	/**
	 * Returns an image which is the scaled version of the given image. 
	 * The image is scaled (resized) to have the given width and height.
	 */
	public static Color[][] scaled(Color[][] image, int width, int height) {
		Color [][] resized = new Color[height][width];
		for (int i = 0; i < resized.length; i++){
			for (int j = 0; j < resized[i].length; j++){
				int iImage = i * image.length / height;
				int jImage = j * image[0].length / width;
				resized[i][j] = image[iImage][jImage];
			}
		}
		return resized;
	}
	
	/**
	 * Computes and returns a blended color which is a linear combination of the two given
	 * colors. Each r, g, b, value v in the returned color is calculated using the formula 
	 * v = alpha * v1 + (1 - alpha) * v2, where v1 and v2 are the corresponding r, g, b
	 * values in the two input color.
	 */
	public static Color blend(Color c1, Color c2, double alpha) {
		int newRed = (int)((alpha * c1.getRed()) + ((1 - alpha) * c2.getRed()));
		int newGreen = (int)((alpha * c1.getGreen()) + ((1 - alpha) * c2.getGreen()));
		int newBlue = (int)((alpha * c1.getBlue()) + ((1 - alpha) * c2.getBlue()));
		Color blending = new Color (newRed, newGreen, newBlue);
		return blending;
	}
	
	/**
	 * Cosntructs and returns an image which is the blending of the two given images.
	 * The blended image is the linear combination of (alpha) part of the first image
	 * and (1 - alpha) part the second image.
	 * The two images must have the same dimensions.
	 */
	public static Color[][] blend(Color[][] image1, Color[][] image2, double alpha) {
		Color [][] blending = new Color[image1.length][image1[0].length];
		for (int i = 0; i < blending.length; i++){
			for (int j = 0; j < blending[i].length; j++){
				blending [i][j] = blend(image1[i][j], image2 [i][j], alpha);
			}
		}

		return blending;
	}

	/**
	 * Morphs the source image into the target image, gradually, in n steps.
	 * Animates the morphing process by displaying the morphed image in each step.
	 * Before starting the process, scales the target image to the dimensions
	 * of the source image.
	 */
	public static void morph(Color[][] source, Color[][] target, int n) {
		//Color [][] change = new Color [][]
		if (source.length != target.length || source[0].length != target[0].length){
			target = scaled(target, source[0].length, source.length);
		}
		double alpha = n;
		for (int i = 0; i < alpha; i++){
			source = blend (source, target, (alpha - i)/alpha);
			display(source);
			StdDraw.pause(500);
		}
	}
	
	/** Creates a canvas for the given image. */
	public static void setCanvas(Color[][] image) {
		StdDraw.setTitle("Runigram 2023");
		int height = image.length;
		int width = image[0].length;
		StdDraw.setCanvasSize(width, height);
		StdDraw.setXscale(0, width);
		StdDraw.setYscale(0, height);
        // Enables drawing graphics in memory and showing it on the screen only when
		// the StdDraw.show function is called.
		StdDraw.enableDoubleBuffering();
	}

	/** Displays the given image on the current canvas. */
	public static void display(Color[][] image) {
		int height = image.length;
		int width = image[0].length;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				// Sets the pen color to the pixel color
				StdDraw.setPenColor( image[i][j].getRed(),
					                 image[i][j].getGreen(),
					                 image[i][j].getBlue() );
				// Draws the pixel as a filled square of size 1
				StdDraw.filledSquare(j + 0.5, height - i - 0.5, 0.5);
			}
		}
		StdDraw.show();
	}
}

