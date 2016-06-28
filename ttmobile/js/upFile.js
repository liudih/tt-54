function ProcessFile( e ) {
	var file = document.getElementById('file').files[0];
	var boxId = document.getElementById("result");
	var boxLi = getByClass(boxId,"lineBlock");
	var img = document.createElement("img");
	var insertedClose=document.createElement("div"); 
	var inputs=document.createElement("input"); 
	var insertedNode=document.createElement("li"); 
	var box = document.getElementById("result");
		insertedNode.className="lineBlock";
		insertedClose.className="deleteAddPic";
	var deleteImg = getByClass(boxId,"deleteAddPic");
	if ( file ) {
		var reader = new FileReader();
			if(boxLi.length<4){
				reader.onload = function ( event ) {
					var txt = event.target.result;
					var insertImg = document.getElementById("result").insertBefore(insertedNode,box.children[1]);
					img.src = txt;
					//document.getElementById("result").appendChild( img );
					insertImg.appendChild(img); 
					insertImg.appendChild(insertedClose); 
					addPicH();
					$(function(){
						$(".deleteAddPic").click(function(){
							$(this).parents("li").remove();
							document.getElementById("addPic").style.display="inline-block";
						})
					})
				};
			}else{
				reader.onload = function ( event ) {
					var txt = event.target.result;
					var insertImg = document.getElementById("result").insertBefore(insertedNode,box.children[1]);
					img.src = txt;
					//document.getElementById("addPic").appendChild(img);
					//document.getElementById("addPic").appendChild(insertedClose);
					//document.getElementById("file").remove();
					document.getElementById("addPic").style.display="none";
					insertImg.appendChild(img); 
					insertImg.appendChild(insertedClose); 
					addPicH();
					$(function(){
						$(".deleteAddPic").click(function(){
							$(this).parents("li").remove();
							document.getElementById("addPic").style.display="inline-block";
						})
					})
				};
			}
	}
	reader.readAsDataURL( file );
}
function contentLoaded() {
	document.getElementById('file').addEventListener( 'change' , ProcessFile , false );
}
window.addEventListener( "DOMContentLoaded" , contentLoaded , false );

