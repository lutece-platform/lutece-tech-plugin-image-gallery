/*
 * Copyright (c) 2002-2022, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.galleryimage.util;


import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.ImageParser;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.formats.bmp.BmpImageParser;
import org.apache.commons.imaging.formats.dcx.DcxImageParser;
import org.apache.commons.imaging.formats.gif.GifImageParser;
import org.apache.commons.imaging.formats.pcx.PcxImageParser;
import org.apache.commons.imaging.formats.png.PngImageParser;
import org.apache.commons.imaging.formats.tiff.TiffImageParser;
import org.apache.commons.imaging.formats.wbmp.WbmpImageParser;
import org.apache.commons.imaging.formats.xbm.XbmImageParser;
import org.apache.commons.imaging.formats.xpm.XpmImageParser;
import org.apache.commons.lang3.StringUtils;
import org.imgscalr.Scalr;

import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.portal.service.util.AppLogService;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

/**
 * Image utils class
 */
public final class ImageUtils
{
    /** Parameter JPG */
    private static final String PARAMETER_JPG = "jpg";

    /**
     * Private constructor
     */
    private ImageUtils( )
    {
    }
 
    /**
     * Build an outputStream of an image from its byte array. If strImageWidth parameter is filled in, the image is resized according to this width
     * 
     * @param fileImage
     *           image byte array
     * @param strImageWidth 
     *           image width
     * @return image outputStream
     */
    public static ByteArrayOutputStream getImageOutputStream( byte[ ] fileImage, String strImageWidth )
    {
    	ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
    	try( ByteArrayInputStream inputStream = new ByteArrayInputStream( fileImage ) )
        {
            BufferedImage image = ImageIO.read( inputStream );
            if( strImageWidth != null )
            {
            	image = Scalr.resize( image, Scalr.Mode.FIT_TO_WIDTH, Integer.parseInt( strImageWidth ) );
            }
            ImageIO.write( image, PARAMETER_JPG, outputStream );            
        }
        catch( Exception e )
        {
            AppLogService.error( "ImageUtils:getImageOutputStream( ): {} ", e.getMessage( ), e );
        }
    	return outputStream;
    }

