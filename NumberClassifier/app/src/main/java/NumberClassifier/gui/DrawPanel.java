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
    Runnable onChanged;

    public DrawPanel( int width, int height ) {
        this.imageWidth = width;
        this.imageHeight = height;
        this.image = new double[height*width];

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public double[] getImage() {
        return image;
    }

    public void setOnChanged( Runnable onChanged ) {
        this.onChanged = onChanged;
    }

    public void mouseClicked(MouseEvent event) {
    }

    public void mousePressed(MouseEvent event) {
        draw( event.getX(), event.getY() );
    }

    public void mouseReleased(MouseEvent event) {    
    }

    int imageToPanelX(int imageX) {      
        int x = (int)(imageX * getWidth() / imageWidth);
        return x;        
    }

    int imageToPanelY(int imageY) {
        int y = (int)(imageY * getHeight() / imageHeight);
        return y;
    }

    int panelToImageX( int x ) {
        int imageX = (int)(x * imageWidth / getWidth());
        if (imageX < 0) {
            imageX = 0;
        } else if (imageX >= imageWidth) {
            imageX = imageWidth - 1;
        }
        return imageX;        
    }

    int panelToImageY( int y ) {
        int imageY = (int)(y * imageHeight / getHeight());
        if (imageY < 0) {
            imageY = 0;
        } else if (imageY >= imageHeight) {
            imageY = imageHeight - 1;
        }
        return imageY;
    }

    void draw(int x, int y) {
        int x_index = panelToImageX(x);
        int y_index = panelToImageY(y);

        image[y_index * imageWidth + x_index] = 1;

        System.out.println(x_index + " " + y_index);

        repaint();  

        if (onChanged != null) {
            onChanged.run();
        }
    }

    public void mouseEntered(MouseEvent event) {
    }


    public void mouseExited(MouseEvent event) {
    }

    public void mouseDragged(MouseEvent event) {
        draw( event.getX(), event.getY() );
    }

    public void mouseMoved(MouseEvent event) {
    }

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
