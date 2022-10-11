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
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.galleryimage.business.Gallery;
import fr.paris.lutece.plugins.galleryimage.business.GalleryHome;
import fr.paris.lutece.portal.service.rbac.Permission;
import fr.paris.lutece.portal.service.rbac.ResourceIdService;
import fr.paris.lutece.portal.service.rbac.ResourceType;
import fr.paris.lutece.portal.service.rbac.ResourceTypeManager;
import fr.paris.lutece.util.ReferenceList;

/**
 * 
 * GalleryResourceIdService
 *
 */
public class GalleryResourceIdService extends ResourceIdService
{
    private static final String PROPERTY_LABEL_RESOURCE_TYPE = "galleryimage.rbac.gallery.resourceType";
    private static final String PROPERTY_LABEL_VIEW = "galleryimage.rbac.gallery.permission.view";
    private static final String PROPERTY_LABEL_CREATE = "galleryimage.rbac.gallery.permission.create";
    private static final String PROPERTY_LABEL_DELETE = "galleryimage.rbac.gallery.permission.delete";
    private static final String PROPERTY_LABEL_MODIFY = "galleryimage.rbac.gallery.permission.modify";
    private static final String PROPERTY_LABEL_MANAGE_GALLERY_IMAGE = "galleryimage.rbac.gallery.permission.manage_gallery_image";

    /**
     * {@inheritDoc}
     */
    @Override
    public void register(  )
    {
        ResourceType rt = new ResourceType(  );
        
        rt.setResourceIdServiceClass( GalleryResourceIdService.class.getName(  ) );
        rt.setPluginName( GalleryImagePlugin.PLUGIN_NAME );
        rt.setResourceTypeKey( Gallery.RESOURCE_TYPE );
        rt.setResourceTypeLabelKey( PROPERTY_LABEL_RESOURCE_TYPE );
        
        Permission p = new Permission(  );
        p.setPermissionKey( Gallery.PERMISSION_VIEW );
        p.setPermissionTitleKey( PROPERTY_LABEL_VIEW );
        rt.registerPermission( p );
        
        p = new Permission(  );
        p.setPermissionKey( Gallery.PERMISSION_CREATE );
        p.setPermissionTitleKey( PROPERTY_LABEL_CREATE );
        rt.registerPermission( p );
        
        p = new Permission(  );
        p.setPermissionKey( Gallery.PERMISSION_MODIFY );
        p.setPermissionTitleKey( PROPERTY_LABEL_MODIFY );
        rt.registerPermission( p );
        
        p = new Permission(  );
        p.setPermissionKey( Gallery.PERMISSION_DELETE );
        p.setPermissionTitleKey( PROPERTY_LABEL_DELETE );
        rt.registerPermission( p );
        
        p = new Permission(  );
        p.setPermissionKey( Gallery.PERMISSION_MANAGE_GALLERY_IMAGE );
        p.setPermissionTitleKey( PROPERTY_LABEL_MANAGE_GALLERY_IMAGE);
        rt.registerPermission( p );
        
        
        ResourceTypeManager.registerResourceType( rt );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReferenceList getResourceIdList( Locale locale )
    {        
        ReferenceList list = new ReferenceList( );
        List<Gallery> listGallery = GalleryHome.findAll( );
        for ( Gallery gallery : listGallery )
        {
            list.addItem( gallery.getResourceId( ), gallery.getLabel( ) );
        }
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle( String strId, Locale locale )
    {
        return StringUtils.EMPTY;
    }

}
