package NumberClassifier.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JPanel;

/**
 * Panel where user can draw an image.
 */
public class DrawPanel extends JPanel implements MouseListener, MouseMotionListener {
    
    private double[] image;
    private int imageWidth, imageHeight;
    private Runnable onChanged;
    private boolean erasing;
    private int lastX, lastY;

    /**
     * Constructs a new DrawPanel.
     * @param width Width of the image in pixels.
     * @param height Height of the image in pixels.
     */
    public DrawPanel( int width, int height ) {
        this.imageWidth = width;
        this.imageHeight = height;
        this.image = new double[height*width];

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    /**
     * Get the drawn image.
     * @return The image data. The array has height*width elements and is stored in row-major order, top row first.
     */
    public double[] getImage() {
        return image;
    }

    /**
     * Set callback to be called when the image is changed.
     * @param onChanged
     */
    public void setOnChanged( Runnable onChanged ) {
        this.onChanged = onChanged;
    }

    /**
     * Implements MouseListener interface. Doesn't do anything.
     */
    public void mouseClicked(MouseEvent event) {
    }

    /**
     * Implements MouseListener interface. Draws to the image.
     */
    public void mousePressed(MouseEvent event) {
        erasing = event.getButton() == MouseEvent.BUTTON3;
        draw( event.getX(), event.getY(), erasing );
    }

    /**
     * Implements MouseListener interface. Doesn't do anything.
     */
    public void mouseReleased(MouseEvent event) {    
    }

    private int imageToPanelX(int imageX) {      
        int x = (int)(imageX * getWidth() / imageWidth);
        return x;        
    }

    private int imageToPanelY(int imageY) {
        int y = (int)(imageY * getHeight() / imageHeight);
        return y;
    }

    private int panelToImageX( int x ) {
        int imageX = (int)(x * imageWidth / getWidth());
        if (imageX < 0) {
            imageX = 0;
        } else if (imageX >= imageWidth) {
            imageX = imageWidth - 1;
        }
        return imageX;        
    }

    private int panelToImageY( int y ) {
        int imageY = (int)(y * imageHeight / getHeight());
        if (imageY < 0) {
            imageY = 0;
        } else if (imageY >= imageHeight) {
            imageY = imageHeight - 1;
        }
        return imageY;
    }

    private void addPixel(int imageX, int imageY, double amount) {
        if ( imageX < 0 || imageX >= imageWidth || imageY < 0 || imageY >= imageHeight ) {
            return;
        }

        int index = imageY * imageWidth + imageX;;
        image[index] += amount;

        if ( image[index] < 0 ) {
            image[index] = 0;
        } else if ( image[index] > 1 ) {
            image[index] = 1;
        }
    }

    private void draw(int x, int y, boolean erase) {

        int imageX = panelToImageX(x);
        int imageY = panelToImageY(y);

        if ( imageX == lastX && imageY == lastY ) {
            return;
        }

        lastX = imageX;
        lastY = imageY;

        double radius1 = 0.25;
        double radius2 = 2.0;

        for( int px = imageX - (int)radius2 - 1; px <= imageX + (int)radius2 + 1; px ++ ) {
            for( int py = imageY - (int)radius2 - 1; py <= imageY + (int)radius2 + 1; py ++ ) {
                double r = Math.sqrt( (px - imageX) * (px - imageX) + (py - imageY) * (py - imageY) );
                double amount = 0.0;
                if ( r <= radius1 ) {
                    amount = 1.0;
                } else if ( r < radius2 ) {
                    amount = (radius2 - r) / (radius2 - radius1);
                }
                if ( erase ) {
                    amount = -amount;
                }
                addPixel(px, py, amount);
            }
        }

        repaint();  

        if (onChanged != null) {
            onChanged.run();
        }
    }

    /**
     * Implements MouseMotionListener interface. Doesn't do anything.
     */
    public void mouseEntered(MouseEvent event) {
    }

    /**
     * Implements MouseMotionListener interface. Doesn't do anything.
     */
    public void mouseExited(MouseEvent event) {
    }

    /**
     * Implements MouseMotionListener interface. Draws to the image.
     */
    public void mouseDragged(MouseEvent event) {
        draw( event.getX(), event.getY(), erasing );
    }

    /**
     * Implements MouseMotionListener interface. Doesn't do anything.
     */
    public void mouseMoved(MouseEvent event) {
    }

    /**
     * Render the image.
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.RED);
        g.fillRect(0, 0, getWidth(), getHeight());

        for (int i = 0; i < imageHeight; i++) {
            for (int j = 0; j < imageWidth; j++) {
                int x = imageToPanelX(j);
                int y = imageToPanelY(i);

                int rectWidth = imageToPanelX(j + 1) - x;
                int rectHeight = imageToPanelY(i + 1) - y;

                float pixel = 1.0f - (float)image[i * imageWidth + j];
                g.setColor(new Color(pixel, pixel, pixel));
                g.fillRect(x, y, rectWidth, rectHeight);
            }
        }
    }


    
}
