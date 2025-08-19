<%@page import="fr.paris.lutece.plugins.galleryimage.web.ManageGalleryJspBean"%>

${ manageGalleryJspBean.init( pageContext.request, ManageGalleryJspBean.RIGHT_GALLERY_IMAGE_MANAGEMENT ) }
${ manageGalleryJspBean.getListGallery( pageContext.request ) }