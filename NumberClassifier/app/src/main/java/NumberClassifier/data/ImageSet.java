package NumberClassifier.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Collection of images and their labels.
 */
public class ImageSet {

    private int numLabelIndices;
    private int imageWidth;
    private int imageHeight;    
    private ArrayList<Image> images;
    private ArrayList<Integer> imageLabels;

    private ImageSet() {}
    
    /**
     * Load an ImageSet with MNIST file format.
     * @param imageFile Input stream containing a MNIST image file data.
     * @param labelFile Input stream containing a corresponding MNIST image label data.
     * @return ImageSet containing images in the file.
     */
    public static ImageSet loadFromMNIST( InputStream imageFile, InputStream labelFile, int numLabelIndices, boolean fit ) throws Exception {
        byte[] magicImage = imageFile.readNBytes(4);
        byte[] magicLabel = labelFile.readNBytes(4);

        if ( magicImage[0] != 0x00 || magicImage[1] != 0x00 || magicImage[2] != 0x08 || magicImage[3] != 0x03 ) {
            throw new Exception("Image file not in MNIST format.");
        }
        if ( magicLabel[0] != 0x00 || magicLabel[1] != 0x00 || magicLabel[2] != 0x08 || magicLabel[3] != 0x01 ) {
            throw new Exception("Label file not in MNIST format.");
        }

        int numImages = readMSBInt(imageFile);
        int numLabels = readMSBInt(labelFile);

        if ( numImages <= 0 || numLabels <= 0 ) {
            throw new Exception("Invalid number of examples.");
        }

        if ( numImages != numLabels ) {
            throw new Exception("Number of images and labels do not match.");
        }
        
        ImageSet imageSet = new ImageSet();
        imageSet.numLabelIndices = numLabelIndices;
        imageSet.imageWidth = readMSBInt(imageFile);
        imageSet.imageHeight = readMSBInt(imageFile);
        imageSet.images = new ArrayList<Image>();
        imageSet.imageLabels = new ArrayList<Integer>();
        
        for ( int i = 0; i < numImages; i++ ) {
            int label = labelFile.read();
            if ( label >= numLabelIndices ) {
                throw new Exception("Label out of range.");
            }
            imageSet.imageLabels.add(label);
            double[] pixels = new double[imageSet.imageWidth * imageSet.imageHeight];
            byte[] imageBytes = imageFile.readNBytes(pixels.length);
            for ( int j = 0; j < pixels.length; j++ ) {
                pixels[j] = Byte.toUnsignedInt(imageBytes[j]) / 255.0;
            }
            Image image = new Image(pixels, imageSet.imageWidth, imageSet.imageHeight);
            if ( fit )
            {
                image.fitImage();
            }
            imageSet.images.add(image);
        }
        
        return imageSet;
    }

    private static int readMSBInt( InputStream stream ) throws IOException {
        byte[] integerMSB = stream.readNBytes(4);
        return (Byte.toUnsignedInt(integerMSB[0]) << 24) | (Byte.toUnsignedInt(integerMSB[1]) << 16) | (Byte.toUnsignedInt(integerMSB[2]) << 8) | Byte.toUnsignedInt(integerMSB[3]);
    }

    /**
     * Create training examples from loaded data, that can be used to train and evaluate a neural network.
     * @return Array of TrainingExample objects containing the image label pairs.
     */
    public TrainingExample[] createTrainingExamples() {
        TrainingExample[] examples = new TrainingExample[images.size()];
        for ( int i = 0; i < images.size(); i++ ) {
            double[] output = new double[numLabelIndices];
            output[imageLabels.get(i)] = 1.0;
            examples[i] = new TrainingExample(images.get(i).getPixels().clone(), output);
        }
        return examples;
    }

    /**
     * Get number of image / label pairs in the dataset.
     * @return Number of images or labels.
     */
    public int getNumImages() {
        return images.size();
    }

    /**
     * Get image width.
     * @return Image width in pixels.
     */
    public int getImageWidth() {
        return imageWidth;
    }

    /**
     * Get image height.
     * @return Image width in pixels.
     */
    public int getImageHeight() {
        return imageHeight;
    }

    /**
     * Get label for image.
     * @param image image index
     * @return Label as integer.
     */
    public int getLabel(int image) {
        return imageLabels.get(image);
    }
    
    /**
     * Get image data in pixels for image. Image data is stored as a array of length imageWidth * imageHeight. The data is stored in row-major order.
     * @param image image index
     * @return Image data as array of doubles.
     */
    public double[] getImage(int image) {
        return images.get(image).getPixels();
    }

}
