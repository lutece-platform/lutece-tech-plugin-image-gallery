<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="manageImage" scope="session" class="fr.paris.lutece.plugins.galleryimage.web.ManageImageJspBean" />
<% manageImage.init( request, manageImage.RIGHT_GALLERY_IMAGE_MANAGEMENT ); %>
<%= manageImage.getListImage( request ) %>

<%@ include file="../../AdminFooter.jsp" %>

