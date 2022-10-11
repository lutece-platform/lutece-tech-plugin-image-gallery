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

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.api.user.User;
import fr.paris.lutece.plugins.galleryimage.business.GalleryImageHome;
import fr.paris.lutece.plugins.galleryimage.business.Image;
import fr.paris.lutece.plugins.galleryimage.service.IImageService;
import fr.paris.lutece.plugins.galleryimage.util.ImageUtils;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.service.file.FileService;
import fr.paris.lutece.portal.service.fileimage.FileImagePublicService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import fr.paris.lutece.util.html.HtmlTemplate;

public class ManageImageJspBean extends PluginAdminPageJspBean
{

    public static final String RIGHT_GALLERY_IMAGE_MANAGEMENT = "GALLERY_IMAGE_MANAGEMENT";

    // JSP
    private static final String JSP_LIST_IMAGE = "/jsp/admin/plugins/galleryimage/GetListImage.jsp";
    private static final String JSP_MANAGE_IMAGE_OF_GALLERY = "jsp/admin/plugins/galleryimage/ManageGallery.jsp?view=listImagesGallery&id=";
    
    // TEMPLATE
    private static final String TEMPLATE_SAVE_IMAGE = "admin/plugins/galleryimage/manageimage/save_image.html";
    private static final String TEMPLATE_LIST_IMAGE = "admin/plugins/galleryimage/manageimage/list_image.html";
    private static final String TEMPLATE_MODIFY_IMAGE = "admin/plugins/galleryimage/manageimage/modify_image.html";

    // MARKS
    private static final String MARK_IMAGE = "image";
    private static final String MARK_LIST_IMAGE = "listImage";
    private static final String MARK_IMAGE_WIDTH = "image_width";
    private static final String MARK_IMAGE_CROPPABLE = "image_croppable";
    private static final String MARK_ID_GALLERY = "idGallery";

    // PARAMETERS
    private static final String PARAMETER_ID = "id";
    private static final String PARAMETER_ID_GALLERY = "idGallery";
    
    // PROPERIES
    private static final String PROPERTY_ERROR_SAFE_IMAGE = "galleryimage.error.file_not_safe";
    private static final String PROPERTY_ERROR_UNAUTHORIZED = "galleryimage.rbac.error.unauthorized";
    
    // SERVICES
    private IImageService _imageService = SpringContextService.getBean( IImageService.BEAN_NAME );

    /**
     * 
     */
    private static final long serialVersionUID = -3336177193606492480L;


    /**
     * getCreateImage
     * 
     * @return
     */
    public String getCreateImage( HttpServletRequest request )
    {
        if ( !RBACService.isAuthorized( Image.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, Image.PERMISSION_CREATE, (User) getUser( ) ) )
        {
            return AdminMessageService.getMessageUrl( request, PROPERTY_ERROR_UNAUTHORIZED, AdminMessage.TYPE_ERROR );
        }
        
        Locale locale = getLocale( );
        Map<String, Object> model = new HashMap<>( );
        
        model.put( MARK_ID_GALLERY, request.getParameter( PARAMETER_ID_GALLERY ) );
        
        HtmlTemplate html = AppTemplateService.getTemplate( TEMPLATE_SAVE_IMAGE, locale, model );

        return html.getHtml( );
    }

    /**
     * doCreateImage
     * 
     * @param request
     * @return
     */
    public String doCreateImage( HttpServletRequest request )
    {
        if ( !RBACService.isAuthorized( Image.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, Image.PERMISSION_CREATE, (User) getUser( ) ) )
        {
            return AdminMessageService.getMessageUrl( request, PROPERTY_ERROR_UNAUTHORIZED, AdminMessage.TYPE_ERROR );
        }
        
        String strImageWidth = request.getParameter( MARK_IMAGE_WIDTH );
        String strImageCroppable = request.getParameter( MARK_IMAGE_CROPPABLE );

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        Image image = new Image( );
        populate( image, request );

        FileItem fileParameterBinaryValue = multipartRequest.getFile( MARK_IMAGE );

        if ( !ImageUtils.safeImage( fileParameterBinaryValue ) )
        {
            return AdminMessageService.getMessageUrl( request, PROPERTY_ERROR_SAFE_IMAGE, AdminMessage.TYPE_ERROR );
        }

        if ( fileParameterBinaryValue != null && fileParameterBinaryValue.getSize( ) > 0 )
        {
            if ( "on".equals( strImageCroppable ) && StringUtils.isNumeric( strImageWidth ) )
            {
                fileParameterBinaryValue = ImageUtils.resizeImage( fileParameterBinaryValue, Integer.parseInt( strImageWidth ) );
            }

            FileImagePublicService.init( );
            image.setIdFile( Integer.parseInt( FileImagePublicService.getInstance( ).addImageResource( fileParameterBinaryValue ) ) );
            _imageService.create( image );
        }

        if( image.getIdGallery( ) > 0 )
        {
            return AppPathService.getBaseUrl( request ) + JSP_MANAGE_IMAGE_OF_GALLERY + image.getIdGallery( );
        } else
        {
            return AppPathService.getBaseUrl( request ) + JSP_LIST_IMAGE;
        }
    }

