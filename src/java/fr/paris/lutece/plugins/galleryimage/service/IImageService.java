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
package fr.paris.lutece.plugins.galleryimage.service;

import java.util.List;

import fr.paris.lutece.plugins.galleryimage.business.Image;

/**
 * 
 * IImageService
 *
 */
public interface IImageService
{
    public static final String BEAN_NAME = "galleryimage.imageService";
    
    /**
     * Create an instance of the image class
     * 
     * @param image
     *            The instance of the Image which contains the informations to store
     * @param plugin
     *            the Plugin
     * @return The instance of image which has been created with its primary key.
     */
    Image create( Image image );

    /**
     * Update of the image which is specified in parameter
     * 
     * @param image
     *            The instance of the Image which contains the data to store
     * @param plugin
     *            the Plugin
     * @return The instance of the image which has been updated
     */
    Image update( Image image );

    /**
     * Remove the image whose identifier is specified in parameter
     * 
     * @param nImageId
     *            The image Id
     * @param plugin
     *            the Plugin
     */
    void remove( int nImageId );

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
    Image findByPrimaryKey( int nKey );

    /**
     * Returns an instance of a image whose identifier is specified in parameter
     * 
     * @param nKey
     *            The image primary key
     * @param plugin
     *            the Plugin
     * @return an instance of Image
     */
    Image findImageWithBinary( int nKey );

    /**
     * Load the data of all the image objects and returns them in form of a list
     * 
     * @param plugin
     *            the Plugin
     * @return the list which contains the data of all the image objects
     */
    List<Image> getImagesList( );

}
