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
package fr.paris.lutece.plugins.galleryimage.web;

import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import com.rometools.rome.io.impl.Base64;

import fr.paris.lutece.api.user.User;
import fr.paris.lutece.plugins.galleryimage.business.Gallery;
import fr.paris.lutece.plugins.galleryimage.business.GalleryHome;
import fr.paris.lutece.plugins.galleryimage.business.GalleryImage;
import fr.paris.lutece.plugins.galleryimage.business.GalleryImageHome;
import fr.paris.lutece.plugins.galleryimage.business.Image;
import fr.paris.lutece.plugins.galleryimage.business.ImageHome;
import fr.paris.lutece.plugins.galleryimage.util.EnumGalleryImageType;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.security.RsaService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.web.util.LocalizedPaginator;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.AbstractPaginator;
import fr.paris.lutece.util.html.HtmlTemplate;

@Controller( controllerJsp = "ManageGallery.jsp", controllerPath = "jsp/admin/plugins/galleryImage/", right = ManageGalleryJspBean.RIGHT_GALLERY_IMAGE_MANAGEMENT )
public class ManageGalleryJspBean extends MVCAdminJspBean
{
    public static final String  RIGHT_GALLERY_IMAGE_MANAGEMENT      = "GALLERY_IMAGE_MANAGEMENT";

    // TEMPLATE
    private static final String TEMPLATE_MANAGE_GALLERY_IMAGE       = "admin/plugins/galleryimage/manage_gallery_image.html";
    private static final String TEMPLATE_CREATE_GALLERY_IMAGE       = "admin/plugins/galleryimage/create_gallery_image.html";
    private static final String TEMPLATE_MODIFY_GALLERY_IMAGE       = "admin/plugins/galleryimage/modify_gallery_image.html";
    private static final String TEMPLATE_SELECT_LIST_GALLERY        = "admin/plugins/galleryimage/select_list_gallery.html";
    private static final String TEMPLATE_MANAGE_IMAGE               = "admin/plugins/galleryimage/manage_image_of_gallery.html";

    // VIEW
    private static final String VIEW_MANAGE_GALLERY_IMAGE           = "manageGalleryImage";
    private static final String VIEW_CREATE_GALLERY_IMAGE           = "createGalleryImage";
    private static final String VIEW_MODIFY_GALLERY_IMAGE           = "modifyGalleryImage";
    private static final String VIEW_SELECT_LIST_GALLERY            = "selectListGallery";
    private static final String VIEW_LIST_GALLERY_IMAGE             = "listImagesGallery";

    // ACTION
    private static final String ACTION_CREATE_GALLERY_IMAGE         = "createGalleryImage";
    private static final String ACTION_MODIFY_GALLERY_IMAGE         = "modifyGalleryImage";
    private static final String ACTION_DELETE_GALLERY_IMAGE         = "deleteGalleryImage";
    private static final String ACTION_ADD_IMAGE_TO_GALLERY         = "addImageToGallery";
    private static final String ACTION_DELETE_IMAGE_TO_GALLERY      = "deleteImageToGallery";

    // MARKS
    private static final String MARK_LIST_GALLERY_TYPE              = "refListGallerytype";
    private static final String MARK_LIST_GALLERY_IMAGE_CONFIG      = "listGalleryImageConfig";
    private static final String MARK_GALLERY_IMAGE_CONFIG           = "config";
    private static final String MARK_LIST_GALLERY                   = "listGallery";
    private static final String MARK_LIST_IMAGE_SELECTED            = "listImageSelected";
    private static final String MARK_LIST_IMAGE_OF_GALLERY          = "listImageOfGallery";
    private static final String MARK_LIST_IMAGE_AVAILABLE           = "listAvailableImage";
    private static final String MARK_ID_GALLERY                     = "idGallery";
    private static final String MARK_PAGINATOR_IMAGE                = "paginatorImage";
    private static final String MARK_PAGINATOR_IMAGE_SELECTED       = "paginatorImageSelected";
    private static final String MARK_PAGINATOR_IMAGE_OF_GALLERY     = "paginatorImageOfGallery";
    private static final String MARK_NB_ITEMS_PER_PAGE              = "nb_items_per_page";

    // PARAMETER
    private static final String PARAMETER_ID                        = "id";
    private static final String PARAMETER_ID_IMAGE                  = "idImage";
    private static final String PROPERTY_NUMBER_OF_DEMAND_PER_PAGE  = "galleryimage.paginator.image_gallery.numberOfItemsPerPage";

