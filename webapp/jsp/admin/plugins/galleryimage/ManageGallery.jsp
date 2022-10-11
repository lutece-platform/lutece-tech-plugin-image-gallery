<jsp:useBean id="manageGalleryImage" scope="session" class="fr.paris.lutece.plugins.galleryimage.web.ManageGalleryJspBean" />
<% String strContent = manageGalleryImage.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
