var SUCCESS = 0;
var ERROR = 1;

$(function() {
	//网页加载后，立即读取笔记本系列
	//loadNotebooks();
	$(document).data('page',0);
	loadPagedNotebooks();
	
	
	
	//绑定笔记本列表区域的点击事件
	//on()方法绑定事件可以区别事件源
	//click()方法绑定事件，无法区别事件源
	//绑定笔记本列表区域的点击事件
	$('#notebook-list').on('click','.notebook',loadNotes);
	
	$('#notebook-list').on('click','.more',loadPagedNotebooks);
	
	$('#note-list').on('click','#add_note',showAddNoteDialog);
	$('#can').on('click','.close,.cancel',closeDialog);
	$('#can').on('click','.create-note',addNote);
	$('#can').on('click','.move-note',moveNote);
	$('#note-list').on('click','.note',showNote);
	//绑定笔记子菜单的触发事件
	$('#note-list').on('click','.btn-note-menu',showNoteMenu);
	$(document).click(hideNoteMenu);
	$('#note-list').on('click','.btn_move',showMoveNoteDialog);
	$('#note-list').on('click','.btn_delete',showDeleteNoteDialog);
	$(document).data('notebookId',null);
	$(document).data('noteId',null);
	
	$('#trash_button').click(showTrashBin);
	
	
});

function loadPagedNotebooks(){
	var page = $(document).data('page');
	var userId = getCookie('userId');
	//从服务器拉取数据
	var url = 'notebook/page.do';
	var data = {userId:userId,page:page};
	$.getJSON(url,data,function(result){
		if(result.state == SUCCESS){
			var notebooks = result.data;
			showPagedNotebooks(notebooks,page);
			$(document).data('page',page + 1);
		}else{
			alert(result.message);
		}
		
	});
}

function showPagedNotebooks(notebooks,page){
	var ul = $('#notebook-list ul');
	if(page == 0){//第一页时清空ul的li
		ul.empty();
	}else{//不是第一页，只删除.more元素
		ul.find('.more').remove();
	}
	for(var i = 0;i < notebooks.length;i++){
		var notebook = notebooks[i];
		var li = notebookTemplate.replace('[name]',notebook.name);
		li = $(li);
		li.data('notebookId',notebook.id);
		ul.append(li);
	}
	if(notebooks.length != 0){
		ul.append(moreTemplate);
	}
	
}

var moreTemplate = 
	'<li class="online more">'
	+'<a>'
	+'<i class="fa fa-plus" title="online" rel="tooltip-bottom">'
	+'</i>加载更多</a></li>';

function showTrashBin(){
	$('#trash-bin').show();
	$('#note-list').hide();
	loadTrashBin();
}

function loadTrashBin(){
    var url = 'note/trash.do';
    var data = {userId: getCookie('userId')};
    $.getJSON(url, data, function(result){
        if(result.state==SUCCESS){
            showNotesInTrashBin(result.data);
        }else{
            alert(result.message);
        }
    });
}

function showNotesInTrashBin(notes){
    var ul = $('#trash-bin ul');
    ul.empty();
    for(var i=0; i<notes.length; i++){
        var note = notes[i];
        var li = trashBinItem.replace('[title]', note.title);
        li = $(li);
        li.data('noteId', note.id);
        ul.append(li);
    }
}

var trashBinItem = 
    '<li class="disable">'+
        '<a><i class="fa fa-file-text-o" title="online" rel="tooltip-bottom"></i>'+
        ' [title]'+
        '<button type="button" class="btn btn-default btn-xs btn_position btn_delete">'+
            '<i class="fa fa-times"></i>'+
        '</button>'+
        '<button type="button" class="btn btn-default btn-xs btn_position_2 btn_replay">'+
            '<i class="fa fa-reply"></i>'+
        '</button></a>'+
    '</li>';
function showDeleteNoteDialog(){
	$('#can').load('alert/alert_delete_note.html',function(){
		$('.opacity_bg').show();
	});
}

function showMoveNoteDialog(){
	$('#can').load('alert/alert_move.html',function(){
		$('.opacity_bg').show();
		listNotes();
	});
}

function listNotes(){
	var data = {userId:getCookie('userId')};
	$.getJSON('notebook/list.do',data,function(result){
		if(result.state == SUCCESS){
			for(i = 0;i < result.data.length;i++){
				var notebookName = result.data[i].name;
				var notebookId = result.data[i].id;
				var note_option = '<option value= "[notebookId]">- '+ notebookName +' -</option>';
				var option =$(note_option.replace('[notebookId]',notebookId));
				$('#moveSelect').append(option);
			}
		}else{
			alert(result.message);
		}
	});
}

function moveNote(){
	console.log("hhgg");
	var notebookId = $('#moveSelect').val();
	var noteId = $(document).data('noteId');
	if(notebookId == 'none'){
		alert("请选择目标notebook！");
		return;
	}
	if(notebookId == $(document).data('notebookId')){
		return;
	}
	var data = {notebookId:notebookId,noteId:noteId};
	$.post('note/move.do',data,function(result){
		if(result.state == SUCCESS){
			var li = $('#note-list .checked').parent();
			
			var lis = li.siblings();
			if(lis.size()>0){
				lis.eq(0).click();
			}else{
				$('#input_note_title').val("");
				um.setContent("");
			}
			li.remove();
			closeDialog();
		}else{
			alert(result.message);
		}
	});
	
}