    // CONSTANTS
    private static final String CURRENT_PAGE_INDEX_IMAGE            = "current_page_index_image";
    private static final String CURRENT_PAGE_INDEX_GALLERY_IMAGE    = "current_page_index_gallery_image";
    private static final String CURRENT_PAGE_INDEX_IMAGE_OF_GALLERY = "current_page_index_image_of_gallery";
    private static final String UNAUTHORIZED                        = "Unauthorized";

    /**
     * 
     */
    private static final long   serialVersionUID                    = -3336177193606492480L;

    @View( value = VIEW_MANAGE_GALLERY_IMAGE, defaultView = true )
    public String getManageGalleryImage( HttpServletRequest request )
    {
        Locale locale = getLocale( );
        Map<String, Object> model = new HashMap<>( );

        model.put( MARK_LIST_GALLERY_IMAGE_CONFIG, GalleryHome.findAll( ) );

        HtmlTemplate html = AppTemplateService.getTemplate( TEMPLATE_MANAGE_GALLERY_IMAGE, locale, model );

        return html.getHtml( );
    }

    @View( VIEW_CREATE_GALLERY_IMAGE )
    public String getCreateGalleryImage( HttpServletRequest request ) throws AccessDeniedException
    {
        if ( !RBACService.isAuthorized( Gallery.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, Gallery.PERMISSION_CREATE, ( User ) getUser( ) ) )
        {
            throw new AccessDeniedException( UNAUTHORIZED );
        }

        Locale locale = getLocale( );
        Map<String, Object> model = new HashMap<>( );

        model.put( MARK_LIST_GALLERY_TYPE, EnumGalleryImageType.getReferenceList( ) );

        HtmlTemplate html = AppTemplateService.getTemplate( TEMPLATE_CREATE_GALLERY_IMAGE, locale, model );

        return html.getHtml( );
    }

    @View( VIEW_MODIFY_GALLERY_IMAGE )
    public String getModifyGalleryImage( HttpServletRequest request ) throws AccessDeniedException
    {
        Locale locale = getLocale( );
        Map<String, Object> model = new HashMap<>( );

        String strIdGalleryImageConfig = request.getParameter( PARAMETER_ID );

        if ( StringUtils.isNumeric( strIdGalleryImageConfig ) )
        {
            Gallery gallery = GalleryHome.find( Integer.parseInt( strIdGalleryImageConfig ) );

            if ( !RBACService.isAuthorized( Gallery.RESOURCE_TYPE, gallery.getCodeGallery( ), Gallery.PERMISSION_MODIFY, ( User ) getUser( ) ) )
            {
                throw new AccessDeniedException( UNAUTHORIZED );
            }

            model.put( MARK_GALLERY_IMAGE_CONFIG, gallery );
        }

        model.put( MARK_LIST_GALLERY_TYPE, EnumGalleryImageType.getReferenceList( ) );

        HtmlTemplate html = AppTemplateService.getTemplate( TEMPLATE_MODIFY_GALLERY_IMAGE, locale, model );

        return html.getHtml( );
    }

    @Action( ACTION_CREATE_GALLERY_IMAGE )
    public String doCreateGalleryImage( HttpServletRequest request ) throws AccessDeniedException, GeneralSecurityException
    {
        if ( !RBACService.isAuthorized( Gallery.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, Gallery.PERMISSION_CREATE, ( User ) getUser( ) ) )
        {
            throw new AccessDeniedException( UNAUTHORIZED );
        }

        Gallery gallery = new Gallery( );
        populate( gallery, request );
        
        gallery = GalleryHome.create( gallery );
        //Generate gallery code
        gallery.setCodeGallery( DigestUtils.sha256Hex( String.valueOf( gallery.getIdGallery( ) )  ) );
        GalleryHome.update( gallery );
        
        return getManageGalleryImage( request );
    }

    @Action( ACTION_MODIFY_GALLERY_IMAGE )
    public String doModifyGalleryImage( HttpServletRequest request ) throws AccessDeniedException
    {
        Gallery config = new Gallery( );
        populate( config, request );

        if ( !RBACService.isAuthorized( Gallery.RESOURCE_TYPE, config.getCodeGallery( ), Gallery.PERMISSION_MODIFY, ( User ) getUser( ) ) )
        {
            throw new AccessDeniedException( UNAUTHORIZED );
        }

        if ( config.getIdGallery( ) > 0 )
        {
            GalleryHome.update( config );
        }

        return getManageGalleryImage( request );
    }

