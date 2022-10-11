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
 * This class provides Data Access methods for GalleryImage objects
 */
public final class GalleryImageDAO implements IGalleryImageDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT = "SELECT id_gallery_image, id_gallery, id_image FROM galleryimage_gallery_image WHERE id_gallery_image = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO galleryimage_gallery_image ( id_gallery, id_image ) VALUES ( ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM galleryimage_gallery_image WHERE id_gallery_image = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE galleryimage_gallery_image SET id_gallery_image = ?, id_gallery = ?, id_image = ? WHERE id_gallery_image = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_gallery_image, id_gallery, id_image FROM galleryimage_gallery_image";
    private static final String SQL_QUERY_SELECT_BY_GALLERY = "SELECT id_gallery_image, id_gallery, id_image FROM galleryimage_gallery_image WHERE id_gallery = ?";
    private static final String SQL_QUERY_DELETE_BY_GALLERY = "DELETE FROM galleryimage_gallery_image WHERE id_gallery = ? ";
    private static final String SQL_QUERY_DELETE_BY_IMAGE = "DELETE FROM galleryimage_gallery_image WHERE id_image = ? ";
    private static final String SQL_QUERY_DELETE_BY_GALLERY_AND_IMAGE = "DELETE FROM galleryimage_gallery_image WHERE id_gallery = ? AND id_image = ? ";

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( GalleryImage galleryImage )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS, GalleryImagePlugin.getPlugin( ) ) )
        {
            int nIndex = 0;
            daoUtil.setInt( ++nIndex, galleryImage.getIdGallery( ) );
            daoUtil.setInt( ++nIndex, galleryImage.getIdImage( ) );

            daoUtil.executeUpdate( );
            if ( daoUtil.nextGeneratedKey( ) )
            {
                galleryImage.setIdGalleryImage( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public GalleryImage load( int nId )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, GalleryImagePlugin.getPlugin( ) ) )
        {
            daoUtil.setInt( 1, nId );
            daoUtil.executeQuery( );

            GalleryImage galleryImage = null;
            
            if ( daoUtil.next( ) )
            {
                int nIndex = 0;
                galleryImage = new GalleryImage( );

                galleryImage.setIdGalleryImage( daoUtil.getInt( ++nIndex ) );
                galleryImage.setIdGallery( daoUtil.getInt( ++nIndex ) );
                galleryImage.setIdImage( daoUtil.getInt( ++nIndex ) );
            }

            return galleryImage;
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<GalleryImage> findByGallery( int nIdGallery )
    {
        List<GalleryImage> listGalleryImages = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_GALLERY, GalleryImagePlugin.getPlugin( ) ) )
        {
            daoUtil.setInt( 1, nIdGallery );
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                int nIndex = 0;
                GalleryImage galleryImage = new GalleryImage( );
                galleryImage.setIdGalleryImage( daoUtil.getInt( ++nIndex ) );
                galleryImage.setIdGallery( daoUtil.getInt( ++nIndex ) );
                galleryImage.setIdImage( daoUtil.getInt( ++nIndex ) );
                listGalleryImages.add( galleryImage );
            }

            return listGalleryImages;
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nGalleryImageId )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, GalleryImagePlugin.getPlugin( ) ) )
        {
            daoUtil.setInt( 1, nGalleryImageId );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void deleteByGallery( int nGalleryId )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_BY_GALLERY, GalleryImagePlugin.getPlugin( ) ) )
        {
            daoUtil.setInt( 1, nGalleryId );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void deleteByImage( int nImageId )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_BY_IMAGE, GalleryImagePlugin.getPlugin( ) ) )
        {
            daoUtil.setInt( 1, nImageId );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void deleteByGalleryAndImage( int nIdGallery, int nIdImage )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_BY_GALLERY_AND_IMAGE, GalleryImagePlugin.getPlugin( ) ) )
        {
            daoUtil.setInt( 1, nIdGallery );
            daoUtil.setInt( 2, nIdImage );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( GalleryImage galleryImage )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, GalleryImagePlugin.getPlugin( ) ) )
        {
            int nIndex = 0;
            daoUtil.setInt( ++nIndex, galleryImage.getIdGalleryImage( ) );
            daoUtil.setInt( ++nIndex, galleryImage.getIdGallery( ) );
            daoUtil.setInt( ++nIndex, galleryImage.getIdImage( ) );
            daoUtil.setInt( ++nIndex, galleryImage.getIdGalleryImage( ) );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<GalleryImage> selectGalleryImagesList( )
    {
        List<GalleryImage> listGalleryImages = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, GalleryImagePlugin.getPlugin( ) ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                int nIndex = 0;
                GalleryImage galleryImage = new GalleryImage( );
                galleryImage.setIdGalleryImage( daoUtil.getInt( ++nIndex ) );
                galleryImage.setIdGallery( daoUtil.getInt( ++nIndex ) );
                galleryImage.setIdImage( daoUtil.getInt( ++nIndex ) );
                listGalleryImages.add( galleryImage );
            }

            return listGalleryImages;
        }
    }
}
