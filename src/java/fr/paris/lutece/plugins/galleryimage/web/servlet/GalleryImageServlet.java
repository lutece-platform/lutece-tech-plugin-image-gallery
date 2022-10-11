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
package fr.paris.lutece.plugins.galleryimage.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.api.user.User;
import fr.paris.lutece.plugins.galleryimage.business.Gallery;
import fr.paris.lutece.plugins.galleryimage.business.GalleryHome;
import fr.paris.lutece.plugins.galleryimage.business.ImageHome;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.util.LocalizedPaginator;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * 
 * GalleryImageServlet
 *
 */
public class GalleryImageServlet extends HttpServlet
{

    /**
     * 
     */
    private static final long serialVersionUID = 6470921900283978447L;

    // TEMPLATE
    private static final String TEMPLATE_GALLERY_IMAGE = "skin/plugins/galleryimage/gallery_image.html";
    private static final String TEMPLATE_LIST_IMAGE = "skin/plugins/galleryimage/list_image.html";

    // MARKS
    private static final String MARK_LIST_IMAGES = "listImages";
    private static final String MARK_GALLERY_CONFIG = "config";
    private static final String MARK_INDEX = "index";
    private static final String MARK_PAGES_COUNT = "pagesCount";
    private static final String MARK_SHOW_PREVIOUS_BTN = "showPreviousBtn";
    private static final String MARK_SHOW_NEXT_BTN = "showNextBtn";
    private static final String MARK_INPUT_NAME = "inputName";
    private static final String MARK_ID_FILE = "idFileSelected";
    
    // PARAMETER
    private static final String PARAMTER_CODE_GALLERY = "codeGallery";
    private static final String PARAMTER_INPUT_NAME = "inputName";
    private static final String PARAMTER_ID_FILE = "idFileSelected";

    // PROPERTY
    private static final String PROPERTY_NB_ITEMS = "galleryimage.paginator.gallery.front.numberOfItems";
    private static final String UNAUTHORIZED = "unauthorized";
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        String strCodeGallery = request.getParameter( PARAMTER_CODE_GALLERY );        
        String strIndex = request.getParameter( MARK_INDEX );
        int nIndex = StringUtils.isEmpty( strIndex ) ? 1 : Integer.parseInt( strIndex );
        int nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_NB_ITEMS, 5 );

        Map<String, Object> model = new HashMap<>( );

        if ( StringUtils.isNotEmpty( strCodeGallery ) )
        {
            Gallery gallery = GalleryHome.findByCodeGallery( strCodeGallery );
            PrintWriter out = response.getWriter( );
            
            if( gallery.isAuthenticatedMode( )&&
                    !RBACService.isAuthorized( Gallery.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, Gallery.PERMISSION_VIEW, getUser( request ) ) )
            {
                out.println( UNAUTHORIZED );
            }
            else
            {          
                List<Integer> listIds = ImageHome.getIdsListLinkedToGallery( gallery.getIdGallery( ) );
                
                LocalizedPaginator<Integer> paginator = new LocalizedPaginator<>( listIds, nDefaultItemsPerPage, StringUtils.EMPTY,
                        StringUtils.EMPTY, String.valueOf( nIndex ), request.getLocale( ) );
                                        
                model.put( MARK_PAGES_COUNT, paginator.getPagesCount( ) );
                model.put( MARK_INDEX, nIndex );
                model.put( MARK_SHOW_PREVIOUS_BTN, nIndex > 1 );
                model.put( MARK_SHOW_NEXT_BTN, nIndex < paginator.getPagesCount( ) && paginator.getPagesCount( ) > 1 );
                model.put( MARK_LIST_IMAGES, ImageHome.getImagesByIdsWithBinary( paginator.getPageItems( ) ) );
                model.put( MARK_INPUT_NAME, request.getParameter( PARAMTER_INPUT_NAME ) );
                model.put( MARK_ID_FILE, request.getParameter( PARAMTER_ID_FILE ) );
                model.put( MARK_GALLERY_CONFIG, gallery );
                
                HtmlTemplate html ;
                if( nIndex < 2 )
                {
                    html = AppTemplateService.getTemplate( TEMPLATE_GALLERY_IMAGE, request.getLocale( ), model );
                }
                else
                {
                    html = AppTemplateService.getTemplate( TEMPLATE_LIST_IMAGE, request.getLocale( ), model );
                }
    
                out.println( html.getHtml( ) );
            }
        }
    }
    
    /**
     * Get user authenticated
     * @param request
     * @return user authenticated
     */
    private User getUser ( HttpServletRequest request )
    {
        User user = SecurityService.getInstance( ).getRegisteredUser( request );
        
        if( user == null )
        {
            user = AdminUserService.getAdminUser( request );
        }
        return user;
    }
}
