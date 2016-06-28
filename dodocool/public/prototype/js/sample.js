/**
 * Copyright (c) 2003-2015, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */

/* exported initSample */

if ( CKEDITOR.env.ie && CKEDITOR.env.version < 9 )
	CKEDITOR.tools.enableHtml5Elements( document );

// The trick to keep the editor in the sample quite small
// unless user specified own height.
CKEDITOR.config.height = 150;
CKEDITOR.config.width = 'auto';

var initSample = ( function() {
	var wysiwygareaAvailable = isWysiwygareaAvailable(),
		isBBCodeBuiltIn = !!CKEDITOR.plugins.get( 'bbcode' );

	return function() {
		var editorElement1 = CKEDITOR.document.getById( 'editor1' );
		var editorElement2 = CKEDITOR.document.getById( 'editor2' );
		var editorElement3 = CKEDITOR.document.getById( 'editor3' );
		

		// Depending on the wysiwygare plugin availability initialize classic or inline editor.
		if ( wysiwygareaAvailable ) {
			CKEDITOR.replace( 'editor1' );
		} else {
			editorElement1.setAttribute( 'contenteditable', 'true' );
			CKEDITOR.inline( 'editor1' );

			// TODO we can consider displaying some info box that
			// without wysiwygarea the classic editor may not work.
		}
		if ( wysiwygareaAvailable ) {
			CKEDITOR.replace( 'editor2' );
		} else {
			editorElement2.setAttribute( 'contenteditable', 'true' );
			CKEDITOR.inline( 'editor2' );

			// TODO we can consider displaying some info box that
			// without wysiwygarea the classic editor may not work.
		}
		if ( wysiwygareaAvailable ) {
			CKEDITOR.replace( 'editor3' );
		} else {
			editorElement3.setAttribute( 'contenteditable', 'true' );
			CKEDITOR.inline( 'editor3' );

			// TODO we can consider displaying some info box that
			// without wysiwygarea the classic editor may not work.
		}
	};

	function isWysiwygareaAvailable() {
		// If in development mode, then the wysiwygarea must be available.
		// Split REV into two strings so builder does not replace it :D.
		if ( CKEDITOR.revision == ( '%RE' + 'V%' ) ) {
			return true;
		}

		return !!CKEDITOR.plugins.get( 'wysiwygarea' );
	}
} )();