function hideNoteMenu(){
	$('.note_menu').hide();
}

function showNoteMenu(){
	var btn = $(this);
	
	btn.parent('.checked').next().toggle();
	//btn.parent('.checked')获取当前按钮的父元素,
	//这个元素必须符合选择器'.checked'，如果不符合就返回空的JQuery元素
	return false;//阻止点击事件的继续传播！
}

function showNote(){
	var noteId = $(this).data('noteId');
	var data = {noteId:noteId};
	
	$(this).parent().find('a').removeClass('checked');
	$(this).find('a').addClass('checked');
	
	$(document).data('noteId',noteId);
	$.getJSON('note/load.do',data,function(result){
		if(result.state == SUCCESS){
			$('#input_note_title').val(result.data.title);
			$('#myEditor').html(result.data.body);
		}else{
			alert(result.message);
		}
	});
	closeDialog();
}


function addNote(){
	var noteTitle = $('#input_note').val();	
	var data = {
		userId:getCookie('userId'),
		notebookId:$(document).data('notebookId'),
		noteTitle:noteTitle
	};
	
	$.post('note/add.do',data,function(result){
		if(result.state == SUCCESS){
			var ul = $('#note-list ul');
			var li = noteTemplate.replace('[title]',result.data.title);
			li = $(li);
			li.data('noteId',result.data.id);
			ul.prepend(li);
		}else{
			alert(result.message);
		}
	});
	closeDialog();
}

function closeDialog(){
	$('.opacity_bg').hide();
	$('#can').empty();
}

function showAddNoteDialog(){
	if($(document).data('notebookId') == null){
		alert('请选择笔记本！');
		return;
	}
	$('#can').load('alert/alert_note.html',
		function(){
			$('.opacity_bg').show();
			$('#input_note').focus();
	});
}

/**笔记本项目点击事件处理方法,加载全部笔记*/
function loadNotes(){
	//在点击笔记列表的笔记时，为了显示笔记列表：关闭回收站，打开笔记列表
	$('#trash-bin').hide();
	$('#note-list').show();
	
	var li = $(this);
	//在li上被点击的笔记本li增加选定效果
	li.parent().find('a').removeClass('checked');
	li.find('a').addClass('checked');
	
	var url = 'note/list.do';
	var data = {notebookId:li.data('notebookId')};
	
	//绑定笔记本ID， 用于添加笔记功能
	$(document).data('notebookId', li.data('notebookId'));
	$.getJSON(url,data,function(result){
		if(result.state == SUCCESS){
			var notes = result.data;
			showNotes(notes);
		}else{
			alert(result.message);
		}
		
	});
}
/**将笔记本列表信息显示到屏幕上**/
function showNotes(notes){
	var ul = $('#note-list ul');
	ul.empty();	
	for(i = 0;i < notes.length;i++){
		var note = notes[i];
		var li = noteTemplate.replace('[title]',note.title);
		li = $(li);
		li.data('noteId',note.id);
		ul.append(li);
	}
}

var noteTemplate = '<li class="online note">'
					+ '<a>'
					+ '<i class="fa fa-file-text-o" title="online" rel="tooltip-bottom"></i>[title]<button type="button" class="btn btn-default btn-xs btn_position btn_slide_down btn-note-menu"><i class="fa fa-chevron-down"></i></button>'
					+ '</a>'
					+ '<div class="note_menu" tabindex="-1">'
					+ '<dl>'
					+ '<dt><button type="button" class="btn btn-default btn-xs btn_move" title="移动至..."><i class="fa fa-random"></i></button></dt>'
					+ '<dt><button type="button" class="btn btn-default btn-xs btn_share" title="分享"><i class="fa fa-sitemap"></i></button></dt>'
					+ '<dt><button type="button" class="btn btn-default btn-xs btn_delete" title="删除"><i class="fa fa-times"></i></button></dt>'
					+ '</dl>'
					+ '</div>'
					+ '</li>';
	
function loadNotebooks(){
	//利用ajax从服务器获取(get)数据
	var url = 'notebook/list.do';
	var data = {userId:getCookie('userId')};
	$.getJSON(url,data,function(result){
		if(result.state == SUCCESS){
			var notebooks = result.data;
			//在showNotebooks方法中
			//将全部的笔记本数据notebooks显示到notebook-list区域
			showNotebooks(notebooks);
		}else{
			alert(result.message);
		}
	});
}
/*在notebook-list区域中显示笔记本列表*/
function showNotebooks(notebooks){
	//找显示笔记本列表的区域
	//遍历notebooks数组，将为每个对象创建一个li元素，添加到ul元素中
	var ul = $('#notebook-list ul');
	for(var i = 0;i < notebooks.length;i++){
		var notebook = notebooks[i];
		var li = notebookTemplate.replace('[name]',notebook.name);
		li = $(li);
		//将notebook.id绑定到li
		li.data('notebookId',notebook.id);
		ul.append(li);
	}
}

var notebookTemplate = '<li class="online notebook">'
						+'<a>'
						+'<i class="fa fa-book" title="online" rel="tooltip-bottom">'
						+'</i>[name]</a></li>';



