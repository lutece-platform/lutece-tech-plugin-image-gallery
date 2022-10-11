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
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.sql.Statement;

/**
 * This class provides Data Access methods for Image objects
 */
public final class ImageDAO implements IImageDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT = "SELECT id_image, title, description, id_file, id_gallery FROM galleryimage_image WHERE id_image = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO galleryimage_image ( id_image, title, description, id_file, id_gallery ) VALUES ( ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM galleryimage_image WHERE id_image = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE galleryimage_image SET id_image = ?, title = ?, description = ?, id_file = ?, id_gallery = ? WHERE id_image = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_image, title, description, id_file, id_gallery FROM galleryimage_image WHERE id_gallery < 1 ";
    private static final String SQL_QUERY_SELECTALL_IDS = "SELECT id_image FROM galleryimage_image WHERE id_gallery < 1 ";
    private static final String SQL_QUERY_SELECTALL_IDS_BY_ID_GALLERY = "SELECT id_image FROM galleryimage_image WHERE id_gallery = ? "; 
    private static final String SQL_QUERY_SELECT_LINKED_IDS_BY_ID_GALLERY = "SELECT gi.id_image FROM galleryimage_image gi, galleryimage_gallery_image ggi WHERE gi.id_image = ggi.id_image AND ggi.id_gallery = ?";
    private static final String SQL_QUERY_SELECT_BY_IDS = "SELECT id_image, title, description, id_file, id_gallery FROM galleryimage_image WHERE id_image IN ( ";

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( Image image, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS, plugin ) )
        {
            int nIndex = 0;
            daoUtil.setInt( ++nIndex, image.getIdImage( ) );
            daoUtil.setString( ++nIndex, image.getTitle( ) );
            daoUtil.setString( ++nIndex, image.getDescription( ) );
            daoUtil.setInt( ++nIndex, image.getIdFile( ) );
            daoUtil.setInt( ++nIndex, image.getIdGallery( ) );
            
            daoUtil.executeUpdate( );
            if ( daoUtil.nextGeneratedKey( ) )
            {
                image.setIdImage( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Image load( int nId, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin ) )
        {
            daoUtil.setInt( 1, nId );
            daoUtil.executeQuery( );

            Image image = null;

            if ( daoUtil.next( ) )
            {
                image = new Image( );
                int nIndex = 0;
                image.setIdImage( daoUtil.getInt( ++nIndex ) );
                image.setTitle( daoUtil.getString( ++nIndex  ) );
                image.setDescription( daoUtil.getString( ++nIndex  ) );
                image.setIdFile( daoUtil.getInt( ++nIndex  ) );
                image.setIdGallery( daoUtil.getInt( ++nIndex  ) );
            }

            return image;
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nImageId, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin ) )
        {
            daoUtil.setInt( 1, nImageId );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( Image image, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
        {
            int nIndex = 0;
            daoUtil.setInt( ++nIndex, image.getIdImage( ) );
            daoUtil.setString( ++nIndex, image.getTitle( ) );
            daoUtil.setString( ++nIndex, image.getDescription( ) );
            daoUtil.setInt( ++nIndex, image.getIdFile( ) );
            daoUtil.setInt( ++nIndex, image.getIdGallery( ) );
            
            daoUtil.setInt( ++nIndex, image.getIdImage( ) );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Image> selectImagesList( Plugin plugin )
    {
        List<Image> listImages = new ArrayList<>( );

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                Image image = new Image( );
                int nIndex = 0;
                
                image.setIdImage( daoUtil.getInt( ++nIndex ) );
                image.setTitle( daoUtil.getString( ++nIndex ) );
                image.setDescription( daoUtil.getString( ++nIndex ) );
                image.setIdFile( daoUtil.getInt( ++nIndex ) );
                image.setIdGallery( daoUtil.getInt( ++nIndex ) );
                
                listImages.add( image );
            }

            return listImages;
        }
    }

    @Override
    public List<Integer> selectIdsLinkedToGallery( int nGalleryId, Plugin plugin )
    {
        List<Integer> listIdsImage = new ArrayList< >( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_LINKED_IDS_BY_ID_GALLERY, plugin ) )
        {
            daoUtil.setInt( 1, nGalleryId );
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                int nIndex = 0;               
                listIdsImage.add( daoUtil.getInt( ++nIndex ) );
            }

            return listIdsImage;
        }
    }

    @Override
    public List<Image> selectImagesByIds( List<Integer> listIds, Plugin plugin )
    {
        List<Image> listImages = new ArrayList< >( );
        String query = SQL_QUERY_SELECT_BY_IDS + listIds.stream( ).map( i -> "?" ).collect( Collectors.joining( "," ) ) + " )";

        try ( DAOUtil daoUtil = new DAOUtil( query, plugin ) )
        {
            if( !listIds.isEmpty( ) )
            {
                for ( int i = 0; i < listIds.size( ); i++ )
                {
                    daoUtil.setInt( i + 1, listIds.get( i ) );
                }
                
                daoUtil.executeQuery( );
    
                while ( daoUtil.next( ) )
                {
                    Image image = new Image( );
                    int nIndex = 0;
                    image.setIdImage( daoUtil.getInt( ++nIndex ) );
                    image.setTitle( daoUtil.getString( ++nIndex ) );
                    image.setDescription( daoUtil.getString( ++nIndex ) );
                    image.setIdFile( daoUtil.getInt( ++nIndex ) );
                    image.setIdGallery( daoUtil.getInt( ++nIndex ) );
                    
                    listImages.add( image );
                }
            }

            return listImages;
        }
    }

    @Override
    public List<Integer> selectIdsByGallery( int nGalleryId, Plugin plugin )
    {
        List<Integer> listIdsImage = new ArrayList< >( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_IDS_BY_ID_GALLERY, plugin ) )
        {
            daoUtil.setInt( 1, nGalleryId );
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                int nIndex = 0;               
                listIdsImage.add( daoUtil.getInt( ++nIndex ) );
            }

            return listIdsImage;
        }
    }

    @Override
    public List<Integer> selectIds( Plugin plugin )
    {
        List<Integer> listIdsImage = new ArrayList< >( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_IDS, plugin ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                int nIndex = 0;               
                listIdsImage.add( daoUtil.getInt( ++nIndex ) );
            }

            return listIdsImage;
        }
    }

}
