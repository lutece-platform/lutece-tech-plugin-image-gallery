/*
 * Copyright (c) 2002-2026, City of Paris
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

import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.galleryimage.business.Gallery;
import fr.paris.lutece.plugins.galleryimage.business.Image;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.test.LuteceTestCase;

/**
 * Renders the galleryimage templates under HTML auto-escaping (LUT-33153): the data is written as text in the HTML, as a
 * JavaScript string or as a number in the scripts
 */
public class GalleryImageTemplatesRenderTest extends LuteceTestCase
{
    private static final String TEMPLATE_GALLERY_IMAGE = "skin/plugins/galleryimage/gallery_image.html";
    private static final String TITLE = "Parc <b>\"l'été\"</b>";
    private static final String TITLE_ESCAPED = "Parc &lt;b&gt;&quot;l&#39;été&quot;&lt;/b&gt;";
    private static final String CODE_GALLERY = "parc'</script>";
    private static final String CODE_GALLERY_JS = "parc\\'<\\/script>";

    @Test
    public void testGalleryWritesTheDataAsTextAndInItsScripts( )
    {
        Map<String, Object> model = new HashMap<>( );
        model.put( "config", gallery( ) );
        model.put( "listImages", List.of( image( ) ) );
        model.put( "pagesCount", 12 );
        model.put( "index", 1 );
        model.put( "inputName", "attribute1" );
        model.put( "idFileSelected", "7&8" );

        String strHtml = AppTemplateService.getTemplate( TEMPLATE_GALLERY_IMAGE, Locale.FRENCH, model ).getHtml( );

        assertTrue( strHtml, strHtml.contains( "title=\"Selectionner " + TITLE_ESCAPED + "\"" ) );
        assertTrue( strHtml, strHtml.contains( "alt=\"" + TITLE_ESCAPED + "\"" ) );
        assertFalse( strHtml, strHtml.contains( "<b>" ) );
        assertTrue( strHtml, strHtml.contains( "codeGallery:'" + CODE_GALLERY_JS + "'" ) );
        assertTrue( strHtml, strHtml.contains( "var parginator = 12;" ) );
        assertTrue( strHtml, strHtml.contains( "var index = 1;" ) );
        assertTrue( strHtml, strHtml.contains( "&id=7%268\"" ) );
        assertTrue( strHtml, strHtml.contains( "width: 1200px;" ) );
        assertFalse( strHtml, strHtml.contains( "onkeypress" ) );
    }

    @Test
    public void testMacroWritesItsArgumentsAsJavaScriptStrings( )
    {
        String strTemplate = "<#include \"/commons_gallery_image.html\" /><@galleryImage codeGallery=code inputName='attribute1' idFileSelected=idFile />";
        Map<String, Object> model = new HashMap<>( );
        model.put( "code", CODE_GALLERY );
        model.put( "idFile", 12345 );

        String strHtml = AppTemplateService.getTemplateFromStringFtl( strTemplate, Locale.FRENCH, model ).getHtml( );

        assertTrue( strHtml, strHtml.contains( "codeGallery:'" + CODE_GALLERY_JS + "'" ) );
        assertTrue( strHtml, strHtml.contains( "inputName:'attribute1'" ) );
        assertTrue( strHtml, strHtml.contains( "idFileSelected:'12345'" ) );
    }

    private static Gallery gallery( )
    {
        Gallery gallery = new Gallery( );
        gallery.setIdGallery( 1 );
        gallery.setCodeGallery( CODE_GALLERY );
        gallery.setLabel( "Parc" );
        gallery.setGalleryImageType( "NAVIGATION" );
        gallery.setWidthGallery( 1200 );
        gallery.setHeightGallery( 500 );
        return gallery;
    }

    private static Image image( )
    {
        Image image = new Image( );
        image.setIdImage( 2 );
        image.setIdFile( 7 );
        image.setTitle( TITLE );
        image.setImageBase64( "data:image/png;base64,AAAA" );
        return image;
    }
}
