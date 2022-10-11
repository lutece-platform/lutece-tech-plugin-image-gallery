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

import fr.paris.lutece.portal.service.rbac.RBACResource;

/**
 * This is the business class for the object Image
 */
public class Image implements RBACResource
{
    // RBAC management
    public static final String RESOURCE_TYPE = "GALLERYIMAGE_IMAGE_RESOURCE";

    // Perimissions
    public static final String PERMISSION_VIEW = "VIEW";
    public static final String PERMISSION_CREATE = "CREATE";
    public static final String PERMISSION_MODIFY = "MODIFY";
    public static final String PERMISSION_DELETE = "DELETE";
    
    // Variables declarations
    private int _nIdImage;
    private String _strTitle;
    private String _strDescription;
    private int _nIdFile;
    private int _nIdGallery;
    private String _strImageBase64;

    /**
     * Returns the IdImage
     * 
     * @return The IdImage
     */
    public int getIdImage( )
    {
        return _nIdImage;
    }

    /**
     * Sets the IdImage
     * 
     * @param nIdImage
     *            The IdImage
     */
    public void setIdImage( int nIdImage )
    {
        _nIdImage = nIdImage;
    }

    /**
     * Returns the Title
     * 
     * @return The Title
     */
    public String getTitle( )
    {
        return _strTitle;
    }

    /**
     * Sets the Title
     * 
     * @param strTitle
     *            The Title
     */
    public void setTitle( String strTitle )
    {
        _strTitle = strTitle;
    }

    /**
     * Returns the Description
     * 
     * @return The Description
     */
    public String getDescription( )
    {
        return _strDescription;
    }

    /**
     * Sets the Description
     * 
     * @param strDescription
     *            The Description
     */
    public void setDescription( String strDescription )
    {
        _strDescription = strDescription;
    }

    /**
     * @return the _nIdFile
     */
    public int getIdFile( )
    {
        return _nIdFile;
    }

    /**
     * @param nIdFile
     *            the _nIdFile to set
     */
    public void setIdFile( int nIdFile )
    {
        this._nIdFile = nIdFile;
    }
    
    /**
     * @return the _nIdGallery
     */
    public int getIdGallery( )
    {
        return _nIdGallery;
    }

    /**
     * @param nIdGallery the _nIdGallery to set
     */
    public void setIdGallery( int nIdGallery )
    {
        this._nIdGallery = nIdGallery;
    }
    
    /**
     * @return the _strImageBase64
     */
    public String getImageBase64( )
    {
        return _strImageBase64;
    }

    /**
     * @param strImageBase64 the _strImageBase64 to set
     */
    public void setImageBase64( String strImageBase64 )
    {
        this._strImageBase64 = strImageBase64;
    }

    @Override
    public String getResourceTypeCode( )
    {
        return RESOURCE_TYPE;
    }

    @Override
    public String getResourceId( )
    {
        return String.valueOf( _nIdImage );
    }

}