    @Action( ACTION_DELETE_GALLERY_IMAGE )
    public String doDeleteGalleryImage( HttpServletRequest request ) throws AccessDeniedException
    {
        String strIdGalleryImageConfig = request.getParameter( PARAMETER_ID );

        if ( StringUtils.isNumeric( strIdGalleryImageConfig ) )
        {
            Gallery gallery = GalleryHome.find( Integer.parseInt( strIdGalleryImageConfig ) );

            if ( !RBACService.isAuthorized( Gallery.RESOURCE_TYPE, gallery.getCodeGallery( ), Gallery.PERMISSION_DELETE, ( User ) getUser( ) ) )
            {
                throw new AccessDeniedException( UNAUTHORIZED );
            }

            GalleryImageHome.removeByGalleryId( gallery.getIdGallery( ) );
            GalleryHome.remove( gallery.getIdGallery( ) );
        }

        return getManageGalleryImage( request );
    }

    /**
     * getManageImage
     * 
     * @param request
     * @return
     * @throws AccessDeniedException
     */
    @View( VIEW_LIST_GALLERY_IMAGE )
    public String getListImagesGallery( HttpServletRequest request ) throws AccessDeniedException
    {
        String strIdGallery = request.getParameter( PARAMETER_ID );

        if ( StringUtils.isNumeric( strIdGallery ) )
        {
            Locale locale = getLocale( );
            Map<String, Object> model = new HashMap<>( );
            Gallery config = GalleryHome.find( Integer.parseInt( strIdGallery ) );

            if ( !RBACService.isAuthorized( Gallery.RESOURCE_TYPE, config.getCodeGallery( ), Gallery.PERMISSION_MANAGE_GALLERY_IMAGE, ( User ) getUser( ) ) )
            {
                throw new AccessDeniedException( ManageGalleryJspBean.UNAUTHORIZED );
            }

            List<Integer> listIdsSelected = ImageHome.getIdsListLinkedToGallery( config.getIdGallery( ) );
            List<Integer> listIdsAvailable = ImageHome.getIdsList( );
            List<Integer> listIdsAvailableOnlyGallery = ImageHome.getIdsList( config.getIdGallery( ) );
            
            LocalizedPaginator<Integer> paginatorImageSelected = getPaginator( request, listIdsSelected, CURRENT_PAGE_INDEX_GALLERY_IMAGE, strIdGallery );
            LocalizedPaginator<Integer> paginatorImageAvailable = getPaginator( request, listIdsAvailable, CURRENT_PAGE_INDEX_IMAGE, strIdGallery );
            LocalizedPaginator<Integer> paginatorImageAvailableOnlyGallery = getPaginator( request, listIdsAvailableOnlyGallery, CURRENT_PAGE_INDEX_IMAGE_OF_GALLERY, strIdGallery );

            model.put( MARK_LIST_IMAGE_AVAILABLE, ImageHome.getImagesByIdsWithBinary( paginatorImageAvailable.getPageItems( ) ) );
            model.put( MARK_LIST_IMAGE_SELECTED, ImageHome.getImagesByIdsWithBinary( paginatorImageSelected.getPageItems( ) ) );
            model.put( MARK_LIST_IMAGE_OF_GALLERY, ImageHome.getImagesByIdsWithBinary( paginatorImageAvailableOnlyGallery.getPageItems( ) ));
            model.put( MARK_ID_GALLERY, strIdGallery );
            model.put( MARK_PAGINATOR_IMAGE, paginatorImageAvailable );
            model.put( MARK_PAGINATOR_IMAGE_SELECTED, paginatorImageSelected );
            model.put( MARK_PAGINATOR_IMAGE_OF_GALLERY, paginatorImageAvailableOnlyGallery );
            model.put( MARK_NB_ITEMS_PER_PAGE, AppPropertiesService.getPropertyInt( PROPERTY_NUMBER_OF_DEMAND_PER_PAGE, 10 ) );

            HtmlTemplate html = AppTemplateService.getTemplate( TEMPLATE_MANAGE_IMAGE, locale, model );

            return html.getHtml( );
        }

        return getHomeUrl( request );
    }

