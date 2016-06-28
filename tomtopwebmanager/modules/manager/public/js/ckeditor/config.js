/**
 * @license Copyright (c) 2003-2015, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function( config ) {
	// Define changes to default configuration here. For example:
	// config.language = 'fr';
	// config.uiColor = '#AADC6E';
	// 编辑器的z-index值
	config.baseFloatZIndex = 999999;
	// 开启图片上传功能
	config.toolbarGroups = [ {
		name : 'others'
	}, {
		name : 'basicstyles',
		groups : [ 'basicstyles', 'cleanup' ]
	}, {
		name : 'styles'
	}, {
		name : 'insert'
	}, {
		name : 'links'
	}, {
		name : 'colors'
	}, {
		name : 'paragraph',
		groups : [ 'list', 'align' ]
	}, {
		name : 'document',
		groups : [ 'mode', 'document', 'doctools' ]
	}, {
		name : 'tools'
	} ];

	config.removePlugins = 'horizontalrule,specialchar,pagebreak,iframe,smiley,flash,preview,blockquote,save,newpage,print,templates,showblocks';

	// Remove some buttons, provided by the standard plugins, which we don't
	// need to have in the Standard(s) toolbar.
	config.removeButtons = 'Subscript,Superscript';

	// Se the most common block elements.
	config.format_tags = 'p;h1;h2;h3;pre';
	config.enterMode = CKEDITOR.ENTER_BR;
	config.tabSpaces = 4;

	// Make dialogs simpler.
	config.removeDialogTabs = 'image:advanced;link:advanced';
	config.allowedContent = true;
	config.fullPage = false;

	config.autoParagraph = false;
	config.autoUpdateElement = false;
	config.entities_greek = false;
	config.fillEmptyBlocks = false;
	config.removeFormatTags = 'tbody';

	config.pasteFromWordRemoveFontStyles = false;
	config.pasteFromWordRemoveStyles = false;

	// 开启图片上传功能
	config.filebrowserImageUploadUrl = '../ckeditor/upload';
	config.filebrowserUploadUrl = '../ckeditor/upload';

	config.disableNativeSpellChecker = false;
	
	config.contentsCss = '../assets/css/manager/ckeditor/contents.css';
	// size
	// config.height = '150';
	// config.width = '80%';
	config.protectedSource.push(/<em[\s\S]*?\><\/em>/g);
	config.protectedSource.push(/<ol[\s\S]*?\>/g );
};

CKEDITOR.on('instanceReady', function(ev) {

	var writer = ev.editor.dataProcessor.writer;
	// The character sequence to use for every indentation step.
	writer.indentationChars = '    ';

	var dtd = CKEDITOR.dtd;
	// Elements taken as an example are: block-level elements (div or p), list
	// items (li, dd), and table elements (td, tbody).
	for ( var e in CKEDITOR.tools.extend({}, dtd.$block, dtd.$listItem, dtd.$tableContent)) {
		ev.editor.dataProcessor.writer.setRules(e, {
			// Indicates that an element creates indentation on line breaks that
			// it contains.
			indent : false,
			// Inserts a line break before a tag.
			breakBeforeOpen : true,
			// Inserts a line break after a tag.
			breakAfterOpen : false,
			// Inserts a line break before the closing tag.
			breakBeforeClose : false,
			// Inserts a line break after the closing tag.
			breakAfterClose : true
		});
	}

	for ( var e in CKEDITOR.tools.extend({}, dtd.$list, dtd.$listItem, dtd.$tableContent)) {
		ev.editor.dataProcessor.writer.setRules(e, {
			indent : true,
		});
	}

	// You can also apply the rules to a single element.
	ev.editor.dataProcessor.writer.setRules('table', {
		indent : true
	});

	ev.editor.dataProcessor.writer.setRules('form', {
		indent : true
	});
});
