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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.galleryimage.business.Gallery;
import fr.paris.lutece.plugins.galleryimage.business.Image;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.util.LocalizedPaginator;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.ReferenceList;

/**
 * Every back office page of the plugin has one page heading (h1), written by @pageHeader
 */
public class GalleryImageAdminPagesRenderTest extends LuteceTestCase
{
    /** The back office commons, which the admin theme includes at runtime and the test context does not */
    private static final String COMMONS = "/commons_bs5_tabler.html";
    private static final String TEMPLATES = "/admin/plugins/galleryimage/";
    private static final String [ ] PAGES = {
            "manage_gallery_image.html", "create_gallery_image.html", "modify_gallery_image.html", "manage_image_of_gallery.html",
            "manageimage/list_image.html", "manageimage/save_image.html", "manageimage/modify_image.html"
    };
    private static final Pattern H1 = Pattern.compile( "<h1\\b[^>]*>([^<]*)</h1>" );

    @Test
    public void testEveryAdminPageHasOnePageHeading( )
    {
        List<String> listFailures = new ArrayList<>( );

        for ( String strPage : PAGES )
        {
            String strTemplate = "<#include \"" + COMMONS + "\" /><#include \"" + TEMPLATES + strPage + "\" />";
            String strHtml = AppTemplateService.getTemplateFromStringFtl( strTemplate, Locale.ENGLISH, model( ) ).getHtml( );
            Matcher matcher = H1.matcher( strHtml );
            int nHeadings = 0;
            boolean bNamed = true;

            while ( matcher.find( ) )
            {
                nHeadings++;
                bNamed &= !matcher.group( 1 ).isBlank( );
            }
            if ( nHeadings != 1 || !bNamed )
            {
                listFailures.add( strPage + " : " + nHeadings + " h1" + ( bNamed ? "" : " (empty)" ) );
            }
        }

        assertTrue( String.join( "\n", listFailures ), listFailures.isEmpty( ) );
    }

    private static Map<String, Object> model( )
    {
        Gallery gallery = new Gallery( );
        gallery.setIdGallery( 1 );
        gallery.setCodeGallery( "parc" );
        gallery.setLabel( "Parc" );
        gallery.setGalleryImageType( "NAVIGATION" );
        gallery.setWidthGallery( 850 );
        gallery.setHeightGallery( 500 );

        Image image = new Image( );
        image.setIdImage( 2 );
        image.setIdFile( 7 );
        image.setTitle( "Parc" );
        image.setImageBase64( "data:image/png;base64,AAAA" );

        Map<String, Object> model = new HashMap<>( );
        model.put( "config", gallery );
        model.put( "image", image );
        model.put( "idGallery", "1" );
        model.put( "refListGallerytype", new ReferenceList( ) );
        model.put( "nb_items_per_page", 10 );
        // set by the admin theme at runtime, read by the rich text editor of the image forms
        model.put( "commonsGlobalThemeCode", "default" );
        model.put( "listGalleryImageConfig", List.of( gallery ) );
        model.put( "listImage", List.of( image ) );
        for ( String strList : new String [ ] { "listImageSelected", "listImageOfGallery", "listAvailableImage" } )
        {
            model.put( strList, List.of( image ) );
        }
        for ( String strPaginator : new String [ ] { "paginatorImageSelected", "paginatorImageOfGallery", "paginatorImage" } )
        {
            model.put( strPaginator, new LocalizedPaginator<>( List.of( 2 ), 10, "jsp/admin/plugins/galleryimage/ManageGallery.jsp", "page_index", "1", Locale.ENGLISH ) );
        }
        return model;
    }
}