    /**
     * doAddImageToGallery
     * 
     * @param request
     * @return
     * @throws AccessDeniedException
     */
    @Action( ACTION_ADD_IMAGE_TO_GALLERY )
    public String doAddImageToGallery( HttpServletRequest request ) throws AccessDeniedException
    {
        String strIdGallery = request.getParameter( PARAMETER_ID );
        String strIdImage = request.getParameter( PARAMETER_ID_IMAGE );

        if ( StringUtils.isNumeric( strIdGallery ) && StringUtils.isNumeric( strIdImage ) )
        {
            Gallery config = GalleryHome.find( Integer.parseInt( strIdGallery ) );

            if ( !RBACService.isAuthorized( Gallery.RESOURCE_TYPE, config.getCodeGallery( ), Gallery.PERMISSION_MANAGE_GALLERY_IMAGE, ( User ) getUser( ) ) )
            {
                throw new AccessDeniedException( ManageGalleryJspBean.UNAUTHORIZED );
            }

            Image image = ImageHome.find( Integer.parseInt( strIdImage ) );

            if ( image != null )
            {
                GalleryImageHome.removeByGalleryIdAndIdImage( config.getIdGallery( ), image.getIdImage( ) );               
                GalleryImage galleryImage = new GalleryImage( );
                galleryImage.setIdGallery( config.getIdGallery( ) );
                galleryImage.setIdImage( image.getIdImage( ) );

                GalleryImageHome.create( galleryImage );
                return getListImagesGallery( request );
            }
        }

        return getHomeUrl( request );
    }

    /**
     * doDeleteImageToGallery
     * 
     * @param request
     * @return
     * @throws AccessDeniedException
     */
    @Action( ACTION_DELETE_IMAGE_TO_GALLERY )
    public String doDeleteImageToGallery( HttpServletRequest request ) throws AccessDeniedException
    {
        String strIdGallery = request.getParameter( PARAMETER_ID );
        String strIdImage = request.getParameter( PARAMETER_ID_IMAGE );

        if ( StringUtils.isNumeric( strIdGallery ) && StringUtils.isNumeric( strIdImage ) )
        {
            int nIdGallery = Integer.parseInt( strIdGallery );
            int nIdImage = Integer.parseInt( strIdImage );

            Gallery gallery = GalleryHome.find( Integer.parseInt( strIdGallery ) );

            if ( !RBACService.isAuthorized( Gallery.RESOURCE_TYPE, gallery.getCodeGallery( ), Gallery.PERMISSION_MANAGE_GALLERY_IMAGE, ( User ) getUser( ) ) )
            {
                throw new AccessDeniedException( ManageGalleryJspBean.UNAUTHORIZED );
            }

            GalleryImageHome.removeByGalleryIdAndIdImage( nIdGallery, nIdImage );
            return getListImagesGallery( request );
        }

        return getHomeUrl( request );
    }

    @View( VIEW_SELECT_LIST_GALLERY )
    public String getListGallery( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<>( );
        ReferenceList refList = new ReferenceList( );

        for ( Gallery gallery : GalleryHome.findAll( ) )
        {
            refList.addItem( gallery.getCodeGallery( ), gallery.getLabel( ) );
        }

        model.put( MARK_LIST_GALLERY, refList );
        HtmlTemplate html = AppTemplateService.getTemplate( TEMPLATE_SELECT_LIST_GALLERY, getLocale( ), model );

        return html.getHtml( );
    }

    /**
     * Paginator
     * 
     * @param <T>
     * @param request
     * @param listItems
     * @param strAttrCurrentPageIndex
     * @param strIdGallery
     * @return
     */
    private static <T> LocalizedPaginator<T> getPaginator( HttpServletRequest request, List<T> listItems, String strAttrCurrentPageIndex, String strIdGallery )
    {
        HttpSession session = request.getSession( true );

        String strCurrentPageIndex = session.getAttribute( strAttrCurrentPageIndex ) != null ? ( String ) session.getAttribute( strAttrCurrentPageIndex ) : null;

        strCurrentPageIndex = AbstractPaginator.getPageIndex( request, strAttrCurrentPageIndex, strCurrentPageIndex );
        session.setAttribute( strAttrCurrentPageIndex, strCurrentPageIndex );

        int nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_NUMBER_OF_DEMAND_PER_PAGE, 5 );

        String strUrl = AppPathService.getBaseUrl( request ) + "jsp/admin/plugins/galleryimage/ManageGallery.jsp?view=listImagesGallery" + "&id=" + strIdGallery;

        // PAGINATOR
        return new LocalizedPaginator<>( listItems, nDefaultItemsPerPage, strUrl, strAttrCurrentPageIndex, strCurrentPageIndex, request.getLocale( ) );
    }
}
