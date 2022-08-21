package NumberClassifier.data;

public class Image {
    
    private int width;
    private int height;
    private double[] pixels;

    public Image(double[] pixels, int width, int height) {
        this.pixels = pixels;
        this.width = width;
        this.height = height;
    }

    public void fitImage() {
        final double threshold = 0.5;
        Rect bounds = calculateBounds(threshold);

        int xOffset = (width - bounds.width) / 2;
        int yOffset = (height - bounds.height) / 2;

        int xDelta = bounds.x - xOffset;
        int yDelta = bounds.y - yOffset;

        double xscale = (double)width / bounds.width;
        double yscale = (double)height / bounds.height;
        double scale = Math.min(xscale, yscale);

        transform(xDelta, yDelta, scale);
    }

    public double[] getPixels() {
        return pixels;
    }

    private class Rect {
        int x, y, width, height;

        public Rect(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }

    private double getPixel(int x, int y) {
        if ( x < 0 || x >= width || y < 0 || y >= height ) {
            return 0.0;
        }

        return pixels[y * width + x];
    }

    private void transform(int translateX, int translateY, double scale) {
        // translate
        double[] translatedPixels = new double[pixels.length];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int newX = x + translateX;
                int newY = y + translateY;
                if (newX < 0 || newX >= width || newY < 0 || newY >= height) {
                    translatedPixels[y * width + x] = 0;
                } else {
                    translatedPixels[y * width + x] = pixels[newY * width + newX];
                }
            }
        }
        
        pixels = translatedPixels;

        // scale
        double[] newPixels = new double[pixels.length];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double sourceX = (x - width*0.5) / scale + width*0.5;
                double sourceY = (y - height*0.5) / scale + height*0.5;

                int sourceXInt = (int) Math.floor(sourceX);
                int sourceYInt = (int) Math.floor(sourceY);

                double p00 = getPixel(sourceXInt, sourceYInt);
                double p01 = getPixel(sourceXInt, sourceYInt + 1);
                double p10 = getPixel(sourceXInt + 1, sourceYInt);
                double p11 = getPixel(sourceXInt + 1, sourceYInt + 1);

                double xt = sourceX - sourceXInt;
                double yt = sourceY - sourceYInt;

                double p0 = p00 * (1 - xt) + p10 * xt;
                double p1 = p01 * (1 - xt) + p11 * xt;
                double p = p0 * (1 - yt) + p1 * yt;

                newPixels[y * width + x] = p;
            }
        }        

        pixels = newPixels;
    }

    private Rect calculateBounds(double threshold) {
        int minX = width;
        int maxX = 0;
        int minY = height;
        int maxY = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (pixels[y * width + x] > threshold) {
                    if (x < minX) {
                        minX = x;
                    }
                    if (x > maxX) {
                        maxX = x;
                    }
                    if (y < minY) {
                        minY = y;
                    }
                    if (y > maxY) {
                        maxY = y;
                    }
                }
            }
        }
        return new Rect(minX, minY, maxX - minX + 1, maxY - minY + 1);
    }
    

}
