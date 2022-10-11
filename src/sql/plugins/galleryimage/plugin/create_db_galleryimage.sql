--
-- Structure for table galleryimage_gallery
--
DROP TABLE IF EXISTS galleryimage_gallery;
CREATE TABLE galleryimage_gallery (
	id_gallery int AUTO_INCREMENT,
	code_gallery LONG VARCHAR DEFAULT NULL,
	label VARCHAR(255) NOT NULL,
	gallery_image_type VARCHAR(255) NOT NULL,
	height_gallery INT NOT NULL,
	width_gallery INT NOT NULL,
	authenticated_mode SMALLINT default 0,
	PRIMARY KEY (id_gallery)
);

--
-- Structure for table galleryimage_image
--
DROP TABLE IF EXISTS galleryimage_image;
CREATE TABLE galleryimage_image (
	id_image int AUTO_INCREMENT,
	title VARCHAR(255) NOT NULL,
	description LONG VARCHAR NOT NULL,
	id_file INT NOT NULL,
	id_gallery INT NOT NULL,
	PRIMARY KEY (id_image)
);

--
-- Structure for table galleryimage_gallery_image
--
DROP TABLE IF EXISTS galleryimage_gallery_image;
CREATE TABLE galleryimage_gallery_image (
	id_gallery_image int AUTO_INCREMENT,
	id_gallery INT NOT NULL,
	id_image INT NOT NULL,
	PRIMARY KEY (id_gallery_image)
);