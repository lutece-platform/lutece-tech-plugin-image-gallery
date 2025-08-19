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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.api.user.User;
import fr.paris.lutece.plugins.galleryimage.business.GalleryImageHome;
import fr.paris.lutece.plugins.galleryimage.business.Image;
import fr.paris.lutece.plugins.galleryimage.service.IImageService;
import fr.paris.lutece.plugins.galleryimage.util.ImageUtils;
import fr.paris.lutece.portal.business.file.File;
import fr.paris.lutece.portal.business.physicalfile.PhysicalFile;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.service.file.FileService;
import fr.paris.lutece.portal.service.file.FileServiceException;
import fr.paris.lutece.portal.service.file.IFileStoreServiceProvider;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.upload.MultipartItem;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import fr.paris.lutece.util.html.HtmlTemplate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

@RequestScoped
@Named
@Controller( controllerJsp = "ManageImage.jsp", controllerPath = "jsp/admin/plugins/galleryimage/", right = ManageGalleryJspBean.RIGHT_GALLERY_IMAGE_MANAGEMENT )
public class ManageImageJspBean extends MVCAdminJspBean
{

    public static final String RIGHT_GALLERY_IMAGE_MANAGEMENT = "GALLERY_IMAGE_MANAGEMENT";
    
    // TEMPLATE
    private static final String TEMPLATE_SAVE_IMAGE = "admin/plugins/galleryimage/manageimage/save_image.html";
    private static final String TEMPLATE_LIST_IMAGE = "admin/plugins/galleryimage/manageimage/list_image.html";
    private static final String TEMPLATE_MODIFY_IMAGE = "admin/plugins/galleryimage/manageimage/modify_image.html";

    // VIEWS
    private static final String VIEW_MANAGE_IMAGE = "manageImage";
    private static final String VIEW_CREATE_IMAGE = "createImage";
    private static final String VIEW_MODIFY_IMAGE = "modifyImage";
    
    // ACTIONS
    private static final String ACTION_CREATE_IMAGE = "createImage";
    private static final String ACTION_MODIFY_IMAGE = "modifyImage";
    private static final String ACTION_DELETE_IMAGE = "deleteImage";
    
    // MARKS
    private static final String MARK_IMAGE = "image";
    private static final String MARK_LIST_IMAGE = "listImage";
    private static final String MARK_IMAGE_WIDTH = "image_width";
    private static final String MARK_IMAGE_CROPPABLE = "image_croppable";
    private static final String MARK_ID_GALLERY = "idGallery";

    // PARAMETERS
    private static final String PARAMETER_ID = "id";
    private static final String PARAMETER_ID_GALLERY = "idGallery";
    private static final String PARAMETER_ID_GALLERY_IMAGE_GALLERY = "id";
    
    // PROPERTIES
    private static final String PROPERTY_ERROR_SAFE_IMAGE = "galleryimage.error.file_not_safe";
    private static final String PROPERTY_ERROR_MISSING_IMAGE = "galleryimage.error.file_missing";
    private static final String PROPERTY_ERROR_UNAUTHORIZED = "galleryimage.rbac.error.unauthorized";
    
    //CONSTANTS
    private static final String CROPPING_ACTION_ON = "on";
    
    // SERVICES
    @Inject
    private IImageService _imageService;
    
    @Inject
    private FileService _fileService;
    
    @Inject
	@Named( "defaultDatabaseFileStoreProvider" )
	private IFileStoreServiceProvider _fileStoreService;

    /**
     * 
     */
    private static final long serialVersionUID = -3336177193606492480L;


