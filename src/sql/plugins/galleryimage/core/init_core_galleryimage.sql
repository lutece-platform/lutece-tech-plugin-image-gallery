-- liquibase formatted sql
-- changeset galleryimage:init_core_galleryimage.sql
-- preconditions onFail:MARK_RAN onError:WARN
--
-- Data for table core_user_right
--
DELETE FROM core_user_right WHERE id_right = 'GALLERY_IMAGE_MANAGEMENT' AND id_user = 1;
INSERT INTO core_user_right (id_right,id_user) VALUES ('GALLERY_IMAGE_MANAGEMENT',1);
