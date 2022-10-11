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
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;

/**
 * This class provides Data Access methods for GalleryDAO objects
 */
public final class GalleryDAO implements IGalleryDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT = "SELECT id_gallery, code_gallery, label, gallery_image_type, height_gallery, width_gallery, authenticated_mode FROM galleryimage_gallery WHERE id_gallery = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO galleryimage_gallery ( label, gallery_image_type, height_gallery, width_gallery, authenticated_mode ) VALUES ( ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM galleryimage_gallery WHERE id_gallery = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE galleryimage_gallery SET id_gallery = ?, code_gallery = ?, label = ?, gallery_image_type = ?, height_gallery = ?, width_gallery = ?, authenticated_mode = ? WHERE id_gallery = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_gallery, code_gallery, label, gallery_image_type, height_gallery, width_gallery, authenticated_mode FROM galleryimage_gallery";
    private static final String SQL_QUERY_SELECT_BY_CODE = "SELECT id_gallery, code_gallery, label, gallery_image_type, height_gallery, width_gallery, authenticated_mode FROM galleryimage_gallery WHERE code_gallery = ?";

    /**
     * {@inheritDoc }
     */
    @Override
    public Gallery insert( Gallery gallery )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS, GalleryImagePlugin.getPlugin( ) ) )
        {
            int nIndex = 0;
            daoUtil.setString( ++nIndex, gallery.getLabel( ) );
            daoUtil.setString( ++nIndex, gallery.getGalleryImageType( ) );
            daoUtil.setInt( ++nIndex, gallery.getHeightGallery( ) );
            daoUtil.setInt( ++nIndex, gallery.getWidthGallery( ) );
            daoUtil.setBoolean( ++nIndex, gallery.isAuthenticatedMode( ) );
            
            daoUtil.executeUpdate( );
            if ( daoUtil.nextGeneratedKey( ) )
            {
                gallery.setIdGallery( daoUtil.getGeneratedKeyInt( 1 ) );
            }
            return gallery;
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Gallery load( int nId )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, GalleryImagePlugin.getPlugin( ) ) )
        {
            daoUtil.setInt( 1, nId );
            daoUtil.executeQuery( );

            Gallery gallery = null;

            if ( daoUtil.next( ) )
            {
                gallery = new Gallery( );
                int nIndex = 0;

                gallery.setIdGallery( daoUtil.getInt( ++nIndex ) );
                gallery.setCodeGallery( daoUtil.getString( ++nIndex ) );
                gallery.setLabel( daoUtil.getString( ++nIndex ) );
                gallery.setGalleryImageType( daoUtil.getString( ++nIndex ) );
                gallery.setHeightGallery( daoUtil.getInt( ++nIndex ) );
                gallery.setWidthGallery( daoUtil.getInt( ++nIndex ) );
                gallery.setAuthenticatedMode( daoUtil.getBoolean( ++nIndex ) );
            }

            return gallery;
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nGalleryId )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, GalleryImagePlugin.getPlugin( ) ) )
        {
            daoUtil.setInt( 1, nGalleryId );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( Gallery gallery )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, GalleryImagePlugin.getPlugin( ) ) )
        {
            int nIndex = 0;
            daoUtil.setInt( ++nIndex, gallery.getIdGallery( ) );
            daoUtil.setString( ++nIndex, gallery.getCodeGallery( ) );
            daoUtil.setString( ++nIndex, gallery.getLabel( ) );
            daoUtil.setString( ++nIndex, gallery.getGalleryImageType( ) );
            daoUtil.setInt( ++nIndex, gallery.getHeightGallery( ) );
            daoUtil.setInt( ++nIndex, gallery.getWidthGallery( ) );
            daoUtil.setBoolean( ++nIndex, gallery.isAuthenticatedMode( ) );
            
            daoUtil.setInt( ++nIndex, gallery.getIdGallery( ) );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Gallery> selectGalleryList( )
    {
        List<Gallery> listGallery = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, GalleryImagePlugin.getPlugin( ) ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                int nIndex = 0;
                Gallery gallery = new Gallery( );
                gallery.setIdGallery( daoUtil.getInt( ++nIndex ) );
                gallery.setCodeGallery( daoUtil.getString( ++nIndex ) );
                gallery.setLabel( daoUtil.getString( ++nIndex ) );
                gallery.setGalleryImageType( daoUtil.getString( ++nIndex ) );
                gallery.setHeightGallery( daoUtil.getInt( ++nIndex ) );
                gallery.setWidthGallery( daoUtil.getInt( ++nIndex ) );
                gallery.setAuthenticatedMode( daoUtil.getBoolean( ++nIndex ) );
                
                listGallery.add( gallery );
            }

            return listGallery;
        }
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public Gallery loadByCodeGallery( String strCodeGallery )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_CODE, GalleryImagePlugin.getPlugin( ) ) )
        {
            daoUtil.setString( 1, strCodeGallery );
            daoUtil.executeQuery( );

            Gallery gallery = null;

            if ( daoUtil.next( ) )
            {
                gallery = new Gallery( );
                int nIndex = 0;

                gallery.setIdGallery( daoUtil.getInt( ++nIndex ) );
                gallery.setCodeGallery( daoUtil.getString( ++nIndex ) );
                gallery.setLabel( daoUtil.getString( ++nIndex ) );
                gallery.setGalleryImageType( daoUtil.getString( ++nIndex ) );
                gallery.setHeightGallery( daoUtil.getInt( ++nIndex ) );
                gallery.setWidthGallery( daoUtil.getInt( ++nIndex ) );
                gallery.setAuthenticatedMode( daoUtil.getBoolean( ++nIndex ) );
            }

            return gallery;
        }
    }

}
