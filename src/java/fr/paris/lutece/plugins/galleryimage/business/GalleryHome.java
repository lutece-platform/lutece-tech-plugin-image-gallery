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

import java.util.List;

import fr.paris.lutece.portal.service.spring.SpringContextService;

/**
 * 
 * GalleryHome
 *
 */
public final class GalleryHome
{
    private static IGalleryDAO _galleryDAO = SpringContextService.getBean( "galleryimage.galleryDAO" );

    /**
     * Private constructor
     */
    private GalleryHome( )
    {
        //
    }

    /**
     * Insert Gallery
     * 
     * @param gallery
     */
    public static Gallery create( Gallery gallery )
    {
        return _galleryDAO.insert( gallery );
    }

    /**
     * Remove Gallery
     * 
     * @param nIdGallery
     */
    public static void remove( int nIdGallery )
    {
        _galleryDAO.delete( nIdGallery );
    }

    /**
     * Update Gallery
     * 
     * @param gallery
     */
    public static void update( Gallery gallery )
    {
        _galleryDAO.store( gallery );
    }

    /**
     * Returns Gallery by id
     * 
     * @param nIdGallery
     * @return The gallery
     */
    public static Gallery find( int nIdGallery )
    {
        return _galleryDAO.load( nIdGallery );
    }
    
    /**
     * Returns Gallery by code gallery
     * 
     * @param strCodeGallery
     * @return The gallery
     */
    public static Gallery findByCodeGallery( String strCodeGallery )
    {
        return _galleryDAO.loadByCodeGallery( strCodeGallery );       
    }
    

    /**
     * Returns all Gallery
     * 
     * @return list of Gallery
     */
    public static List<Gallery> findAll( )
    {
        return _galleryDAO.selectGalleryList( );
    }
}
