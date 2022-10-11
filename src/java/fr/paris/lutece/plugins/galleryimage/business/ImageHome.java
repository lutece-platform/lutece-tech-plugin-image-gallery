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
package fr.paris.lutece.plugins.galleryimage.business;

import fr.paris.lutece.plugins.galleryimage.service.GalleryImagePlugin;
import fr.paris.lutece.plugins.galleryimage.util.ImageUtils;
import fr.paris.lutece.portal.service.fileimage.FileImagePublicService;
import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import java.util.List;

import com.google.common.collect.Lists;

/**
 *  This class provides instances management methods (create, find, ...) for Image objects
 */
public final class ImageHome
{
    // Static variable pointed at the DAO instance
    private static IImageDAO _dao = (IImageDAO) SpringContextService.getBean( "galleryimage.imageDAO" );
    
    /**
     * Private constructor
     */
    private ImageHome( )
    {
        
    }
    
    /**
     * Create an instance of the image class
     * 
     * @param image
     *            The instance of the Image which contains the informations to store
     * @param plugin
     *            the Plugin
     * @return The instance of image which has been created with its primary key.
     */
    public static Image create( Image image )
    {
        _dao.insert( image, GalleryImagePlugin.getPlugin( ) );

        return image;
    }

    /**
     * Update of the image which is specified in parameter
     * 
     * @param image
     *            The instance of the Image which contains the data to store
     * @param plugin
     *            the Plugin
     * @return The instance of the image which has been updated
     */
    public static Image update( Image image )
    {
        _dao.store( image, GalleryImagePlugin.getPlugin( ) );

        return image;
    }

    /**
     * Remove the image whose identifier is specified in parameter
     * 
     * @param nImageId
     *            The image Id
     * @param plugin
     *            the Plugin
     */
    public static void remove( int nImageId )
    {
        _dao.delete( nImageId, GalleryImagePlugin.getPlugin( ) );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a image whose identifier is specified in parameter
     * 
     * @param nKey
     *            The image primary key
     * @param plugin
     *            the Plugin
     * @return an instance of Image
     */
    public static Image find( int nKey )
    {
        return _dao.load( nKey, GalleryImagePlugin.getPlugin( ) );
    }
    
    /**
     * Returns an instance of a image whose identifier is specified in parameter
     * 
     * @param nKey
     *            The image primary key
     * @param plugin
     *            the Plugin
     * @return an instance of Image
     */
    public static Image findWihtBinary( int nKey )
    {
        Image image = _dao.load( nKey, GalleryImagePlugin.getPlugin( ) );

        if( image != null )
        {
            FileImagePublicService.init( );
            ImageResource imageResource =  FileImagePublicService.getInstance( ).getImageResource( image.getIdFile( ) );
            image.setImageBase64( ImageUtils.imageBase64( imageResource ) );
        }
        return image;
    }

    /**
     * Load the data of all the image objects and returns them in form of a list
     * 
     * @param plugin
     *            the Plugin
     * @return the list which contains the data of all the image objects
     */
    public static List<Image> getImagesList( )
    {
        return _dao.selectImagesList( GalleryImagePlugin.getPlugin( ) );
    }
    
    /**
     * Load the data of all the image objects and returns them in form of a list
     * 
     * @param plugin
     *            the Plugin
     * @return the list which contains the data of all the image objects
     */
    public static List<Image> getImagesListWithBinary( )
    {        
        List<Image> listImages = _dao.selectImagesList( GalleryImagePlugin.getPlugin( ) );
        FileImagePublicService.init( );

        for ( Image image : listImages )
        {
            ImageResource imageResource =  FileImagePublicService.getInstance( ).getImageResource( image.getIdFile( ) );
            image.setImageBase64( ImageUtils.imageBase64( imageResource ) );
        }

        return listImages;
    }
    
    /**
     * Load the ids image of gallery
     * 
     * @param nIdGallery
     *            the nIdGallery
     * @return the list which contains the data of all the image objects
     */
    public static List<Integer> getIdsList( int nIdGallery )
    {
        return _dao.selectIdsByGallery( nIdGallery, GalleryImagePlugin.getPlugin( ) );
    }
    
    /**
     * Load all ids list
     * 
     * @param plugin
     *            the Plugin
     * @return the list which contains the data of all the image objects
     */
    public static List<Integer> getIdsList( )
    {
        return _dao.selectIds( GalleryImagePlugin.getPlugin( ) );
    }
    
    /**
     * Load the ids image linked with gallery
     * 
     * @param nIdGallery
     *            the nIdGallery
     * @return the list which contains the data of all the image objects
     */
    public static List<Integer> getIdsListLinkedToGallery( int nIdGallery )
    {
        return _dao.selectIdsLinkedToGallery( nIdGallery, GalleryImagePlugin.getPlugin( ) );
    }
      
    /**
     * Load images by ids
     * 
     * @param plugin
     *            the Plugin
     * @return the list which contains the data of all the image objects
     */
    public static List<Image> getImagesByIdsWithBinary( List<Integer > listIds )
    {
        List<Image> listImages = _dao.selectImagesByIds( listIds, GalleryImagePlugin.getPlugin( ) );
        FileImagePublicService.init( );
        
        for ( Image image : listImages )
        {
            ImageResource imageResource =  FileImagePublicService.getInstance( ).getImageResource( image.getIdFile( ) );
            image.setImageBase64( ImageUtils.imageBase64( imageResource ) );
        }

        return listImages;
    }
}
