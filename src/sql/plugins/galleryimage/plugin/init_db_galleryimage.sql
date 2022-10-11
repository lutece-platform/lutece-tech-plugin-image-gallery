--
-- Data for table core_admin_right
--
DELETE FROM core_admin_right WHERE id_right = 'GALLERY_IMAGE_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('GALLERY_IMAGE_MANAGEMENT','galleryimage.adminFeature.ManageGalleryImage.name',1,'jsp/admin/plugins/galleryimage/ManageGallery.jsp','galleryimage.adminFeature.ManageGalleryImage.description',0,'plugin-galleryimage','MANAGERS',NULL,NULL,4)
