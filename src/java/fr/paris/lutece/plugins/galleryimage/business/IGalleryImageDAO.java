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

/**
 * IGalleryImageDAO Interface
 */

public interface IGalleryImageDAO
{

    /**
     * Insert a new record in the table.
     * 
     * @param galleryImage
     *            instance of the GalleryImage object to inssert
     */

    void insert( GalleryImage galleryImage );

    /**
     * Update the record in the table
     * 
     * @param galleryImage
     *            the reference of the GalleryImage
     */

    void store( GalleryImage galleryImage );

    /**
     * Delete a record from the table
     * 
     * @param nIdGalleryImage
     *            int identifier of the GalleryImage to delete
     */

    void delete( int nIdGalleryImage );

    /**
     * Delete a record by id gallery from the table
     * 
     * @param nIdGalleryImage
     *            int identifier of the GalleryImage to delete
     */

    void deleteByGallery( int nIdGallery );

    /**
     * Delete a record by id image from the table
     * 
     * @param nIdGalleryImage
     *            int identifier of the GalleryImage to delete
     */

    void deleteByImage( int nIdImage );

    /**
     * Delete a record by id gallery id image from the table
     * 
     * @param nIdGallery
     * @param nIdImage
     */
    void deleteByGalleryAndImage( int nIdGallery, int nIdImage );

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Load the data from the table
     * 
     * @param strId
     *            The identifier of the galleryImage
     * @return The instance of the galleryImage
     */

    GalleryImage load( int nKey );

    /**
     * 
     * @param nIdGallery
     * @return list of GalleryImage
     */
    List<GalleryImage> findByGallery( int nIdGallery );

    /**
     * Load the data of all the galleryImage objects and returns them as a List
     * 
     * @return The List which contains the data of all the galleryImage objects
     */

    List<GalleryImage> selectGalleryImagesList( );

}
