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

import fr.paris.lutece.portal.service.spring.SpringContextService;
import java.util.List;

/**
 * This class provides instances management methods (create, find, ...) for GalleryImage objects
 */

public final class GalleryImageHome
{

    // Static variable pointed at the DAO instance
    private static IGalleryImageDAO _dao = (IGalleryImageDAO) SpringContextService.getBean( "galleryimage.galleryImageDAO" );

    /**
     * Private constructor - this class need not be instantiated
     */

    private GalleryImageHome( )
    {
    }

    /**
     * Create an instance of the galleryImage class
     * 
     * @param galleryImage
     *            The instance of the GalleryImage which contains the informations to store
     * @return The instance of galleryImage which has been created with its primary key.
     */

    public static GalleryImage create( GalleryImage galleryImage )
    {
        _dao.insert( galleryImage );

        return galleryImage;
    }

    /**
     * Update of the galleryImage data specified in parameter
     * 
     * @param galleryImage
     *            The instance of the GalleryImage which contains the data to store
     * @return The instance of the galleryImage which has been updated
     */

    public static GalleryImage update( GalleryImage galleryImage )
    {
        _dao.store( galleryImage );

        return galleryImage;
    }

    /**
     * Remove the galleryImage whose identifier is specified in parameter
     * 
     * @param nGalleryImageId
     *            The galleryImage Id
     */

    public static void remove( int nGalleryImageId )
    {
        _dao.delete( nGalleryImageId );
    }

    /**
     * Remove the galleryImage by id gallery
     * 
     * @param nGalleryId
     *            The gallery Id
     */
    public static void removeByGalleryId( int nGalleryId )
    {
        _dao.deleteByGallery( nGalleryId );
    }

    /**
     * Remove the galleryImage by id image
     * 
     * @param nImageId
     *            The gallery Id
     */
    public static void removeByImageId( int nImageId )
    {
        _dao.deleteByImage( nImageId );
    }
    
    /**
     * Remove the galleryImage by id gallery and id image
     * 
     * @param nGalleryId
     *            The gallery Id
     */
    public static void removeByGalleryIdAndIdImage( int nGalleryId, int nImageId  )
    {
        _dao.deleteByGalleryAndImage( nGalleryId, nImageId );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a galleryImage whose identifier is specified in parameter
     * 
     * @param nKey
     *            The galleryImage primary key
     * @return an instance of GalleryImage
     */

    public static GalleryImage findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey );
    }

    /**
     * Load the data of all the galleryImage bu gallery id
     * 
     * @param nKey
     *            The gallery id
     * @return the list of GalleryImage
     */

    public static List<GalleryImage> findByGalleryId( int nKey )
    {
        return _dao.findByGallery( nKey );
    }

    /**
     * Load the data of all the galleryImage objects and returns them in form of a collection
     * 
     * @return the list which contains the data of all the galleryImage objects
     */

    public static List<GalleryImage> findAll( )
    {
        return _dao.selectGalleryImagesList( );
    }
}