    /**
     * Checks if the image is safe
     * 
     * @param fileImage
     *           image byte array
     * @return true if file is safe, false otherwise
     */
    public static boolean safeImage( byte[ ] fileImage )
    {
        boolean safeState = false;
        boolean fallbackOnApacheCommonsImaging;
        try
        {
            if ( fileImage != null )
            {
                // Get the image format
                String formatName;
                try (   ByteArrayInputStream bis = new ByteArrayInputStream( fileImage );
                		ImageInputStream iis = ImageIO.createImageInputStream( bis ) )
                {
                    Iterator<ImageReader> imageReaderIterator = ImageIO.getImageReaders( iis );
                    // If there not ImageReader instance found so it's means that the current
                    // format is not supported by the Java built-in API
                    if ( !imageReaderIterator.hasNext( ) )
                    {
                        ImageInfo imageInfo = Imaging.getImageInfo( fileImage );
                        if ( imageInfo != null && imageInfo.getFormat( ) != null && imageInfo.getFormat( ).getName( ) != null )
                        {
                            formatName = imageInfo.getFormat( ).getName( );
                            fallbackOnApacheCommonsImaging = true;
                        }
                        else
                        {
                            throw new IOException( "Format of the original image is " + "not supported for read operation !" );
                        }
                    }
                    else
                    {
                        ImageReader reader = imageReaderIterator.next( );
                        formatName = reader.getFormatName( );
                        fallbackOnApacheCommonsImaging = false;
                    }
                }

                BufferedImage originalImage = loadImage( fileImage, fallbackOnApacheCommonsImaging );       

                // Get current Width and Height of the image
                int originalWidth = originalImage.getWidth( null );
                int originalHeight = originalImage.getHeight( null );

                // Resize the image by removing 1px on Width and Height
                Image resizedImage = originalImage.getScaledInstance( originalWidth - 1, originalHeight - 1, Image.SCALE_SMOOTH );

                // Resize the resized image by adding 1px on Width and Height
                // In fact set image to is initial size
                Image initialSizedImage = resizedImage.getScaledInstance( originalWidth, originalHeight, Image.SCALE_SMOOTH );

                // Save image by overwriting the provided source file content
                BufferedImage sanitizedImage = new BufferedImage( initialSizedImage.getWidth( null ), initialSizedImage.getHeight( null ),
                        BufferedImage.TYPE_INT_RGB );
                Graphics bg = sanitizedImage.getGraphics( );
                bg.drawImage( initialSizedImage, 0, 0, null );
                bg.dispose( );

                try ( OutputStream fos = new ByteArrayOutputStream( ) )
                {
                    if ( !fallbackOnApacheCommonsImaging )
                    {
                        ImageIO.write( sanitizedImage, formatName, fos );
                    }
                    else
                    {
                        ImageParser imageParser;
                        // Handle only formats for which Apache Commons Imaging can successfully write
                        // (YES in Write column of the reference link) the image format
                        // See reference link in the class header
                        switch( formatName )
                        {
                            case "TIFF":
                            {
                                imageParser = new TiffImageParser( );
                                break;
                            }
                            case "PCX":
                            {
                                imageParser = new PcxImageParser( );
                                break;
                            }
                            case "DCX":
                            {
                                imageParser = new DcxImageParser( );
                                break;
                            }
                            case "BMP":
                            {
                                imageParser = new BmpImageParser( );
                                break;
                            }
                            case "GIF":
                            {
                                imageParser = new GifImageParser( );
                                break;
                            }
                            case "PNG":
                            {
                                imageParser = new PngImageParser( );
                                break;
                            }
                            case "WBMP":
                            {
                                imageParser = new WbmpImageParser( );
                                break;
                            }
                            case "XBM":
                            {
                                imageParser = new XbmImageParser( );
                                break;
                            }
                            case "XPM":
                            {
                                imageParser = new XpmImageParser( );
                                break;
                            }
                            default:
                            {
                                throw new IOException( "Format of the original image is not" + " supported for write operation !" );
                            }

                        }
                        imageParser.writeImage( sanitizedImage, fos, null );
                    }

                }

                // Set state flag
                safeState = true;
            }
        }
        catch( Exception e )
        {
            safeState = false;
            AppLogService.error( "Error during Image file processing ! {}", e.getMessage( ), e );
        }

        return safeState;
    }

    /**
     * Returns a buffered image from the image byte array passed in parameter. fallbackOnApacheCommonsImaging parameter indicates which API will be used to read 
     * the file : Java standard API or Apache Commons Imaging. If it is equals to true, Java Standard API will not be able to read the file so Apache commons Imaging 
     * will be used to try to read the file.
     * 
     * @param fileImage
     *           image byte array
     * @param fallbackOnApacheCommonsImaging
     *           true if Apache Commons Imaging will be used to try to read the file, false if Java Standard API will be used
     * @return buffered image
     * @throws IOException
     * @throws ImageReadException
     */
    private static BufferedImage loadImage( byte[ ] fileImage, boolean fallbackOnApacheCommonsImaging ) throws IOException, ImageReadException
    {
    	BufferedImage originalImage;
        try ( ByteArrayInputStream bis = new ByteArrayInputStream( fileImage ) )
        {
        	if ( !fallbackOnApacheCommonsImaging )
            {
                originalImage = ImageIO.read( bis );
            }
            else
            {
                originalImage = Imaging.getBufferedImage( bis );
            }

            // Check that image has been successfully loaded
            if ( originalImage == null )
            {
                throw new IOException( "Cannot load the original image !" );
            }
        }
        return originalImage;
    }
    
    /**
     * imageBase64
     * @param imageResource
     * @return
     */
    public static String imageBase64 ( ImageResource imageResource )
    {
        if ( imageResource != null )
        {
            return "data:" + imageResource.getMimeType( ) + ";base64," + Base64.getEncoder( ).encodeToString( imageResource.getImage( ) );
        }
        return StringUtils.EMPTY;
    }
}
