//js公共方法
const jumpHref = '/detail/detail';

//url取参
function getRequest() {
    let url = location.search; //获取url中"?"符后的字串
    let theRequest = new Object();
    if(url.indexOf("?") != -1) {
        let str = url.substr(1);
        let strs = str.split("&");
        for(let i = 0; i < strs.length; i++) {
            theRequest[strs[i].split("=")[0]] = decodeURIComponent(strs[i].split("=")[1]);//uri编码
        }
    }
    return theRequest;
}

/**
 * layui table 单元格 添加title属性
 */
function tdTitle() {
    $('th').each(function (index, element) {
        $(element).attr('title', $(element).text());
    });
    $('td').each(function (index, element) {
        $(element).attr('title', $(element).text());
    });
}

/**
 * layui table表格指定单元格自适应宽度
 * 注意：需要在表格配置参数中设置相应字段{width: "auto"}
 * 推荐在table的done事件中调用
 * @param {jQueryObject} $curTableDom 所在表格table元素的jQuery对象
 * @param {Array} fieldArr 需要宽度自适应的字段数组
 */
function setAutoColWidth($curTableDom, fieldArr){
    let styleDom = $curTableDom.next().find('style')[0];    //获取相应表格html中style元素的DOM对象
    let sheet = styleDom.sheet || styleDom.styleSheet || {};
    let rules = sheet.cssRules || sheet.rules;  //获取css样式规则类数组对象
    let relesLength = rules.length;

    let $targetTd = ''; //字段所在td元素的jq对象
    let key = '';    //字段的key
    let colWidth = '';   //已经宽度自适应的div宽度

    fieldArr.forEach( (item, index) => {
        $targetTd = $curTableDom.next().find(`td[data-field=${ item }]`);
        key = $targetTd.data('key');
        colWidth = $targetTd.find('div').css('width');
        for(let i = 0; i < relesLength; i ++){
            if(rules[i].selectorText === ('.laytable-cell-'+ key)){ //对应字段的css样式规则
                rules[i].style.width = colWidth;    //设置对应字段的单元格宽度
                break;
            }
        }
    });
}

function hoverOpenImg(){
    let img_show = null; // tips提示
    $('td img').hover(function(){
        let img = "<img class='img_msg' src='"+$(this).attr('src')+"' style='width:200px;' />";
        img_show = layer.tips(img, this,{
            tips:[2, 'rgba(41,41,41,.5)']
            ,area: ['230px']
        });
    },function(){
        layer.close(img_show);
    });
    $('td img').attr('style','max-width:100px');
}

//滚动到指定位置
function scrollTo(element,speed) {
    if(!speed){
        speed = 300;
    }
    if(!element){
        $("html,body").animate({scrollTop:0},speed);
    }else{
        if(element.length>0){
            $("html,body").animate({scrollTop:$(element).offset().top-200},speed);
        }
    }
}

// 网络图片转base64
function setBase64(imgUrl, img) {
    let xhr = new XMLHttpRequest();
    xhr.open("get", imgUrl, true);
    // 至关重要
    xhr.responseType = "blob";
    xhr.onload = function () {
        if (this.status === 200) {
            //得到一个blob对象
            let blob = this.response;
            let oFileReader = new FileReader();
            oFileReader.onloadend = function (e) {
                // 此处拿到的已经是 base64的图片了
                let base64 = e.target.result;
                img.src = base64;
            };
            oFileReader.readAsDataURL(blob);
        }
    };
    xhr.send();
}

function _post_form(opt) {
    $.ajax({
        url: opt.url,
        type: opt.type,
        data: opt.data,
        async: opt.async,
        dataType: opt.dataType,
        contentType: opt.contentType,
        processData: opt.processData,
        success: res=>{
            if(res.code === -1){
                layui.use('layer', function () {
                    let layer = layui.layer;
                    layer.msg("请先登录再操作...");
                    setTimeout(()=>{
                        window.location.href = "/login.html";
                    }, 1200);
                })
                return;
            }
            let success = opt.success;
            success && success(res);
        },
        error: res=>{
            failTip("网络连接出错!");
            let error = opt.error;
            error && error(res);
        },
        complete: res=>{
            let complete = opt.complete;
            complete && complete(res);
        }
    })
}