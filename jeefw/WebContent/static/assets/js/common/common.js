
var ISS = {
	version : '1.0',
	Core:{},
	Constants:{},
	FileType:{}
};

ISS.Config = {
	    Global: {
	        refresh: 0,
	        rootUrl:document.location.pathname.substr(0, document.location.pathname.substr(1).indexOf("/") + 1)
	    },
	};
//判断文件类型是否是Excel
ISS.FileType.Exvel=function(obj,mess){
	var fileType=obj.substr(obj.lastIndexOf(".")).toLowerCase();//获得文件后缀名
    if(fileType != '.xls' && fileType != '.xlsx'){
    	$("#"+mess).tips({
			side:3,
            msg:'请上传xls格式的文件',
            bg:'#AE81FF',
            time:3
        });
    	$("#"+mess).val()=="";
    	return false;
    }else{
    	return true;
    }
}

//判断是否选择文件
ISS.FileType.Have=function(mess){
	if($("#"+mess).val()==""){
		$("#"+mess).tips({
			side:3,
            msg:'请选择文件',
            bg:'#AE81FF',
            time:3
        });
		return false;
	}else{
		return true;
	}
}



