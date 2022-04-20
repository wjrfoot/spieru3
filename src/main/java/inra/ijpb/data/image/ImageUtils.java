/**
 * 
 */
package inra.ijpb.data.image;

import ij.ImagePlus;
import ij.ImageStack;
import ij.process.ImageProcessor;

/**
 * A collection of static utility methods for processing 2D or 3D images.
 * 
 * @author dlegland
 */
public class ImageUtils
{
    /**
     * Fills a rectangle within the image with the specified value.
     * 
     * @param image
     *            the image to process.
     * @param x0
     *            the x-coordinate of the left corner of the rectangle to fill
     * @param y0
     *            the y-coordinate of the top corner of the rectangle to fill
     * @param w
     *            the width of the rectangle to fill, in pixels
     * @param h
     *            the height of the rectangle to fill, in pixels
     * @param value
     *            the value to fill the rectangle with
     */
    public static final void fillRect(ImageProcessor image, int x0, int y0, int w, int h, double value)
    {
        // retrieve image size for bounds check
        int width = image.getWidth();
        int height = image.getHeight();
        
        // fill rectangle
        for (int y = y0; y < Math.min(y0 + h, height); y++)
        {
            for (int x = x0; x < Math.min(x0 + w, width); x++)
            {
                image.setf(x, y, (float) value);
            }
        }
    }

    /**
     * Fills a rectangle within the image with the specified value.
     * 
     * @param image
     *            the image to process.
     * @param x0
     *            the x-coordinate of the left corner of the rectangle to fill
     * @param y0
     *            the y-coordinate of the top corner of the rectangle to fill
     * @param z0
     *            the z-coordinate of the front corner of the rectangle to fill
     * @param w
     *            the width of the rectangle to fill, in voxels
     * @param h
     *            the height of the rectangle to fill, in voxels
     * @param d
     *            the depth of the rectangle to fill, in voxels
     * @param value
     *            the value to fill the 3D rectangle with
     */
    public static final void fillRect3d(ImageStack image, int x0, int y0, int z0, int w, int h, int d, double value)
    {
        // retrieve image size for bounds check
        int width = image.getWidth();
        int height = image.getHeight();
        int depth = image.getSize();
        
        // fill 3D rectangle
        for (int z = z0; z < Math.min(z0 + d, depth); z++)
        {
            for (int y = y0; y < Math.min(y0 + h, height); y++)
            {
                for (int x = x0; x < Math.min(x0 + w, width); x++)
                {
                    image.setVoxel(x, y, z, value);
                }
            }
        }
    }

    /**
     * Replaces the elements of an image with a given value by a new value.
     * 
     * @param image
     *            the image to process
     * @param initialValue
     *            the value of the elements to replace
     * @param finalValue
     *            the new value of the elements
     */
    public static final void replaceValue(ImagePlus image, double initialValue, double finalValue) 
    { 
        if (image.getStackSize() == 1) 
        {
            replaceValue(image.getProcessor(), initialValue, finalValue);
        } 
        else 
        {
            replaceValue(image.getStack(), initialValue, finalValue);
        }
    }

    /**
     * Replaces the elements of a 2D image with a given value by a new value.
     * 
     * @param image
     *            the 3D image to process
     * @param initialValue
     *            the value of the elements to replace
     * @param finalValue
     *            the new value of the elements
     */
    public static final void replaceValue(ImageProcessor image, double initialValue, double finalValue) 
    { 
        for (int y = 0; y < image.getHeight(); y++)
        {
            for (int x = 0; x < image.getWidth(); x++)
            {
                if (image.getf(x, y) == initialValue) 
                {
                    image.setf(x, y, (float) finalValue);
                }
            }
        }
    }

    /**
     * Replaces the elements of a 3D image with a given value by a new value.
     * 
     * @param image
     *            the 3D image to process
     * @param initialValue
     *            the value of the elements to replace
     * @param finalValue
     *            the new value of the elements
     */
    public static final void replaceValue(ImageStack image, double initialValue, double finalValue) 
    { 
        for (int z = 0; z < image.getSize(); z++)
        {
            for (int y = 0; y < image.getHeight(); y++)
            {
                for (int x = 0; x < image.getWidth(); x++)
                {
                    if (image.getVoxel(x, y, z) == initialValue) 
                    {
                        image.setVoxel(x, y, z, finalValue);
                    }
                }
            }
        }
    }

    /**
     * Prints the content of the given ImageProcessor on the console. This can be used
     * for debugging (small) images.
     * 
     * @param image the image to display on the console 
     */
    public static final void print(ImageProcessor image) 
    {
        for (int y = 0; y < image.getHeight(); y++)
        {
            for (int x = 0; x < image.getWidth(); x++)
            {
                System.out.print(String.format("%3d ", (int) image.getf(x, y)));
            }
            System.out.println("");
        }
    }
    
    /**
     * Prints the content of the given 3D image on the console. This can be used
     * for debugging (small) images.
     * 
     * @param image the 3D image to display on the console 
     */
    public static final void print(ImageStack image) 
    {
        int nSlices = image.getSize();
        for (int z = 0; z < nSlices; z++)
        {
            System.out.println(String.format("slice %d/%d", z, nSlices - 1));
            for (int y = 0; y < image.getHeight(); y++)
            {
                for (int x = 0; x < image.getWidth(); x++)
                {
                    System.out.print(String.format("%3d ", (int) image.getVoxel(x, y, z)));
                }
                System.out.println("");
            }
        }
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private ImageUtils()
    {
    }
}