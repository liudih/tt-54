// bootstrap-ckeditor-modal-fix.js
// hack to fix ckeditor/bootstrap compatiability bug when ckeditor appears in a bootstrap modal dialog
// 解决模态框中ckeditor组件，链接与插入图片等div中 input不可写问题
// Include this AFTER both bootstrap and ckeditor are loaded.
// From: http://stackoverflow.com/questions/14420300/bootstrap-with-ckeditor-equals-problems;
//       http://ckeditor.com/forums/Support/Issue-with-Twitter-Bootstrap
// Author: http://stackoverflow.com/users/185839/aaron
// add :xcf

$.fn.modal.Constructor.prototype.enforceFocus = function() {
  modal_this = this
  $(document).on('focusin.modal', function (e) {
    if (modal_this.$element[0] !== e.target && !modal_this.$element.has(e.target).length 
    && !$(e.target.parentNode).hasClass('cke_dialog_ui_input_select') 
    && !$(e.target.parentNode).hasClass('cke_dialog_ui_input_text')) {
      modal_this.$element.focus()
    }
  })
};