    /**
     * getCreateImage
     * 
     * @return
     */
    @View( VIEW_CREATE_IMAGE )
    public String getCreateImage( HttpServletRequest request )
    {
        if ( !RBACService.isAuthorized( Image.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, Image.PERMISSION_CREATE, (User) getUser( ) ) )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, PROPERTY_ERROR_UNAUTHORIZED, AdminMessage.TYPE_ERROR ) );
        }
        
        Locale locale = getLocale( );
        Map<String, Object> model = getModel( );
        
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
    @Action( ACTION_CREATE_IMAGE )
    public String doCreateImage( HttpServletRequest request )
    {
        if ( !RBACService.isAuthorized( Image.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, Image.PERMISSION_CREATE, (User) getUser( ) ) )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, PROPERTY_ERROR_UNAUTHORIZED, AdminMessage.TYPE_ERROR ) );
        }
        
        MultipartItem fileItem = ((MultipartHttpServletRequest) request).getFile( MARK_IMAGE );   
        
        if( fileItem == null || !( fileItem.getSize( ) > 0 ) )
        {
        	return redirect( request, AdminMessageService.getMessageUrl( request, PROPERTY_ERROR_MISSING_IMAGE, AdminMessage.TYPE_ERROR ) );
        }
        
        if ( !ImageUtils.safeImage( fileItem.get( ) ) )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, PROPERTY_ERROR_SAFE_IMAGE, AdminMessage.TYPE_ERROR ) );
        }
        
        Image image = createImage( request, fileItem );       

        if( image.getIdGallery( ) > 0 )
        {
            return redirectToListGalleryImageView( request, image.getIdGallery( ) );
        } else
        {
            return redirectView( request, VIEW_MANAGE_IMAGE );
        }
    }
    
    /**
     * Performs image creation
     * 
     * @param request
     * @param fileItem
     * @return image object
     */
    private Image createImage( HttpServletRequest request, MultipartItem fileItem )
    {
    	Image image = new Image( );
        populate( image, request );

        String strImageWidth = request.getParameter( MARK_IMAGE_WIDTH );
        String strImageCroppable = request.getParameter( MARK_IMAGE_CROPPABLE );
        	
        try( ByteArrayOutputStream imageOutputStream = getImageOutputStream( fileItem.get( ), strImageCroppable, strImageWidth ) )
        {
        	File file = getFile( fileItem.getName( ), fileItem.getContentType( ), imageOutputStream.toByteArray( ) );
        	image.setIdFile( Integer.parseInt( _fileStoreService.storeFile( file ) ) );
                    
        	image = _imageService.create( image );                	  
        }
        catch( FileServiceException | IOException e )
        {
        	AppLogService.error( e );
        }
        return image;
    }
    
    /**
     * Gets an outputStream from image byte array. The image is resized if resizing check box has been checked and if width is valid (numeric value)
     * 
     * @param fileImage
     * @param strImageCroppable
     * @param strImageWidth
     * @return ByteArrayOutputStream object
     */
    private ByteArrayOutputStream getImageOutputStream( byte[ ] fileImage, String strImageCroppable, String strImageWidth )
    {
    	if ( isValidCroppingActionPerformed( strImageCroppable, strImageWidth ) )
        {
    		return ImageUtils.getImageOutputStream( fileImage, strImageWidth );
        }

    	return ImageUtils.getImageOutputStream( fileImage, null );
    }
    
    /**
     *  returns a file object from image byte array
     * 
     * @param fileName
     * @param mimeType
     * @param content
     * @return file
     */
    private File getFile( String fileName, String fileMimeType, byte[] fileImageContent )
    {
    	File file = new File( );
        file.setTitle( fileName );
        file.setMimeType( fileMimeType );
        file.setSize( fileImageContent.length );

        PhysicalFile physicalFile = new PhysicalFile( );
        physicalFile.setValue( fileImageContent );

        file.setPhysicalFile( physicalFile );
        
        return file;
    }

    /**
     * Redirect to list image gallery view
     * 
     * @param request
     * @param idGallery
     * @return list gallery iage view
     */
    private String redirectToListGalleryImageView( HttpServletRequest request, int idGallery )
    {
    	Map<String, String> mapParameters = new LinkedHashMap<>( );
        mapParameters.put( PARAMETER_ID_GALLERY_IMAGE_GALLERY, String.valueOf( idGallery ) );
    	return redirect( request, ManageGalleryJspBean.VIEW_LIST_GALLERY_IMAGE, mapParameters );
    }
    
    /**
     * getListImage
     * 
     * @return
     */
    @View( value = VIEW_MANAGE_IMAGE, defaultView = true )
    public String getListImage( HttpServletRequest request )
    {
        if ( !RBACService.isAuthorized( Image.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, Image.PERMISSION_VIEW, (User) getUser( ) ) )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, PROPERTY_ERROR_UNAUTHORIZED, AdminMessage.TYPE_ERROR ) );
        }
        
        Locale locale = getLocale( );
        Map<String, Object> model = getModel( );

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
    @View( VIEW_MODIFY_IMAGE )
    public String getModifyImage( HttpServletRequest request )
    {
        if ( !RBACService.isAuthorized( Image.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, Image.PERMISSION_MODIFY, (User) getUser( ) ) )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, PROPERTY_ERROR_UNAUTHORIZED, AdminMessage.TYPE_ERROR ) );
        }
        
        String strIdImage = request.getParameter( PARAMETER_ID );

        Locale locale = getLocale( );
        Map<String, Object> model = getModel( );

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
    @Action( ACTION_MODIFY_IMAGE )
    public String doModifyImage( HttpServletRequest request )
    {
        if ( !RBACService.isAuthorized( Image.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, Image.PERMISSION_MODIFY, (User) getUser( ) ) )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, PROPERTY_ERROR_UNAUTHORIZED, AdminMessage.TYPE_ERROR ) );
        }
        
        String strIdImage = request.getParameter( PARAMETER_ID );

        MultipartItem fileItem = ((MultipartHttpServletRequest) request).getFile( MARK_IMAGE );
        
        if ( fileItem != null && fileItem.getSize( ) > 0 && !ImageUtils.safeImage( fileItem.get( ) ) )
        {
            return AdminMessageService.getMessageUrl( request, PROPERTY_ERROR_SAFE_IMAGE, AdminMessage.TYPE_ERROR );
        }
        
        if ( StringUtils.isNumeric( strIdImage ) )
        {
        	Image image = updateImage( request, fileItem, strIdImage );
            
            if( image.getIdGallery( ) > 0 )
            {
            	return redirectToListGalleryImageView( request, image.getIdGallery( ) );
            }
        }
        
        return redirectView( request, VIEW_MANAGE_IMAGE );
    }
    
    /**
     * Performs image update
     * 
     * @param request
     * @param fileItem
     * @param strIdImage
     * @return image object
     */
    private Image updateImage( HttpServletRequest request, MultipartItem fileItem, String strIdImage )
    {	
        Image image = _imageService.findByPrimaryKey( Integer.parseInt( strIdImage ) );
        populate( image, request );

        String strImageWidth = request.getParameter( MARK_IMAGE_WIDTH );
        String strImageCroppable = request.getParameter( MARK_IMAGE_CROPPABLE );
       
        String fileName = null;
        String fileMimeType = null;
        byte[ ] fileImageContent = null;
        
        if ( isNewImageLoaded( fileItem ) )
        {       	
        	fileName = fileItem.getName( );
        	fileMimeType = fileItem.getContentType( );
        	fileImageContent = fileItem.get( );
        }
        else
        {
        	if( isValidCroppingActionPerformed( strImageCroppable, strImageWidth ) )
            {
        		try
        		{
        			File currentFile = _fileService.getFileStoreServiceProvider( ).getFile( String.valueOf( image.getIdFile( ) ) );
        			if( currentFile != null && currentFile.getPhysicalFile( ) != null )
            		{        				
            			fileName = currentFile.getTitle( );
            			fileMimeType = currentFile.getMimeType( );
            			fileImageContent = currentFile.getPhysicalFile( ).getValue( );
            		}
        		}
        		catch( FileServiceException e )
                {
                	AppLogService.error( e );
                }        		
            }
        	else
        	{
        		//No action (actually, cropping) is performed on the current image so there is no need to update the content of the image file
        	}
        }
        
        if( fileImageContent != null )
        {
        	try( ByteArrayOutputStream imageOutputStream = getImageOutputStream( fileImageContent, strImageCroppable, strImageWidth ) )
        	{
        		_fileService.getFileStoreServiceProvider( ).delete( String.valueOf( image.getIdFile( ) ) );
            	
                File file = getFile( fileName, fileMimeType, imageOutputStream.toByteArray( ) );                       
                image.setIdFile( Integer.parseInt( _fileStoreService.storeFile( file ) ) );
        	}
        	catch( FileServiceException | IOException e )
            {
            	AppLogService.error( e );
            }
        }
            
        return _imageService.update( image );
    }
    
    /**
     * Checks if a new image has been uploaded to replace current one
     * 
     * @param fileItem
     * @return true if a new image has been uploaded in the form, false otherwise
     */
    private boolean isNewImageLoaded( MultipartItem fileItem )
    {
    	return fileItem != null && fileItem.getSize( ) > 0;
    }
    
    /**
     * Checks if the image (current or new) must be cropped. Cropping/resizing is considered valid if entered width is numeric
     * 
     * @param strImageCroppable
     * @param strImageWidth
     * @return true if a cropping action has been asked with a valid width, false otherwise
     */
    private boolean isValidCroppingActionPerformed( String strImageCroppable, String strImageWidth )
    {
    	return CROPPING_ACTION_ON.equals( strImageCroppable ) && StringUtils.isNumeric( strImageWidth );
    }
    
    /**
     * doDeleteImage
     * 
     * @param request
     * @return
     */
    @Action( value = ACTION_DELETE_IMAGE, securityTokenDisabled = true )
    public String doDeleteImage( HttpServletRequest request )
    {
        if ( !RBACService.isAuthorized( Image.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, Image.PERMISSION_DELETE, (User) getUser( ) ) )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, PROPERTY_ERROR_UNAUTHORIZED, AdminMessage.TYPE_ERROR ) );
        }
        
        String strIdImage = request.getParameter( PARAMETER_ID );

        if ( StringUtils.isNumeric( strIdImage ) )
        {
            Image image = _imageService.findByPrimaryKey( Integer.parseInt( strIdImage ) );

            try
            {
            	_fileService.getFileStoreServiceProvider( ).delete( String.valueOf( image.getIdFile( ) ) );
            }
            catch( FileServiceException e )
            {
            	AppLogService.error( e );
            }

            GalleryImageHome.removeByImageId( image.getIdImage( ) );
            _imageService.remove( image.getIdImage( ) );
            
            if( image.getIdGallery( ) > 0 )
            {
            	return redirectToListGalleryImageView( request, image.getIdGallery( ) );
            }
        }

        return redirectView( request, VIEW_MANAGE_IMAGE );
    }

}
