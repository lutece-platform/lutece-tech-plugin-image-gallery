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
 * This is the business class for the object Gallery
 */
public class Gallery implements RBACResource
{
    // RBAC management
    public static final String RESOURCE_TYPE = "GALLERYIMAGE_RESOURCE";
    
    // Perimissions
    public static final String PERMISSION_VIEW = "VIEW";
    public static final String PERMISSION_CREATE = "CREATE";
    public static final String PERMISSION_MODIFY = "MODIFY";
    public static final String PERMISSION_DELETE = "DELETE";
    public static final String PERMISSION_MANAGE_GALLERY_IMAGE = "MANAGE_GALLERY_IMAGE";
    
    // Variables declarations
    private int _nIdGallery;
    private String _strCodeGallery;
    private String _strLabel;
    private String _strGalleryImageType;
    private int _nHeightGallery;
    private int _nWidthGallery;
    private boolean _bAuthenticatedMode;

    /**
     * Returns the IdGallery
     * 
     * @return The IdGallery
     */
    public int getIdGallery( )
    {
        return _nIdGallery;
    }

    /**
     * Sets the IdGallery
     * 
     * @param nIdGallery
     *            The IdGallery
     */
    public void setIdGallery( int nIdGallery )
    {
        _nIdGallery = nIdGallery;
    }


    /**
     * @return the _strCodeGallery
     */
    public String getCodeGallery( )
    {
        return _strCodeGallery;
    }

    /**
     * @param strCodeGallery the _strCodeGallery to set
     */
    public void setCodeGallery( String strCodeGallery )
    {
        this._strCodeGallery = strCodeGallery;
    }

    /**
     * Returns the Label
     * 
     * @return The Label
     */
    public String getLabel( )
    {
        return _strLabel;
    }

    /**
     * Sets the Label
     * 
     * @param strLabel
     *            The Label
     */
    public void setLabel( String strLabel )
    {
        _strLabel = strLabel;
    }

    /**
     * Returns the GalleryImageType
     * 
     * @return The GalleryImageType
     */
    public String getGalleryImageType( )
    {
        return _strGalleryImageType;
    }

    /**
     * Sets the GalleryImageType
     * 
     * @param strGalleryImageType
     *            The GalleryImageType
     */
    public void setGalleryImageType( String strGalleryImageType )
    {
        _strGalleryImageType = strGalleryImageType;
    }

    /**
     * Returns the HeightGallery
     * 
     * @return The HeightGallery
     */
    public int getHeightGallery( )
    {
        return _nHeightGallery;
    }

    /**
     * Sets the HeightGallery
     * 
     * @param nHeightGallery
     *            The HeightGallery
     */
    public void setHeightGallery( int nHeightGallery )
    {
        _nHeightGallery = nHeightGallery;
    }

    /**
     * Returns the WidthGallery
     * 
     * @return The WidthGallery
     */
    public int getWidthGallery( )
    {
        return _nWidthGallery;
    }

    /**
     * Sets the WidthGallery
     * 
     * @param nWidthGallery
     *            The WidthGallery
     */
    public void setWidthGallery( int nWidthGallery )
    {
        _nWidthGallery = nWidthGallery;
    }

    /**
     * @return the _bAuthenticatedMode
     */
    public boolean isAuthenticatedMode( )
    {
        return _bAuthenticatedMode;
    }

    /**
     * @param bAuthenticatedMode the _bAuthenticatedMode to set
     */
    public void setAuthenticatedMode( boolean bAuthenticatedMode )
    {
        this._bAuthenticatedMode = bAuthenticatedMode;
    }

    @Override
    public String getResourceTypeCode( )
    {
        return RESOURCE_TYPE;
    }

    @Override
    public String getResourceId( )
    {
        return getCodeGallery( );
    }
}
