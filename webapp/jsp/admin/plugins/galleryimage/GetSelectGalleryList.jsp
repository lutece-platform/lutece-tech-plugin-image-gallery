<jsp:useBean id="manageGallery" scope="session" class="fr.paris.lutece.plugins.galleryimage.web.ManageGalleryJspBean" />
<% manageGallery.init( request, manageGallery.RIGHT_GALLERY_IMAGE_MANAGEMENT ); %>
<%= manageGallery.getListGallery( request ) %>