    /**
     * getListImage
     * 
     * @return
     */
    public String getListImage( HttpServletRequest request )
    {
        if ( !RBACService.isAuthorized( Image.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, Image.PERMISSION_VIEW, (User) getUser( ) ) )
        {
            return AdminMessageService.getMessageUrl( request, PROPERTY_ERROR_UNAUTHORIZED, AdminMessage.TYPE_ERROR );
        }
        
        Locale locale = getLocale( );
        Map<String, Object> model = new HashMap<>( );

        List<Image> listImage = _imageService.getImagesList( );

        model.put( MARK_LIST_IMAGE, listImage );

        HtmlTemplate html = AppTemplateService.getTemplate( TEMPLATE_LIST_IMAGE, locale, model );

        return html.getHtml( );
    }

    /**
     * getModifyImage
     * 
     * @param request
     * @return
     */
    public String getModifyImage( HttpServletRequest request )
    {
        if ( !RBACService.isAuthorized( Image.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, Image.PERMISSION_MODIFY, (User) getUser( ) ) )
        {
            return AdminMessageService.getMessageUrl( request, PROPERTY_ERROR_UNAUTHORIZED, AdminMessage.TYPE_ERROR );
        }
        
        String strIdImage = request.getParameter( PARAMETER_ID );

        Locale locale = getLocale( );
        Map<String, Object> model = new HashMap<>( );

        if ( StringUtils.isNumeric( strIdImage ) )
        {
            Image image = _imageService.findImageWithBinary( Integer.parseInt( strIdImage ) );

            model.put( MARK_IMAGE, image );
        }

        HtmlTemplate html = AppTemplateService.getTemplate( TEMPLATE_MODIFY_IMAGE, locale, model );

        return html.getHtml( );
    }

    /**
     * doModifyImage
     * 
     * @param request
     * @return
     */
    public String doModifyImage( HttpServletRequest request )
    {
        if ( !RBACService.isAuthorized( Image.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, Image.PERMISSION_MODIFY, (User) getUser( ) ) )
        {
            return AdminMessageService.getMessageUrl( request, PROPERTY_ERROR_UNAUTHORIZED, AdminMessage.TYPE_ERROR );
        }
        
        String strIdImage = request.getParameter( PARAMETER_ID );
        String strImageWidth = request.getParameter( MARK_IMAGE_WIDTH );
        String strImageCroppable = request.getParameter( MARK_IMAGE_CROPPABLE );

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        FileItem fileParameterBinaryValue = multipartRequest.getFile( MARK_IMAGE );

        if ( !ImageUtils.safeImage( fileParameterBinaryValue ) )
        {
            return AdminMessageService.getMessageUrl( request, PROPERTY_ERROR_SAFE_IMAGE, AdminMessage.TYPE_ERROR );
        }

        if ( StringUtils.isNumeric( strIdImage ) )
        {
            Image image = _imageService.findByPrimaryKey( Integer.parseInt( strIdImage ) );
            populate( image, request );

            if ( fileParameterBinaryValue != null && fileParameterBinaryValue.getSize( ) > 0 )
            {
                if ( "on".equals( strImageCroppable ) && StringUtils.isNumeric( strImageWidth ) )
                {
                    fileParameterBinaryValue = ImageUtils.resizeImage( fileParameterBinaryValue, Integer.parseInt( strImageWidth ) );
                }

                FileImagePublicService.init( );
                FileService.getInstance( ).getFileStoreServiceProvider( ).delete( String.valueOf( image.getIdFile( ) ) );

                image.setIdFile( Integer.parseInt( FileImagePublicService.getInstance( ).addImageResource( fileParameterBinaryValue ) ) );
            }
            _imageService.update( image );
            
            if( image.getIdGallery( ) > 0 )
            {
                return AppPathService.getBaseUrl( request ) + JSP_MANAGE_IMAGE_OF_GALLERY + image.getIdGallery( );
            }
        }

        return AppPathService.getBaseUrl( request ) + JSP_LIST_IMAGE;
    }

    /**
     * doDeleteImage
     * 
     * @param request
     * @return
     */
    public String doDeleteImage( HttpServletRequest request )
    {
        if ( !RBACService.isAuthorized( Image.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, Image.PERMISSION_DELETE, (User) getUser( ) ) )
        {
            return AdminMessageService.getMessageUrl( request, PROPERTY_ERROR_UNAUTHORIZED, AdminMessage.TYPE_ERROR );
        }
        
        String strIdImage = request.getParameter( PARAMETER_ID );

        if ( StringUtils.isNumeric( strIdImage ) )
        {
            Image image = _imageService.findByPrimaryKey( Integer.parseInt( strIdImage ) );

            FileService.getInstance( ).getFileStoreServiceProvider( ).delete( String.valueOf( image.getIdFile( ) ) );

            GalleryImageHome.removeByImageId( image.getIdImage( ) );
            _imageService.remove( image.getIdImage( ) );
            
            if( image.getIdGallery( ) > 0 )
            {
                return AppPathService.getBaseUrl( request ) + JSP_MANAGE_IMAGE_OF_GALLERY + image.getIdGallery( );
            }
        }

        return AppPathService.getBaseUrl( request ) + JSP_LIST_IMAGE;
    }

}
