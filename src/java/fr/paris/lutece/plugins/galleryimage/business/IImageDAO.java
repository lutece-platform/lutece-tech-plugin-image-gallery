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

import fr.paris.lutece.portal.service.plugin.Plugin;
import java.util.List;

/**
 * IImageDAO Interface
 */

public interface IImageDAO
{

    /**
     * Insert a new record in the table.
     * 
     * @param image
     *            instance of the Image object to inssert
     * @param plugin
     *            the Plugin
     */

    void insert( Image image, Plugin plugin );

    /**
     * Update the record in the table
     * 
     * @param image
     *            the reference of the Image
     * @param plugin
     *            the Plugin
     */

    void store( Image image, Plugin plugin );

    /**
     * Delete a record from the table
     * 
     * @param nIdImage
     *            int identifier of the Image to delete
     * @param plugin
     *            the Plugin
     */

    void delete( int nIdImage, Plugin plugin );

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Load the data from the table
     * 
     * @param strId
     *            The identifier of the image
     * @param plugin
     *            the Plugin
     * @return The instance of the image
     */

    Image load( int nKey, Plugin plugin );

    /**
     * Load the data of all the image objects and returns them as a List
     * 
     * @param plugin
     *            the Plugin
     * @param displayType
     *            the displayType ( Value by null if you want all the images)
     * @return The List which contains the data of all the image objects
     */

    List<Image> selectImagesList( Plugin plugin );
    
    /**
     * Load the list of image identifiers that are linked with a gallery
     * 
     * @param nGalleryId
     *            the gallery id
     * @param plugin
     *            the Plugin
     * @return The list of image identifiers that are linked with a gallery
     */
    
    List<Integer> selectIdsLinkedToGallery( int nGalleryId, Plugin plugin );
    
    /**
     * Load list of ids image by id gallery
     * 
     * @param nGalleryId
     *            the gallery id
     * @param plugin
     *            the Plugin
     * @return The List which contains the data of all the image objects
     */
    
    List<Integer> selectIdsByGallery( int nGalleryId, Plugin plugin );
    
    /**
     * Load list of ids image
     * 
     * @param nGalleryId
     *            the gallery id
     * @param plugin
     *            the Plugin
     * @return The List which contains the data of all the image objects
     */
    
    List<Integer> selectIds( Plugin plugin );
    
    /**
     * Load list images by ids
     * 
     * @param listIds
     *            the list ids
     * @param plugin
     *            the Plugin
     * @return The List which contains the data of all the image objects
     */
    
    List<Image> selectImagesByIds( List<Integer> listIds, Plugin plugin );
}
